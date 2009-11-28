package com.javaxyq.graph;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;

import com.javaxyq.core.GameMain;
import com.javaxyq.event.EventDelegator;

public class TextField extends JTextField {

	private List<String> historyInputs;

	private int historyIndex = -1;

	private int maxHistoryCount = 15;

	/** 是否保留历史记录 */
	private boolean history = true;

	private class Handler extends MouseAdapter implements KeyListener, ActionListener {
		public void mouseEntered(MouseEvent e) {
			try {
				GameMain.setCursor(com.javaxyq.widget.Cursor.TEXT_CURSOR);
			} catch (Exception e1) {
			}
		}

		public void mouseExited(MouseEvent e) {
			try {
				GameMain.restoreCursor();
			} catch (Exception e1) {
			}
		}

		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			switch (keyCode) {
			case KeyEvent.VK_ENTER:
				historyIndex = -1;
				addHistoryInput(getText());
				return;
			case KeyEvent.VK_ESCAPE:
				historyIndex = -1;
				setText("");
				return;
			}

			int historyCount = historyInputs.size();
			if (historyCount == 0) {
				return;
			}
			switch (keyCode) {
			case KeyEvent.VK_UP:
				if (historyIndex < historyCount - 1) {
					historyIndex++;
					setText(historyInputs.get(historyIndex));
				}
				break;
			case KeyEvent.VK_DOWN:
				if (historyIndex > 0) {
					historyIndex--;
					setText(historyInputs.get(historyIndex));
				} else {
					historyIndex = -1;
					setText("");
				}
				break;

			default:
				break;
			}
		}

		public void actionPerformed(ActionEvent e) {
			EventDelegator.delegateEvent(e);
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}
	}

	private Handler handler = new Handler();

	public TextField() {
		this("");
	}

	public TextField(String text) {
		super(text);
		this.historyInputs = new ArrayList<String>();
		setFont(GameMain.TEXT_FONT);
		setForeground(Color.WHITE);
		setCaretColor(Color.WHITE);
		setBorder(null);
		setOpaque(false);
		try {
			setCursor(Cursor.getSystemCustomCursor("BLANK_CURSOR"));
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		this.addMouseListener(handler);
		this.addKeyListener(handler);
		this.addActionListener(handler);
	}

	public boolean isHistory() {
		return history;
	}

	public void setHistory(boolean history) {
		this.history = history;
	}

	public void addHistoryInput(String text) {
		this.historyInputs.add(0, text);
		if (this.historyInputs.size() > maxHistoryCount) {
			this.historyInputs.remove(this.historyInputs.size() - 1);
		}
	}

	public int getMaxHistoryCount() {
		return maxHistoryCount;
	}

	public void setMaxHistoryCount(int maxHistoryCount) {
		this.maxHistoryCount = maxHistoryCount;
	}

	public List<String> getHistoryInputs() {
		return historyInputs;
	}

	@Override
	public void paintImmediately(int x, int y, int w, int h) {
		// super.paintImmediately(x, y, w, h);
	}

}
