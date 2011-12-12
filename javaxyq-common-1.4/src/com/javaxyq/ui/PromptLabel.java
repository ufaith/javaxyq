/**
 * 
 */
package com.javaxyq.ui;


import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.AbstractBorder;

import com.javaxyq.util.UIUtils;
/**
 * @author dewitt
 *
 */
public class PromptLabel extends JLabel {
	
	public PromptLabel(String text) {
		super(text);
		setSize(320, 36);
		setFont(UIUtils.TEXT_FONT);
		setForeground(Color.YELLOW);
		setBorder(new CompoundBorder(new RoundLineBorder(Color.WHITE,1, 8, 8),new EmptyBorder(10, 10, 10, 10)));
	}
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		g2d.setColor(Color.BLACK);
		g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
		g2d.dispose();
		super.paint(g);
	}
		
}
