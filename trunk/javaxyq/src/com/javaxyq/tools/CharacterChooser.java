/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-12-7
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.tools;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * �����ɫ����ѡ����
 * @author dewitt
 * @date 2009-12-7 create
 */
public class CharacterChooser extends JDialog {

	private static final long serialVersionUID = 1L;
	private File selectedFile;

	public CharacterChooser(Frame owner) {
		super(owner,"��ѡ��һ����ɫ",true);
		initGUI();
	}

	private void initGUI() {
		add(createMainPanel(),BorderLayout.CENTER);
		add(createButtonPanel(), BorderLayout.SOUTH);
		setSize(400, 300);
	}

	/**
	 * @return
	 */
	private Component createMainPanel() {
		JPanel panel = new JPanel();
		return panel;
	}

	/**
	 * @return
	 */
	private Component createButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton btnOK = new  JButton("ȷ��");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confirm();
				dispose();
			}
		});
		JButton btnCancel = new JButton("ȡ��");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancel();
				dispose();
			}
		});
		panel.add(btnOK);
		panel.add(btnCancel);
		return panel ;
	}

	public File getSelectedFile() {
		return selectedFile;
	}
	
	/**
	 * ȷ��ѡ��
	 */
	private void confirm() {
		
	}
	
	/**
	 * ȡ��ѡ��
	 */
	private void cancel() {
		selectedFile = null;
	}
}
