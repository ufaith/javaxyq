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
		//FIXME ��ʼ������
		//data.level = cfg.level;
		DataStore.initPlayerData(data);
		DataStore.recalcProperties(data);
		System.out.println("create player: "+data);
		player.setData(data);
		return player;
	}
	
	/**
	 * ������ͨ��С�֣�����������
	 * @param type
	 * @param name
	 * @param level
	 * @return
	 */
	public static Player createElf(String type,String name,int level) {
		Player player = store.createPlayer(type,null);
		player.setName(name);
		//��ʼ����������
		PlayerVO data = new PlayerVO("elf-"+(++elfId),name,type);
		data.level = level;
		//��ʼ���������������
		initElf(data);
		player.setData(data);
		//player.setNameForeground(GameMain.TEXT_NAME_NPC_COLOR);
		System.out.println("create elf: "+data);
		return player;
	}

	private static int elfId;

	public static String[] �����б� = { "���ƹٸ�", "����ɽ" ,"������", "Ů����", "���ܵظ�", "ħ��կ", "ʨ����", "��˿�� ","�칬" ,
	                         "����", "��ׯ��", "����ɽ"};
	private static void initElf(PlayerVO elf) {
		//TODO �������Ե�������
		Random random = new Random(); 
		//��ʼ������5�㣬ÿ�ȼ�����1��
		int base = elf.level *1 +5;
		//Ұ����������������25����ʼ50���һ�룩+ �ȼ�*3��ÿ�ȼ�3�㣩
		int toAssign = 25 + elf.level * 3;
		int[] assigns = new int[5];
		//����������
		for(int i=0;i<toAssign;i++) {
			assigns[random.nextInt(5)] ++;
		}
		elf.���� = base + assigns[0];
		elf.ħ�� = base + assigns[1];
		elf.���� = base + assigns[2];
		elf.���� = base + assigns[3];
		elf.���� = base + assigns[4];
		//�������
		elf.���� = �����б�[random.nextInt(12)];
		//���¼�������
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
//				System.err.println("��ֵʧ��! $attr=$val");
//				e.printStackTrace();
//			}
//		}
		store.registerNPC(sceneId, playerCfg);
	}


}
