package com.javaxyq.action;

import com.javaxyq.core.GameMain;
import com.javaxyq.event.ActionEvent;
import com.javaxyq.widget.Player;
import com.javaxyq.widget.Sprite;

public class MoveActions extends BaseAction {

    public void doAction(ActionEvent e) {
        Player player = GameMain.getSceneCanvas().getPlayer();// ��ɫ
        String cmd = e.getCommand();
        //stop
        if ("com.javaxyq.action.Stop".equals(cmd)) {
            player.stop(false);
            //player.setChatText("Yes,Stop!#95");
            return;
        }

        //move
        int direction = Sprite.DIRECTION_BOTTOM_RIGHT;
        if ("com.javaxyq.action.MoveLeft".equals(cmd)) {// direction
            direction = Sprite.DIRECTION_LEFT;
            //player.setChatText("�����ƶ�#92");
        } else if ("com.javaxyq.action.MoveUp".equals(cmd)) {
            direction = Sprite.DIRECTION_TOP;
            //player.setChatText("�����ƶ�#92");
        } else if ("com.javaxyq.action.MoveRight".equals(cmd)) {
            direction = Sprite.DIRECTION_RIGHT;
            //player.setChatText("�����ƶ�#92");
        } else if ("com.javaxyq.action.MoveDown".equals(cmd)) {
            direction = Sprite.DIRECTION_BOTTOM;
            //player.setChatText("�����ƶ�#92");
        } else {
            return;
        }
        player.stepTo(direction);
        player.moveOn();
    }

}
