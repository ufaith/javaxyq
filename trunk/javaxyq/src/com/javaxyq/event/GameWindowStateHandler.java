package com.javaxyq.event;

import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;

public class GameWindowStateHandler implements WindowStateListener {

	public void windowStateChanged(WindowEvent e) {
		// if(e.getNewState()==JFrame.MAXIMIZED_BOTH) {
		// String cmd="com.javaxyq.action.ȫ���л�";
		// GameMain.actionPerformed(e.getSource(), cmd);
		// }else
	    switch (e.getNewState()) {
        case JFrame.ICONIFIED:
            System.out.println("��Ϸ���ڱ���С����");
            break;
        case JFrame.NORMAL:
            System.out.println("��Ϸ���ڻָ�������");
            break;
        case JFrame.MAXIMIZED_BOTH:
            System.out.println("��Ϸ����ȫ��״̬��");
            break;

        default:
            break;
        }
	}

}
