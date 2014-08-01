package com.texasjake95.core.asm.transformer;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MethodRemover extends ClassVisitor implements Opcodes {

	private String className;

	public MethodRemover(ClassVisitor cv, String className)
	{
		super(ASM4, cv);
		this.className = className;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
	{
		if (this.className.equals("com.texasjake95.core.data.FMPValueProvider"))
			if (name.equals("getStack"))
				return null;
		return super.visitMethod(access, name, desc, signature, exceptions);
	}
}
