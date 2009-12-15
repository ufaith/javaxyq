/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-12-5
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.tools;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.SingleFrameApplication;

import com.javaxyq.core.SpriteFactory;
import com.javaxyq.util.WASDecoder;
import com.javaxyq.widget.Animation;

/**
 * 场景编辑器
 * 
 * @author dewitt
 * @date 2009-12-5 create
 */
public class SceneEditor extends JPanel implements MouseListener {

	private static final long serialVersionUID = 1L;
	private JMap map;
	private CellPanel cellPanel;
	private File mapfile;
	private boolean gridShowing = true;
	private SceneLayer eventLayer;
	private SceneLayer topLayer;
	private CharacterChooser spriteChooser;
	private int cellWidth = 20;
	private int cellHeight = 20;

	public SceneEditor() {
		initGUI();
	}

	/**
	 * 初始化GUI
	 * 
	 * @return
	 */
	private void initGUI() {
		setLayout(null);
		this.map = new JMap();
		cellPanel = new CellPanel();
		eventLayer = new SceneLayer();
		eventLayer.setComponentPopupMenu(createEventLayerMenu());
		add(eventLayer);
		add(cellPanel);
		add(this.map);

		setTopLayer(eventLayer);
		
		addMouseListener(this);
		
		spriteChooser = new CharacterChooser(((SingleFrameApplication)Application.getInstance()).getMainFrame());
	}

	/**
	 * 获取Action实例
	 * 
	 * @param name
	 * @return
	 */
	private javax.swing.Action getAction(String name) {
		ApplicationContext context = Application.getInstance().getContext();
		return context.getActionMap(this).get(name);
	}

	/**
	 * @return
	 */
	private JPopupMenu createEventLayerMenu() {
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(getAction("place"));
		return popupMenu;
	}

	/**
	 * 设置当前地图
	 * 
	 * @param filename
	 * @return
	 */
	public void setMap(File file) {
		this.mapfile = file;
		this.map.loadMap(file);
		this.cellPanel.setSize(this.map.getWidth(), this.map.getHeight());
		this.eventLayer.setSize(this.map.getWidth(), this.map.getHeight());
		this.eventLayer.clearStatus();
		this.setPreferredSize(this.cellPanel.getSize());
	}

	public CellPanel getCellPanel() {
		return cellPanel;
	}

	public File getMapfile() {
		return mapfile;
	}

	public int getCellHeight() {
		return cellPanel.getCellHeight();
	}

	public int getCellWidth() {
		return cellPanel.getCellWidth();
	}

	public void setCellHeight(int cellHeight) {
		cellPanel.setCellHeight(cellHeight);
	}

	public void setCellWidth(int cellWidth) {
		cellPanel.setCellWidth(cellWidth);
	}

	/**
	 * 显示、隐藏网格
	 */
	public void toggleGrid() {
		gridShowing = !gridShowing;
		cellPanel.setVisible(gridShowing);
	}

	/**
	 * 当前是否显示网格
	 * 
	 * @return the gridShowing
	 */
	public boolean isGridShowing() {
		return gridShowing;
	}

	/**
	 * 设置最顶层的图层（置顶后可接收鼠标、键盘操作）
	 * 
	 * @param layer
	 */
	private void setTopLayer(SceneLayer layer) {
		if (this.topLayer != null) {
			removeListeners();
		}
		this.topLayer = layer;
		installListeners();
	}

	/**
	 * 删除topLayer的鼠标事件监听器
	 */
	private void removeListeners() {
		MouseListener[] listeners1 = getListeners(MouseListener.class);
		MouseMotionListener[] listeners2 = getListeners(MouseMotionListener.class);
		for (int i = 0; i < listeners1.length; i++) {
			topLayer.removeMouseListener(listeners1[i]);
		}
		for (int i = 0; i < listeners2.length; i++) {
			topLayer.removeMouseMotionListener(listeners2[i]);
		}
	}

	/**
	 * 给topLayer添加监听器
	 */
	private void installListeners() {
		MouseListener[] listeners1 = getListeners(MouseListener.class);
		MouseMotionListener[] listeners2 = getListeners(MouseMotionListener.class);
		for (int i = 0; i < listeners1.length; i++) {
			topLayer.addMouseListener(listeners1[i]);
		}
		for (int i = 0; i < listeners2.length; i++) {
			topLayer.addMouseMotionListener(listeners2[i]);
		}

	}

	public void addMouseListener(MouseListener l) {
		super.addMouseListener(l);
		topLayer.addMouseListener(l);
	}

	public void addMouseMotionListener(MouseMotionListener l) {
		super.addMouseMotionListener(l);
		topLayer.addMouseMotionListener(l);
	}

	public void removeMouseListener(MouseListener l) {
		super.removeMouseListener(l);
		topLayer.removeMouseListener(l);
	}

	public void removeMouseMotionListener(MouseMotionListener l) {
		super.removeMouseMotionListener(l);
		topLayer.removeMouseMotionListener(l);
	}

	public Point getSelectedCell() {
		return eventLayer.getSelectedCell();
	}
	
	public Point getSelectingCell() {
		return eventLayer.getSelectingCell();
	}


	// ------------------------- Actions --------------------------------//
	/**
	 * 放置npc、触发器等
	 */
	@Action
	public void place() {
		Point cell = eventLayer.getSelectedCell();
		System.out.println("place: (" + cell.x + "," + cell.y + ")");
		//spriteChooser.setLocationRelativeTo(this);
		//spriteChooser.setVisible(true);
		
		String filename = "shape/char/0001/stand.tcp";
		//File file = new File(filename);
		
		Animation anim = SpriteFactory.loadAnimation(filename);
		ImageIcon icon = new ImageIcon(anim.getImage());
		JLabel label = new JLabel(icon);
		label.setSize(icon.getIconWidth(), icon.getIconHeight());
		Point pos = sceneToLocal(cell.x,cell.y);
		pos.translate(-anim.getCenterX(), -anim.getCenterY());
		label.setLocation(pos);
		eventLayer.add(label);
	}
	
	private Point sceneToLocal(int x,int y) {
		return new Point(x*cellWidth, getHeight()-y*cellHeight);
	}
	
	// --------------------- Listeners -----------------------------//
	public void mouseClicked(MouseEvent e) {
		if(e.getButton()==MouseEvent.BUTTON1 && e.getClickCount()==2) {
			place();
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

}
