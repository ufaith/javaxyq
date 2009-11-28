/*
 * JavaXYQ Source Code 
 * ItemDetailLabel ItemDetailLabel.groovy
 * by kylixs 2009-10
 * All Rights Reserved.
 * Please see also http://javaxyq.cn or http://javaxyq.googlecode.com.
 * Please email to  javaxyq@qq.com.
 */
package com.javaxyq.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.FontMetrics;

import com.javaxyq.core.GameMain;
import com.javaxyq.core.SpriteFactory;
import com.javaxyq.model.Item;
import com.javaxyq.widget.Animation;



/**
 * @author dewitt
 *
 */
class ItemDetailLabel extends PromptLabel {

	private static final int IMG_WIDTH = 120;
	private static final int IMG_TOP = 10;
	private static final int TITLE_POS = 30;
	private static Font titleFont = new Font('宋体', Font.BOLD, 18); 
	
	private Item item;
	private Animation anim;
	public ItemDetailLabel(Item item) {
		super('');
		this.setItem(item);
		setSize(310,170);
	}
	public ItemDetailLabel() {
		super('');
		setSize(310,170);
	}
	
	public void setItem(Item item) {
		this.item = item;
		this.anim = SpriteFactory.loadAnimation("item/item120/${item.id}.tcp");
	}
	
	protected void paintComponent(java.awt.Graphics g) {
		if(!this.item)return;
		int imgX = (IMG_WIDTH- this.anim.getWidth())/2;
		if(this.anim) this.anim.draw(g, imgX+this.anim.centerX, IMG_TOP+this.anim.centerY);
		
		g.translate(IMG_WIDTH, TITLE_POS);
		g.setColor(Color.YELLOW);
		g.setFont(titleFont);
		g.drawString(item.name, 0, 0);//title
		def strs = [];
		//说明
		strs << item.desc;
		//功效
		if(item.efficacy) {
			strs << '【功效】'+item.efficacy;
			strs << '#Y'+item.efficacy+'，等级：'+item.level;
		}
			
		drawStrings(g,strs)
		g.translate(-IMG_WIDTH, -TITLE_POS);
	}
	
	private void drawStrings(Graphics g,List lines) {
		g.setFont(GameMain.TEXT_FONT);
		FontMetrics fm = g.getFontMetrics();
		int fw = fm.charWidth('中'.charAt(0));
		int fh = fm.getHeight();
		int textWidth = getWidth()-IMG_WIDTH;
		int lineLen = textWidth.intdiv(fw);
		int dy = 0;
		lines.each{line ->
			if(line.startsWith('#Y')) {
				g.setColor(Color.YELLOW);
				line = line.substring(2);
			}else {
				g.setColor(Color.WHITE);
			}
			int index = 0;
			int count = line.length();
			String str;
			while(index < count) {
				dy += fh;
				str = line.substring(index,index + Math.min(lineLen, count - index))
				index += str.length();
				g.drawString(str, 0, dy);
			}
		}
	}
}
