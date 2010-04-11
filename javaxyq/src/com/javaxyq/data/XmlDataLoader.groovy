/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-3-21
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.data

import groovy.util.Node;
import groovy.util.XmlParser;

import javax.swing.Action;
import javax.swing.KeyStroke;

import com.javaxyq.config.LinkConfig;
import com.javaxyq.config.MapConfig;
import com.javaxyq.config.TalkConfig;
import com.javaxyq.core.DialogFactory;
import com.javaxyq.core.GameMain;
import com.javaxyq.core.Helper 
import com.javaxyq.core.ItemManager 
import com.javaxyq.core.ResourceStore;
import com.javaxyq.trigger.JumpTrigger;
import com.javaxyq.trigger.Transport;

/**
 * @author Administrator
 * @date 2010-3-21 create
 */
public class XmlDataLoader {

	public static void defShortcut(String key,String actionId){
		GameMain.getInputMap().put(KeyStroke.getKeyStroke(key), actionId);
	}
	
	public static void defAction(String actionId,String className){
		try {
		Action action = (Action) Class.forName(className).newInstance();
		action.putValue(Action.ACTION_COMMAND_KEY, actionId);
		GameMain.getActionMap().put(actionId, action);
		}catch(ClassNotFoundException e) {
			System.out.println("警告：找不到[ ${actionId}] 的处理类${className}");
		}
	}	
	public static void defActions() {
		Node xml = new groovy.util.XmlParser().parse(GameMain.getFile("xml/actions.xml"));
		def rs = ResourceStore.getInstance();
		for(el in xml.Action) {
			System.out.println "define Action: ${el.@id}"
			defAction(el.@id,el.@class);
			if(el.@shortcut) {
				defShortcut(el.@shortcut,el.@id);
			}
		}
		for(el in xml.Listener) {
			System.out.println "define Listener: ${el.@type} -> ${el.@class}"
			GameMain.addListener(el.@type,el.@class);
		}
	}
	
	public static void defScenes() {
		def scenes = new XmlParser().parse(GameMain.getFile("xml/scenes.xml"));
		def rs = ResourceStore.getInstance();
		for(scene in scenes.scene) {
			System.out.println "define scene: ${scene.@id}"
			rs.registerMap(new MapConfig(scene.@id,scene.@name,scene.@map,scene.@music));
			for(t in scene.transport) {
				rs.registerTrigger( scene.@id, new JumpTrigger(
						new Transport(scene.@id,t.@x.toInteger(),t.@y.toInteger(),
								t.@toSence,t.@toX.toInteger(),t.@toY.toInteger())));
			}
		}
		
		
	}

	public static void defTalks() {
		def talks = new XmlParser().parse(GameMain.getFile("xml/talks.xml"));
		for (def t : talks.Scene.NPC.talk) {
			TalkConfig talk;
			if (t.text()) {
				talk = new TalkConfig(t.text());
			} else {
				talk = new TalkConfig(t.text.text());
				for (def link in t.link) {
					talk.addLink(new LinkConfig(link.text(), link.@action, link.@value));
				}
			}
			talk.setId(t.@id);
			DataStore.addTalk(t.parent().@id,talk);
		}
				
	}

	public static void loadUI(String file) {
		def dialogs = new XmlParser().parse(GameMain.getFile(file));
		for(def dlg in dialogs.Dialog) {
			DialogFactory.addDialog(dlg.@id, file);
		}
		
	}

	/**
	 * 加载场景npc数据
	 */
	public static void loadNPCs() {
		def xml =  new XmlParser().parse(GameMain.getFile("xml/npcs.xml"));
		def npcs = xml.Scene.NPC;
		for(def npc in npcs) {
			Helper.registerNPC(npc.parent().@id,npc.attributes());
		}
	}
	
	public static void loadItems() {
		try {
		def xml = new XmlParser().parse(GameMain.getFile('xml/items.xml'));
		for(Node item in xml.Item) {
			if(item.@type && item.@class) {
				try {
				ItemManager.addItem(item.@type, Class.forName(item.@class).newInstance());
				}catch(Exception e) {
					println "加载物品的处理类失败！${item.@type} => ${item.@class}"
					e.printStackTrace();
				}
			}
		}
		}catch(e) {
			println "load items failed!";
			e.printStackTrace();
		}
	}
	
}
