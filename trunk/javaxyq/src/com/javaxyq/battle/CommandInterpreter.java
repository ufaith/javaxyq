/**
 * 
 */
package com.javaxyq.battle;

import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.javaxyq.core.ItemManager;
import com.javaxyq.core.SpriteFactory;
import com.javaxyq.data.DataStore;
import com.javaxyq.event.ItemEvent;
import com.javaxyq.event.ItemListener;
import com.javaxyq.model.Item;
import com.javaxyq.widget.Player;
import com.javaxyq.widget.Sprite;


/**
 *
 * ս��ָ�������
 * @author dewitt
 */
public class CommandInterpreter {
	private BattleCanvas canvas; 
	
	public CommandInterpreter(BattleCanvas canvas) {
		this.canvas = canvas;
	}
	
	public boolean exec(Command cmd) {
		try {
			this.invokeMethod (cmd.getCmd(), cmd);
			return true;
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
		return false;
	}
	
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
			System.out.println("${source.name}����${target.name}���˺�${hitpoints}��");
			showPoints(target, -hitpoints);
			target.getData().hp -= hitpoints;
			if(cmd.get("defend")!=null) {
				target.playOnce("defend");
				target.playEffect("defend");
			}else {
				target.playOnce("hit");
				target.playEffect("hit");
			}
			source.waitFor();
		}else {
			backward(target);
			System.out.println("${target.name} �㿪��${source.name}�Ĺ���");
		}
		rushBack(source, p0.x, p0.y);
		source.setState("stand");
		//target.waitFor();
		if(target.getData().hp <=0) {
			target.playOnce("die");
			target.getData().hp = 0;
			System.out.println("${target.name}�мܲ�ס������ս���ϡ�");
		}else {
			target.setState(oldState);
		}
		setMsg("");
		hidePoints(target);
	}
	
	public void magic(Command cmd) {
		Player source = cmd.getSource();
		Player target = cmd.getTarget();
		String oldState = target.getState();
		boolean hit = cmd.getBool("hit");
		int hitpoints = cmd.getInt("hitpoints");
		int usemp = cmd.getInt("mp");
		String magicId = (String) cmd.get("magic");
		setMsg("${source.name} ʩ������ �� $magicId");
		//����ħ��
		source.getData().mp -= usemp;
		//effect
		source.playOnce("magic");
		delay(100);
		target.playEffect(magicId);
		delay(100);
		if(hit) {
			System.out.println("${source.name}����${target.name}���˺�${hitpoints}��");
			target.playOnce("hit");
			//target.playEffect("hit");
			showPoints(target, -hitpoints);
			target.getData().hp -= hitpoints;
			source.waitFor();
			source.setState("stand");
		}else {
			System.out.println("${target.name} ������${source.name}�ķ�������");
		}
		target.waitForEffect(null);//�ȴ�����ʩ�����
		if(target.getData().hp <=0) {
			//TODO ���������ļ���
			target.playOnce("die");
			target.getData().hp = 0;
			System.out.println("${target.name}�мܲ�ס������ս���ϡ�");
		}else {
			target.setState(oldState);
		}
		setMsg("");
		hidePoints(target);
	}

	private void defend(Command cmd) {
		setMsg("${cmd.source.name}���������ʽ");
		//cmd.source.playOnce("defend");
		delay(300);
	}
	
	private void runaway(Command cmd) {
		
	}
	
	private void item(Command cmd) {
		Player source = cmd.getSource();
		Player target = cmd.getTarget();
		String oldState = target.getState();
		
		//effect
		source.playOnce("magic");
		delay(100);
		Item item = (Item) cmd.get("item");
		ItemListener listener = ItemManager.findItemAction(item.type);
		setMsg("${source.name} ʹ����һ��${item.name}");
		if(listener!=null) {
			listener.itemUsed(new ItemEvent(target,item,""));
		}
		if(item.amount <= 0) {//��������꣬��������Ʒ
			DataStore.removePlayerItem(source,item);
		}
		int hpval = cmd.getInt("hp");
		if(hpval > 0) {
			showPoints(target, hpval);
			System.out.println("${target.name} �ָ���${hpval}����Ѫ");
		}
		source.waitFor();
		source.setState("stand");
		delay(300);
		setMsg("");
		hidePoints(target);		
	}
	
	private void delay(long t) {
		try {
			Thread.sleep(t);
		}catch(Exception e) {}
	}

	public void hidePoints(Player player) {
		canvas.hidePoints(player);
	}

	public void rushBack(Player player, int x, int y) {
		canvas.rushBack(player, x, y);
	}

	public void rushForward(Player player, int x, int y) {
		canvas.rushForward(player, x, y);
	}

	public void setMsg(String text) {
		canvas.setMsg(text);
	}

	public void showPoints(Player player, int value) {
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
