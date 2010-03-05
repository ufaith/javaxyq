/*
 * JavaXYQ Source Code
 * by kylixs
 * http://javaxyq.googlecode.com
 * kylixs@qq.com
 */
package com.javaxyq.core;

import com.javaxyq.model.PlayerVO;
import com.javaxyq.widget.Player;

import java.util.Map;

import com.javaxyq.config.PlayerConfig;
import com.javaxyq.core.ResourceStore;

/**
 * @author dewitt
 *
 */
public class Helper {
	
	public static ResourceStore store = ResourceStore.getInstance(); 
	public static Player createPlayer(String character,Map cfg) {
		def player = store.createPlayer(character,cfg.colorations);
		def data = new PlayerVO(cfg['id'],cfg['name'],character);
		for(attr in cfg.keySet()) {
			try {
				player[attr] = cfg[attr];
			}catch(e) {};
			try {
				data[attr] = cfg[attr];
			}catch(e) {};
		}
		//FIXME ��ʼ������
		//data.level = cfg.level;
		DataStore.initPlayerData(data);
		DataStore.recalcProperties(data);
		println "create player: $data"
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
		def player = store.createPlayer(type,null);
		player.name = name;
		//��ʼ����������
		def data = new PlayerVO('elf-'+(++elfId),name,type);
		data.level = level;
		//��ʼ���������������
		initElf(data);
		player.setData(data);
		//player.setNameForeground(GameMain.TEXT_NAME_NPC_COLOR);
		println "create elf: $data"
		return player;
	}

	private static int elfId;

	public static def �����б� = [ '���ƹٸ�', '����ɽ' ,'������', 'Ů����', '���ܵظ�', 'ħ��կ', 'ʨ����', '��˿�� ','�칬' ,
	                         '����', '��ׯ��', '����ɽ'];
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
		for(attr in cfg.keySet()) {
			def val = cfg.get(attr)
			try {
				def clazz = playerCfg.class.getDeclaredField(attr).getType();
				if(clazz == int)clazz = Integer
				else if(clazz ==long)clazz = Long;
				//println "$clazz: $attr = $val"
				playerCfg[attr] = val.asType(clazz);
			}catch(Exception e) {
				System.err.println("��ֵʧ��! $attr=$val");
				e.printStackTrace();
			}
		}
		store.registerNPC(sceneId, playerCfg);
	}

	/**
	 * ���س���npc����
	 */
	public static void loadNPCs() {
		def xml =  new XmlParser().parse(GameMain.getFile("xml/npcs.xml"));
		def npcs = xml.Scene.NPC;
		for(def npc in npcs) {
			registerNPC(npc.parent().@id,npc.attributes());
		}
	}
}
