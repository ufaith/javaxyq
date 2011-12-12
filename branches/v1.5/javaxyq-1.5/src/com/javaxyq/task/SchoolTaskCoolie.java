/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-23
 * http://javaxyq.googlecode.com
 * kylixs@qq.com
 */
package com.javaxyq.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.javaxyq.action.Actions;
import com.javaxyq.battle.BattleEvent;
import com.javaxyq.battle.BattleListener;
import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.core.Context;
import com.javaxyq.core.DataManager;
import com.javaxyq.core.Environment;
import com.javaxyq.core.GameWindow;
import com.javaxyq.data.ItemInstance;
import com.javaxyq.model.Task;
import com.javaxyq.ui.UIHelper;
import com.javaxyq.util.StringUtils;
import com.javaxyq.widget.Player;

/**
 * "��������"����Ԫ
 * @author dewitt
 * @date 2009-11-23 create
 */
public class SchoolTaskCoolie extends TaskCoolie {
	
	/** ÿ10��Ϊһ������ */
	private static final int ROUND = 10;
	
	private Random rand = new Random();
	/** ������� */
	int times;
	int rounds = 1;

	private Context context;

	private DataManager dataManager;

	private UIHelper helper;

	private GameWindow window;
	
	public SchoolTaskCoolie() {
		dataManager = ApplicationHelper.getApplication().getDataManager();
		context = ApplicationHelper.getApplication().getContext();
		window = context.getWindow();
		helper = window.getHelper();
	}
	
	/**
	 * ����
	 * @param task
	 * @return
	 */
	public boolean deliver(Task task) {
		//System.out.println("deliver $task");
		if(!task.isFinished()) {
			Player player =context.getPlayer();
			Player target = (Player) task.get("target");
			task.setFinished(true);
			rounds = task.getInt("rounds");
			times = task.getInt("times");
			ApplicationHelper.getApplication().doTalk(target,"�����յ���ʦ�������ţ��Ͽ��ȥ�����ɡ�", null);
			return true;
		}else {
			System.out.println("��������ɣ�"+task);
		}
		return false;
	}
	
	/**
	 * Ѱ��
	 * @param task
	 * @return
	 */
	public boolean lookfor(Task task) {
		//System.out.println("lookfor $task");
		if(!task.isFinished()) {
			Player player =context.getPlayer();
			Player target = (Player) task.get("target");
			String required = (String) task.get("item");
			ItemInstance[] items = dataManager.getItems(player);
			for (ItemInstance item : items) {
				if(item!=null && StringUtils.equals(required, item.getName())) {
					item.alterAmount(-1);
					if(item.getAmount() == 0) {//TODO ��Ʒ���٣�
						dataManager.removePlayerItem(player,item);
					}
					task.setFinished(true);
					rounds = (Integer)task.get("rounds");
					times = (Integer)task.get("times");
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Ѳ��
	 * @param task
	 * @return
	 */
	public boolean patrol(final Task task) {
		//System.out.println("patrol $task");
		final Player player = context.getPlayer();
		player.stop(true);
		//��ʼ��С�ֶ���
		int level = context.getPlayer().getData().getLevel();
		List<Player> t1 = new ArrayList<Player>();
		List<Player> t2 = new ArrayList<Player>();
		String[] elfs = {"2036","2037","2009","2010","2011","2012"};
		String[] elfNames = {"�󺣹�","����","ܽ������","����","��������","����"};
		Random random = new Random();
		final int elfCount = random.nextInt(3)+1;
		for(int i=0;i<elfCount;i++) {		
			int elflevel = Math.max(0,level+random.nextInt(4)-2);
			int elfIndex = random.nextInt(elfs.length); 
			t2.add(dataManager.createElf(elfs[elfIndex], elfNames[elfIndex],elflevel));
		}
		t1.add(player);
		//����ս��
		ApplicationHelper.getApplication().doAction(this, Actions.ENTER_BATTLE,new Object[] {t1,t2});
		helper.prompt("һȺС�������ƻ����ɣ�����ץ�����š�",3000);
		window.addBattleListener(new BattleListener() {
			//ս��ʤ������
			public void battleWin(BattleEvent e) {
				System.out.println("ս��ʤ��");
				task.add("battle",1);
				int exp = player.getData().level*(150 + rand.nextInt(20))*elfCount/10;
				player.getData().exp += exp;
				helper.prompt("���"+exp+"�㾭�顣",3000);
				if((Integer)task.get("battle") == 2) {
					task.setFinished(true);
					rounds = (Integer) task.get("rounds");
					times = (Integer)task.get("times");
					helper.prompt("ʦ��Ѳ��������ɣ���ȥ����ʦ���ɡ�",3000);
				}else {
					helper.prompt("С���ǳ��㲻ע�⣬�ֲ�֪���ﵽ���",3000);
				}
				Environment.set(Environment.LAST_PATROL_TIME,System.currentTimeMillis());
			}
			public void battleTimeout(BattleEvent e) {
			}
			//ս��ʧ�ܴ���
			public void battleDefeated(BattleEvent e) {
				System.out.println("ս��ʧ��");
				//��ѪΪ0������ָ�һ����Ѫ
				if(player.getData().hp ==0) {
					player.getData().hp = 1;
				}
				Environment.set(Environment.LAST_PATROL_TIME,System.currentTimeMillis());
				helper.prompt("�벻��С����Ҳ���ո�ǿ~~!",3000);
				//helper.prompt("ʦ��Ѳ������ʧ�ܣ�����ʦ��ȡ������",3000);
			}
			public void battleBreak(BattleEvent e) {
			}
		});
		return true;
	}
	
	/**
	 * ������������
	 * @param sender
	 * @return
	 */
	public Task create_deliver(String sender) {
		Task task = new Task("school","deliver",sender,randomNpc());
		task.setAutoSpark(true);
		this.times ++;
		if(this.times >ROUND) {
			this.rounds ++;
			this.times %= ROUND;
		}
		task.set("times",times);
		task.set("rounds",rounds);
		int level = context.getPlayer().getData().level;
		task.setExp(this.times*780*level);
		task.setMoney(this.times * 1150*level);
		return task;
	}
	
	/**
	 * ����Ѱ������
	 * @param sender
	 * @return
	 */
	public Task create_lookfor(String sender) {
		Task task = new Task("school","lookfor",sender,sender);
		task.set("item",randomItem());
		this.times ++;
		if(this.times >ROUND) {
			this.rounds ++;
			this.times %= ROUND;
		}
		task.set("times",times);
		task.set("rounds",rounds);
		int level = context.getPlayer().getData().level;
		task.setExp(this.times*980*level);
		task.setMoney(this.times * 2150*level);
		
		return task;
	}
	
	/**
	 * ����Ѳ������
	 * @param sender
	 * @return
	 */
	public Task create_patrol(String sender) {
		Task task = new Task("school","patrol",sender,sender);
		task.setAutoSpark(false);
		//FIXME ������������Ѳ�ߵĳ���id
		if("��Ԫ����".equals(sender)) {
			task.set("sceneId","1146");
		}
		task.set("battle",0);
		
		this.times ++;
		if(this.times >ROUND) {
			this.rounds ++;
			this.times %= ROUND;
		}
		task.set("times",times);
		task.set("rounds",rounds);
		int level = context.getPlayer().getData().level;
		task.setExp(this.times*1080*level);
		task.setMoney(this.times * 1550*level);
				
		return task;
	}
	
	/**�����������������
	 * @param task
	 * @return
	 */
	public String desc_deliver(Task task) {
		return "ʦ��������#R"+task.getReceiver()+"#n���飬�㽫�ż��͹�ȥ��";
	}
	
	public String desc_lookfor(Task task) {
		return "���ɽ�����Ҫ#R"+task.get("item")+"#n������ɽȥѰ��һ��������";
	}
	
	public String desc_patrol(Task task) {
		return "�����������춯���㵽���浽�����߿����������в���֮ͽ����ѵһ�¡�";
	}
	
	private String[] npclist = {"����","��ɫʦ","ҩ���ƹ�"};//FIXME
	/**
	 * ���ѡ��һ��NPC
	 * @return
	 */
	public String randomNpc() {
		//FIXME �������NPC 
		int index = rand.nextInt(npclist.length);
		return npclist[index];
	}
	
	String[] items = {"��Ҷ��","��Ҷ��","����ذ�","�ݹ�","�����","ˮ����","�ϵ���","����","������","��ɫ��",
	             "��Ҷ","�����","��֬","�����ͷ","���в�","��٢�޻�","ɽҩ","�˽���Ҷ","�˲�","�¼���"};
	/**
	 * ���һ����Ʒ
	 * @return
	 */
	public String randomItem() {
		return items[rand.nextInt(items.length)];
	}
}
