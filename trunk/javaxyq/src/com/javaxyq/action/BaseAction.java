/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.action;

/**
 * @author ����ΰ
 * @history 2008-6-9 ����ΰ �½�
 */
public abstract class BaseAction extends javax.swing.AbstractAction {

    public abstract void doAction(ActionEvent e);

    public void actionPerformed(java.awt.event.ActionEvent e) {
        this.doAction(new ActionEvent(e.getSource(), e.getActionCommand()));
    }

}
