/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.event;

import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author 龚德伟
 * @history 2008-6-18 龚德伟 新建
 */
public class EventDispatcher<S extends EventTarget, E extends EventObject> {

	/** 事件调度器实例表 */
	private static final Map<Object, EventDispatcher> instances = new HashMap<Object, EventDispatcher>();

	private BlockingQueue<E> eventQueue = new LinkedBlockingQueue<E>();

	private EventProcessor eventProcessor = null;

	private EventDispatcher() {
		eventProcessor = new EventProcessor();
		eventProcessor.start();
	}

	public void dispatchEvent(E evt) {
		eventQueue.offer(evt);
	}

	public static <T1 extends EventTarget, T2 extends EventObject> EventDispatcher<T1, T2> getInstance(
			Class<T1> clazz1, Class<T2> clazz2) {
		EventDispatcher<T1, T2> dispatcher = instances.get(clazz1);
		if (dispatcher == null) {
			dispatcher = new EventDispatcher<T1, T2>();
			instances.put(clazz1, dispatcher);
		}
		return dispatcher;
	}

	private static int processorCount;

	private class EventProcessor extends Thread {
		public EventProcessor() {
			processorCount++;
			setName("EventProcessor-" + processorCount);
			setDaemon(true);
		}

		@Override
		public void run() {
			while (true) {
				// System.out.println(this.getId()+" "+this.getName());
				try {
					// 等待下一个事件
					E evt = eventQueue.take();
					if (evt != null) {
						((S) evt.getSource()).handleEvent(evt);
					}
				} catch (Exception e) {
					System.err.println("event process error!");
					e.printStackTrace();
				}
			}
		}
	}
}
