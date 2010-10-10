/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-14
 * http://javaxyq.googlecode.com
 * kylixs@qq.com
 */
package com.javaxyq.battle;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.javaxyq.data.ItemInstance;
import com.javaxyq.data.MedicineItem;
import com.javaxyq.model.Item;
import com.javaxyq.model.PlayerVO;
import com.javaxyq.widget.Player;

/**
 * ս��ָ�������
 * @author dewitt
 * @date 2009-11-14 create
 */
public class BattleCalculator {
	
	private Random random = new Random();
	private Map<Player,PlayerVO> datas = new HashMap<Player, PlayerVO>(); 
	private Map defends = new HashMap() ;
	private List ownsideTeam;
	private List adversaryTeam;
	public List calc(List<Command> cmdlist,List ownsideTeam,List adversaryTeam,Map params) {
		List results = new ArrayList();
		defends.clear();
		datas.clear();
		this.ownsideTeam = ownsideTeam;
		this.adversaryTeam = adversaryTeam;
		for (int i = 0; i < ownsideTeam.size(); i++) {
			Player player = (Player) ownsideTeam.get(i);
			PlayerVO vo = new PlayerVO(player.getData());
			datas.put(player, vo);
		}
		for (int i = 0; i < adversaryTeam.size(); i++) {
			Player player = (Player) adversaryTeam.get(i);
			PlayerVO vo = new PlayerVO(player.getData());
			datas.put(player, vo);
		}
		
		//�������ٶ�����
		Collections.sort(cmdlist);
		//cmdlist = cmdlist.sort{cmd -> cmd.source.data.�ٶ� + cmd.source.data.tmp�ٶ�}.reverse()
		System.out.println("cmdlist: "+cmdlist);
		for (int i = 0; i < cmdlist.size(); i++) {
			Command cmd = (Command) cmdlist.get(i);
			Object result = this.invokeMethod(cmd.getCmd(),cmd);
			if(result!=null)results.add(result);
			if(ownsideTeam.size()==0 || adversaryTeam.size() == 0) {
				//���һ���ĳ�Աȫ���뿪ս������ս������
				break;
			}
		}
		return results;
	}
	
	public Command attack(Command cmd) {
		PlayerVO src = datas.get(cmd.getSource());
		PlayerVO target = datas.get(cmd.getTarget());
		if(target == null) {
			System.out.println(cmd.getTarget().getName()+"�Ѿ��˳�ս����������Ч��");
			return null;
		}
		if(src.hp <=0) {
			System.out.println(src.getName()+" �ѵ��£��޷���������");
			return null;
		}else if(target.hp <= 0) {
			System.out.println(target.getName()+"�ѵ��£�"+src.getName() +"�Ĺ�����Ч");
			return null;
		}
		int hitpoints = 0;
		//�ж��Ƿ�����
		float d = src.���� - target.���;
		if(d<0)d = 0;
		boolean hit = (random.nextFloat()<= 0.7+d*1.0/src.����);
		cmd.add("hit",hit);
		//�����˺�ֵ
		if(hit) {
			d = src.�˺� - target.����;
			if(d<0)d=0;
			hitpoints = (int) (0.05 * src.�˺� + (0.9+ (random.nextInt(20)-9)*1.0/100)*d);
			if(defends.get(target)!=null) {
				hitpoints *= 0.5;
				cmd.add("defend",true);
			}
			cmd.add("hitpoints",hitpoints);
			target.hp -= hitpoints;
			if(target.hp <0)target.hp = 0;
			cmd.add("die",true);
		}
		return cmd;
	}
	
	public Command magic(Command cmd) {
		PlayerVO src = datas.get(cmd.getSource());
		PlayerVO target = datas.get(cmd.getTarget());
		int usedmp = cmd.getInt("mp");
		if(target == null) {
			System.out.println(cmd.getTarget().getName()+"�Ѿ��˳�ս����������Ч��");
			return null;
		}
		if(src.hp <=0) {
			System.out.println(src.getName()+" �ѵ��£��޷���������");
			return null;
		}else if(target.hp <= 0) {
			System.out.println(target.getName()+"�ѵ��£�"+src.getName() +"�Ĺ�����Ч");
			return null;
		}else if(src.mp < usedmp) {
			System.out.println(src.getName()+" �������㣬ʩ�ŷ���ʧ�ܣ�");
			return null;
		}
		src.mp -= usedmp;
		int hitpoints = 0;
		//�ж��Ƿ�����
		float d = src.���� - target.����;
		if(d<0)d = 0;
		boolean hit = (random.nextFloat()<= 0.9+d*1.0/src.����);
		cmd.add("hit",hit);
		//TODO ���㷨���˺�ֵ
		if(hit) {
			//���������˺�ϵ��
			int basehit = cmd.getInt("basehit");
			hitpoints =(int) (0.5*src.����*basehit+ 0.5*(1+ (random.nextInt(20)-9)*1.0/100)*d* basehit);
			cmd.add("hitpoints",hitpoints);
			target.hp -= hitpoints;
			if(target.hp <0)target.hp = 0;
			cmd.add("die",true);
		}
		return cmd;
	}

	public Command defend(Command cmd) {
		defends.put(datas.get(cmd.getSource()),true);
		return cmd;
	}
	
	public Command runaway(Command cmd) {
		boolean success = random.nextFloat()<0.9; 
		cmd.add("runaway",success);
		if(success) {
			datas.remove(cmd.getSource());
			ownsideTeam.remove(cmd.getSource());
			adversaryTeam.remove(cmd.getSource());
		}
		return cmd;
	}

	public Command item(Command cmd) {
		PlayerVO src = datas.get(cmd.getSource());
		PlayerVO target = datas.get(cmd.getTarget());
		if(target == null) {
			System.out.println(cmd.getTarget().getName()+"�Ѿ��˳�ս����������Ч��");
			return null;
		}
		if(src.hp <=0) {
			System.out.println(src.getName()+" �ѵ��£��޷�ʹ�õ���");
			return null;
		}else if(target.hp <= 0) {
			System.out.println(target.getName()+"�ѵ��£��޷�����ҩ��");
			return null;
		}		
		ItemInstance iteminst = (ItemInstance) cmd.get("item");
		Item item = iteminst.getItem();
		if(item != null) {
			if (item instanceof MedicineItem) {
				MedicineItem mitem = (MedicineItem) item;				
				if(mitem.getHp()!=0) {
					target.hp += mitem.getHp();
					cmd.add("hp",mitem.getHp());
				}
				if(mitem.getMp()!=0) {
					target.mp += mitem.getMp();
					cmd.add("mp",mitem.getMp());
				}
			}
		}
		return cmd;
	}
	/**
	 * invoke a  method
	 * @param mName method name
	 * @param arg argument 
	 * @return
	 */
	private Object invokeMethod(String mName, Object arg) {
		try {
			Method m = this.getClass().getMethod(mName, arg.getClass());
			return m.invoke(this, arg);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
