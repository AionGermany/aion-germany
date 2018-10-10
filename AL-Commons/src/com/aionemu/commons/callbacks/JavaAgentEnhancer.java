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

package com.aionemu.commons.callbacks;

import java.lang.instrument.Instrumentation;

import com.aionemu.commons.callbacks.enhancer.GlobalCallbackEnhancer;
import com.aionemu.commons.callbacks.enhancer.ObjectCallbackEnhancer;

/**
 * This class is used as javaagent to do on-class-load transformations with
 * objects whose methods are marked by
 * {@link com.aionemu.commons.callbacks.metadata.ObjectCallback} or
 * {@link com.aionemu.commons.callbacks.metadata.GlobalCallback} annotation.<br>
 * Code is inserted dynamicly before method call and after method call.<br>
 * For implementation docs please reffer to:
 * http://www.csg.is.titech.ac.jp/~chiba/javassist/tutorial/tutorial2.html<br>
 * <br>
 * Usage: java -javaagent:lib/ae_commons.jar
 *
 * @author SoulKeeper
 */
public class JavaAgentEnhancer {

	/**
	 * Premain method that registers this class as ClassFileTransformer
	 *
	 * @param args
	 *            arguments passed to javaagent, ignored
	 * @param instrumentation
	 *            Instrumentation object
	 */
	public static void premain(String args, Instrumentation instrumentation) {
		instrumentation.addTransformer(new ObjectCallbackEnhancer(), true);
		instrumentation.addTransformer(new GlobalCallbackEnhancer(), true);
	}
}