/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-27
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package ui_script;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Timer;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import com.javaxyq.core.SpriteFactory;
import com.javaxyq.event.ItemEvent;
import com.javaxyq.event.ItemListener;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelHandler;
import com.javaxyq.graph.Label;
import com.javaxyq.graph.Panel;
import com.javaxyq.ui.*;
import com.javaxyq.util.ClosureTask;
import com.javaxyq.core.*;
import com.javaxyq.data.*;

/**
 * 道具行囊对话框脚本
 * @author dewitt
 * @date 2009-11-27 create
 */
class item extends PanelHandler implements MouseListener,MouseMotionListener {
	private Timer timer;
	
	int x0=28,y0 = 198;
	int rows = 4,cols = 5;
	int cellWidth = 51,cellHeight = 51;
	private ItemDetailLabel detailLabel;
	private ItemLabel[] itemlabels ;
	private int selectedIndex = -1;
	private Label selectedBorder = null;
	private Label selectingBorder = null;
	private ItemLabel selItemLabel = null;
	private def template;
	private def labels;
			
	public item() {
		this.detailLabel = new ItemDetailLabel();
		itemlabels = new ItemLabel[rows*cols];
		selectedBorder = new Label(SpriteFactory.loadAnimation('wzife/button/itemselected.tcp'));
		selectingBorder = new Label(SpriteFactory.loadAnimation('wzife/button/itemselecting.tcp'));
	}
	
	public void initial(PanelEvent evt) {
		super.initial(evt);
		this.panel.addMouseListener(this);
		this.panel.addMouseMotionListener(this);
		
		def player = GameMain.getPlayer();
		Label face  = this.panel.findCompByName("face");
		face.setAnim(SpriteFactory.loadAnimation("wzife/photo/facebig/${player.character}.tcp"));
		this.updateItems();
		this.updateLabels();
		this.timer = new Timer();
		this.timer.schedule(new ClosureTask({
			this.updateLabels();
			this.updateItems();
		}), 50, 1000);
				
	}
	
	public void dispose(PanelEvent evt) {
		this.timer.cancel();
		this.panel.removeMouseListener(this);
		this.panel.removeMouseMotionListener(this);
	}

	public void update(PanelEvent evt) {
		
	}
	/**
	 * 更新物品栏
	 */
	synchronized private void updateItems() {
		def items = DataStore.getPlayerItems(GameMain.getPlayer());
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
	
	private void updateLabels() {
		def attrs = GameMain.getPlayer().getData().getProperties();
		attrs['levelExp'] = DataStore.getLevelExp(attrs.level);
		if(this.template == null) {
			this.labels = panel.getComponents().findAll {it.getClass() == Label.class };
			def engine = new groovy.text.SimpleTemplateEngine()
			def vars = []
			for(Label label in labels) {
				vars.add(label.textTpl)
			}
			String strTemplate = vars.join('#');
			this.template = engine.createTemplate(strTemplate);
			System.out.println("template: "+strTemplate);
		}
		int i=0;
		String[] values = this.template.make(attrs).toString().split('#');
		//System.out.println("labels: "+this.labels.size()+", values:"+values.size());
		for(Label label in this.labels) {
			label.text = values[i++];
		}
	}
		
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
	public boolean useItem(MouseEvent e) {
		Component c = e.getComponent();
		if (c instanceof ItemLabel) {
			ItemLabel label = (ItemLabel) c;
			ItemManager.useItem(GameMain.getPlayer(),label.item);
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
