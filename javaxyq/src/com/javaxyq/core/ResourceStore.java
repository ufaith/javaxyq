/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.core;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import com.javaxyq.action.DefaultTalkAction;
import com.javaxyq.config.Config;
import com.javaxyq.config.CursorConfig;
import com.javaxyq.config.ImageConfig;
import com.javaxyq.config.MapConfig;
import com.javaxyq.config.PlayerConfig;
import com.javaxyq.config.PlayerDefine;
import com.javaxyq.config.StateConfig;
import com.javaxyq.trigger.Trigger;
import com.javaxyq.widget.Cursor;
import com.javaxyq.widget.Player;
import com.javaxyq.widget.Sprite;
import com.javaxyq.widget.SpriteImage;
import com.javaxyq.widget.Widget;

/**
 * @author 龚德伟
 * @history 2008-5-31 龚德伟 新建
 */
public class ResourceStore {
	private static ResourceStore store = new ResourceStore();

	private Map<String, Config> configMap = new HashMap<String, Config>();

	private Map<String, Widget> widgetMap = new WeakHashMap<String, Widget>();

	private Map<String, List<Trigger>> triggersMap = new HashMap<String, List<Trigger>>();

	private Map<String, List<PlayerConfig>> sceneNpcsMap = new HashMap<String, List<PlayerConfig>>();

	private Map<String, Player> npcMap = new WeakHashMap<String, Player>();

	private Map<String, PlayerDefine> playerDefines = new HashMap<String, PlayerDefine>();

	private Map<String, Cursor> cursorMap = new HashMap<String, Cursor>();

	private DefaultTalkAction defaultTalkAction = new DefaultTalkAction();

	private ResourceStore() {
	}

	public static ResourceStore getInstance() {
		return store;
	}

	public void registerMap(MapConfig cfg) {
		if (cfg == null) {
			return;
		}
		configMap.put(cfg.getId(), cfg);
	}

	public void registerTrigger(String scenceId, Trigger t) {
		List<Trigger> listTrigger = triggersMap.get(scenceId);
		if (listTrigger == null) {
			listTrigger = new ArrayList<Trigger>();
			triggersMap.put(scenceId, listTrigger);
		}
		listTrigger.add(t);
	}

	public Config findConfig(String id) {
		return configMap.get(id);
	}

	public Widget findWidget(String id) {
		return widgetMap.get(id);
	}

	public List<Trigger> findTriggers(String sceneId) {
		return triggersMap.get(sceneId);
	}

	public List<Player> findNPCs(String sceneId) {
		List<Player> listPlayer = new ArrayList<Player>();
		List<PlayerConfig> listPlayerCfg = this.sceneNpcsMap.get(sceneId);
		if (listPlayerCfg == null) {
			listPlayerCfg = new ArrayList<PlayerConfig>();
		}
		for (PlayerConfig cfg : listPlayerCfg) {
			listPlayer.add(this.findNPC(cfg));
		}
		return listPlayer;
	}

	private Player findNPC(PlayerConfig cfg) {
		Player npc = this.npcMap.get(cfg.getId());
		if (npc == null) {
			npc = this.createNPC(cfg);
			this.npcMap.put(cfg.getId(), npc);
		}
		return npc;
	}

	//FIXME 重复创建同一个角色时，动画更新有问题（共享了同一个Sprite！）
	public Player createPlayer(PlayerConfig cfg) {
		Player player = new Player(cfg.getId(), cfg.getName(), cfg.getCharacter());
		player.setSceneLocation(cfg.getX(), cfg.getY());
		String strColorations = cfg.getColorations();
		if (strColorations != null) {// 染色
			String[] colors = strColorations.split(",");
			int[] colorations = new int[colors.length];
			for (int i = 0; i < colors.length; i++) {
				colorations[i] = colors[i].charAt(0) - '0';
			}
			player.setColorations(colorations, false);//在setState之前
		}
		player.setState(cfg.getState());
		player.setDirection(cfg.getDirection());
		String mAction = cfg.getMovement();
		if (mAction != null) {
			MovementManager.put(player, mAction, cfg.getPeriod());
		}
		return player;
	}

	public Player createPlayer(String character, List<Integer> colorations) {
		Player player = new Player(null, "未命名", character);
		if(colorations!=null) {
			int[] colors = new int[colorations.size()];
			for (int i = 0; i < colors.length; i++) {
				colors[i] = colorations.get(i);
			}
			player.setColorations(colors, false);
		}
		player.setState(Player.STATE_STAND);
		player.setDirection(0);
		return player;
	}

	public void definePlayer(PlayerDefine def) {
		this.playerDefines.put(def.getCharacter(), def);
	}

	public PlayerDefine getPlayerDefine(String character) {
		return playerDefines.get(character);
	}

	public void registerNPC(String sceneId, PlayerConfig cfg) {
		List<PlayerConfig> listPlayer = this.sceneNpcsMap.get(sceneId);
		if (listPlayer == null) {
			listPlayer = new ArrayList<PlayerConfig>();
			this.sceneNpcsMap.put(sceneId, listPlayer);
		}
		listPlayer.add(cfg);
	}

	public Player createNPC(PlayerConfig cfg) {
		Player p = this.createPlayer(cfg);
		p.setNameForeground(GameMain.TEXT_NAME_NPC_COLOR);
		p.addPlayerListener(defaultTalkAction);
		return p;
	}

	public SpriteImage createImage(ImageConfig cfg) {
		Sprite s = SpriteFactory.loadSprite(cfg.getPath());
		int width = cfg.getWidth();
		int height = cfg.getHeight();
		if (width > 0 && height > 0) {
			return new SpriteImage(s, cfg.getX(), cfg.getY(), width, height);
		}
		return new SpriteImage(s, cfg.getX(), cfg.getY());
	}

	public Cursor getCursor(String cursorId) {
		return cursorMap.get(cursorId);
	}

	private Cursor loadCursor(CursorConfig cfg) {
		SpriteImage pointer = new SpriteImage(SpriteFactory.loadSprite(cfg.getPointer()));
		SpriteImage effect = null;
		if (cfg.getEffect() != null) {
			effect = new SpriteImage(SpriteFactory.loadSprite(cfg.getEffect()));
		}
		Cursor cursor = new Cursor(cfg, pointer, effect);
		return cursor;
	}

	public void registerCursor(CursorConfig cfg) {
		this.cursorMap.put(cfg.getId(), this.loadCursor(cfg));
	}

	public Sprite findHead(String character) {
		if(character.compareTo("0012")<=0) {			
			return SpriteFactory.loadSprite("/wzife/photo/hero/" + character + ".tcp");
		}
		return SpriteFactory.loadSprite("/wzife/photo/npc/" + character + ".tcp");
	}

}
