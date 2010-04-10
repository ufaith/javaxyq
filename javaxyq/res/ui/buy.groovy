/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-27
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package ui_script;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import com.javaxyq.model.Item;
import java.awt.Color;
import java.awt.Desktop;
import java.net.URI;
import java.util.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.javaxyq.graph.*;

import com.javaxyq.event.*;
import com.javaxyq.core.*;
import com.javaxyq.event.*;
import com.javaxyq.ui.*;
import com.javaxyq.util.*;
/**
 * 购买对话框脚本
 * @author dewitt
 * @date 2009-11-27 create
 */
class buy extends PanelHandler implements MouseListener,MouseMotionListener,DocumentListener{
	private int amount = 0;
	private int price = 0;
	private int totalCost = 0;
	private Timer timer;
	private Label selectedBorder;
	private Label selectingBorder;
	private Item selectedItem;
	private ItemDetailLabel detailLabel = new ItemDetailLabel();
	private TextField fieldAmount; 
	
	@Override
	public void initial(PanelEvent evt) {
		super.initial(evt);
		selectedBorder = new Label(SpriteFactory.loadAnimation('wzife/button/itemselected.tcp'));
		selectingBorder = new Label(SpriteFactory.loadAnimation('wzife/button/itemselecting.tcp'));
		//初始化物品
		def items = ['四叶花','七叶莲','天青地白','草果','九香虫','水黄莲','紫丹罗','佛手','旋复花','百色花',
		             '香叶','龙须草','灵脂','白玉骨头','鬼切草','曼佗罗花','山药','八角莲叶','人参','月见草'];
		int x0 = 8, y0 = 36;
		int rows = 4, cols = 5;
		for(int y =0;y<rows;y++) {
			for(int x=0;x<cols;x++) {
				ItemLabel label = new ItemLabel(DataStore.createItem(items[y*cols+x]));
				label.setName("item-${items[y*cols+x]}");
				label.setSize(50,50);
				label.setLocation(x0+x*51,y0+y*51);
				panel.add(label);
				label.addMouseListener(this);
				label.addMouseMotionListener(this);
			}
		}
		fieldAmount = panel.findCompByName('field_amount');
		fieldAmount.getDocument().addDocumentListener(this);
		timer = new Timer();
		timer.schedule(new ClosureTask({update(null)}) , 100, 500);
	}

	public void update(PanelEvent evt) {
		this.totalCost = amount * price;
		panel.findCompByName('lbl_price').setText("${price}");
		panel.findCompByName('lbl_cost').setText("${totalCost}");
		def player = GameMain.getPlayer();
		panel.findCompByName('lbl_cash').setText("${player.data.money}");
		
	}
	public void confirm_buy(ActionEvent evt) {
		update(null);
		def money = GameMain.getPlayer().getData().money;
		if(money < totalCost) {
			GameMain.doTalk(GameMain.getTalker(),"总共需要#R${totalCost}#n两，你的现金不够呀？！");
		}else {
			money -= totalCost;
			GameMain.getPlayer().getData().money = money;
			def item = selectedItem.clone();
			item.amount=amount;
			DataStore.addItemToPlayer(GameMain.getPlayer(),item);
			GameMain.doTalk(GameMain.getTalker(),"你购买了${amount}个${selectedItem.name}，总共花费了#R${totalCost}#n两。#32");
			println "buy ${selectedItem.name}*${amount}, cost $totalCost"
		}	}
	private void setSelectedItem(Item item) {
		this.selectedItem = item;		
		def label = panel.findCompByName("item-${item.name}");
		selectedBorder.location = [label.x-1, label.y-1];
		panel.add(selectedBorder,0);
		this.price = item.price;
		this.amount = 1;
		update(null);
		fieldAmount.setText("${amount}");
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		setSelectedItem(e.source.item);		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}


	@Override
	public void mouseExited(MouseEvent e) {
		panel.remove(selectingBorder);
		UIHelper.hideToolTip(detailLabel);		
	}


	@Override
	public void mousePressed(MouseEvent e) {
	}


	@Override
	public void mouseReleased(MouseEvent e) {
	}


	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		def label = e.getSource();
		selectingBorder.location = [label.x-1, label.y-1];
		panel.add(selectingBorder,0);
		detailLabel.setItem(label.item);
		UIHelper.showToolTip(detailLabel, label, e);
				
	}
	
	private void syncAmount() {
		try {
			this.amount = fieldAmount.getText().toInteger();
		}catch(ex) {
			//fieldAmount.setText(''+this.amount);
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		syncAmount();
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		syncAmount();
		
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		syncAmount();
		
	}

	
}
