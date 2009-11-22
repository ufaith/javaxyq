/**
 * 
 */
package com.javaxyq.battle;

import java.awt.Point;

import com.javaxyq.core.GameMain;
import com.javaxyq.core.ItemManager;
import com.javaxyq.core.SpriteFactory;
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
		this.invokeMethod (cmd.getCmd(), cmd);
	}
	
	public void attack(Command cmd) {
		Player source = cmd.getSource();
		Player target = cmd.getTarget();
		String oldState = target.getState();
		boolean hit = cmd.get("hit");
		int hitpoints = cmd.params['hitpoints'];
		
		Point p0 = source.getLocation();
		setMsg(source.getName() + " ���й��� ");
		Sprite s = SpriteFactory.loadSprite("shape/char/"+source.getCharacter()+"/attack.tcp");
		int dx = s.getWidth()-s.getCenterX();
		int dy = s.getHeight()-s.getCenterY();
		if(target.x > source.x) {
			dx = -dx;
			dy = -dy;
		}
		rushForward(source, target.getX() + dx, target.getY() +dy);
		source.playOnce("attack");
		delay(300);
		if(hit) {
			println "${source.name}����${target.name}���˺�${hitpoints}��"
			showPoints(target, -hitpoints);
			target.getData().hp -= hitpoints;
			if(cmd.get("defend")) {
				target.playOnce("defend");
				target.playEffect("defend");
			}else {
				target.playOnce("hit");
				target.playEffect("hit");
			}
			source.waitFor();
		}else {
			backward(target);
			println "${target.name} �㿪��${source.name}�Ĺ���"
		}
		rushBack(source, p0.@x, p0.@y);
		source.setState("stand");
		//target.waitFor();
		if(target.data.hp <=0) {
			target.playOnce("die");
			target.data.hp = 0;
			println "${target.name}�мܲ�ס������ս���ϡ�"
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
		boolean hit = cmd.get("hit");
		int hitpoints = cmd.params['hitpoints'];
		int usemp = cmd.getInt('mp');
		String magicId = cmd.params['magic'];
		setMsg("${source.name} ʩ������ �� $magicId");
		//����ħ��
		source.getData().mp -= usemp;
		//effect
		source.playOnce("magic");
		delay(100);
		target.playEffect(magicId);
		delay(100);
		if(hit) {
			println "${source.name}����${target.name}���˺�${hitpoints}��"
			target.playOnce("hit");
			//target.playEffect("hit");
			showPoints(target, -hitpoints);
			target.getData().hp -= hitpoints;
			source.waitFor();
			source.setState("stand");
		}else {
			println "${target.name} ������${source.name}�ķ�������"
		}
		target.waitForEffect();//�ȴ�����ʩ�����
		if(target.data.hp <=0) {
			//TODO ���������ļ���
			target.playOnce("die");
			target.data.hp = 0;
			println "${target.name}�мܲ�ס������ս���ϡ�"
		}else {
			target.setState(oldState);
		}
		setMsg("");
		hidePoints(target);
	}

	private void defend(Command cmd) {
		setMsg("${cmd.source.name}���������ʽ");
		//cmd.source.playOnce('defend');
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
		Item item = cmd.get('item');
		ItemListener listener = ItemManager.findItemAction(item.type);
		setMsg("${source.name} ʹ����һ��${item.name}")
		if(listener) {
			listener.itemUsed(new ItemEvent(target,item,''));
		}
		if(item.amount <= 0) {//��������꣬��������Ʒ
			DataStore.removePlayerItem(source,item);
		}
		def hpval = cmd.get('hp');
		if(hpval && hpval >0) {
			showPoints(target, hpval);
			println "${target.name} �ָ���${hpval}����Ѫ"
		}
		source.waitFor();
		source.setState('stand');
		delay(300);
		setMsg("");
		hidePoints(target);		
	}
	
	private void delay(long t) {
		try {
			Thread.sleep(t);
		}catch(e) {}
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
	
}
