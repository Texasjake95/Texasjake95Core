package texasjake95.Core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ModLoader;


public class ClassReader {
	public static File getModsFolder()
{
	return new File(Minecraft.getMinecraftDir(), "mods");
}

public static ArrayList<Class<?>> findClasses(ClassMatcher match, Class<?>... class1)
{
	ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
	ClassLoader loader = (net.minecraft.src.ModLoader.class).getClassLoader();
	
    try
	{
		readFromClassPath(loader, getJarFile(), match, class1, classes);	        
        readFromModFolder(loader, getModsFolder(), match, class1, classes);
	}
	catch(Exception e)
	{
        ModLoader.throwException("", e);
	}
    return classes;
}

private static File getJarFile()
{
	try
	{
		return new File((net.minecraft.src.ModLoader.class).getProtectionDomain().getCodeSource().getLocation().toURI());
	}
	catch(URISyntaxException e)
	{
        ModLoader.throwException("", e);
		return null;
	}
}
private static void readFromClassPath(ClassLoader loader, File file, ClassMatcher match, Class<?>[] superclasses, ArrayList<Class<?>> classlist) throws FileNotFoundException, IOException
{
    if(file.isFile() && (file.getName().endsWith(".jar") || file.getName().endsWith(".zip")))
    {
    	readFromZipFile(loader, file, match, superclasses, classlist);
        
    } 
    else if(file.isDirectory())
    {
        readFromDirectory(loader, file, file, match, superclasses, classlist);
    }
}
private static void readFromModFolder(ClassLoader loader, File file, ClassMatcher match, Class<?>[] superclasses, ArrayList<Class<?>> classlist) throws IOException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException
{
    if(!file.isDirectory())
    {
        throw new IllegalArgumentException("folder must be a Directory.");
    }
    for(File child : file.listFiles())
    {
        if(child.isFile() && (child.getName().endsWith(".jar") || child.getName().endsWith(".zip")))
        {
            readFromZipFile(loader, child, match, superclasses, classlist);
        } 
        else if(child.isDirectory())
        {
            readFromDirectory(loader, child, child, match, superclasses, classlist);
        }
    }
}
private static void readFromZipFile(ClassLoader loader, File file, ClassMatcher match, Class<?>[] superclasses, ArrayList<Class<?>> classlist) throws IOException
{
	FileInputStream fileinputstream = new FileInputStream(file);
    ZipInputStream zipinputstream = new ZipInputStream(fileinputstream);
    do
    {
        ZipEntry zipentry = zipinputstream.getNextEntry();
        if(zipentry == null)
        {
            break;
        }
        String fullname = zipentry.getName().replace('\\', '/');
        int pos = fullname.lastIndexOf('/');
        String name = pos == -1 ? fullname : fullname.substring(pos+1);
        if(!zipentry.isDirectory() && match.matches(name))
        {
        	addClass(loader, fullname, superclasses, classlist);
        }
    } 
    while(true);
    fileinputstream.close();
}
private static void readFromDirectory(ClassLoader loader, File directory, File basedirectory, ClassMatcher match, Class<?>[] superclasses, ArrayList<Class<?>> classlist)
{
	for(File child : directory.listFiles())
	{
		if(child.isDirectory())
		{
			readFromDirectory(loader, child, basedirectory, match, superclasses, classlist);
		}
		else if(child.isFile() && match.matches(child.getName()))
		{
			addClass(loader, getRelativePath(basedirectory, child), superclasses, classlist);
		}
	}
}
public static String getRelativePath(File parent, File child)
{
	if(parent.isFile() || !child.getPath().startsWith(parent.getPath()))
	{
		return null;
	}
	return child.getPath().substring(parent.getPath().length() + 1);
}
private static void addClass(ClassLoader loader, String resource, Class<?>[] superclasses, ArrayList<Class<?>> classlist)
{
	try
    {
		String classname = resource.replace("\\", ".");
		classname = classname.replace("/", ".");
		classname = classname.substring(0, classname.length()-6);
        Class<?> class1 = Class.forName(classname, true, loader);
        for(Class<?> superclass : superclasses)
        {
            if(!superclass.isAssignableFrom(class1))
            {
                return;
            }
        }
        classlist.add(class1);
    }
    catch(Throwable t)
    {
        ModLoader.throwException("", t);
    }
}
}
