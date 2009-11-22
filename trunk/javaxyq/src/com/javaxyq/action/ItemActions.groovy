package com.javaxyq.action;


import com.javaxyq.ui.ItemDetailLabel;
import com.javaxyq.ui.UIHelper;
import com.javaxyq.ui.ItemLabel;
import com.javaxyq.util.ClosureTask;
import com.javaxyq.core.SpriteFactory;
import com.javaxyq.graph.Label;
import com.javaxyq.graph.Label;
import com.javaxyq.core.DataStore;
import com.javaxyq.core.GameMain;
import com.javaxyq.core.ItemManager;
import com.javaxyq.event.ItemEvent;
import com.javaxyq.event.ItemListener;


import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import com.javaxyq.graph.Panel;
import com.javaxyq.model.Item;

/**
 * @author dewitt
 *
 */
public class ItemActions extends BaseAction implements MouseListener,MouseMotionListener{
	
	private Timer timer;
	private Panel panel;
    
    int x0=8,y0 = 186;
    int rows = 4,cols = 5;
    int cellWidth = 51,cellHeight = 51;
    private ItemDetailLabel detailLabel;
    private ItemLabel[] itemlabels ;
    
    public ItemActions() {
		this.detailLabel = new ItemDetailLabel();
		itemlabels = new ItemLabel[rows*cols];
	}
    
    public void doAction(ActionEvent e) {
    	String cmd = e.getCommand();
    	Object source = e.getSource();
    	System.out.println("ItemActions:" + cmd);
    	if(!selectedBorder) {
    		selectedBorder = new Label(SpriteFactory.loadAnimation('wzife/button/itemselected.tcp'));
    	}
    	if(!selectingBorder) {
    		selectingBorder = new Label(SpriteFactory.loadAnimation('wzife/button/itemselecting.tcp'));
    	}
    	switch(cmd) {
    	case ~/.*\.initial$/:
    		this.panel = source;
    		this.panel.addMouseListener(this);
    		this.panel.addMouseMotionListener(this);
			
			def player = GameMain.getPlayer();
			Label face  = this.panel.getComponentByName("face");
			face.setAnim(SpriteFactory.loadAnimation("wzife/photo/facebig/${player.character}.tcp"));
    		this.updateLabels();
    		this.updateItems();
    		this.timer = new Timer();
    		this.timer.schedule(new ClosureTask({
				this.updateLabels();
				this.updateItems();
    		}), 50, 1000);
    		break;
    	case ~/.*\.dispose$/:
    		this.timer.cancel();
    		this.panel.removeMouseListener(this);
    		this.panel.removeMouseMotionListener(this);
    		
    		break;
    	}
    }
    
	/**
	 * 更新物品栏
	 */
	synchronized private void updateItems() {
		def items = DataStore.getPlayerItems(GameMain.getPlayer());
//		int i=0;
//		println '------ Update Items -------'
//		for(def item in items) {   				
//			print " ${item?item.name:'null'}"
//			i++;
//			if(i%5==0){
//				println '';
//			}
//		}
//		println '-------------'
//		i=0;
//		println '------ Update Labels -------'
//		for(def label in itemlabels) {   				
//			print " ${label?label.item.name:'null'}"
//			i++;
//			if(i%5==0){
//				println '';
//			}
//		}
//		println '-------------'
		for(int r=0;r<rows;r++) {
			for(int c=0;c<cols;c++) {
				//create label
				int index = r*cols + c;
				def item = items[index];
				ItemLabel label = itemlabels[index];
				if(item) {//数据列表有item
					if(label) {//格子有物品
						if(label.item != item) {//如果不是同一个物品
							label.setItem(item);
						}
					}else {//格子空着
						try {
							label = new ItemLabel(item);
							label.setLocation(x0 + c*cellWidth , y0+r*cellHeight+1);
							label.addMouseListener(this);
							label.addMouseMotionListener(this);
							panel.add(label);
							itemlabels[index] = label;
						}catch(Exception e) {
							println "添加item失败！$item"
							e.printStackTrace();
						}
					}
				}else {//清除格子
					if(label) {
						panel.remove(label);
						itemlabels[index] = null;
					}
				}
			}
		}
	}
    
    private def template;
    private def labels;
	private void updateLabels() {
		def attrs = GameMain.getPlayer().getData().getProperties();
		attrs['levelExp'] = DataStore.getLevelExp(attrs.level);
		if(!template) {
			labels = panel.getComponents().findAll {it instanceof Label };
			def engine = new groovy.text.SimpleTemplateEngine()
			def vars = []
			for(Label label in labels) {
				vars.add(label.textTpl)
			}
			this.template = engine.createTemplate(vars.join(';'))
		}
		def values = template.make(attrs).toString().split(';')
		def i=0;
		for(Label label in labels) {
			label.text = values[i++];
		}
	}
	
	//============ Mouse Event ====================//
	private int selectedIndex = -1;
	private Label selectedBorder = null;
	private Label selectingBorder = null;
	private ItemLabel selItemLabel = null;
    public void mouseClicked(MouseEvent e) {
    	e.consume();
		switch(e.getButton()){
			case MouseEvent.BUTTON1:
				//左键点击移动物品
				moveItems(e);
				break;
			case MouseEvent.BUTTON3:
				//右键点击使用物品
				useItem(e);
				break;
		}

    }
	
	/**
	 * 移动物品
	 * @param e
	 */
	synchronized private void moveItems(MouseEvent e){
    	Object src = e.getComponent();
    	Point cell = getCell(e);
    	if(cell) {
    		if(selItemLabel) {//已经选择了物品
    			int newIndex = cell.@x + cols*cell.@y;
				if(selectedIndex == newIndex) {
					stopMoving();
					return;
				}
				def player = GameMain.getPlayer();
				//如果不能叠加物品则进行移动
				if(!overlayItems(selectedIndex,newIndex)) {
					//交换模型中的数据
					DataStore.swapItem(player, selectedIndex, newIndex);
				}
    			//完成移动
    			updateItems();
				stopMoving();
    		}else if(src instanceof ItemLabel){//选择物品
    			Label label = (Label) src;
    			selectedBorder.location = [x0+cell.@x*cellWidth, y0+cell.@y*cellHeight];
    			panel.add(selectedBorder,0);
    			label.visible = false;
    			UIHelper.setMovingObject(label.anim, new Point(-e.x,-e.y))
    			selItemLabel = label;
    			selectedIndex = cell.@y*cols +cell.@x;
    		}
    		
    	}else {//没有点击在单元格上，撤销移动物品
    		stopMoving();
    	}
    			
	}
    
    /**
     * 停止移动物品（移动完成或者取消移动）
     */
    private void stopMoving() {
		if(selItemLabel)selItemLabel.setVisible(true);
		selectedIndex = -1;
		selItemLabel = null;
		UIHelper.removeMovingObject();
		panel.remove(selectedBorder);
    	
    }
	
	/**
	 * 判断两个物品是否可以叠加，如果可以则进行叠加
	 * @param srcIndex
	 * @param destIndex
	 * @return
	 */
	private boolean overlayItems(srcIndex,destIndex) {
		def items = DataStore.getPlayerItems(GameMain.getPlayer());
		if(items[srcIndex] && items[destIndex]) {
			//如果叠加成功
			if(DataStore.overlayItems(items[srcIndex],items[destIndex])) {
				if(items[srcIndex].amount==0) {//如果物品数量为0，则销毁之
					items[srcIndex] = null;
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 使用物品
	 * @param e
	 * @return
	 */
	private boolean useItem(MouseEvent e) {
		Component c = e.getComponent();
		if (c instanceof ItemLabel) {
			ItemLabel label = (ItemLabel) c;
			Item item = label.item;
			ItemListener listener = ItemManager.findItemAction(item.type);
			if(listener) {
				listener.itemUsed(new ItemEvent(GameMain.getPlayer(),item,''));
			}
			if(item.amount <= 0) {//如果消耗完，则销毁物品
				def cell =getCell(e);
				DataStore.removePlayerItem(GameMain.getPlayer(),cell.@x+cols*cell.@y);
			}
			updateItems();
		}
	}

    public void mouseMoved(MouseEvent e){
    	Object src = e.getComponent();
    	Point cell = getCell(e);
    	if(cell) {
	    	selectingBorder.location = [x0+cell.@x*cellWidth-1, y0+cell.@y*cellHeight-1];
	    	panel.add(selectingBorder,0);
    	}else {
    		panel.remove(selectingBorder);
    	}
    	
    	
    	if (src instanceof ItemLabel && src.visible) {
    		Label label = (Label) src;
    		this.detailLabel.setItem(label.item);
    		UIHelper.showToolTip(this.detailLabel,label,e);
    	}else {
    		//panel.remove(this.detailLabel);
    		UIHelper.hideToolTip(this.detailLabel);
		}
    	
    }
    
    private Point getCell(MouseEvent e) {
		JComponent src = e.getComponent();
    	Point p = e.getPoint();
    	if(src != panel) {
    		p = SwingUtilities.convertPoint(src, p, panel);
    	}
    	if(p.x>x0 && p.x <x0+cellWidth*cols && p.y>y0 && p.y<y0+cellHeight*rows) {
	    	int r = (p.@y-y0)/cellHeight;
	    	int c = (p.@x-x0)/cellWidth;
	    	return new Point(c,r);
    	}    	
    	return null;
    }
    
    public void mousePressed(MouseEvent e) {e.consume();}
    
    public void mouseReleased(MouseEvent e) {}
    
    public void mouseEntered(MouseEvent e) {}
    
    public void mouseExited(MouseEvent e) {}
    
    public void mouseDragged(MouseEvent e){}
	
}