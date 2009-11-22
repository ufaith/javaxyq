package com.javaxyq.action;

import java.awt.Component;
import java.awt.Container;

import com.javaxyq.core.GameMain;
import com.javaxyq.graph.Panel;

public class SharedActions extends BaseAction {

    private static final long serialVersionUID = 1L;

    public void doAction(ActionEvent e) {
        String cmd = e.getCommand();
        if ("com.javaxyq.action.CloseAction".endsWith(cmd)) {
            Component comp = (Component) e.getSource();
            Container parent = comp.getParent();
            GameMain.hideDialog((Panel) parent);
        } else if ("com.javaxyq.action.HelpAction".endsWith(cmd)) {

        } else {
            System.out.println("SharedActions:" + cmd);
        }
    }

}
