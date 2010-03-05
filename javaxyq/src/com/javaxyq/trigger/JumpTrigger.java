/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.trigger;

import java.awt.Point;
import java.awt.Rectangle;

import com.javaxyq.core.GameMain;
import com.javaxyq.core.ResourceStore;
import com.javaxyq.core.SpriteFactory;
import com.javaxyq.widget.Sprite;
import com.javaxyq.widget.Widget;

/**
 * 场景跳转触发器
 * 
 * @author 龚德伟
 * @history 2008-5-31 龚德伟 新建
 */
public class JumpTrigger implements Trigger {
    private Transport transport;

    private boolean enable;

    private Rectangle bounds;

    private Sprite s;

    public JumpTrigger(Transport port) {
        Point p = port.getOriginPosition();
        s = getSprite();
        this.bounds = new Rectangle(p.x-2, p.y-2, 4, 4);
        this.transport = port;
    }

    public Rectangle getBounds() {
        return this.bounds;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean b) {
        this.enable = b;
    }

    public void doAction() {
        String sceneId = this.transport.getGoalScene();
        Point p = this.transport.getGoalPositoin();
        //MapManager.getInstance().fadeToMap(sceneId, p);
        GameMain.doAction(GameMain.getPlayer(), "com.javaxyq.action.transport", new Object[] { sceneId, p.x, p.y });
    }

    public void dispose() {
        this.transport = null;
        this.bounds = null;
    }

    public boolean hit(Point p) {
        return this.bounds.contains(p);
    }

    public Widget getWidget() {
        return ResourceStore.getInstance().findWidget("sprite.jump1");
    }

    public Sprite getSprite() {
        if (s == null) {
            s = SpriteFactory.loadSprite("/magic/jump.tcp");
        }
        return s;
    }

    public Point getLocation() {
        return new Point(this.bounds.x, this.bounds.y);
    }
}
