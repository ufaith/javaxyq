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
 * 数据服务类
 * 
 * @author 龚德伟
 * @history 2008-6-8 龚德伟 新建
 */
public class DataStore {

	private static Map<String, TalkConfig> talkMap = new HashMap<String, TalkConfig>();

	public static def 人族门派 = [ '大唐官府', '方寸山' ,'化生寺', '女儿村'];
	public static def 魔族门派 = [  '阴曹地府', '魔王寨', '狮驼岭', '盘丝洞 '];
	public static def 仙族门派 = ['天宫' ,'龙宫', '五庄观', '普陀山'];
	
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
			//TODO 人物死亡
		}
	}
	public static void addMp(Player player,int mp) {
		PlayerVO vo = player.getData()
		vo.mp = Math.min(vo.mp+mp,vo.maxMp);
	}
	
	/**
	 * 增加某属性的值
	 * @param player
	 * @param prop
	 * @param val
	 */
	public static void addProp(Player player,String prop,int val) {
		PlayerVO vo = player.getData()
		//可能改属性有上限值
		String maxProp = 'max'+prop
		//if()
	}
	
/**
 * 	 (一)各种族初始资料
	 1)魔族
	 体12-魔力11-力量11-耐力8-敏8
	 血172-魔法值107-命中55-伤害43-防御11-速度8-躲闪18-灵力17
	 2)人族
	 体10-魔力10-力量10-耐力10-敏10
	 血150-魔法值110-命中50-伤害41-防御15-速度10-躲闪30-灵力16
	 3)仙族
	 体12-魔力5-力量11-耐力12-敏10
	 血154-魔法值97-命中48-伤害46-防御19-速度10-躲闪20-灵力13	
 */
	private static def 人族 = ['0001','0002','0003','0004'];
	private static def 魔族 = ['0005','0006','0007','0008'];
	private static def 仙族 = ['0000','0010','0011','0012'];
	private static Map 人族初始属性=[体质:10,魔力:10,力量:10,耐力:10,敏捷:10,hp:150,mp:110,maxHp:150,maxMp:110,命中:50,伤害:41,防御:15,速度:10,躲避:20,灵力:16]
	private static Map 魔族初始属性=[体质:12,魔力:11,力量:11,耐力:8,敏捷:8,hp:172,mp:107,maxHp:172,maxMp:107,命中:55,伤害:43,防御:11,速度:8,躲避:18,灵力:17]
	private static Map 仙族初始属性=[体质:12,魔力:5,力量:11,耐力:12,敏捷:10,hp:154,mp:97,maxHp:154,maxMp:97,命中:48,伤害:46,防御:19,速度:10,躲避:20,灵力:13]
	public static void initPlayerData(PlayerVO vo) {
		switch(vo.character) {
			case 人族:
				for( attr in 人族初始属性.keySet()) {
					vo[attr] = 人族初始属性[attr];
				}
				break;
			case 魔族:
				for( attr in 魔族初始属性.keySet()) {
					vo[attr] = 魔族初始属性[attr];
				}
				break;
			case 仙族:
				for( attr in 魔族初始属性.keySet()) {
					vo[attr] = 魔族初始属性[attr];
				}
				break;
		}
		vo.tmpMaxHp = vo.maxHp;
		vo.potentiality = 5 + 5*vo.level;
		vo.体质 += vo.level;
		vo.魔力+= vo.level;
		vo.力量+= vo.level;
		vo.耐力+= vo.level;
		vo.敏捷+= vo.level;
	}

	/**
	 * 初始化npc属性（怪物）
	 * @param vo
	 */
	public static void initNPC(Player player) {
		//TODO
	}
	/**
	 * 计算人物的属性值
	 * @param vo
	 */
	public static void recalcProperties(PlayerVO vo) {
		List attrs = ['速度','灵力','躲避','伤害','命中','防御','体力','活力'];
		for( attr in attrs) {
			vo[attr] = PlayerPropertyCalculator.invokeMethod("calc$attr", vo);
		}
		//,'气血','魔法'
		int maxHp0 = vo.maxHp;
		int maxMp0 = vo.maxMp;
		vo.maxHp = vo.tmpMaxHp = PlayerPropertyCalculator.calc气血(vo);
		vo.maxMp = PlayerPropertyCalculator.calc魔法(vo);
		vo.hp += vo.maxHp-maxHp0;
		vo.mp += vo.maxMp-maxMp0;
	}

	
	/**
	 * 成长率表
	 */
	private static Map<String,Float> growthRateTable = ['2009':1.5, '2010': 0.9, '2011':1.0, '2012':1.2,
	                                      '2036':0.8,'2037':0.8];
	/**
	 * 计算怪物的属性值
	 * 
	 * @param vo
	 */
	public static void recalcElfProps(PlayerVO vo) {
		//TODO 完善成长率
		float rate = growthRateTable[vo.character];
		int maxhp0 = vo.maxHp;
		int maxmp0 = vo.maxMp;
		vo.maxHp = vo.体质*5 + 100;
		vo.maxMp = vo.魔力*3+80;
		vo.命中 = rate*(vo.力量*2+30);
		vo.伤害 = rate*(vo.力量*0.7+34);
		vo.防御 = rate*(vo.耐力*1.5 );
		vo.速度 = 0.8 * rate*(vo.体质*0.1 + vo.耐力*0.1 + vo.力量*0.1 + vo.敏捷*0.7 + vo.魔力*0);
		vo.灵力 = rate*(vo.体质*0.3 + vo.魔力*0.7 + vo.耐力*0.2 + vo.力量*0.4 + vo.敏捷*0 );
		vo.躲避 = rate*(vo.敏捷*1 + 10);
		
	}

	/**
	 * 人物升级经验表
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
		//一级药品
		File file = new File('data/一级药品');
		file.eachLine{
			if(!it.startsWith('//')){
				def vals = it.trim().split(' ');
				MedicineItem item = new  MedicineItem();
				item.id = vals[0];
				item.name = vals[1];
				item.desc = vals[2];
				item.efficacy = vals[3];
				item.price = vals[4].toInteger();
				item.type = '一级药品';
				item.level = 1;
				medicines[item.name] = item;
			}
		}
		//二级药品
		file = new File('data/二级药品');
		file.eachLine{
			if(!it.startsWith('//')){
				def vals = it.trim().split(' ');
				MedicineItem item = new  MedicineItem();
				item.id = vals[0];
				item.name = vals[1];
				item.desc = vals[2];
				item.efficacy = vals[3];
				item.type = '二级药品';
				item.level = 2;
				medicines[item.name] = item;
			}
		}
		//三级药品
		file = new File('data/三级药品');
		file.eachLine{
			if(!it.startsWith('//')){
				def vals = it.trim().split(' ');
				MedicineItem item = new  MedicineItem();
				item.id = vals[0];
				item.name = vals[1];
				item.desc = vals[2];
				item.efficacy = vals[3];
				item.type = '三级药品';
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
	 * 读取游戏人物道具列表
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
	 * 设置人物的道具
	 * @param index 道具位置序号，自上而下，自左至右排列 in 5*4 = [0,20) 
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
	 * 给以人物某物品
	 * @return 给予成功返回true
	 */
	public static boolean addItemToPlayer(Player player,Item item) {
		//TODO 获得物品
		synchronized(player) {
			Item[] items = getPlayerItems(player);
			int index = 0;
			for(index =0;index < items.length;index++) {
				if(!items[index]) {
					items[index] = item;
					return true;
				}else if(overlayItems(item, items[index])) {//可以叠加
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
		itemsOverlayAmount['一级药品'] = 99;
		itemsOverlayAmount['二级药品'] = 30;
		itemsOverlayAmount['烹饪'] = 30;

		
	}
	public static int getItemOverlayAmount(String type) {
		def n = itemsOverlayAmount[type];
		return n?n:1;
	}
	/**
	 * 叠加物品
	 * @param srcItem 源物品
	 * @param destItem 目标物品
	 * @return 叠加成功返回true
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
	 * 初始化数据中心
	 */
	public static void init() {
		initItems();
	}
	
	public static void main(String[] args) {
		def item = DataStore.createItem('四叶花');
		println "$item"
	}

	/**
	 * 初始化数据
	 */
	public static void initData() {
		
		def colorations = new int[3];
		colorations[0] = 2;
		colorations[1] = 4;
		colorations[2] = 3;
		def p = Helper.createPlayer('0010',[
				name:'逍遥葫芦',
		  		level : 5,
				门派:'五庄观',
		   		direction:0,
				state:'stand',
				colorations: colorations,
				sceneLocation :[52,32]
		]);
//		p.setSceneLocation(52,32);
		GameMain.setPlayer(p);
		Main.setScene("wzg",p.sceneX,p.sceneY);//五庄观	
		
		def item = DataStore.createItem('四叶花');
		item.amount = 99;
		DataStore.addItemToPlayer(p,item);
		item = DataStore.createItem('佛手');
		item.amount = 99;
		DataStore.addItemToPlayer(p,item);
				
	}
	
	/**
	 * 加载默认存档
	 */
	public static void loadData() {
		def file = new File("save/0.jxd");
		if(!file.exists()) {
			initData();
			return ;
		}
		def ois = new ObjectInputStream(file.newInputStream());
		println "读取游戏存档（创建于${ois.readObject()}）..."
		//场景ID
		def sceneId = ois.readUTF();
		//人物数据
		def playerData = ois.readObject();
		//物品数据
		def items = new Item[ois.readInt()];
		for(int i=0;i<items.length;i++) {
			items[i] = ois.readObject();
		}
		//任务数据
		def tasks = new Task[ois.readInt()]; 
		for(int i=0;i<tasks.length;i++) {
			tasks[i] = ois.readObject();
		}
		ois.close();
		
		//初始化
		def player = Helper.createPlayer(playerData.character,[
			name: playerData.name,
	  		level : playerData.level,
			门派: playerData.门派,
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
	 * 保存游戏数据到存档
	 */
	public static void saveData() {
		def file = new File("save/0.jxd");
		if(!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		def out = file.newOutputStream()
		def oos = new ObjectOutputStream(out)
		//创建时间
		oos.writeObject(new java.util.Date());
		//场景
		oos.writeUTF(GameMain.getSceneCanvas().getSceneId());
		
		//人物数据
		def player = GameMain.getPlayer();
		def playerData = player.getData();
		playerData.state = 'stand';
		playerData.direction = player.direction;
		playerData.colorations = player.colorations;
		playerData.sceneLocation = player.sceneLocation;
		oos.writeObject(playerData);
		
		//物品数据
		def items = getPlayerItems(player);
		oos.writeInt(items.size());
		items.each{
			oos.writeObject(it);
		}

		//任务数据
		def tasks = TaskManager.instance.getTaskList();
		oos.writeInt(tasks.size());
		tasks.each{
			oos.writeObject(it);
		}
		oos.close();
		println "游戏存档完毕"
	}

	public boolean existItem(String name,int amount) {
		
	}
	
	public boolean removeItem(String name,int amount) {
		
	}
}
