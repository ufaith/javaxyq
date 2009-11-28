package com.javaxyq.event;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.javaxyq.ui.*;

public class GameWindowHandler implements WindowListener {

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		UIHelper.showDialog("game_exit");
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}

}
