/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.event;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.javaxyq.widget.Player;

/**
 * @author 龚德伟
 * @history 2008-6-18 龚德伟 新建
 */
public class EventDispatcher {

    private static EventDispatcher instance = new EventDispatcher();

    private BlockingQueue<EventPeer> eventQueue = new LinkedBlockingQueue<EventPeer>();
    
    private EventProcessor eventProcessor = new EventProcessor();

    private EventDispatcher() {
        eventProcessor.start();
    }

    public void dispatchEvent(Player source, PlayerEvent e) {
        eventQueue.offer(new EventPeer(source,e));
    }

    public static EventDispatcher getInstance() {
        return instance;
    }

    private class EventProcessor extends Thread {
        public EventProcessor() {
            setName("EventProcessor");
            setDaemon(true);
        }

        @Override
        public void run() {
            while (true) {
            	//System.out.println(this.getId()+" "+this.getName());
            	try {
            		//等待下一个事件
            		EventPeer peer = eventQueue.take();
            		if (peer != null) {
            			peer.getPlayer().processEvent(peer.getEvent());
            		}
            	} catch (Exception e) {
            		System.err.println("event process error!");
            		e.printStackTrace();
            	}
            }
        }
    }
    
    private class EventPeer {
        private Player player;
        private PlayerEvent event;

        public EventPeer(Player p,PlayerEvent e) {
            this.player = p;
            this.event = e;
        }

        public Player getPlayer() {
            return player;
        }

        public PlayerEvent getEvent() {
            return event;
        }
        
    }
}
