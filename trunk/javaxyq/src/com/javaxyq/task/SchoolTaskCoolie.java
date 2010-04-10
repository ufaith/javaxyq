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

import com.javaxyq.battle.BattleEvent;
import com.javaxyq.battle.BattleListener;
import com.javaxyq.config.PlayerConfig;
import com.javaxyq.config.TalkConfig;
import com.javaxyq.core.GameMain;
import com.javaxyq.core.Helper;
import com.javaxyq.core.ResourceStore;
import com.javaxyq.data.DataStore;
import com.javaxyq.model.Item;
import com.javaxyq.model.Task;
import com.javaxyq.ui.UIHelper;
import com.javaxyq.util.StringUtils;
import com.javaxyq.widget.Player;

/**
 * "��������"����Ԫ
 * @author dewitt
 * @date 2009-11-23 create
 */
class SchoolTaskCoolie extends TaskCoolie {
	
	/** ÿ10��Ϊһ������ */
	private static final int ROUND = 10;
	
	private Random rand = new Random();
	/** ������� */
	int times;
	int rounds = 1;
	/**
	 * ����
	 * @param task
	 * @return
	 */
	public boolean deliver(Task task) {
		//System.out.println("deliver $task");
		if(!task.isFinished()) {
			Player player =GameMain.getPlayer();
			Player target = (Player) task.get("target");
			task.setFinished(true);
			rounds = task.getInt("rounds");
			times = task.getInt("times");
			GameMain.doTalk(target,new TalkConfig("�����յ���ʦ�������ţ��Ͽ��ȥ�����ɡ�"));
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
			Player player =GameMain.getPlayer();
			Player target = (Player) task.get("target");
			String required = (String) task.get("item");
			Item[] items = DataStore.getPlayerItems(player);
			for (Item item : items) {
				if(item!=null && StringUtils.equals(required, item.name)) {
					item.amount --;
					if(item.amount == 0) {
						DataStore.removePlayerItem(player,item);
					}
					task.setFinished(true);
					rounds = Integer.valueOf((String) task.get("rounds"));
					times = Integer.valueOf((String)task.get("times"));
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
		final Player player = GameMain.getPlayer();
		player.stop(true);
		//��ʼ��С�ֶ���
		int level = GameMain.getPlayer().getData().getLevel();
		List<Player> t1 = new ArrayList<Player>();
		List<Player> t2 = new ArrayList<Player>();
		String[] elfs = {"2036","2037","2009","2010","2011","2012"};
		String[] elfNames = {"�󺣹�","����","ܽ������","����","��������","����"};
		Random random = new Random();
		final int elfCount = random.nextInt(3)+1;
		for(int i=0;i<elfCount;i++) {		
			int elflevel = Math.max(0,level+random.nextInt(4)-2);
			int elfIndex = random.nextInt(elfs.length); 
			t2.add(Helper.createElf(elfs[elfIndex], elfNames[elfIndex],elflevel));
		}
		t1.add(player);
		//����ս��
		GameMain.enterBattle(t1,t2);
		UIHelper.prompt("һȺС�������ƻ����ɣ�����ץ�����š�",3000);
		GameMain.getBattleCanvas().addBattleListener(new BattleListener() {
			//ս��ʤ������
			public void battleWin(BattleEvent e) {
				System.out.println("ս��ʤ��");
				task.add("battle",1);
				int exp = player.getData().level*(150 + rand.nextInt(20))*elfCount/10;
				player.getData().exp += exp;
				UIHelper.prompt("���"+exp+"�㾭�顣",3000);
				if((Integer)task.get("battle") == 2) {
					task.setFinished(true);
					rounds = Integer.valueOf((String)task.get("rounds"));
					times = Integer.valueOf((String)task.get("times"));
					UIHelper.prompt("ʦ��Ѳ��������ɣ���ȥ����ʦ���ɡ�",3000);
				}else {
					UIHelper.prompt("С���ǳ��㲻ע�⣬�ֲ�֪���ﵽ���",3000);
				}
				GameMain.getSceneCanvas().setLastPatrolTime(System.currentTimeMillis());
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
				GameMain.getSceneCanvas().setLastPatrolTime(System.currentTimeMillis());
				UIHelper.prompt("�벻��С����Ҳ���ո�ǿ~~!",3000);
				//UIHelper.prompt("ʦ��Ѳ������ʧ�ܣ�����ʦ��ȡ������",3000);
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
		int level = GameMain.getPlayer().getData().level;
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
		int level = GameMain.getPlayer().getData().level;
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
		task.set("sceneId","wzg");
		task.set("battle",0);
		
		this.times ++;
		if(this.times >ROUND) {
			this.rounds ++;
			this.times %= ROUND;
		}
		task.set("times",times);
		task.set("rounds",rounds);
		int level = GameMain.getPlayer().getData().level;
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
	
	/**
	 * ���ѡ��һ��NPC
	 * @return
	 */
	public String randomNpc() {
		//FIXME �������NPC 
		List<PlayerConfig> npcs = ResourceStore.getInstance().getAllNpcs();
		int index = rand.nextInt(npcs.size());
		return npcs.get(index).getName();
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
