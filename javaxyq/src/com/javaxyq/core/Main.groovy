package com.javaxyq.core;
import groovy.ui.InteractiveShell;

import com.javaxyq.event.SceneListener;

import javax.swing.Action;

import com.javaxyq.action.*;
import com.javaxyq.event.*;
import com.javaxyq.core.*;
import com.javaxyq.config.*;
import com.javaxyq.task.TaskManager;
import com.javaxyq.trigger.Transport;
import javax.swing.KeyStroke;
import com.javaxyq.trigger.JumpTrigger;

import com.javaxyq.ui.UIHelper;
import com.javaxyq.ui.XmlDialogBuilder;
import com.javaxyq.util.ClassUtil;

import groovy.util.Node;
import groovy.util.XmlParser;
/**
 * @author dewitt
 *
 */
class Main {
	
	public static void main(String[] args) {
		
		ClassUtil.init();
		UIHelper.init();
		DataStore.init();
		ItemManager.init();
		
		GameMain.setDebug(false);
		GameMain.setShowCopyright(false);
		GameMain.setApplicationName("JavaXYQ ");
		GameMain.setVersion("1.4 M1");
		GameMain.setHomeURL("http://javaxyq.googlecode.com/");
		
		defActions();
		
		defCursors();
		
		defScenes();
		
		defTalks();
		
		preprocessUIs();
		
		Helper.loadNPCs();
		
		GameMain.registerAction("com.javaxyq.action.transport",new DefaultTransportAction());
		MovementManager.addMovementAction("random", new RandomMovementAction());
		
		//task
		TaskManager.instance.register("school", "com.javaxyq.task.SchoolTaskCoolie");
		
		GameMain.init(args);
		DataStore.loadData();
		GameMain.stopLoading();
		System.out.println("游戏加载完毕！");

	}


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

	public static void addListener(String type,String className){
		GameMain.addListener(type,Class.forName(className));
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
			addListener(el.@type,el.@class);
		}
	}
	public static void defCursors() {
		def cursors = new XmlParser().parse(GameMain.getFile("xml/cursors.xml"));
		def rs = ResourceStore.getInstance();
		for(el in cursors.Cursor) {
			System.out.println "define Cursor: ${el.@id}"
			rs.registerCursor(new CursorConfig(el.@id, el.@cursor, el.@effect));
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

	public static void preprocessUIs() {
		//def filelist = GameMain.getFile("ui").list().findAll{ it.endsWith(".xml")};
//		for(def file in filelist) {
//			file = "ui/"+file;
//			System.out.println("find ui: $file")
//			//processUI(file);
//			def dialogs = new XmlParser().parse(GameMain.getFile(file));
//			for(def dlg in dialogs.Dialog) {
//				DialogFactory.addDialog(dlg.@id, file);
//			}
//		}
		
		GameMain.getFile("ui/list.txt").eachLine{
			try {
				String file = "ui/"+it;
				System.out.println("find ui: $file")
				//processUI(file);
				def dialogs = new XmlParser().parse(GameMain.getFile(file));
				for(def dlg in dialogs.Dialog) {
					DialogFactory.addDialog(dlg.@id, file);
				}
			}catch(e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void setScene(id,x,y){
		if(!id || id=="null")id="wzg";
		SceneListener action = null;
		try {
		String currentScene = GameMain.getCurrentScene();
		if(currentScene) {
			action = findSceneAction(currentScene);
			if(action)action.onUnload(new SceneEvent(currentScene,-1,-1));
		}
		}catch(e) {e.printStackTrace();}
		System.out.println("切换场景："+id+" ("+x+","+y+")");
		try {
			action = findSceneAction(id);
			if(action)action.onInit(new SceneEvent(id,x,y));
		}catch(e) {e.printStackTrace();};
	
		GameMain.fadeToMap(id,x,y);
		
		try {
			if(action)action.onLoad(new SceneEvent(id,x,y));
		}catch(e) {e.printStackTrace();};
	}

	public static Object findSceneAction(String id) {
		def action = GroovyScript.loadClass("scripts/scene/${id}.groovy");
		return action;
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
}
