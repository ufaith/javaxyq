/**
 * 
 */
package com.javaxyq.widget;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.javaxyq.widget.impl.CharacterImpl;

/**
 * @author gongdewei
 * @date 2011-7-24 create
 */
public class CharacterTest {

	private JFrame frame;
	private JPanel buttonPanel;
	private TestCanvas canvas;
	private Character character;
	private String characterId = "0010";
	private boolean moveon;
	
	public static void main(String[] args) {
		CharacterTest test = new CharacterTest();
		test.initGUI();
		test.showFrame();
	}

	private void initGUI() {
		frame = new JFrame("Character Test");
		frame.setSize(740, 480);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.getContentPane().add(createCanvas(), BorderLayout.WEST);
		frame.getContentPane().add(createButtonPanel(), BorderLayout.EAST);
	}

	private Component createCanvas() {
		if(canvas ==null) {
			canvas = new TestCanvas(640, 480);
			canvas.setCharacter(createCharacter());
		}
		return canvas;
	}

	private Character createCharacter() {
		character = new CharacterImpl(characterId );
		character.initialize();
		character.moveTo(100, 150);
		return character;
	}

	private Component createButtonPanel() {
		if(buttonPanel == null) {
			buttonPanel = new JPanel();
		
			JCheckBox jcbMoveon = new JCheckBox("连续行走");
			jcbMoveon.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					JCheckBox checkbox =  (JCheckBox) e.getItem();
					moveon = checkbox.getModel().isSelected();
					character.setMoveon(moveon);
				}
			});
			
			JButton btnWalk = new JButton("行走");
			btnWalk.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					character.walk();
					character.setMoveon(moveon);
				}
			});
			
			JButton btnStop = new JButton("停止");
			btnStop.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					character.setMoveon(false);
					//character.stand();
				}
			});
			
			JButton btnTurn = new JButton("转向");
			btnTurn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					character.turn();
				}
			});
			
			JButton btnTurnAround = new JButton("旋转");
			btnTurnAround.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					long interval=80, t = 8 * interval;
					while(t > 0) {
						try {
							Thread.sleep(interval);
							t -= interval;
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						character.turn();
					}
				}
			});
			
			buttonPanel.add(jcbMoveon);
			buttonPanel.add(btnWalk);
			buttonPanel.add(btnStop);
			buttonPanel.add(btnTurn);
			buttonPanel.add(btnTurnAround);
			buttonPanel.setPreferredSize(new Dimension(100, 200));
		}
		return buttonPanel;
	}

	private void showFrame() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame.setVisible(true);
			}
		});
	}
	
}
