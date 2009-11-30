package com.javaxyq.core;

import groovy.util.XmlParser;

import java.util.HashMap;
import java.util.Map;

import com.javaxyq.config.TalkConfig;
import com.javaxyq.task.TaskManager;
import com.javaxyq.util.ClassUtil;
import com.javaxyq.core.PlayerPropertyCalculator;
import com.javaxyq.widget.Player;
import com.javaxyq.model.*;

/**
 * ���ݷ�����
 * 
 * @author ����ΰ
 * @history 2008-6-8 ����ΰ �½�
 */
public class DataStore {

	private static Map<String, TalkConfig> talkMap = new HashMap<String, TalkConfig>();

	public static def �������� = [ '���ƹٸ�', '����ɽ' ,'������', 'Ů����'];
	public static def ħ������ = [  '���ܵظ�', 'ħ��կ', 'ʨ����', '��˿�� '];
	public static def �������� = ['�칬' ,'����', '��ׯ��', '����ɽ'];
	
	private DataStore() {
	}
	
	public static loadDataFromFile(String filename) {
		//TODO
	}
	
	public static storeDataToFile(String filename) {
		//TODO
	}
	
	public static TalkConfig getTalk(String npcId, String talkId) {
		return talkMap.get(npcId + "@" + talkId);
	}

	public static void addTalk(String npcId, TalkConfig talk) {
		talkMap.put(npcId + "@" + talk.getId(), talk);
	}
	
	public static void addExp(Player player,int exp) {
		PlayerVO vo = player.getData()
		vo.exp += exp;
	}
	
	public static void addMoney(Player player,int money) {
		PlayerVO vo = player.getData()
		vo.money += money;
	}
	public static void addHp(Player player,int hp) {
		PlayerVO vo = player.getData()
		vo.hp = Math.min(vo.hp+hp,vo.maxHp);
		if(vo.hp<=0) {
			//TODO ��������
		}
	}
	public static void addMp(Player player,int mp) {
		PlayerVO vo = player.getData()
		vo.mp = Math.min(vo.mp+mp,vo.maxMp);
	}
	
	/**
	 * ����ĳ���Ե�ֵ
	 * @param player
	 * @param prop
	 * @param val
	 */
	public static void addProp(Player player,String prop,int val) {
		PlayerVO vo = player.getData()
		//���ܸ�����������ֵ
		String maxProp = 'max'+prop
		//if()
	}
	
/**
 * 	 (һ)�������ʼ����
	 1)ħ��
	 ��12-ħ��11-����11-����8-��8
	 Ѫ172-ħ��ֵ107-����55-�˺�43-����11-�ٶ�8-����18-����17
	 2)����
	 ��10-ħ��10-����10-����10-��10
	 Ѫ150-ħ��ֵ110-����50-�˺�41-����15-�ٶ�10-����30-����16
	 3)����
	 ��12-ħ��5-����11-����12-��10
	 Ѫ154-ħ��ֵ97-����48-�˺�46-����19-�ٶ�10-����20-����13	
 */
	private static def ���� = ['0001','0002','0003','0004'];
	private static def ħ�� = ['0005','0006','0007','0008'];
	private static def ���� = ['0000','0010','0011','0012'];
	private static Map �����ʼ����=[����:10,ħ��:10,����:10,����:10,����:10,hp:150,mp:110,maxHp:150,maxMp:110,����:50,�˺�:41,����:15,�ٶ�:10,���:20,����:16]
	private static Map ħ���ʼ����=[����:12,ħ��:11,����:11,����:8,����:8,hp:172,mp:107,maxHp:172,maxMp:107,����:55,�˺�:43,����:11,�ٶ�:8,���:18,����:17]
	private static Map �����ʼ����=[����:12,ħ��:5,����:11,����:12,����:10,hp:154,mp:97,maxHp:154,maxMp:97,����:48,�˺�:46,����:19,�ٶ�:10,���:20,����:13]
	public static void initPlayerData(PlayerVO vo) {
		switch(vo.character) {
			case ����:
				for( attr in �����ʼ����.keySet()) {
					vo[attr] = �����ʼ����[attr];
				}
				break;
			case ħ��:
				for( attr in ħ���ʼ����.keySet()) {
					vo[attr] = ħ���ʼ����[attr];
				}
				break;
			case ����:
				for( attr in ħ���ʼ����.keySet()) {
					vo[attr] = ħ���ʼ����[attr];
				}
				break;
		}
		vo.tmpMaxHp = vo.maxHp;
		vo.potentiality = 5 + 5*vo.level;
		vo.���� += vo.level;
		vo.ħ��+= vo.level;
		vo.����+= vo.level;
		vo.����+= vo.level;
		vo.����+= vo.level;
	}

	/**
	 * ��ʼ��npc���ԣ����
	 * @param vo
	 */
	public static void initNPC(Player player) {
		//TODO
	}
	/**
	 * �������������ֵ
	 * @param vo
	 */
	public static void recalcProperties(PlayerVO vo) {
		List attrs = ['�ٶ�','����','���','�˺�','����','����','����','����'];
		for( attr in attrs) {
			vo[attr] = PlayerPropertyCalculator.invokeMethod("calc$attr", vo);
		}
		//,'��Ѫ','ħ��'
		int maxHp0 = vo.maxHp;
		int maxMp0 = vo.maxMp;
		vo.maxHp = vo.tmpMaxHp = PlayerPropertyCalculator.calc��Ѫ(vo);
		vo.maxMp = PlayerPropertyCalculator.calcħ��(vo);
		vo.hp += vo.maxHp-maxHp0;
		vo.mp += vo.maxMp-maxMp0;
	}

	
	/**
	 * �ɳ��ʱ�
	 */
	private static Map<String,Float> growthRateTable = ['2009':1.5, '2010': 0.9, '2011':1.0, '2012':1.2,
	                                      '2036':0.8,'2037':0.8];
	/**
	 * ������������ֵ
	 * 
	 * @param vo
	 */
	public static void recalcElfProps(PlayerVO vo) {
		//TODO ���Ƴɳ���
		float rate = growthRateTable[vo.character];
		int maxhp0 = vo.maxHp;
		int maxmp0 = vo.maxMp;
		vo.maxHp = vo.����*5 + 100;
		vo.maxMp = vo.ħ��*3+80;
		vo.���� = rate*(vo.����*2+30);
		vo.�˺� = rate*(vo.����*0.7+34);
		vo.���� = rate*(vo.����*1.5 );
		vo.�ٶ� = 0.8 * rate*(vo.����*0.1 + vo.����*0.1 + vo.����*0.1 + vo.����*0.7 + vo.ħ��*0);
		vo.���� = rate*(vo.����*0.3 + vo.ħ��*0.7 + vo.����*0.2 + vo.����*0.4 + vo.����*0 );
		vo.��� = rate*(vo.����*1 + 10);
		
	}

	/**
	 * �������������
	 */
	private static int [] levelExpTable = 
		[40,110,237,450,779,1252,1898,2745,3822,5159,6784,8726,11013,13674,16739,20236,24194,28641,330606,39119,
		45208,51902,59229,67218,75899,85300,95450,106377,118110,130679,144112,158438,173685,189882,207059,
		225244,244466,264753,286134,308639,332296,357134,383181,410466,439019,468868,500042,532569,566478,
		601799,638560,676790,716517,757770,800579,844972,890978,938625,987942,1038959,1091704,1146206,1202493,
		1260594,1320539,1382356,1446074,1511721,1579326,1648919,1720528,1794182,1869909,1947738,2027699,
		2109820,2194130,2280657,2369430,2460479,2553832,2649518,2747565,2848002,2950859,3056164,3163946,
		3274233,3387054,3502439,3620416,3741014,3864261,3990186,4118819,4250188,4384322,4521249,4660998,4803599];
	
	public static long getLevelExp(int level) {
		
		return levelExpTable[level];
	}


	public static Map getProperties(Player player) {
		Map props =  player.getData().getProperties();
		props['levelExp'] = getLevelExp(props['level']);
		return props;
	}
	
	private static Map medicines = null;
	private static void loadMedicines() {
		medicines = [:];
		//һ��ҩƷ
		File file = new File('data/һ��ҩƷ');
		file.eachLine{
			if(!it.startsWith('//')){
				def vals = it.trim().split(' ');
				MedicineItem item = new  MedicineItem();
				item.id = vals[0];
				item.name = vals[1];
				item.desc = vals[2];
				item.efficacy = vals[3];
				item.price = vals[4].toInteger();
				item.type = 'һ��ҩƷ';
				item.level = 1;
				medicines[item.name] = item;
			}
		}
		//����ҩƷ
		file = new File('data/����ҩƷ');
		file.eachLine{
			if(!it.startsWith('//')){
				def vals = it.trim().split(' ');
				MedicineItem item = new  MedicineItem();
				item.id = vals[0];
				item.name = vals[1];
				item.desc = vals[2];
				item.efficacy = vals[3];
				item.type = '����ҩƷ';
				item.level = 2;
				medicines[item.name] = item;
			}
		}
		//����ҩƷ
		file = new File('data/����ҩƷ');
		file.eachLine{
			if(!it.startsWith('//')){
				def vals = it.trim().split(' ');
				MedicineItem item = new  MedicineItem();
				item.id = vals[0];
				item.name = vals[1];
				item.desc = vals[2];
				item.efficacy = vals[3];
				item.type = '����ҩƷ';
				item.level = 3;
				medicines[item.name] = item;
			}
		}
		
	}
	public static Item createItem(String name) {
		if(!name) return null;
		name = name.trim();
		if(!medicines) {
			loadMedicines();
		}
		return medicines.get(name).clone();
	}
	
	private static Map<Player,Item[]> itemsMap = [:];
	/**
	 * ��ȡ��Ϸ��������б�
	 */
	public static Item[] getPlayerItems(Player player) {
		Item[] items = itemsMap[player];
		if(!items) {
			items = new Item[20];
			itemsMap[player] = items;
		}
		return items;
	}
	public static Item getItemAt(Player player,int index) {
		Item[] list = itemsMap[player];
		return list?list[index]: null;
	}
	
	/**
	 * ��������ĵ���
	 * @param index ����λ����ţ����϶��£������������� in 5*4 = [0,20) 
	 */
	public static void setPlayerItem(Player player,int index,Item item) {
		Item[] items = getPlayerItems(player);
		items[index] = item;
	}
	public static void setPlayerItemName(Player player,int index,String itemName) {
		setPlayerItem(player,index,createItem(itemName))
	}
	
	public static void removePlayerItem(Player player,int index) {
		Item[] items = getPlayerItems(player);
		items[index] = null;
		println "remove item: $index "
	}
	
	public static void removePlayerItem(Player player,Item item) {
		Item[] items = getPlayerItems(player);
		for (int i = 0; i < items.length; i++) {
			if(items[i]==item) {
				items[i] = null;
				break;
			}
		}
		println "remove item: $item "
	}
	
	public static void swapItem(Player player, int srcIndex, int destIndex){
		synchronized(player) {
			Item[] items = getPlayerItems(player);
			Item temp = items[srcIndex];
			items[srcIndex] = items[destIndex];
			items[destIndex] = temp;
		}
	}
	
	/**
	 * ��������ĳ��Ʒ
	 * @return ����ɹ�����true
	 */
	public static boolean addItemToPlayer(Player player,Item item) {
		//TODO �����Ʒ
		synchronized(player) {
			Item[] items = getPlayerItems(player);
			int index = 0;
			for(index =0;index < items.length;index++) {
				if(!items[index]) {
					items[index] = item;
					return true;
				}else if(overlayItems(item, items[index])) {//���Ե���
					if(item.amount ==0)return true;
				}
			}
			return false;
		}
	}
	
	public static boolean removeItemFromPlayer() {
		
	}
	
	private static Map itemsOverlayAmount = [:];
	private static void initItems() {
		itemsOverlayAmount['һ��ҩƷ'] = 99;
		itemsOverlayAmount['����ҩƷ'] = 30;
		itemsOverlayAmount['���'] = 30;

		
	}
	public static int getItemOverlayAmount(String type) {
		def n = itemsOverlayAmount[type];
		return n?n:1;
	}
	/**
	 * ������Ʒ
	 * @param srcItem Դ��Ʒ
	 * @param destItem Ŀ����Ʒ
	 * @return ���ӳɹ�����true
	 */
	public static boolean overlayItems(Item srcItem,Item destItem) {
		if(srcItem.name == destItem.name) {
			int maxAmount = getItemOverlayAmount(srcItem.type); 
			if(maxAmount > destItem.amount) {
				int total = srcItem.amount + destItem.amount;
				destItem.amount = Math.min(total,maxAmount);
				srcItem.amount = total - destItem.amount;
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * ��ʼ����������
	 */
	public static void init() {
		initItems();
	}
	
	public static void main(String[] args) {
		def item = DataStore.createItem('��Ҷ��');
		println "$item"
	}

	/**
	 * ��ʼ������
	 */
	public static void initData() {
		
		def colorations = new int[3];
		colorations[0] = 2;
		colorations[1] = 4;
		colorations[2] = 3;
		def p = Helper.createPlayer('0010',[
				name:'��ң��«',
		  		level : 5,
				����:'��ׯ��',
		   		direction:0,
				state:'stand',
				colorations: colorations,
				sceneLocation :[52,32]
		]);
//		p.setSceneLocation(52,32);
		GameMain.setPlayer(p);
		Main.setScene("wzg",p.sceneX,p.sceneY);//��ׯ��	
		
		def item = DataStore.createItem('��Ҷ��');
		item.amount = 99;
		DataStore.addItemToPlayer(p,item);
		item = DataStore.createItem('����');
		item.amount = 99;
		DataStore.addItemToPlayer(p,item);
				
	}
	
	/**
	 * ����Ĭ�ϴ浵
	 */
	public static void loadData() {
		def file = new File("save/0.jxd");
		if(!file.exists()) {
			initData();
			return ;
		}
		def ois = new ObjectInputStream(file.newInputStream());
		println "��ȡ��Ϸ�浵��������${ois.readObject()}��..."
		//����ID
		def sceneId = ois.readUTF();
		//��������
		def playerData = ois.readObject();
		//��Ʒ����
		def items = new Item[ois.readInt()];
		for(int i=0;i<items.length;i++) {
			items[i] = ois.readObject();
		}
		//��������
		def tasks = new Task[ois.readInt()]; 
		for(int i=0;i<tasks.length;i++) {
			tasks[i] = ois.readObject();
		}
		ois.close();
		
		//��ʼ��
		def player = Helper.createPlayer(playerData.character,[
			name: playerData.name,
	  		level : playerData.level,
			����: playerData.����,
	   		direction: playerData.direction,
			state: playerData.state,
			colorations: playerData.colorations,
			sceneLocation :playerData.sceneLocation
			]);
		player.setData(playerData);
		GameMain.setPlayer(player);
		Main.setScene(sceneId,player.sceneX,player.sceneY);
		for(int i=0;i<items.size();i++) {
			DataStore.setPlayerItem(player,i,items[i]);
		}
		tasks.each{
			TaskManager.instance.add(it);
		}
	}
	
	/**
	 * ������Ϸ���ݵ��浵
	 */
	public static void saveData() {
		def file = new File("save/0.jxd");
		if(!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		def out = file.newOutputStream()
		def oos = new ObjectOutputStream(out)
		//����ʱ��
		oos.writeObject(new java.util.Date());
		//����
		oos.writeUTF(GameMain.getSceneCanvas().getSceneId());
		
		//��������
		def player = GameMain.getPlayer();
		def playerData = player.getData();
		playerData.state = 'stand';
		playerData.direction = player.direction;
		playerData.colorations = player.colorations;
		playerData.sceneLocation = player.sceneLocation;
		oos.writeObject(playerData);
		
		//��Ʒ����
		def items = getPlayerItems(player);
		oos.writeInt(items.size());
		items.each{
			oos.writeObject(it);
		}

		//��������
		def tasks = TaskManager.instance.getTaskList();
		oos.writeInt(tasks.size());
		tasks.each{
			oos.writeObject(it);
		}
		oos.close();
		println "��Ϸ�浵���"
	}

	public boolean existItem(String name,int amount) {
		
	}
	
	public boolean removeItem(String name,int amount) {
		
	}
}
