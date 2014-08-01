package com.texasjake95.core.asm.transformer;

import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

public class FMPTransformer extends AccessTransformer implements Opcodes {

	public FMPTransformer() throws IOException
	{
		super();
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes)
	{
		bytes = this.transformFMP(transformedName, bytes);
		return super.transform(name, transformedName, bytes);
	}

	private byte[] transformFMP(String transformedName, byte[] bytes)
	{
		if (transformedName.equals("com.texasjake95.core.data.FMPValueProvider"))
			try
			{
				if (this.getClass().getClassLoader().loadClass("codechicken.microblock.ItemMicroPart") != null)
				{
					ClassNode classNode = new ClassNode();
					ClassVisitor vistor = new MethodRemover(classNode, transformedName);
					ClassReader classReader = new ClassReader(bytes);
					classReader.accept(vistor, 0);
					ClassWriter cw = new ClassWriter(0);
					classNode.accept(cw);
					MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "getStack", "(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;", null, null);
					mv.visitCode();
					Label l0 = new Label();
					mv.visitLabel(l0);
					mv.visitLineNumber(18, l0);
					mv.visitVarInsn(ALOAD, 0);
					mv.visitMethodInsn(INVOKESTATIC, "codechicken/microblock/ItemMicroPart", "getMaterial", "(Lnet/minecraft/item/ItemStack;)Lcodechicken/microblock/MicroMaterialRegistry$IMicroMaterial;");
					mv.visitMethodInsn(INVOKEINTERFACE, "codechicken/microblock/MicroMaterialRegistry$IMicroMaterial", "getItem", "()Lnet/minecraft/item/ItemStack;");
					mv.visitInsn(ARETURN);
					Label l1 = new Label();
					mv.visitLabel(l1);
					mv.visitLocalVariable("stack", "Lnet/minecraft/item/ItemStack;", null, l0, l1, 0);
					mv.visitMaxs(1, 1);
					mv.visitEnd();
					return cw.toByteArray();
				}
			}
			catch (ClassNotFoundException e)
			{
			}
		return bytes;
	}
}
