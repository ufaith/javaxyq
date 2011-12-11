package com.javaxyq.core;

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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import com.javaxyq.config.PlayerConfig;
import com.javaxyq.data.ItemInstance;
import com.javaxyq.data.MedicineItem;
import com.javaxyq.data.MedicineItemDAO;
import com.javaxyq.data.MedicineItemException;
import com.javaxyq.data.MedicineItemJpaController;
import com.javaxyq.data.Scene;
import com.javaxyq.data.SceneDAO;
import com.javaxyq.data.SceneJpaController;
import com.javaxyq.data.SceneNpc;
import com.javaxyq.data.SceneNpcDAO;
import com.javaxyq.data.SceneNpcJpaController;
import com.javaxyq.data.SceneTeleporter;
import com.javaxyq.data.SceneTeleporterDAO;
import com.javaxyq.data.SceneTeleporterJpaController;
import com.javaxyq.data.impl.MedicineItemDAOImpl;
import com.javaxyq.data.impl.SceneDAOImpl;
import com.javaxyq.data.impl.SceneNpcDAOImpl;
import com.javaxyq.data.impl.SceneTeleporterDAOImpl;
import com.javaxyq.io.CacheManager;
import com.javaxyq.model.Item;
import com.javaxyq.model.ItemTypes;
import com.javaxyq.model.PlayerVO;
import com.javaxyq.model.Task;
import com.javaxyq.task.TaskManager;
import com.javaxyq.util.StringUtils;
import com.javaxyq.util.UIUtils;
import com.javaxyq.widget.Player;

/**
 * ���ݷ�����
 * 
 * @author ����ΰ
 * @history 2008-6-8 ����ΰ �½�
 */
public class DataStore implements DataManager {

	private static int elfId;
	/**
	 * �������������
	 */
	private static final int [] levelExpTable = 
		{40,110,237,450,779,1252,1898,2745,3822,5159,6784,8726,11013,13674,16739,20236,24194,28641,330606,39119,
		45208,51902,59229,67218,75899,85300,95450,106377,118110,130679,144112,158438,173685,189882,207059,
		225244,244466,264753,286134,308639,332296,357134,383181,410466,439019,468868,500042,532569,566478,
		601799,638560,676790,716517,757770,800579,844972,890978,938625,987942,1038959,1091704,1146206,1202493,
		1260594,1320539,1382356,1446074,1511721,1579326,1648919,1720528,1794182,1869909,1947738,2027699,
		2109820,2194130,2280657,2369430,2460479,2553832,2649518,2747565,2848002,2950859,3056164,3163946,
		3274233,3387054,3502439,3620416,3741014,3864261,3990186,4118819,4250188,4384322,4521249,4660998,4803599};
	public static String[] �����б� = { "���ƹٸ�", "����ɽ" ,"������", "Ů����", "���ܵظ�", "ħ��կ", "ʨ����", "��˿�� ","�칬" ,
	                         "����", "��ׯ��", "����ɽ"};
	
	public static final String[] ħ������ = {  "���ܵظ�", "ħ��կ", "ʨ����", "��˿�� "};
	
	public static final String[] �������� = { "���ƹٸ�", "����ɽ" ,"������", "Ů����"};
	
	public static final String[] �������� = {"�칬" ,"����", "��ׯ��", "����ɽ"};
	
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
			System.out.println("����Configʧ�ܣ�"+str);
			e.printStackTrace();
		}
		return configs;
	}
	
	private Context context;

	private Map<String,Integer> devilData = new HashMap<String, Integer>();
	
	/**
	 * �ɳ��ʱ�
	 */
	private Map<String,Double> growthRateTable = new HashMap<String, Double>();
	
	private Map<String,Integer> humanData = new HashMap<String, Integer>();
	
	private Map<String,Integer> immortalData = new HashMap<String, Integer>();
	
	private Map<Player,ItemInstance[]> itemsMap = new HashMap<Player, ItemInstance[]>();
	
	private String lastchat = "";
	private MedicineItemDAO medicineDAO;
	private Random rand = new Random();
	private SceneDAO sceneDAO;
	private SceneNpcDAO sceneNpcDAO;
	private SceneTeleporterDAO sceneTeleporterDAO;
	private String[] ħ�� = {"0005","0006","0007","0008"};

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
	private String[] ���� = {"0001","0002","0003","0004"};
	private String[] ���� = {"0000","0010","0011","0012"};
	public DataStore(Context context) {
		this.context = context;
		init();
		//�����ʼ����
		humanData.put("����", 10);
		humanData.put("ħ��", 10);
		humanData.put("����", 10);
		humanData.put("durability", 10);
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
		devilData.put("durability", 8);
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
		immortalData.put("durability", 12);
		immortalData.put("����", 10);
		
		immortalData.put("����", 48);
		immortalData.put("�˺�", 46);
		immortalData.put("����", 19);
		immortalData.put("�ٶ�", 10);
		immortalData.put("���", 20);
		immortalData.put("����", 13);
		
		immortalData.put("hp", 154);
		immortalData.put("mp", 97);

		//�ɳ���
		growthRateTable.put("2009", 1.5);
		growthRateTable.put("2010", 0.9);
		growthRateTable.put("2011", 1.0);
		growthRateTable.put("2012", 1.2);
		growthRateTable.put("2036", 0.8);
		growthRateTable.put("2037", 0.8);
	}

	public void addExp(Player player,int exp) {
		PlayerVO vo = player.getData();
		vo.exp += exp;
	}
	public void addHp(Player player,int hp) {
		PlayerVO vo = player.getData();
		vo.hp = Math.min(vo.hp+hp,vo.maxHp);
		if(vo.hp < 0) {
			vo.hp = 0;
		}
	}

	
	/**
	 * ��������ĳ��Ʒ
	 * @return ����ɹ�����true
	 */
	public boolean addItemToPlayer(Player player,ItemInstance item) {
		//TODO �����Ʒ
		synchronized(player) {
			ItemInstance[] items = getItems(player);
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
	public void addMoney(Player player,int money) {
		PlayerVO vo = player.getData();
		vo.money += money;
	}
	public void addMp(Player player,int mp) {
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
	public void addProp(Player player,String prop,int val) {
		PlayerVO vo = player.getData();
		//���ܸ�����������ֵ
		String maxProp = "max"+prop;
		//if()
	}
	
	/**
	 * ������ͨ��С�֣�����������
	 * @param type
	 * @param name
	 * @param level
	 * @return
	 */
	public Player createElf(String type,String name,int level) {
		Player player = createPlayer(type,(int[])null);
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

	public ItemInstance createItem(String name) {
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
		try {
			MedicineItem itemVO = medicineDAO.findMedicineItemByName(name);
			return new ItemInstance(itemVO,1);
		} catch (MedicineItemException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ����NPCʵ��
	 * @param cfg
	 * @return
	 */
	public Player createNPC(PlayerConfig cfg) {
		Player p = this.createPlayer(cfg);
		p.setNameForeground(UIUtils.TEXT_NAME_NPC_COLOR);
		return p;
	}
	
	/**
		 * ����NPCʵ��
		 * @param sceneNpc
		 * @return
		 */
		public Player createNPC(SceneNpc _npc) {
			Player player = new Player(String.valueOf(_npc.getId()), _npc.getName(), _npc.getCharacterId());
			player.setSceneLocation(_npc.getSceneX(), _npc.getSceneY());
			//String strColorations = cfg.getColorations();
	//		if (strColorations != null) {// Ⱦɫ
	//			String[] colors = strColorations.split(",");
	//			int[] colorations = new int[colors.length];
	//			for (int i = 0; i < colors.length; i++) {
	//				colorations[i] = colors[i].charAt(0) - '0';
	//			}
	//			player.setColorations(colorations, false);//��setState֮ǰ
	//		}
			Properties configs = parseConfig(_npc.getConfig());
			player.setState(configs.getProperty("state","stand"));
			player.setDirection(Integer.parseInt(configs.getProperty("direction","0")));
			//String mAction = cfg.getMovement();
	//		if (mAction != null) {
	//			MovementManager.put(player, mAction, cfg.getPeriod());
	//		}
			
			player.setNameForeground(UIUtils.TEXT_NAME_NPC_COLOR);
			return player;
		}

	//FIXME �ظ�����ͬһ����ɫʱ���������������⣨������ͬһ��Sprite����
	public Player createPlayer(PlayerConfig cfg) {
		Player player = new Player(cfg.getId(), cfg.getName(), cfg.getCharacter());
		player.setSceneLocation(cfg.getX(), cfg.getY());
		String strColorations = cfg.getColorations();
		if (strColorations != null) {// Ⱦɫ
			String[] colors = strColorations.split(",");
			int[] colorations = new int[colors.length];
			for (int i = 0; i < colors.length; i++) {
				colorations[i] = colors[i].charAt(0) - '0';
			}
			player.setColorations(colorations, false);//��setState֮ǰ
		}
		player.setState(cfg.getState());
		player.setDirection(cfg.getDirection());
		String mAction = cfg.getMovement();
//		if (mAction != null) {
//			MovementManager.put(player, mAction, cfg.getPeriod());
//		}
		return player;
	}

	public Player createPlayer(PlayerVO data) {
		Player player = new Player(data.id, data.name, data.character);
		player.setData(data);
		return player;
	}

	public Player createPlayer(String character, int[] colorations) {
		Player player = new Player(null, "δ����", character);
		player.setColorations(colorations, true);
		player.setState(Player.STATE_STAND);
		player.setDirection(0);
		return player;
	}

	public Player createPlayer(String character,Map<String,Object> cfg) {
		Player player = createPlayer(character,(int[]) cfg.get("colorations"));
		PlayerVO data = new PlayerVO((String)cfg.get("id"),(String)cfg.get("name"),character);
		try {
			BeanUtils.populate(player, cfg);
			BeanUtils.populate(data, cfg);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		//FIXME ��ʼ������
		//data.level = cfg.level;
		this.initPlayerData(data);
		this.recalcProperties(data);
		System.out.println("create player: "+data);
		player.setData(data);
		return player;
	}
	
	public boolean existItem(String name,int amount) {
		return false;
	}
	
	/**
	 * ����NPC��������
	 * @param npcId
	 * @return
	 */
	public String findChat(String npcId) {
		File file = CacheManager.getInstance().getFile("chat/"+npcId+".txt");
		if(file!=null && file.exists()) {
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
				String chat = chats.get(index);
				lastchat = npcId+"_"+index;
				return chat;
			}
		}
		return null;
	}
	
	/**
	 * ����ĳ���͵���Ʒ
	 * @param player
	 * @param type ��Ʒ���ͣ��ο�ItemTypes
	 * @return
	 */
	public ItemInstance[] findItems(Player player,int type) {
		ItemInstance[] allitems = getItems(player);
		List<ItemInstance> results = new ArrayList<ItemInstance>();
		for (int i = 0; i < allitems.length; i++) {
			ItemInstance item = allitems[i];
			if(item!=null && ItemTypes.isType(item.getItem(), type)) {
				results.add(item);
			}
		}
		return results.toArray(new ItemInstance[results.size()]);
	}
	
	public List<SceneNpc> findNpcsBySceneId(int sceneId) {
		try {
			return sceneNpcDAO.findNpcsBySceneId(sceneId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * @return 
	 * 
	 */
	public Scene findScene(int sceneId) {
		try {
			return sceneDAO.findScene(sceneId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public SceneNpc findSceneNpc(Integer id) {
		try {
			return sceneNpcDAO.findSceneNpc(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public SceneTeleporter findSceneTeleporter(Integer id) {
		try {
			return sceneTeleporterDAO.findSceneTeleporter(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<SceneTeleporter> findTeleportersBySceneId(int sceneId) {
		try {
			return sceneTeleporterDAO.findTeleportersBySceneId(sceneId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ItemInstance getItemAt(Player player,int index) {
		ItemInstance[] list = itemsMap.get(player);
		return list!=null?list[index]: null;
	}
	
	/**
	 * ��ȡ��Ϸ��������б�
	 */
	public ItemInstance[] getItems(Player player) {
		ItemInstance[] items = itemsMap.get(player);
		if(items == null) {
			items = new ItemInstance[20];
			itemsMap.put(player, items);
		}
		return items;
	}
	
	public long getLevelExp(int level) {
		
		return levelExpTable[level];
	}
	/**
	 * ��Ʒ���Ե��ӵ��������
	 * @param item
	 * @return
	 */
	public int getOverlayAmount(Item item) {
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
	 * ��ȡ������ݼ�ֵ��
	 * @param player
	 * @return
	 */
	public Map<String, Object> getProperties(Player player) {
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
	
	/**
	 * ��ʼ����������
	 */
	private void init() {
		System.out.println("initializing datastore: "+new java.util.Date());
		System.setProperty("derby.system.home",CacheManager.getInstance().getCacheBase());
		//medicineDAO = new MedicineItemJpaController();
		//sceneDAO = new SceneJpaController();
		//sceneNpcDAO = new SceneNpcJpaController();
		//sceneTeleporterDAO = new SceneTeleporterJpaController();
		medicineDAO = new MedicineItemDAOImpl();
		sceneDAO = new SceneDAOImpl();
		sceneNpcDAO = new SceneNpcDAOImpl();
		sceneTeleporterDAO = new SceneTeleporterDAOImpl();
		System.out.println("initialized datastore: "+new java.util.Date());
//		Runnable action = new Runnable() {
//			public void run() {
//				System.out.println("initializing DAO: "+new java.util.Date());
//				try {
//					medicineDAO.findMedicineItemByName("��Ҷ��");
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//				System.out.println("initialized DAO: "+new java.util.Date());
//			}
//		};
//		new Thread(action).start();
	}
	
	/**
	 * ��ʼ������
	 */
	private void initData() {
		System.out.println("creating game data: "+new java.util.Date());
		int[] colorations = new int[3];
		colorations[0] = 2;
		colorations[1] = 4;
		colorations[2] = 3;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("name","��ң��«" );
		data.put("level", 5);
		data.put("school", "��ׯ��");
		data.put("direction",0 );
		data.put("state","stand" );
		data.put("colorations", colorations);
		Player p = this.createPlayer("0010",data);
		context.setPlayer(p);
		p.setSceneLocation(52,32);
		context.setScene("1146");//��ׯ��	
		
		ItemInstance item = createItem("��Ҷ��");
		item.setAmount(99);
		addItemToPlayer(p,item);
		item = createItem("����");
		item.setAmount(99);
		addItemToPlayer(p,item);
		item = createItem("Ѫɫ�軨");
		item.setAmount(30);
		addItemToPlayer(p,item);
		int money = 50000;
		addMoney(p, money);		
		System.out.println("create game data: "+new java.util.Date());
	}
	
	private void initElf(PlayerVO elf) {
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
		elf.physique = base + assigns[0];
		elf.magic = base + assigns[1];
		elf.strength = base + assigns[2];
		elf.defense = base + assigns[3];
		elf.agility = base + assigns[4];
		//�������
		elf.school = �����б�[random.nextInt(12)];
		//���¼�������
		this.recalcElfProps(elf);
	}

	/**
	 * ��ʼ��npc���ԣ����
	 * @param vo
	 */
	public void initNPC(Player player) {
		//TODO
	}
	
	public void initPlayerData(PlayerVO vo) {
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
			vo.physique += vo.level;
			vo.magic+= vo.level;
			vo.strength+= vo.level;
			vo.durability+= vo.level;
			vo.agility+= vo.level;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public PlayerVO createPlayerData(String character) {
		try {
			PlayerVO vo = new PlayerVO();
			if(inArray(����, character)) {
				BeanUtils.populate(vo, humanData);
			}else if(inArray(ħ��, character)) {
				BeanUtils.populate(vo, devilData);
			}else if(inArray(����, character)) {
				BeanUtils.populate(vo, immortalData);
			}
			vo.character = character;
			vo.createDate = new java.util.Date();
			vo.maxHp = vo.hp;
			vo.maxMp = vo.mp;
			vo.tmpMaxHp = vo.maxHp;
			vo.potentiality = 5 + 5*vo.level;
			vo.physique += vo.level;
			vo.magic+= vo.level;
			vo.strength+= vo.level;
			vo.durability+= vo.level;
			vo.agility+= vo.level;
			return vo;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ����Ĭ�ϴ浵
	 */
	public void loadData() {
		System.out.println("loading game data: "+new java.util.Date());
		File file = CacheManager.getInstance().getFile("save/0.jxd");
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
			data.put("school", playerData.school);
			data.put("direction",playerData.direction );
			data.put("state",playerData.state );
			data.put("colorations", playerData.colorations);
			data.put("sceneLocation", playerData.sceneLocation);
			Player player = this.createPlayer(playerData.character,data);
			player.setData(playerData);
			context.setPlayer(player);
			context.setScene(sceneId);
			for(int i=0;i<items.length;i++) {
				setItem(player,i,items[i]);
			}
			if(tasks!=null) {
				TaskManager taskManager = ApplicationHelper.getApplication().getTaskManager();
				for (int i = 0; i < tasks.length; i++) {
					taskManager.add(tasks[i]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("loaded game data: "+new java.util.Date());
	}

	public void loadDataFromFile(String filename) {
		//TODO
	}

	/**
	 * ������Ʒ
	 * @param srcItem Դ��Ʒ
	 * @param destItem Ŀ����Ʒ
	 * @return ���ӳɹ�����true
	 */
	public boolean overlayItems(ItemInstance srcItem,ItemInstance destItem) {
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
	 * ������������ֵ
	 * 
	 * @param vo
	 */
	public void recalcElfProps(PlayerVO vo) {
		//TODO ���Ƴɳ���
		Double rate = growthRateTable.get(vo.character);
		if(rate == null) {
			rate = 1.0;
		}
		int maxhp0 = vo.maxHp;
		int maxmp0 = vo.maxMp;
		vo.maxHp = vo.physique*5 + 100;
		vo.maxMp = vo.magic*3+80;
		vo.hitrate = (int) (rate*(vo.strength*2+30));
		vo.harm = (int) (rate*(vo.strength*0.7+34));
		vo.defense = (int) (rate*(vo.durability*1.5 ));
		vo.speed = (int) (0.8 * rate*(vo.physique*0.1 + vo.durability*0.1 + vo.strength*0.1 + vo.agility*0.7 + vo.magic*0));
		vo.wakan = (int) (rate*(vo.physique*0.3 + vo.magic*0.7 + vo.durability*0.2 + vo.strength*0.4 + vo.agility*0 ));
		vo.shun = (int) (rate*(vo.agility*1 + 10));
		
	}

	/**
	 * �������������ֵ
	 * @param vo
	 */
	public void recalcProperties(PlayerVO vo) {
		String[] attrs = {"�ٶ�","����","���","�˺�","����","����","stamina","energy"};
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

	public boolean removeItem(String name,int amount) {
		return false;
	}
	
	public boolean removeItemFromPlayer() {
		return false;
	}
	
	public boolean removePlayerItem(Player player,int index) {
		ItemInstance[] items = getItems(player);
		if(items[index]!=null) {
			System.out.println("remove item: "+items[index]);
			items[index] = null;
			return true;
		}
		return false;
	}

	public void removePlayerItem(Player player,ItemInstance item) {
		ItemInstance[] items = getItems(player);
		for (int i = 0; i < items.length; i++) {
			if(items[i] == item) {
				items[i] = null;
				break;
			}
		}
		System.out.println("remove item: "+item );
	}

	/**
		 * ������Ϸ���ݵ��浵
		 */
		public void saveData() {
			try {
				File file = CacheManager.getInstance().createFile("save/tmp.jxd");
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
				//����ʱ��
				oos.writeObject(new java.util.Date());
				//����
				oos.writeUTF(context.getScene());
				
				//��������
				Player player = context.getPlayer();
				PlayerVO playerData = player.getData();
				playerData.state = "stand";
				playerData.direction = player.getDirection();
				playerData.colorations = player.getColorations();
				playerData.sceneLocation = player.getSceneLocation();
				oos.writeObject(playerData);
				
				//��Ʒ����
				ItemInstance[] items = getItems(player);
				oos.writeInt(items.length);
				for (int i = 0; i < items.length; i++) {
					oos.writeObject(items[i]);
				}

				//��������
				TaskManager taskManager = ApplicationHelper.getApplication().getTaskManager();
				List tasks = taskManager.getTaskList();
				oos.writeInt(tasks.size());
				for (int i = 0; i < tasks.size(); i++) {
					oos.writeObject(tasks.get(i));
				}
				oos.close();
				//�滻Ĭ�ϴ浵
				File defaultfile = CacheManager.getInstance().createFile("save/0.jxd");
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

	/**
	 * ��������ĵ���
	 * @param index ����λ����ţ����϶��£������������� in 5*4 = [0,20) 
	 */
	public void setItem(Player player,int index,ItemInstance item) {
		ItemInstance[] items = getItems(player);
		items[index] = item;
		try {
			if(item != null && item.getItem() == null) {
				item.setItem(medicineDAO.findMedicineItem(item.getItemId()));
			}
		} catch (MedicineItemException e) {
			e.printStackTrace();
		}
	}

	public void setItemByName(Player player,int index,String itemName) {
		setItem(player,index,createItem(itemName));
	}

	public void setItems(Player player, ItemInstance[] items) {
		if(items != null) {
			ItemInstance[] _items = getItems(player);
			for (int i = 0; i < _items.length; i++) {
				ItemInstance _inst = items[i];
				_items[i] = _inst;
				try {
					if(_inst!=null && _inst.getItem() == null) {
						_inst.setItem(medicineDAO.findMedicineItem(_inst.getItemId()));
					}
				} catch (MedicineItemException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void storeDataToFile(String filename) {
		//TODO
	}

	public void swapItem(Player player, int srcIndex, int destIndex){
		synchronized(player) {
			ItemInstance[] items = getItems(player);
			ItemInstance temp = items[srcIndex];
			items[srcIndex] = items[destIndex];
			items[destIndex] = temp;
		}
	}
}
