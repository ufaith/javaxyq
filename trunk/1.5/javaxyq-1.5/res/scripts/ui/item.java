/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-27
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package ui;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.jexl2.UnifiedJEXL;
import org.apache.commons.jexl2.UnifiedJEXL.Expression;

import com.javaxyq.core.SpriteFactory;
import com.javaxyq.data.ItemInstance;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelHandler;
import com.javaxyq.ui.ItemDetailLabel;
import com.javaxyq.ui.ItemLabel;
import com.javaxyq.ui.Label;
import com.javaxyq.ui.Panel;
import com.javaxyq.util.StringUtils;
import com.javaxyq.widget.Player;

/**
 * �������ҶԻ���ű�
 * @author dewitt
 * @date 2009-11-27 create
 */
public class item extends PanelHandler implements MouseListener,MouseMotionListener {
	int x0=28,y0 = 198;
	int rows = 4,cols = 5;
	int cellWidth = 51,cellHeight = 51;
	private ItemDetailLabel detailLabel;
	private ItemLabel[] itemlabels ;
	private int selectedIndex = -1;
	private Label selectedBorder = null;
	private Label selectingBorder = null;
	private ItemLabel selItemLabel = null;
	private Expression expression;
	
	public item() {
		this.detailLabel = new ItemDetailLabel();
		itemlabels = new ItemLabel[rows*cols];
		selectedBorder = new Label(SpriteFactory.loadAnimation("wzife/button/itemselected.tcp"));
		selectingBorder = new Label(SpriteFactory.loadAnimation("wzife/button/itemselecting.tcp"));
	}
	
	public void initial(PanelEvent evt) {
		super.initial(evt);
		this.panel.addMouseListener(this);
		this.panel.addMouseMotionListener(this);
		
		Player player = context.getPlayer();
		Label face  = (Label) this.panel.findCompByName("face");
		face.setAnim(SpriteFactory.loadAnimation("wzife/photo/facebig/"+player.getCharacter()+".tcp"));
		this.updateItems();
		this.updateLabels(this.panel);
		this.setAutoUpdate(true);
	}
	
	public void dispose(PanelEvent evt) {
		super.dispose(evt);
		this.panel.removeMouseListener(this);
		this.panel.removeMouseMotionListener(this);
	}

	public void update(PanelEvent evt) {
		this.updateLabels(this.panel);
	}
	/**
	 * ������Ʒ��
	 */
	synchronized private void updateItems() {
		ItemInstance[] items = dataManager.getItems(context.getPlayer());
		for(int r=0;r<rows;r++) {
			for(int c=0;c<cols;c++) {
				//create label
				int index = r*cols + c;
				ItemInstance item = items[index];
				ItemLabel label = itemlabels[index];
				if(item!=null) {//�����б���item
					if(label!=null) {//��������Ʒ
						if(label.getItem() != item) {//�������ͬһ����Ʒ
							label.setItem(item);
						}
					}else {//���ӿ���
						try {
							label = new ItemLabel(item);
							label.setLocation(x0 + c*cellWidth , y0+r*cellHeight+1);
							label.addMouseListener(this);
							label.addMouseMotionListener(this);
							panel.add(label);
							itemlabels[index] = label;
						}catch(Exception e) {
							System.out.println("���itemʧ�ܣ�"+item);
							e.printStackTrace();
						}
					}
				}else {//�������
					if(label!=null) {
						panel.remove(label);
						label.removeMouseListener(this);
						label.removeMouseMotionListener(this);
						itemlabels[index] = null;
					}
				}
			}
		}
	}
	
	private void updateLabels(Panel panel) {
		Component[] comps = panel.getComponents();
		List<Label>labels = new ArrayList<Label>();
		for (Component c : comps) {
			if (c instanceof Label) {
				labels.add((Label) c);
			}
		}
		if(expression == null) {
			try {
				List<String> vars = new ArrayList<String>(); 
				for(Label label : labels) {
					String name = label.getName();
					if(StringUtils.isNotBlank(name)) {
						vars.add(name+"#="+ label.getTextTpl());
					}
				}
				String tpl = StringUtils.join(vars,"#;");
				JexlEngine jexl = new JexlEngine();
				UnifiedJEXL ujexl = new UnifiedJEXL(jexl);
				expression = ujexl.parse(tpl);
			} catch (Exception e) {
				System.out.println("����JEXL���ʽʧ��");
				e.printStackTrace();
			}
		}
		if(expression != null) {
			Map<String, Object> properties = dataManager.getProperties(context.getPlayer());
	        JexlContext jexlcontext = new MapContext(properties);
	        String result = expression.evaluate(jexlcontext).toString();
			String[] items = result.split("#;");
			for (String item : items) {
				String[] values = item.split("#=");
				Label label = (Label) panel.findCompByName(values[0]);
				label.setText(values[1]);
			}
		}
	}		
	
    public void mouseClicked(MouseEvent e) {
    	e.consume();
		switch(e.getButton()){
			case MouseEvent.BUTTON1:
				//�������ƶ���Ʒ
				moveItems(e);
				break;
			case MouseEvent.BUTTON3:
				//�Ҽ����ʹ����Ʒ
				useItem(e);
				break;
		}

    }
	
	/**
	 * �ƶ���Ʒ
	 * @param e
	 */
	synchronized private void moveItems(MouseEvent e){
    	Object src = e.getComponent();
    	Point cell = getCell(e);
    	if(cell!=null) {
    		if(selItemLabel!=null) {//�Ѿ�ѡ������Ʒ
    			int newIndex = cell.x + cols*cell.y;
				if(selectedIndex == newIndex) {
					stopMoving();
					return;
				}
				Player player = context.getPlayer();
				//������ܵ�����Ʒ������ƶ�
				if(!overlayItems(selectedIndex,newIndex)) {
					//����ģ���е�����
					dataManager.swapItem(player, selectedIndex, newIndex);
				}
    			//����ƶ�
    			updateItems();
				stopMoving();
    		}else if(src instanceof ItemLabel){//ѡ����Ʒ
    			ItemLabel label = (ItemLabel) src;
    			selectedBorder.setLocation(x0+cell.x*cellWidth, y0+cell.y*cellHeight);
    			panel.add(selectedBorder,0);
    			label.setVisible(false);
    			helper.setMovingObject(label.getAnim(), new Point(-e.getX(),-e.getY()));
    			selItemLabel = label;
    			selectedIndex = cell.y*cols +cell.x;
    		}
    		
    	}else {//û�е���ڵ�Ԫ���ϣ������ƶ���Ʒ
    		stopMoving();
    	}
    			
	}
    
    /**
     * ֹͣ�ƶ���Ʒ���ƶ���ɻ���ȡ���ƶ���
     */
    private void stopMoving() {
		if(selItemLabel!=null) {
			selItemLabel.setVisible(true);
		}
		selectedIndex = -1;
		selItemLabel = null;
		helper.removeMovingObject();
		panel.remove(selectedBorder);
    	
    }
	
	/**
	 * �ж�������Ʒ�Ƿ���Ե��ӣ������������е���
	 * @param srcIndex
	 * @param destIndex
	 * @return
	 */
	private boolean overlayItems(int srcIndex,int destIndex) {
		ItemInstance[] items = dataManager.getItems(context.getPlayer());
		if(items[srcIndex]!=null && items[destIndex]!=null) {
			//������ӳɹ�
			if(dataManager.overlayItems(items[srcIndex],items[destIndex])) {
				if(items[srcIndex].getAmount()==0) {//�����Ʒ����Ϊ0��������֮
					items[srcIndex] = null;
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * ʹ����Ʒ
	 * @param e
	 * @return
	 */
	public boolean useItem(MouseEvent e) {
		Component c = e.getComponent();
		if (c instanceof ItemLabel) {
			ItemLabel label = (ItemLabel) c;
			application.getItemManager().useItem(context.getPlayer(),label.getItem());
			updateItems();
			return true;
		}
		return false;
	}

    public void mouseMoved(MouseEvent e){
    	Object src = e.getComponent();
    	Point cell = getCell(e);
    	if(cell!=null) {
	    	selectingBorder.setLocation(x0+cell.x*cellWidth-1, y0+cell.y*cellHeight-1);
	    	panel.add(selectingBorder,0);
    	}else {
    		panel.remove(selectingBorder);
    	}
    	
    	
    	if (src instanceof ItemLabel) {
    		ItemLabel label = (ItemLabel) src;
    		if(label.isVisible()) {
    		this.detailLabel.setItem(label.getItem());
    		helper.showToolTip(this.detailLabel,label,e);
    		}
    	}else {
    		//panel.remove(this.detailLabel);
    		helper.hideToolTip(this.detailLabel);
		}
    	
    }
    
    private Point getCell(MouseEvent e) {
		JComponent src = (JComponent) e.getComponent();
    	Point p = e.getPoint();
    	if(src != panel) {
    		p = SwingUtilities.convertPoint(src, p, panel);
    	}
    	if(p.x>x0 && p.x <x0+cellWidth*cols && p.y>y0 && p.y<y0+cellHeight*rows) {
	    	int r = (p.y-y0)/cellHeight;
	    	int c = (p.x-x0)/cellWidth;
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
