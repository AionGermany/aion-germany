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

/**
 * This class represents callback result
 *
 * @param <T>
 *            Type of callback result
 * @author SoulKeeper
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class CallbackResult<T> {

	/**
	 * Continue mask for callbacks, future invocation of method or other
	 * callbacks is not blocked
	 */
	public static final int CONTINUE = 0x00;

	/**
	 * Block callbacks mask, future callbacks will be blocked, but method won't
	 * be
	 */
	public static final int BLOCK_CALLBACKS = 0x01;

	/**
	 * Method will be blocked, but not callbacks
	 */
	public static final int BLOCK_CALLER = 0x02;

	/**
	 * Caller and another callbacks will be blocked
	 */
	public static final int BLOCK_ALL = 0x01 | 0x02;

	/**
	 * Cache for continue instance
	 */
	private static final CallbackResult INSTANCE_CONTINUE = new CallbackResult(CONTINUE);

	/**
	 * Cache for callback blocker
	 */
	private static final CallbackResult INSTANCE_BLOCK_CALLBACKS = new CallbackResult(BLOCK_CALLBACKS);

	/**
	 * Result of callback invokation, used only when caller is blocked by
	 * callback
	 */
	private final T result;

	/**
	 * What this callback is blocking
	 */
	private final int blockPolicy;

	/**
	 * Creates new callback with specified blocking policy
	 *
	 * @param blockPolicy
	 *            what this callback should block
	 */
	private CallbackResult(int blockPolicy) {
		this(null, blockPolicy);
	}

	/**
	 * Creates new callback with specified blocking policy and result
	 *
	 * @param result
	 *            result of callback
	 * @param blockPolicy
	 *            what this callback blocks
	 */
	private CallbackResult(T result, int blockPolicy) {
		this.result = result;
		this.blockPolicy = blockPolicy;
	}

	/**
	 * Retruns result of this callback
	 *
	 * @return result of this callback
	 */
	public T getResult() {
		return result;
	}

	/**
	 * Returns true if is blocking callbacks
	 *
	 * @return true if is blocking callbacks
	 */
	public boolean isBlockingCallbacks() {
		return (blockPolicy & BLOCK_CALLBACKS) != 0;
	}

	/**
	 * Returns true if is blocking caller
	 *
	 * @return true if is blocking caller
	 */
	public boolean isBlockingCaller() {
		return (blockPolicy & BLOCK_CALLER) != 0;
	}

	/**
	 * Returns callback for continue action, for perfomance reasons returns
	 * cached instance
	 *
	 * @param <T>
	 *            type of result object, ignored, always null
	 * @return callback with result type continue
	 */
	public static <T> CallbackResult<T> newContinue() {
		return INSTANCE_CONTINUE;
	}

	/**
	 * Returns callback that blocks another callbacks, cached instance is used
	 * for perfomance reasons
	 *
	 * @param <T>
	 *            type of result object, ignored, always null
	 * @return callback that blocks invocation of another callbacks
	 */
	public static <T> CallbackResult<T> newCallbackBlocker() {
		return INSTANCE_BLOCK_CALLBACKS;
	}

	/**
	 * Returns callback that blocks another callbacks and method invocation.<br>
	 * {@link com.aionemu.commons.callbacks.Callback#afterCall(Object, Object[], Object)}
	 * will be invoked with the result from this call.
	 *
	 * @param result
	 *            Result of callback
	 * @param <T>
	 *            type of result
	 * @return new callback instance with given result that will be returned as
	 *         method result
	 */
	public static <T> CallbackResult<T> newFullBlocker(T result) {
		return new CallbackResult<T>(result, BLOCK_ALL);
	}
}
