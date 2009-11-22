/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.event;

import java.awt.Point;

import com.javaxyq.widget.Player;

/**
 * PlayerListener的适配器
 * 
 * @author 龚德伟
 * @history 2008-6-15 龚德伟 新建
 */
public class PlayerAdapter implements PlayerListener {

    public void move(Player player, Point increment) {
    }

    public void stepOver(Player player) {
    }

    public void attack(PlayerEvent evt) {
    }

    public void click(PlayerEvent evt) {
    }

    public void detect(PlayerEvent evt) {
    }

    public void give(PlayerEvent evt) {
    }

    public void talk(PlayerEvent evt) {
    }

}
