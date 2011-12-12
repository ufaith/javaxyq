/**
 * 
 */
package com.javaxyq.battle;

import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.core.BaseApplication;
import com.javaxyq.core.DataManager;
import com.javaxyq.core.ItemManager;
import com.javaxyq.core.SpriteFactory;
import com.javaxyq.data.ItemInstance;
import com.javaxyq.widget.Player;
import com.javaxyq.widget.Sprite;


/**
 *
 * ս��������
 * @author gongdewei
 * @history 2010-5-27 create
 */
public class BattlePlayer {
	private BattleCanvas canvas;
	private DataManager dataManager;
	private ItemManager itemManager; 
	
	public BattlePlayer(BattleCanvas canvas) {
		this.canvas = canvas;
		dataManager = ApplicationHelper.getApplication().getDataManager();
		itemManager = ApplicationHelper.getApplication().getItemManager();
	}
	
	public boolean exec(Command cmd) {
		try {
			this.invokeMethod (cmd.getCmd(), cmd);
			return true;
		} catch (Exception e) {
			System.out.println("����ָ��ʧ�ܣ�");
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * ������
	 * @param cmd
	 */
	public void attack(Command cmd) {
		Player source = cmd.getSource();
		Player target = cmd.getTarget();
		String oldState = target.getState();
		boolean hit = cmd.getBool("hit");
		int hitpoints = cmd.getInt("hitpoints");
		
		Point p0 = source.getLocation();
		setMsg(source.getName() + " ���й��� ");
		Sprite s = SpriteFactory.loadSprite("shape/char/"+source.getCharacter()+"/attack.tcp");
		int dx = s.getWidth()-s.getRefPixelX();
		int dy = s.getHeight()-s.getRefPixelY();
		if(target.getX() > source.getX()) {
			dx = -dx;
			dy = -dy;
		}
		rushForward(source, target.getX() + dx, target.getY() +dy);
		source.playOnce("attack");
		delay(300);
		if(hit) {
		}else {
		}
		rushBack(source, p0.x, p0.y);
		source.setState("stand");
		//target.waitFor();
		if(target.getData().hp <=0) {
		}else {
			target.setState(oldState);
		}
		setMsg("");
		hidePoints(target);
	}
	
	/**
	 * ����ͣ��
	 * @param cmd
	 */
	public void attackstay(Command cmd) {
		
	}
	
	/**
	 * ʩ�ŷ���
	 * @param cmd
	 */
	public void castspell(Command cmd) {
		Player source = cmd.getSource();
		Player target = cmd.getTarget();
		String oldState = target.getState();
		boolean hit = cmd.getBool("hit");
		int hitpoints = cmd.getInt("hitpoints");
		int usemp = cmd.getInt("mp");
		String magicId = (String) cmd.get("magic");
		setMsg(source.getName()+" ʩ������ �� "+magicId);
		//����ħ��
		source.getData().mp -= usemp;
		//effect
		source.playOnce("magic");
		delay(100);
		target.playEffect(magicId, true);
		delay(100);
		if(hit) {
			System.out.printf("%s����%s���˺�%s��\n",source.getName(),target.getName(),hitpoints);
			target.playOnce("hit");
			//target.playEffect("hit");
			showPoints(target, -hitpoints);
			target.getData().hp -= hitpoints;
			source.waitFor();
			source.setState("stand");
		}else {
			System.out.printf("%s ������%s�ķ�������\n",target.getName(),source.getName());
		}
		target.waitForEffect(null);//�ȴ�����ʩ�����
		if(target.getData().hp <=0) {
			//TODO ���������ļ���
			//target.playOnce("die");
			target.getData().hp = 0;
			System.out.printf("%s�мܲ�ס������ս���ϡ�\n",target.getName());
			canvas.cleanPlayer(target);
		}else {
			target.setState(oldState);
		}
		setMsg("");
		hidePoints(target);
	}

	/**
	 * �ܵ��˺�
	 * @param cmd
	 */
	public void damage(Command cmd) {
		setMsg(String.format("%s���������ʽ",cmd.getSource().getName()));
		//cmd.source.playOnce("defend");
		delay(300);
	}
	
	/**
	 * ���˲�����
	 * @param cmd
	 */
	public void downdamage(Command cmd) {
		//target.playOnce("die");
		//target.getData().hp = 0;
		//System.out.printf("%s�мܲ�ס������ս���ϡ�\n",target.getName());
		
		
	}
	/**
	 * ����״̬��ʼ
	 * @param cmd
	 */
	public void buffbegin(Command cmd) {
		
	}
	/**
	 * ����״̬����
	 * @param cmd
	 */
	public void buffend(Command cmd) {
		
	}
	/**
	 * ����״̬��ʼ
	 * @param cmd
	 */
	public void debuffbegin(Command cmd) {
		
	}
	/**
	 * ����״̬����
	 * @param cmd
	 */
	public void debuffend(Command cmd) {
		
	}
	/**
	 * ����
	 * @param cmd
	 */
	public void protect(Command cmd) {
		
	}
	/**
	 * �˺�
	 * @param cmd
	 */
	public void hurt(Command cmd) {
		
	}
	
	/**
	 * ���
	 * @param cmd
	 */
	public void dodge(Command cmd) {
		backward(cmd.getSource());
		//System.out.printf("%s �㿪��%s�Ĺ���\n",target.getName(),source.getName());
		
	}
	
	/**
	 * ʹ��ҩƷ
	 * @param cmd
	 */
	public void medicine(Command cmd) {
		Player source = cmd.getSource();
		Player target = cmd.getTarget();
		String oldState = target.getState();
		
		//effect
		source.playOnce("magic");
		delay(100);
		ItemInstance item = (ItemInstance) cmd.get("item");
		itemManager.useItem(source, item);
		setMsg(String.format("%s ʹ����һ��%s",source.getName(),item.getName()));
		int hpval = cmd.getInt("hp");
		if(hpval > 0) {
			showPoints(target, hpval);
			System.out.printf("%s �ָ���%s����Ѫ\n",target.getName(),hpval);
		}
		source.waitFor();
		source.setState("stand");
		delay(300);
		setMsg("");
		hidePoints(target);		
	}
	/**
	 * ����
	 * @param cmd
	 */
	public void heal(Command cmd) {
		
	}
	/**
	 * �ؼ�
	 * @param cmd
	 */
	public void teji(Command cmd) {
		
	}
	/**
	 * ����
	 * @param cmd
	 */
	public void fabao(Command cmd) {
		
	}
	
	/**
	 * ����ʧ��
	 * @param cmd
	 */
	public void failrun(Command cmd) {
		canvas.runaway(cmd.getSource(),false);
	}
	/**
	 * ���ܳɹ�
	 * @param cmd
	 */
	public void successrun(Command cmd) {
		canvas.runaway(cmd.getSource(),true);
	}
	/**
	 * �峡
	 * @param cmd
	 */
	public void outstage(Command cmd) {
		canvas.cleanPlayer(cmd.getSource());
	}	
	
	//-----------------------------------------------------------------//
	private void delay(long t) {
		try {
			Thread.sleep(t);
		}catch(Exception e) {}
	}

	private void hidePoints(Player player) {
		canvas.hidePoints(player);
	}

	private void rushBack(Player player, int x, int y) {
		canvas.rushBack(player, x, y);
	}

	private void rushForward(Player player, int x, int y) {
		canvas.rushForward(player, x, y);
	}

	private void setMsg(String text) {
		canvas.setMsg(text);
	}

	private void showPoints(Player player, int value) {
		canvas.showPoints(player, value);
	}
	
	private void backward(Player player) {
		canvas.backward(player);
	}
	/**
	 * invoke a  method
	 * @param mName method name
	 * @param arg argument 
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	private Object invokeMethod(String mName, Object arg) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		Method m = this.getClass().getMethod(mName, arg.getClass());
		return m.invoke(this, arg);
	}
	
}
