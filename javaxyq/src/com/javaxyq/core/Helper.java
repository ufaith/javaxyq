/*
 * JavaXYQ Source Code
 * by kylixs
 * http://javaxyq.googlecode.com
 * kylixs@qq.com
 */
package com.javaxyq.core;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Random;

import org.apache.commons.beanutils.BeanUtils;

import com.javaxyq.config.PlayerConfig;
import com.javaxyq.data.DataStore;
import com.javaxyq.model.PlayerVO;
import com.javaxyq.widget.Player;

/**
 * @author dewitt
 *
 */
public class Helper {
	
	public static ResourceStore store = ResourceStore.getInstance(); 
	public static Player createPlayer(String character,Map<String,Object> cfg) {
		Player player = store.createPlayer(character,(int[]) cfg.get("colorations"));
		PlayerVO data = new PlayerVO((String)cfg.get("id"),(String)cfg.get("name"),character);
		try {
			BeanUtils.populate(player, cfg);
			BeanUtils.populate(data, cfg);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
//		for(String attr : cfg.keySet()) {
//			try {
//				player[attr] = cfg[attr];
//			}catch(e) {
//				try {
//					data[attr] = cfg[attr];
//				}catch(e) {
//					
//				}
//			}
//		}
		//FIXME 初始化属性
		//data.level = cfg.level;
		DataStore.initPlayerData(data);
		DataStore.recalcProperties(data);
		System.out.println("create player: "+data);
		player.setData(data);
		return player;
	}
	
	/**
	 * 创建普通的小怪（场景练级）
	 * @param type
	 * @param name
	 * @param level
	 * @return
	 */
	public static Player createElf(String type,String name,int level) {
		Player player = store.createPlayer(type,null);
		player.setName(name);
		//初始化怪物属性
		PlayerVO data = new PlayerVO("elf-"+(++elfId),name,type);
		data.level = level;
		//初始化场景怪物的属性
		initElf(data);
		player.setData(data);
		//player.setNameForeground(GameMain.TEXT_NAME_NPC_COLOR);
		System.out.println("create elf: "+data);
		return player;
	}

	private static int elfId;

	public static String[] 门派列表 = { "大唐官府", "方寸山" ,"化生寺", "女儿村", "阴曹地府", "魔王寨", "狮驼岭", "盘丝洞 ","天宫" ,
	                         "龙宫", "五庄观", "普陀山"};
	private static void initElf(PlayerVO elf) {
		//TODO 完善属性点数分配
		Random random = new Random(); 
		//初始至少有5点，每等级至少1点
		int base = elf.level *1 +5;
		//野生怪物待分配点数：25（初始50点的一半）+ 等级*3（每等级3点）
		int toAssign = 25 + elf.level * 3;
		int[] assigns = new int[5];
		//随机分配点数
		for(int i=0;i<toAssign;i++) {
			assigns[random.nextInt(5)] ++;
		}
		elf.体质 = base + assigns[0];
		elf.魔力 = base + assigns[1];
		elf.力量 = base + assigns[2];
		elf.防御 = base + assigns[3];
		elf.敏捷 = base + assigns[4];
		//随机门派
		elf.门派 = 门派列表[random.nextInt(12)];
		//重新计算属性
		DataStore.recalcElfProps(elf);
	}
	
	public static  void registerNPC(String sceneId, Map cfg) {
		PlayerConfig playerCfg = new PlayerConfig();
		try {
			BeanUtils.populate(playerCfg, cfg);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
//		for(attr in cfg.keySet()) {
//			def val = cfg.get(attr)
//			try {
//				def clazz = playerCfg.class.getDeclaredField(attr).getType();
//				if(clazz == int)clazz = Integer
//				else if(clazz ==long)clazz = Long;
//				//System.out.println("$clazz: $attr = $val");
//				playerCfg[attr] = val.asType(clazz);
//			}catch(Exception e) {
//				System.err.println("赋值失败! $attr=$val");
//				e.printStackTrace();
//			}
//		}
		store.registerNPC(sceneId, playerCfg);
	}


}
