package com.javaxyq.core;
import groovy.ui.InteractiveShell;

import com.javaxyq.event.SceneListener;

import javax.swing.Action;

import com.javaxyq.action.*;
import com.javaxyq.event.*;
import com.javaxyq.core.*;
import com.javaxyq.config.*;
import com.javaxyq.trigger.Transport;
import javax.swing.KeyStroke;
import com.javaxyq.trigger.JumpTrigger;

import com.javaxyq.ui.UIHelper;
import com.javaxyq.ui.XmlDialogBuilder;
import com.javaxyq.util.ClassUtil;
/**
 * @author dewitt
 *
 */
class Main {
	
	private static Map<String,Object> sceneActions = new HashMap<String,Object>();
	
	public static void main(String[] args) {
		
		ClassUtil.init();
		DialogFactory.init(new XmlDialogBuilder())
		UIHelper.init();
		DataStore.init();
		ItemManager.init();
		
		GameMain.setDebug(true);
		GameMain.showCopyright = false;
		GameMain.setApplicationName("JavaXYQ ");
		GameMain.setVersion('1.4 build 20091021(Groovy)');
		GameMain.setHomeURL('http://kylixs.blog.163.com/');
		
		defActions();
		
		defCursors();
		
		defScenes();
		
//		defPlayers();
		
		defTalks();
		
		preprocessUIs();
		
		GameMain.registerAction("com.javaxyq.action.transport",new DefaultTransportAction());
		MovementManager.addMovementAction("random", new RandomMovementAction());
		
		GameMain.init(args);
//		installUI();
		def p = Helper.createPlayer('0010',[
           name:'逍遥葫芦',
           level : 5,
           direction:0,
           state:'stand',
           colorations: [2,4,3]
           ]);
		p.setSceneLocation(52,32);
		GameMain.setPlayer(p);
		setScene("wzg",p.sceneX,p.sceneY);//五庄观	
		GameMain.stopLoading();
		
		println("游戏加载完毕！");

		def item = DataStore.createItem('四叶花');
		item.amount = 99;
		DataStore.addItemToPlayer(p,item);
		item = DataStore.createItem('佛手');
		item.amount = 99;
		DataStore.addItemToPlayer(p,item);
		
	}


	public static void defShortcut(String key,String actionId){
		GameMain.getInputMap().put(KeyStroke.getKeyStroke(key), actionId);
	}
	
	public static void defAction(String actionId,String className){
		try {
		Action action = (Action) Class.forName(className).newInstance();
		action.putValue(Action.ACTION_COMMAND_KEY, actionId);
		GameMain.actionMap.put(actionId, action);
		}catch(ClassNotFoundException e) {
			println("警告：找不到[ ${actionId}] 的处理类${className}")
		}
	}

	public static void addListener(String type,String className){
		GameMain.addListener(type,Class.forName(className));
	}

	public static void defActions() {
		def xml = new XmlParser().parse(new File("resources/actions.xml"));
		def rs = ResourceStore.getInstance();
		for(el in xml.Action) {
			println "define Action: ${el.@id}"
			defAction(el.@id,el.@class);
			if(el.@shortcut) {
				defShortcut(el.@shortcut,el.@id);
			}
		}
		for(el in xml.Listener) {
			println "define Listener: ${el.@type} -> ${el.@class}"
			addListener(el.@type,el.@class);
		}
	}
	public static void defCursors() {
		def cursors = new XmlParser().parse(new File("resources/cursors.xml"));
		def rs = ResourceStore.getInstance();
		for(el in cursors.Cursor) {
			println "define Cursor: ${el.@id}"
			rs.registerCursor(new CursorConfig(el.@id, el.@cursor, el.@effect));
		}
	}
	public static void defScenes() {
		def scenes = new XmlParser().parse(new File("resources/scenes.xml"));
		def rs = ResourceStore.getInstance();
		for(scene in scenes.scene) {
			println "define scene: ${scene.@id}"
			rs.registerMap(new MapConfig(scene.@id,scene.@name,scene.@map,scene.@music));
			for(t in scene.transport) {
				rs.registerTrigger( scene.@id, new JumpTrigger(
						new Transport(scene.@id,t.@x.toInteger(),t.@y.toInteger(),
								t.@toSence,t.@toX.toInteger(),t.@toY.toInteger())));
			}
			sceneActions[scene.@id] = scene.@listener;
		}
		
		
	}

	public static void defPlayers() {
		def players = new XmlParser().parse(new File("resources/players.xml"));
		def rs = ResourceStore.getInstance();
		for ( player in players.player) {
			println "define player: ${player.@character}"
			def playerDef = new PlayerDefine();
			playerDef.character = player.@character;
			playerDef.profile = player.@profile;
			for(s in player.state) {
				println "\tstate: ${s.@id}"
				playerDef.addState(new StateConfig(s.@id, s.@person, s.@weapon))
			}
			rs.definePlayer(playerDef);
		}		
	}
	
	public static void preprocessUIs() {
		def filelist = new File('ui').list().findAll{ it.endsWith('.xml')};
		for(def file in filelist) {
			file = 'ui/'+file;
			println("find ui: $file")
			//processUI(file);
			def dialogs = new XmlParser().parse(new File(file));
			for(def dlg in dialogs.Dialog) {
				DialogFactory.registerDialog(dlg.@id, file);
			}
		}
	}
	
//	public static void installUI(){
//		def uiIds=['ui.main.dialog1','ui.main.dialog2','ui.main.dialog3']
//		for(id in uiIds) {
//			println("安装UI："+id);
//			def dlg = DialogFactory.getDialog(id);
//			GameMain.addUIComponent(dlg);
//		}
//		//GameMain.initUI();
//	}	

	public static void setScene(id,x,y){
		String currentScene = GameMain.getCurrentScene();
		SceneListener action = findSceneAction(currentScene);
		if(action)action.onUnload(new SceneEvent(currentScene,-1,-1));
		println("切换场景："+id+" ("+x+","+y+")");
		action = findSceneAction(id);
		if(action)action.onInit(new SceneEvent(id,x,y));
		GameMain.fadeToMap(id,x,y);
		if(action)action.onLoad(new SceneEvent(id,x,y));
	}

	public static Object findSceneAction(String id) {
		def action = sceneActions[id];
		if(action) {
			if (action instanceof String) {
				action = Class.forName(action).newInstance();
				sceneActions[id] = action;
			}
		}
		return action;
	}

	public static void defTalks() {
		def talks = new XmlParser().parse(new File("resources/talks.xml"));
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
