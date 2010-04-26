package com.javaxyq.data;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import com.javaxyq.config.TalkConfig;
import com.javaxyq.core.GameMain;
import com.javaxyq.core.Helper;
import com.javaxyq.core.PlayerPropertyCalculator;
import com.javaxyq.model.Item;
import com.javaxyq.model.ItemTypes;
import com.javaxyq.model.PlayerVO;
import com.javaxyq.model.Task;
import com.javaxyq.task.TaskManager;
import com.javaxyq.util.StringUtils;
import com.javaxyq.widget.Player;

/**
 * ���ݷ�����
 * 
 * @author ����ΰ
 * @history 2008-6-8 ����ΰ �½�
 */
public class DataStore {

	private static Map<String, TalkConfig> talkMap = new HashMap<String, TalkConfig>();

	public static String[] �������� = { "���ƹٸ�", "����ɽ" ,"������", "Ů����"};
	public static String[] ħ������ = {  "���ܵظ�", "ħ��կ", "ʨ����", "��˿�� "};
	public static String[] �������� = {"�칬" ,"����", "��ׯ��", "����ɽ"};
	
	private DataStore() {
	}
	
	public static void loadDataFromFile(String filename) {
		//TODO
	}
	
	public static void storeDataToFile(String filename) {
		//TODO
	}
	
	private static String lastchat = "";
	
	private static Random rand = new Random();
	
	public static TalkConfig getTalk(String npcId, String talkId) {
		File file = GameMain.getFile("chat/"+npcId+".txt");
		List<String> chats = new ArrayList<String>();
		try {
			String str = null;
			BufferedReader br= new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			while((str=br.readLine())!=null) {
				if(StringUtils.isNotBlank(str) && !"P N".equals(str.trim())) {
					chats.add(str);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int index = rand.nextInt(chats.size());
		if(lastchat!=null && lastchat.startsWith(npcId)) {
			int lastindex = Integer.valueOf(lastchat.substring(npcId.length()+1));
			index = (lastindex+1)%chats.size();
		}
		if(chats.size()>index) {
			String text = chats.get(index);
			TalkConfig talk = new TalkConfig(text);
			lastchat = npcId+"_"+index;
			return talk;
		}
		return null;
		//return talkMap.get(npcId + "@" + talkId);
	}

	public static void addTalk(String npcId, TalkConfig talk) {
		talkMap.put(npcId + "@" + talk.getId(), talk);
	}
	
	public static void addExp(Player player,int exp) {
		PlayerVO vo = player.getData();
		vo.exp += exp;
	}
	
	public static void addMoney(Player player,int money) {
		PlayerVO vo = player.getData();
		vo.money += money;
	}
	
	public static void addHp(Player player,int hp) {
		PlayerVO vo = player.getData();
		vo.hp = Math.min(vo.hp+hp,vo.maxHp);
		if(vo.hp < 0) {
			vo.hp = 0;
		}
	}
	
	public static void addMp(Player player,int mp) {
		PlayerVO vo = player.getData();
		vo.mp = Math.min(vo.mp+mp,vo.maxMp);
		if(vo.mp < 0) {
			vo.mp = 0;
		}
	}
	
	/**
	 * ����ĳ���Ե�ֵ
	 * @param player
	 * @param prop
	 * @param val
	 */
	public static void addProp(Player player,String prop,int val) {
		PlayerVO vo = player.getData();
		//���ܸ�����������ֵ
		String maxProp = "max"+prop;
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
	private static String[] ���� = {"0001","0002","0003","0004"};
	private static String[] ħ�� = {"0005","0006","0007","0008"};
	private static String[] ���� = {"0000","0010","0011","0012"};
	private static Map<String,Integer> humanData = new HashMap<String, Integer>();
	private static Map<String,Integer> devilData = new HashMap<String, Integer>();
	private static Map<String,Integer> immortalData = new HashMap<String, Integer>();
	static {
		//�����ʼ����
		humanData.put("����", 10);
		humanData.put("ħ��", 10);
		humanData.put("����", 10);
		humanData.put("����", 10);
		humanData.put("����", 10);
		
		humanData.put("����", 50);
		humanData.put("�˺�", 41);
		humanData.put("����", 15);
		humanData.put("�ٶ�", 10);
		humanData.put("���", 20);
		humanData.put("����", 16);
		
		humanData.put("hp", 150);
		humanData.put("mp", 110);
		
		//ħ���ʼ����
		devilData.put("����", 12);
		devilData.put("ħ��", 11);
		devilData.put("����", 11);
		devilData.put("����", 8);
		devilData.put("����", 8);
		
		devilData.put("����", 55);
		devilData.put("�˺�", 43);
		devilData.put("����", 11);
		devilData.put("�ٶ�", 8);
		devilData.put("���", 18);
		devilData.put("����", 17);
		
		devilData.put("hp", 172);
		devilData.put("mp", 107);
		
		//�����ʼ����
		immortalData.put("����", 12);
		immortalData.put("ħ��", 5);
		immortalData.put("����", 11);
		immortalData.put("����", 12);
		immortalData.put("����", 10);
		
		immortalData.put("����", 48);
		immortalData.put("�˺�", 46);
		immortalData.put("����", 19);
		immortalData.put("�ٶ�", 10);
		immortalData.put("���", 20);
		immortalData.put("����", 13);
		
		immortalData.put("hp", 154);
		immortalData.put("mp", 97);
	}
	/**
	 * �ж�����array�Ƿ����ֵvalue
	 * @return
	 */
	private static boolean inArray(String[] array, String value) {
		for (int i = 0; i < array.length; i++) {
			if(array[i].equals(value))return true;
		}
		return false;
	}	
	public static void initPlayerData(PlayerVO vo) {
		try {
			if(inArray(����, vo.character)) {
				BeanUtils.populate(vo, humanData);
			}else if(inArray(ħ��, vo.character)) {
				BeanUtils.populate(vo, devilData);
			}else if(inArray(����, vo.character)) {
				BeanUtils.populate(vo, immortalData);
			}
			vo.maxHp = vo.hp;
			vo.maxMp = vo.mp;
			vo.tmpMaxHp = vo.maxHp;
			vo.potentiality = 5 + 5*vo.level;
			vo.���� += vo.level;
			vo.ħ��+= vo.level;
			vo.����+= vo.level;
			vo.����+= vo.level;
			vo.����+= vo.level;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
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
		String[] attrs = {"�ٶ�","����","���","�˺�","����","����","����","����"};
		try {
			for(String attr : attrs) {
				Object value = PlayerPropertyCalculator.invokeMethod("calc_"+attr, vo);
				BeanUtils.copyProperty(vo, attr, value);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		//,"��Ѫ","ħ��"
		int maxHp0 = vo.maxHp;
		int maxMp0 = vo.maxMp;
		vo.maxHp = vo.tmpMaxHp = PlayerPropertyCalculator.calc_��Ѫ(vo);
		vo.maxMp = PlayerPropertyCalculator.calc_ħ��(vo);
		vo.hp += vo.maxHp-maxHp0;
		vo.mp += vo.maxMp-maxMp0;
	}

	
	/**
	 * �ɳ��ʱ�
	 */
	private static Map<String,Double> growthRateTable = new HashMap<String, Double>();
	static{
		growthRateTable.put("2009", 1.5);
		growthRateTable.put("2010", 0.9);
		growthRateTable.put("2011", 1.0);
		growthRateTable.put("2012", 1.2);
		growthRateTable.put("2036", 0.8);
		growthRateTable.put("2037", 0.8);
	}
	/**
	 * ������������ֵ
	 * 
	 * @param vo
	 */
	public static void recalcElfProps(PlayerVO vo) {
		//TODO ���Ƴɳ���
		Double rate = growthRateTable.get(vo.character);
		if(rate == null) {
			rate = 1.0;
		}
		int maxhp0 = vo.maxHp;
		int maxmp0 = vo.maxMp;
		vo.maxHp = vo.����*5 + 100;
		vo.maxMp = vo.ħ��*3+80;
		vo.���� = (int) (rate*(vo.����*2+30));
		vo.�˺� = (int) (rate*(vo.����*0.7+34));
		vo.���� = (int) (rate*(vo.����*1.5 ));
		vo.�ٶ� = (int) (0.8 * rate*(vo.����*0.1 + vo.����*0.1 + vo.����*0.1 + vo.����*0.7 + vo.ħ��*0));
		vo.���� = (int) (rate*(vo.����*0.3 + vo.ħ��*0.7 + vo.����*0.2 + vo.����*0.4 + vo.����*0 ));
		vo.��� = (int) (rate*(vo.����*1 + 10));
		
	}

	/**
	 * �������������
	 */
	private static int [] levelExpTable = 
		{40,110,237,450,779,1252,1898,2745,3822,5159,6784,8726,11013,13674,16739,20236,24194,28641,330606,39119,
		45208,51902,59229,67218,75899,85300,95450,106377,118110,130679,144112,158438,173685,189882,207059,
		225244,244466,264753,286134,308639,332296,357134,383181,410466,439019,468868,500042,532569,566478,
		601799,638560,676790,716517,757770,800579,844972,890978,938625,987942,1038959,1091704,1146206,1202493,
		1260594,1320539,1382356,1446074,1511721,1579326,1648919,1720528,1794182,1869909,1947738,2027699,
		2109820,2194130,2280657,2369430,2460479,2553832,2649518,2747565,2848002,2950859,3056164,3163946,
		3274233,3387054,3502439,3620416,3741014,3864261,3990186,4118819,4250188,4384322,4521249,4660998,4803599};
	
	public static long getLevelExp(int level) {
		
		return levelExpTable[level];
	}

	/**
	 * ��ȡ������ݼ�ֵ��
	 * @param player
	 * @return
	 */
	public static Map getProperties(Player player) {
		try {
			Map props = PropertyUtils.describe(player.getData());
			props.put("levelExp", getLevelExp(player.getData().level));
			return props;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ItemInstance createItem(String name) {
		if(name == null) return null;
		name = name.trim();
//		if(medicines == null) {
//			loadMedicines();
//		}
//		try {
//			return medicines.get(name).clone();
//		} catch (CloneNotSupportedException e) {
//			e.printStackTrace();
//		}
		MedicineItem itemVO = medicineDAO.findMedicineItemByName(name);
		return new ItemInstance(itemVO,1);
	}
	
	private static Map<Player,ItemInstance[]> itemsMap = new HashMap<Player, ItemInstance[]>();

	private static MedicineItemJpaController medicineDAO;

	private static SceneJpaController sceneDAO;

	private static SceneNpcJpaController sceneNpcDAO;

	private static SceneTeleporterJpaController sceneTeleporterDAO;
	/**
	 * ��ȡ��Ϸ��������б�
	 */
	public static ItemInstance[] getPlayerItems(Player player) {
		ItemInstance[] items = itemsMap.get(player);
		if(items == null) {
			items = new ItemInstance[20];
			itemsMap.put(player, items);
		}
		return items;
	}
	public static ItemInstance getItemAt(Player player,int index) {
		ItemInstance[] list = itemsMap.get(player);
		return list!=null?list[index]: null;
	}
	
	/**
	 * ����ĳ���͵���Ʒ
	 * @param player
	 * @param type ��Ʒ���ͣ��ο�ItemTypes
	 * @return
	 */
	public static ItemInstance[] findItems(Player player,int type) {
		ItemInstance[] allitems = getPlayerItems(player);
		List<ItemInstance> results = new ArrayList<ItemInstance>();
		for (int i = 0; i < allitems.length; i++) {
			ItemInstance item = allitems[i];
			if(item!=null && ItemTypes.isType(item.getItem(), type)) {
				results.add(item);
			}
		}
		return results.toArray(new ItemInstance[results.size()]);
	}
	
	/**
	 * ��������ĵ���
	 * @param index ����λ����ţ����϶��£������������� in 5*4 = [0,20) 
	 */
	public static void setPlayerItem(Player player,int index,ItemInstance item) {
		ItemInstance[] items = getPlayerItems(player);
		items[index] = item;
	}
	public static void setPlayerItemName(Player player,int index,String itemName) {
		setPlayerItem(player,index,createItem(itemName));
	}
	
	public static boolean removePlayerItem(Player player,int index) {
		ItemInstance[] items = getPlayerItems(player);
		if(items[index]!=null) {
			System.out.println("remove item: "+items[index]);
			items[index] = null;
			return true;
		}
		return false;
	}
	
	public static void removePlayerItem(Player player,ItemInstance item) {
		ItemInstance[] items = getPlayerItems(player);
		for (int i = 0; i < items.length; i++) {
			if(items[i] == item) {
				items[i] = null;
				break;
			}
		}
		System.out.println("remove item: "+item );
	}
	
	public static void swapItem(Player player, int srcIndex, int destIndex){
		synchronized(player) {
			ItemInstance[] items = getPlayerItems(player);
			ItemInstance temp = items[srcIndex];
			items[srcIndex] = items[destIndex];
			items[destIndex] = temp;
		}
	}
	
	/**
	 * ��������ĳ��Ʒ
	 * @return ����ɹ�����true
	 */
	public static boolean addItemToPlayer(Player player,ItemInstance item) {
		//TODO �����Ʒ
		synchronized(player) {
			ItemInstance[] items = getPlayerItems(player);
			int index = 0;
			for(index =0;index < items.length;index++) {
				if(items[index] == null) {
					items[index] = item;
					return true;
				}else if(overlayItems(item, items[index])) {//���Ե���
					if(item.getAmount() ==0)return true;//�������
				}
			}
			return false;
		}
	}
	
	public static boolean removeItemFromPlayer() {
		return false;
	}
	
	/**
	 * ��Ʒ���Ե��ӵ��������
	 * @param item
	 * @return
	 */
	public static int getOverlayAmount(Item item) {
		int amount = 1;
		if (item instanceof MedicineItem) {
			MedicineItem mitem = (MedicineItem) item;
			switch (mitem.getLevel()) {
			case 1:
				amount = 99;
				break;
			case 2:
				amount = 30;
				break;
			case 3:
				amount = 10;
				break;

			default:
				break;
			}
		}
		return amount;
	}
	/**
	 * ������Ʒ
	 * @param srcItem Դ��Ʒ
	 * @param destItem Ŀ����Ʒ
	 * @return ���ӳɹ�����true
	 */
	public static boolean overlayItems(ItemInstance srcItem,ItemInstance destItem) {
		if(srcItem.equals(destItem)) {
			int maxAmount = getOverlayAmount(srcItem.getItem()); 
			if(maxAmount > destItem.getAmount()) {
				int total = srcItem.getAmount() + destItem.getAmount();
				destItem.setAmount( Math.min(total,maxAmount));
				srcItem.setAmount(total - destItem.getAmount());
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * ��ʼ����������
	 */
	public static void init() {
		System.setProperty("derby.system.home",GameMain.cacheBase);
		medicineDAO = new MedicineItemJpaController();
		sceneDAO = new SceneJpaController();
		sceneNpcDAO = new SceneNpcJpaController();
		sceneTeleporterDAO = new SceneTeleporterJpaController();
	}
	
	/**
	 * ��ʼ������
	 */
	public static void initData() {
		
		int[] colorations = new int[3];
		colorations[0] = 2;
		colorations[1] = 4;
		colorations[2] = 3;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("name","��ң��«" );
		data.put("level", 5);
		data.put("����", "��ׯ��");
		data.put("direction",0 );
		data.put("state","stand" );
		data.put("colorations", colorations);
		Player p = Helper.createPlayer("0010",data);
		GameMain.setPlayer(p);
		GameMain.setScene("1146",52,32);//��ׯ��	
		
		ItemInstance item = DataStore.createItem("��Ҷ��");
		item.setAmount(99);
		DataStore.addItemToPlayer(p,item);
		item = DataStore.createItem("����");
		item.setAmount(99);
		DataStore.addItemToPlayer(p,item);
		item = DataStore.createItem("Ѫɫ�軨");
		item.setAmount(30);
		DataStore.addItemToPlayer(p,item);
		int money = 50000;
		addMoney(p, money);		
	}
	
	/**
	 * ����Ĭ�ϴ浵
	 */
	public static void loadData() {
		File file = GameMain.getFile("save/0.jxd");
		if(file==null || !file.exists() || file.length()==0) {
			initData();
			return ;
		}
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			System.out.printf("��ȡ��Ϸ�浵��������%s��...\n",ois.readObject());
			//����ID
			String sceneId = ois.readUTF();
			//��������
			PlayerVO playerData = (PlayerVO) ois.readObject();
			//��Ʒ����
			ItemInstance[] items = new ItemInstance[ois.readInt()];
			for(int i=0;i<items.length;i++) {
				ItemInstance _inst = (ItemInstance) ois.readObject();
				items[i] = _inst;
				//read item 
				if(_inst!=null) {
					_inst.setItem(medicineDAO.findMedicineItem(_inst.getItemId()));
				}
			}
			//��������
			Task[] tasks = new Task[ois.readInt()]; 
			for(int i=0;i<tasks.length;i++) {
				tasks[i] = (Task) ois.readObject();
			}
			ois.close();
			
			//��ʼ��
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("name",playerData.name );
			data.put("level", playerData.level);
			data.put("����", playerData.����);
			data.put("direction",playerData.direction );
			data.put("state",playerData.state );
			data.put("colorations", playerData.colorations);
			data.put("sceneLocation", playerData.sceneLocation);
			Player player = Helper.createPlayer(playerData.character,data);
			player.setData(playerData);
			GameMain.setPlayer(player);
			Point localtion = playerData.sceneLocation;
			GameMain.setScene(sceneId,localtion.x,localtion.y);
			for(int i=0;i<items.length;i++) {
				DataStore.setPlayerItem(player,i,items[i]);
			}
			if(tasks!=null) {
				for (int i = 0; i < tasks.length; i++) {
					TaskManager.instance.add(tasks[i]);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ������Ϸ���ݵ��浵
	 */
	public static void saveData() {
		try {
			File file = GameMain.createFile("save/tmp.jxd");
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			//����ʱ��
			oos.writeObject(new java.util.Date());
			//����
			oos.writeUTF(GameMain.getSceneCanvas().getSceneId());
			
			//��������
			Player player = GameMain.getPlayer();
			PlayerVO playerData = player.getData();
			playerData.state = "stand";
			playerData.direction = player.getDirection();
			playerData.colorations = player.getColorations();
			playerData.sceneLocation = player.getSceneLocation();
			oos.writeObject(playerData);
			
			//��Ʒ����
			ItemInstance[] items = getPlayerItems(player);
			oos.writeInt(items.length);
			for (int i = 0; i < items.length; i++) {
				oos.writeObject(items[i]);
			}

			//��������
			List tasks = TaskManager.instance.getTaskList();
			oos.writeInt(tasks.size());
			for (int i = 0; i < tasks.size(); i++) {
				oos.writeObject(tasks.get(i));
			}
			oos.close();
			//�滻Ĭ�ϴ浵
			File defaultfile = GameMain.createFile("save/0.jxd");
//			if(defaultfile!=null && defaultfile.exists()) {
//				GameMain.deleteFile("save/1.jxd");
//				defaultfile.renameTo(new File(defaultfile.getAbsolutePath().replaceFirst("0\\.jxd", "1.jxd")));
//			}
			if(defaultfile!=null && defaultfile.exists()) {
				defaultfile.delete();
			}
			file.renameTo(defaultfile);
			System.out.println("��Ϸ�浵���");
		} catch (FileNotFoundException e) {
			System.out.println("��Ϸ�浵ʧ��,�Ҳ����ļ���");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("��Ϸ�浵ʧ�ܣ�IO����"+e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("��Ϸ�浵ʧ�ܣ�"+e.getMessage());
			e.printStackTrace();
		}
	}

	public boolean existItem(String name,int amount) {
		return false;
	}
	
	public boolean removeItem(String name,int amount) {
		return false;
	}
	
	/**
	 * @return 
	 * 
	 */
	public static Scene findScene(int sceneId) {
		return sceneDAO.findScene(sceneId);
	}

	public static List<SceneNpc> findNpcsBySceneId(int sceneId) {
		return sceneNpcDAO.findNpcsBySceneId(sceneId);
	}

	public static SceneNpc findSceneNpc(Integer id) {
		return sceneNpcDAO.findSceneNpc(id);
	}

	public static SceneTeleporter findSceneTeleporter(Integer id) {
		return sceneTeleporterDAO.findSceneTeleporter(id);
	}

	public static List<SceneTeleporter> findTeleportersBySceneId(int sceneId) {
		return sceneTeleporterDAO.findTeleportersBySceneId(sceneId);
	}
	
}
