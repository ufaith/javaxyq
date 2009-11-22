
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
		Player player = e.getSource();
		def xy = e.getActionCommand().split();
		int x = xy[0].toInteger();
		int y = xy[1].toInteger();
		if(Math.abs(player.sceneX-x)>4) {
			player.stepTo((player.sceneX-x)>0? Sprite.DIRECTION_LEFT : Sprite.DIRECTION_RIGHT);
		}else if(Math.abs(player.sceneY-y)>4) {
			player.stepTo((player.sceneY-y)<0?Sprite.DIRECTION_TOP : Sprite.DIRECTION_BOTTOM)
		}else if(random.nextBoolean()) {
			player.stepTo(random.nextInt(8));
		}
		println "random: (${player.sceneX},${player.sceneY})";
	}

}