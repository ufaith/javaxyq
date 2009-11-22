package com.javaxyq.event;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.KeyStroke;

import com.javaxyq.core.GameMain;

/**
 * @author 龚德伟
 * @history 2008-5-11 龚德伟 新建
 */
public class CanvasKeyHandler implements KeyListener {

    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        String actionId = null;
        switch (keycode) {
        case 37:
            actionId = "com.javaxyq.action.MoveLeft";
            break;
        case 38:
            actionId = "com.javaxyq.action.MoveUp";
            break;
        case 39:
            actionId = "com.javaxyq.action.MoveRight";
            break;
        case 40:
            actionId = "com.javaxyq.action.MoveDown";
            break;
        default:
            //actionId = (String) GameMain.getInputMap().get(KeyStroke.getKeyStroke(keycode, e.getModifiers()));
        }
        if (actionId == null) {
            return;
        }
        //System.out.println("key event: "+actionId);
        GameMain.doAction(e.getSource(), actionId);
    }

    public void keyReleased(KeyEvent e) {
        String actionId = "com.javaxyq.action.Stop";
        GameMain.doAction(e.getSource(), actionId);
    }

    public void keyTyped(KeyEvent e) {
        // TODO CanvasKeyEventHandler: keyTyped

    }

}
