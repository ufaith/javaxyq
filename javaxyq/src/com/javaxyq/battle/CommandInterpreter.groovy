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
 * 战斗指令解释器
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
		setMsg(source.getName() + " 进行攻击 ");
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
			println "${source.name}击中${target.name}，伤害${hitpoints}点"
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
			println "${target.name} 躲开了${source.name}的攻击"
		}
		rushBack(source, p0.@x, p0.@y);
		source.setState("stand");
		//target.waitFor();
		if(target.data.hp <=0) {
			target.playOnce("die");
			target.data.hp = 0;
			println "${target.name}招架不住，倒在战场上。"
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
		setMsg("${source.name} 施法法术 — $magicId");
		//消耗魔法
		source.getData().mp -= usemp;
		//effect
		source.playOnce("magic");
		delay(100);
		target.playEffect(magicId);
		delay(100);
		if(hit) {
			println "${source.name}击中${target.name}，伤害${hitpoints}点"
			target.playOnce("hit");
			//target.playEffect("hit");
			showPoints(target, -hitpoints);
			target.getData().hp -= hitpoints;
			source.waitFor();
			source.setState("stand");
		}else {
			println "${target.name} 抵御了${source.name}的法术攻击"
		}
		target.waitForEffect();//等待法术施法完毕
		if(target.data.hp <=0) {
			//TODO 改善死亡的计算
			target.playOnce("die");
			target.data.hp = 0;
			println "${target.name}招架不住，倒在战场上。"
		}else {
			target.setState(oldState);
		}
		setMsg("");
		hidePoints(target);
	}

	private void defend(Command cmd) {
		setMsg("${cmd.source.name}摆起防御招式");
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
		setMsg("${source.name} 使用了一个${item.name}")
		if(listener) {
			listener.itemUsed(new ItemEvent(target,item,''));
		}
		if(item.amount <= 0) {//如果消耗完，则销毁物品
			DataStore.removePlayerItem(source,item);
		}
		def hpval = cmd.get('hp');
		if(hpval && hpval >0) {
			showPoints(target, hpval);
			println "${target.name} 恢复了${hpval}点气血"
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
