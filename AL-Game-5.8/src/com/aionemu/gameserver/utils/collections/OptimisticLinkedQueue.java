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
package com.aionemu.gameserver.utils.collections;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Optimistic approach to lock-free FIFO queue; E. Ladan-Mozes and N. Shavit algorithm, less CAS failures when enqueueing, if compared with Michael and Scott Nonblocking Queue, in
 * ConcurrentLinkedQueue
 */
@ThreadSafe
public class OptimisticLinkedQueue<E> extends AbstractQueue<E> implements Queue<E>, java.io.Serializable {

	private static final long serialVersionUID = -3445502502831420722L;

	private static class Node<E> {

		private volatile E item;
		private volatile Node<E> next;
		private volatile Node<E> prev;

		@SuppressWarnings("unused")
		Node(E x) {
			item = x;
			next = null;
			prev = null;
		}

		Node(E x, Node<E> n) {
			item = x;
			next = n;
			prev = null;
		}

		E getItem() {
			return item;
		}

		@SuppressWarnings("unused")
		void setItem(E val) {
			this.item = val;
		}

		Node<E> getNext() {
			return next;
		}

		void setNext(Node<E> val) {
			next = val;
		}

		Node<E> getPrev() {
			return prev;
		}

		void setPrev(Node<E> val) {
			prev = val;
		}
	}

	@SuppressWarnings("rawtypes")
	private static final AtomicReferenceFieldUpdater<OptimisticLinkedQueue, Node> tailUpdater = AtomicReferenceFieldUpdater.newUpdater(OptimisticLinkedQueue.class, Node.class, "tail");
	@SuppressWarnings("rawtypes")
	private static final AtomicReferenceFieldUpdater<OptimisticLinkedQueue, Node> headUpdater = AtomicReferenceFieldUpdater.newUpdater(OptimisticLinkedQueue.class, Node.class, "head");

	private boolean casTail(Node<E> cmp, Node<E> val) {
		return tailUpdater.compareAndSet(this, cmp, val);
	}

	private boolean casHead(Node<E> cmp, Node<E> val) {
		return headUpdater.compareAndSet(this, cmp, val);
	}

	/**
	 * Pointer to the head node, initialized to a dummy node. The first actual node is at head.getPrev().
	 */
	private transient volatile Node<E> head = new Node<E>(null, null);
	/**
	 * Pointer to last node on list
	 */
	private transient volatile Node<E> tail = head;

	/**
	 * Creates a <tt>OptimisticLinkedQueue</tt> that is initially empty.
	 */
	public OptimisticLinkedQueue() {
	}

	AtomicInteger count = new AtomicInteger();

	/**
	 * Enqueues the specified element at the tail of this queue.
	 */
	@Override
	public boolean offer(E e) {
		if (e == null) {
			throw new NullPointerException();
		}
		Node<E> n = new Node<E>(e, null);
		for (;;) {
			Node<E> t = tail;
			n.setNext(t);
			count.incrementAndGet();
			if (casTail(t, n)) {
				t.setPrev(n);
				return true;
			}
		}
	}

	/**
	 * Dequeues an element from the queue. After a successful casHead, the prev and next pointers of the dequeued node are set to null to allow garbage collection.
	 */
	@Override
	public E poll() {
		for (;;) {
			Node<E> h = head;
			Node<E> t = tail;
			Node<E> first = h.getPrev();
			if (h == head) {
				if (h != t) {
					if (first == null) {
						fixList(t, h);
						continue;
					}
					E item = first.getItem();
					if (casHead(h, first)) {
						h.setNext(null);
						h.setPrev(null);
						count.decrementAndGet();
						return item;
					}
				}
				else {
					return null;
				}
			}
		}
	}

	/**
	 * Fixing the backwards pointers when needed
	 */
	private void fixList(Node<E> t, Node<E> h) {
		Node<E> curNodeNext;
		Node<E> curNode = t;
		while (h == this.head && curNode != h) {
			curNodeNext = curNode.getNext();
			curNodeNext.setPrev(curNode);
			curNode = curNode.getNext();
		}
	}

	@Override
	public void clear() {
		while (poll() != null);
	}

	public int leaveTail() {
		E elem = null;
		E elem1 = null;
		int removed = 0;
		while ((elem = poll()) != null) {
			elem1 = elem;
			removed++;
		}
		if (elem1 != null) {
			removed--;
			offer(elem1);
		}
		return removed;
	}

	@Override
	public E peek() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<E> iterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return count.get();
	}
}
