/*
 * JavaXYQ Source Code 
 * ItemLabel ItemLabel.groovy
 * by kylixs 2009-10
 * All Rights Reserved.
 * Please see also http://javaxyq.cn or http://javaxyq.googlecode.com.
 * Please email to  javaxyq@qq.com.
 */
package com.javaxyq.ui;

import java.awt.Color;
import java.awt.Font;

import com.javaxyq.core.SpriteFactory;
import com.javaxyq.graph.Label;
import com.javaxyq.model.Item;
import com.javaxyq.widget.Animation;


/**
 * @author dewitt
 *
 */
public class ItemLabel extends Label {
	private Font foregroundFont= new Font("ËÎÌו", Font.PLAIN, 14);
	private Item item;
	
	public ItemLabel() {
		super("");
	}
	
	public ItemLabel(Item item) {
		super("");
		this.setItem(item);
	}
	
	public void setItem(Item item) {
		this.item = item;
		if(item!=null) {
			Animation anim = SpriteFactory.loadAnimation(String.format("item/item50/%s.tcp",item.id)); 
			setAnim(anim);
		}else {
			setAnim(null);
		}
		setSize(51,51);
	}
	
	public Item getItem() {
		return item;
	}
		
	protected void paintComponent(java.awt.Graphics g) {
		super.paintComponent(g);
		if(item!=null && item.amount >1) {
			g.setColor(Color.BLACK);
			g.setFont(foregroundFont);
			String str = String.valueOf(item.amount);
			g.drawString(str, 5-1, 15);
			g.drawString(str, 5+1, 15);
			g.drawString(str, 5, 15-1);
			g.drawString(str, 5, 15+1);
			g.setColor(Color.WHITE);
			g.setFont(foregroundFont);
			g.drawString(str, 5, 15);
		}
	}
}
