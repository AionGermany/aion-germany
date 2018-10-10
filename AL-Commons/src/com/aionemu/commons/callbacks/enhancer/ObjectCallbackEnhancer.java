/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */

package com.aionemu.commons.callbacks.enhancer;

import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.callbacks.Callback;
import com.aionemu.commons.callbacks.CallbackResult;
import com.aionemu.commons.callbacks.EnhancedObject;
import com.aionemu.commons.callbacks.metadata.ObjectCallback;
import com.aionemu.commons.callbacks.util.CallbacksUtil;
import com.aionemu.commons.callbacks.util.ObjectCallbackHelper;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.Modifier;
import javassist.NotFoundException;

public class ObjectCallbackEnhancer extends CallbackClassFileTransformer {

	private static final Logger log = LoggerFactory.getLogger(ObjectCallbackEnhancer.class);

	/**
	 * Field name for callbacks map
	 */
	public static final String FIELD_NAME_CALLBACKS = "$$$callbacks";

	/**
	 * Field name for synchronizer
	 */
	public static final String FIELD_NAME_CALLBACKS_LOCK = "$$$callbackLock";

	/**
	 * Does actual transformation
	 * 
	 * @param loader
	 *            class loader
	 * @param clazzBytes
	 *            class bytecode
	 * @return transformed class bytecode
	 * @throws Exception
	 *             is something went wrong
	 */
	@Override
	protected byte[] transformClass(ClassLoader loader, byte[] clazzBytes) throws Exception {
		ClassPool cp = new ClassPool();
		cp.appendClassPath(new LoaderClassPath(loader));
		CtClass clazz = cp.makeClass(new ByteArrayInputStream(clazzBytes));

		Set<CtMethod> methdosToEnhance = new HashSet<CtMethod>();

		for (CtMethod method : clazz.getDeclaredMethods()) {
			if (!isEnhanceable(method)) {
				continue;
			}

			methdosToEnhance.add(method);
		}

		if (!methdosToEnhance.isEmpty()) {
			CtClass eo = cp.get(EnhancedObject.class.getName());
			for (CtClass i : clazz.getInterfaces()) {
				if (i.equals(eo)) {
					throw new RuntimeException("Class already implements EnhancedObject interface, WTF???");
				}
			}

			log.debug("Enhancing class: " + clazz.getName());
			writeEnhancedObjectImpl(clazz);

			for (CtMethod method : methdosToEnhance) {
				log.debug("Enhancing method: " + method.getLongName());
				enhanceMethod(method);
			}

			return clazz.toBytecode();
		} else {
			log.trace("Class " + clazz.getName() + " was not enhanced");
			return null;
		}
	}

	/**
	 * Responsible for method enhancing, writing service calls to method.
	 * 
	 * @param method
	 *            Method that has to be edited
	 * @throws javassist.CannotCompileException
	 *             if something went wrong
	 * @throws javassist.NotFoundException
	 *             if something went wrong
	 * @throws ClassNotFoundException
	 *             if something went wrong
	 */
	protected void enhanceMethod(CtMethod method)
			throws CannotCompileException, NotFoundException, ClassNotFoundException {
		ClassPool cp = method.getDeclaringClass().getClassPool();

		method.addLocalVariable("___cbr", cp.get(CallbackResult.class.getName()));

		CtClass listenerClazz = cp.get(((ObjectCallback) method.getAnnotation(ObjectCallback.class)).value().getName());

		String listenerFieldName = "$$$" + listenerClazz.getSimpleName();

		CtClass clazz = method.getDeclaringClass();
		try {
			clazz.getField(listenerFieldName);
		} catch (NotFoundException e) {
			clazz.addField(CtField.make(
					"Class " + listenerFieldName + " = Class.forName(\"" + listenerClazz.getName() + "\");", clazz));
		}

		int paramLength = method.getParameterTypes().length;

		method.insertBefore(writeBeforeMethod(method, paramLength, listenerFieldName));
		method.insertAfter(writeAfterMethod(method, paramLength, listenerFieldName));
	}

	/**
	 * Code that is added in the begining of the method
	 * 
	 * @param method
	 *            method that should be edited
	 * @param paramLength
	 *            Lenght of methods parameters
	 * @param listenerFieldName
	 *            Listener class that is used for method
	 * @return code that will be inserted before method
	 * @throws NotFoundException
	 *             if something went wrong
	 * @throws CannotCompileException
	 */
	protected String writeBeforeMethod(CtMethod method, int paramLength, String listenerFieldName)
			throws NotFoundException, CannotCompileException {
		StringBuilder sb = new StringBuilder();
		sb.append('{');

		sb.append(" ___cbr = ");
		sb.append(ObjectCallbackHelper.class.getName()).append(".beforeCall((");
		sb.append(EnhancedObject.class.getName());
		sb.append(")this, " + listenerFieldName + ", ");
		if (paramLength > 0) {
			sb.append("new Object[]{");
			for (int i = 1; i <= paramLength; i++) {
				sb.append("($w)$").append(i);

				if (i < paramLength) {
					sb.append(',');
				}
			}
			sb.append("}");
		} else {
			sb.append("null");
		}
		sb.append(");");

		sb.append("if(___cbr.isBlockingCaller()){");

		// Fake return due to javassist bug
		// $r is not available in "insertBefore"
		CtClass returnType = method.getReturnType();
		if (returnType.equals(CtClass.voidType)) {
			sb.append("return");
		} else if (returnType.equals(CtClass.booleanType)) {
			sb.append("return false");
		} else if (returnType.equals(CtClass.charType)) {
			sb.append("return 'a'");
		} else if (returnType.equals(CtClass.byteType) || returnType.equals(CtClass.shortType)
				|| returnType.equals(CtClass.intType) || returnType.equals(CtClass.floatType)
				|| returnType.equals(CtClass.longType) || returnType.equals(CtClass.longType)) {
			sb.append("return 0");
		}
		sb.append(";}}");

		return sb.toString();
	}

	/**
	 * Writes code that will be inserted after method
	 * 
	 * @param method
	 *            method to edit
	 * @param paramLength
	 *            lenght of method paramenters
	 * @param listenerFieldName
	 *            method listener
	 * @return actual code that should be inserted
	 * @throws NotFoundException
	 *             if something went wrong
	 */
	protected String writeAfterMethod(CtMethod method, int paramLength, String listenerFieldName)
			throws NotFoundException {
		StringBuilder sb = new StringBuilder();
		sb.append('{');

		// workaround for javassist bug, $r is not available in "insertBefore"
		if (!method.getReturnType().equals(CtClass.voidType)) {
			sb.append("if(___cbr.isBlockingCaller()){");
			sb.append("$_ = ($r)($w)___cbr.getResult();");
			sb.append("}");
		}

		sb.append("___cbr = ").append(ObjectCallbackHelper.class.getName()).append(".afterCall((");
		sb.append(EnhancedObject.class.getName()).append(")this, " + listenerFieldName + ", ");
		if (paramLength > 0) {
			sb.append("new Object[]{");
			for (int i = 1; i <= paramLength; i++) {
				sb.append("($w)$").append(i);

				if (i < paramLength) {
					sb.append(',');
				}
			}
			sb.append("}");
		} else {
			sb.append("null");
		}
		sb.append(", ($w)$_);");
		sb.append("if(___cbr.isBlockingCaller()){");
		if (method.getReturnType().equals(CtClass.voidType)) {
			sb.append("return;");
		} else {
			sb.append("return ($r)($w)___cbr.getResult();");
		}
		sb.append("}");
		sb.append("else {return $_;}");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Implements {@link EnhancedObject on class}
	 * 
	 * @param clazz
	 *            class to edit
	 * @throws NotFoundException
	 *             if something went wrong
	 * @throws CannotCompileException
	 *             if something went wrong
	 */
	protected void writeEnhancedObjectImpl(CtClass clazz) throws NotFoundException, CannotCompileException {
		ClassPool cp = clazz.getClassPool();
		clazz.addInterface(cp.get(EnhancedObject.class.getName()));
		writeEnhancedOBjectFields(clazz);
		writeEnhancedObjectMethods(clazz);
	}

	/**
	 * Implements {@link EnhancedObject} fields
	 * 
	 * @param clazz
	 *            Class to add fields
	 * @throws CannotCompileException
	 *             if something went wrong
	 * @throws NotFoundException
	 *             if something went wrong
	 */
	private void writeEnhancedOBjectFields(CtClass clazz) throws CannotCompileException, NotFoundException {
		ClassPool cp = clazz.getClassPool();

		// add map that holds callbacks
		CtField cbField = new CtField(cp.get(Map.class.getName()), FIELD_NAME_CALLBACKS, clazz);
		cbField.setModifiers(java.lang.reflect.Modifier.PRIVATE);
		clazz.addField(cbField, CtField.Initializer.byExpr("null;"));

		// add reetrantReadWriteLock
		CtField cblField = new CtField(cp.get(ReentrantReadWriteLock.class.getName()), FIELD_NAME_CALLBACKS_LOCK,
				clazz);
		cblField.setModifiers(java.lang.reflect.Modifier.PRIVATE);
		clazz.addField(cblField, CtField.Initializer.byExpr("new " + ReentrantReadWriteLock.class.getName() + "();"));
	}

	/**
	 * Implements {@link EnhancedObject methods}
	 * 
	 * @param clazz
	 *            Class to add methods
	 * @throws NotFoundException
	 *             if something went wrong
	 * @throws CannotCompileException
	 *             if something went wrong
	 */
	private void writeEnhancedObjectMethods(CtClass clazz) throws NotFoundException, CannotCompileException {

		ClassPool cp = clazz.getClassPool();

		CtClass callbackClass = cp.get(Callback.class.getName());
		CtClass mapClass = cp.get(Map.class.getName());
		CtClass reentrantReadWriteLockClass = cp.get(ReentrantReadWriteLock.class.getName());

		CtMethod method = new CtMethod(CtClass.voidType, "addCallback", new CtClass[] { callbackClass }, clazz);
		method.setModifiers(java.lang.reflect.Modifier.PUBLIC);
		method.setBody("com.aionemu.commons.callbacks.util.ObjectCallbackHelper.addCallback($1, this);");
		clazz.addMethod(method);

		method = new CtMethod(CtClass.voidType, "removeCallback", new CtClass[] { callbackClass }, clazz);
		method.setModifiers(java.lang.reflect.Modifier.PUBLIC);
		method.setBody("com.aionemu.commons.callbacks.util.ObjectCallbackHelper.removeCallback($1, this);");
		clazz.addMethod(method);

		method = new CtMethod(mapClass, "getCallbacks", new CtClass[] {}, clazz);
		method.setModifiers(java.lang.reflect.Modifier.PUBLIC);
		method.setBody("return " + FIELD_NAME_CALLBACKS + ";");
		clazz.addMethod(method);

		method = new CtMethod(CtClass.voidType, "setCallbacks", new CtClass[] { mapClass }, clazz);
		method.setModifiers(java.lang.reflect.Modifier.PUBLIC);
		method.setBody("this." + FIELD_NAME_CALLBACKS + " = $1;");
		clazz.addMethod(method);

		method = new CtMethod(reentrantReadWriteLockClass, "getCallbackLock", new CtClass[] {}, clazz);
		method.setModifiers(java.lang.reflect.Modifier.PUBLIC);
		method.setBody("return " + FIELD_NAME_CALLBACKS_LOCK + ";");
		clazz.addMethod(method);
	}

	/**
	 * Checks if method is enhanceable. It should be marked with
	 * {@link com.aionemu.commons.callbacks.metadata.ObjectCallback} annotation,
	 * be not native and not abstract
	 * 
	 * @param method
	 *            method to check
	 * @return check result
	 */
	protected boolean isEnhanceable(CtMethod method) {
		int modifiers = method.getModifiers();
		return !(Modifier.isAbstract(modifiers) || Modifier.isNative(modifiers) || Modifier.isStatic(modifiers))
				&& CallbacksUtil.isAnnotationPresent(method, ObjectCallback.class);
	}
}
