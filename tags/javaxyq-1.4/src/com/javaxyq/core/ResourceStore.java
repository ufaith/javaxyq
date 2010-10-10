/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.WeakHashMap;

import com.javaxyq.config.Config;
import com.javaxyq.config.ImageConfig;
import com.javaxyq.config.MapConfig;
import com.javaxyq.config.PlayerConfig;
import com.javaxyq.data.SceneNpc;
import com.javaxyq.trigger.Trigger;
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

	//private Map<String, Config> configMap = new HashMap<String, Config>();

	private Map<String, Widget> widgetMap = new WeakHashMap<String, Widget>();

	//private Map<String, List<Trigger>> triggersMap = new HashMap<String, List<Trigger>>();

	//private Map<String, List<PlayerConfig>> sceneNpcsMap = new HashMap<String, List<PlayerConfig>>();

	//private List<PlayerConfig> allNpcs = new ArrayList<PlayerConfig>();

	private ResourceStore() {
	}

	public static ResourceStore getInstance() {
		return store;
	}

//	public void registerMap(MapConfig cfg) {
//		if (cfg == null) {
//			return;
//		}
//		configMap.put(cfg.getId(), cfg);
//	}
//
//	public void registerTrigger(String scenceId, Trigger t) {
//		List<Trigger> listTrigger = triggersMap.get(scenceId);
//		if (listTrigger == null) {
//			listTrigger = new ArrayList<Trigger>();
//			triggersMap.put(scenceId, listTrigger);
//		}
//		listTrigger.add(t);
//	}
//
//	public Config findConfig(String id) {
//		return configMap.get(id);
//	}

	public Widget findWidget(String id) {
		return widgetMap.get(id);
	}

//	public List<Trigger> createTriggers(String sceneId) {
//		return triggersMap.get(sceneId);
//	}
//
//	public List<Player> createNPCs(String sceneId) {
//		List<Player> listPlayer = new ArrayList<Player>();
//		List<PlayerConfig> listPlayerCfg = this.sceneNpcsMap.get(sceneId);
//		if (listPlayerCfg == null) {
//			listPlayerCfg = new ArrayList<PlayerConfig>();
//		}
//		for (PlayerConfig cfg : listPlayerCfg) {
//			listPlayer.add(this.createNPC(cfg));
//		}
//		return listPlayer;
//	}

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

//	public Player createPlayer(String character, List<Integer> colorations) {
//		if(colorations!=null) {
//			int[] colors = new int[colorations.size()];
//			for (int i = 0; i < colors.length; i++) {
//				colors[i] = colorations.get(i);
//			}
//			return createPlayer(character,)
//		}
//	}
	public Player createPlayer(String character, int[] colorations) {
		Player player = new Player(null, "未命名", character);
		if(colorations!=null) {
			player.setColorations(colorations, false);
		}
		player.setState(Player.STATE_STAND);
		player.setDirection(0);
		return player;
	}


	/**
	 * 注册NPC
	 * @param sceneId
	 * @param cfg
	 */
//	public void registerNPC(String sceneId, PlayerConfig cfg) {
//		List<PlayerConfig> listPlayer = this.sceneNpcsMap.get(sceneId);
//		if (listPlayer == null) {
//			listPlayer = new ArrayList<PlayerConfig>();
//			this.sceneNpcsMap.put(sceneId, listPlayer);
//		}
//		listPlayer.add(cfg);
//		allNpcs.add(cfg);
//	}
//	
//	public void clearNPC(String sceneId) {
//		List<PlayerConfig> listPlayer = this.sceneNpcsMap.get(sceneId);
//		if (listPlayer != null) {
//			allNpcs.removeAll(listPlayer);
//			listPlayer.clear();
//		}
//	}

	/**
	 * 创建NPC实例
	 * @param sceneNpc
	 * @return
	 */
	public Player createNPC(SceneNpc _npc) {
		Player player = new Player(String.valueOf(_npc.getId()), _npc.getName(), _npc.getCharacterId());
		player.setSceneLocation(_npc.getSceneX(), _npc.getSceneY());
		//String strColorations = cfg.getColorations();
//		if (strColorations != null) {// 染色
//			String[] colors = strColorations.split(",");
//			int[] colorations = new int[colors.length];
//			for (int i = 0; i < colors.length; i++) {
//				colorations[i] = colors[i].charAt(0) - '0';
//			}
//			player.setColorations(colorations, false);//在setState之前
//		}
		Properties configs = parseConfig(_npc.getConfig());
		player.setState(configs.getProperty("state","stand"));
		player.setDirection(Integer.parseInt(configs.getProperty("direction","0")));
		//String mAction = cfg.getMovement();
//		if (mAction != null) {
//			MovementManager.put(player, mAction, cfg.getPeriod());
//		}
		
		player.setNameForeground(GameMain.TEXT_NAME_NPC_COLOR);
		return player;
	}

	/**
	 * @param config
	 * @return
	 */
	private static Properties parseConfig(String str) {
		Properties configs = new Properties();
		try {
			if(str!=null) {
				String[] entrys = str.split(";");
				for (int i = 0; i < entrys.length; i++) {
					String[] keyvalue = entrys[i].split("=");
					configs.setProperty(keyvalue[0], keyvalue[1]);
				}
			}
		} catch (Exception e) {
			System.out.println("解析Config失败！"+str);
			e.printStackTrace();
		}
		return configs;
	}

	/**
	 * 创建NPC实例
	 * @param cfg
	 * @return
	 */
	public Player createNPC(PlayerConfig cfg) {
		Player p = this.createPlayer(cfg);
		p.setNameForeground(GameMain.TEXT_NAME_NPC_COLOR);
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

	/**
	 * 读取角色的照片（对话时使用）
	 * @param characterId
	 * @return
	 */
	public Sprite findPhoto(String characterId) {
		if(characterId.compareTo("0012")<=0) {			
			return SpriteFactory.loadSprite("/wzife/photo/hero/" + characterId + ".tcp");
		}
		return SpriteFactory.loadSprite("/wzife/photo/npc/" + characterId + ".tcp");
	}

	/**
	 * @return 注册的NPC列表
	 */
//	public List<PlayerConfig> getAllNpcs() {
//		return allNpcs;
//	}
}
