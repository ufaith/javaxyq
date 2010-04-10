
package com.javaxyq.action;

import com.javaxyq.widget.Sprite;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import com.javaxyq.widget.Player;

/**
 * @author dewitt
 *
 */
public class RandomMovementAction implements ActionListener {
	
	private Random random = new Random();
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Player player = (Player) e.getSource();
		String[] xy = e.getActionCommand().split(" ");
		int x = Integer.valueOf(xy[0]);
		int y = Integer.valueOf(xy[1]);
		if(Math.abs(player.getSceneX()-x)>4) {
			player.stepTo((player.getSceneX()-x)>0? Sprite.DIRECTION_LEFT : Sprite.DIRECTION_RIGHT);
		}else if(Math.abs(player.getSceneY()-y)>4) {
			player.stepTo((player.getSceneY()-y)<0?Sprite.DIRECTION_TOP : Sprite.DIRECTION_BOTTOM);
		}else if(random.nextBoolean()) {
			player.stepTo(random.nextInt(8));
		}
	}

}