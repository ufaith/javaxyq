/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-12-5
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.tools;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;

import com.jidesoft.swing.JideSplitPane;

/**
 * @author dewitt
 * @date 2009-12-5 create
 */
public class GameMaker extends SingleFrameApplication{

	/**
	 * 场景编辑事件监听器 
	 */
	private class SceneHandler extends MouseAdapter{
		
		public void mouseMoved(MouseEvent e) {
			setStatus(1,point2String(e.getPoint()));
			setStatus(2,point2String(sceneEditor.getSelectingCell()));
		}
	}

	/** 选择地图文件 */
	private static final String ACTION_OPEN_MAP_FILE = "openMapFile";
	/** 显示、隐藏网格 */
	private static final String ACTION_TOGGLE_GRID= "toggleGrid";

	public static void main(String[] args) {
		launch(GameMaker.class, args);

	}

	private JFrame mainFrame;
	private JFileChooser fileChooser;
	private int defaultWidth = 800;
	private int defaultHeight = 600;
	private SceneEditor sceneEditor;
	
	private String lastOpenDir="." ;
	private String lastSaveDir="." ;

	private SceneHandler sceneHandler= new SceneHandler();
	private JLabel[] statusLabels;
	
	@Override
	protected void startup() {
		loadProperties();
		mainFrame = getMainFrame();
		initGUI();
		mainFrame.setLocationRelativeTo(null);
		show(mainFrame);
	}
	
	@Override
	protected void shutdown() {
		super.shutdown();
		saveProperties();
	}
	
	/**
	 * 加载配置
	 */
	private void loadProperties() {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream("GameMaker.properties"));
			lastOpenDir = props.getProperty("LastOpenDir",lastOpenDir);
			lastSaveDir = props.getProperty("LastSaveDir",lastSaveDir);
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			System.out.println("没有找到配置文件：GameMaker.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void saveProperties() {
		Properties props = new Properties();
		try {
			props.setProperty("LastOpenDir", lastOpenDir);
			props.setProperty("LastSaveDir", lastSaveDir);
			props.store(new FileOutputStream("GameMaker.properties"),"Create by JavaXYQ Game Maker at "+new java.util.Date());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取Action实例
	 * @param key
	 * @return
	 */
	private javax.swing.Action getAction(String key) {
		return getContext().getActionMap().get(key);
	}
	
	/**
	 * 坐标转成字符串
	 * @param p
	 * @return
	 */
	private String point2String(Point p) {
		if(p!=null)
			return p.x+","+p.y;
		else return "--";
	}

	//---------------- GUI ----------------------------------//
	/**
	 * 
	 */
	private void initGUI() {
		mainFrame.setTitle("JavaXYQ Maker");
		mainFrame.setJMenuBar(createMenubar());
		mainFrame.add(createToolBar(),BorderLayout.NORTH);
		mainFrame.add(createMainPanel(),BorderLayout.CENTER);
		mainFrame.add(createStatusBar(),BorderLayout.SOUTH);
		mainFrame.setSize(defaultWidth, defaultHeight);
		
		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(lastOpenDir));
	}

	/**
	 * @return
	 */
	private JMenuBar createMenubar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu );
		fileMenu.add(getAction(ACTION_OPEN_MAP_FILE));
		
		return menuBar;
	}
	
	/**
	 * @return
	 */
	private Component createToolBar() {
		JToolBar toolbar = new JToolBar();
		
		toolbar.add(getAction(ACTION_OPEN_MAP_FILE));
		toolbar.add(getAction(ACTION_TOGGLE_GRID));
		return toolbar;
	}

	/**
	 * @return
	 */
	private Component createMainPanel() {
		JTabbedPane main = new JTabbedPane();
		main.addTab("总览", createOverviewPanel());
		main.addTab("场景", createScenePanel());
		main.addTab("界面", createUIPanel());
		main.addTab("脚本", createScriptPanel());
		main.addTab("数据", createDataPanel());
		main.setSelectedIndex(1);
		return main;
	}

	/**
	 * @return
	 */
	private Component createDataPanel() {
		return null;
	}

	/**
	 * @return
	 */
	private Component createScriptPanel() {
		return null;
	}

	/**
	 * @return
	 */
	private Component createUIPanel() {
		return null;
	}

	/**
	 * @return
	 */
	private Component createScenePanel() {
		sceneEditor = new SceneEditor();
		sceneEditor.addMouseListener(sceneHandler);
		sceneEditor.addMouseMotionListener(sceneHandler);
		//sceneEditor.setMap("scene/1514.map");
		JScrollPane scrollpane = new JScrollPane(sceneEditor);
		scrollpane.getHorizontalScrollBar().setUnitIncrement(sceneEditor.getCellWidth());
		scrollpane.getVerticalScrollBar().setUnitIncrement(sceneEditor.getCellHeight());
		
		JScrollPane toolkitPanel = new JScrollPane();
		
		//JideSplitPane scenePanel = new JideSplitPane();
		JSplitPane scenePanel = new JSplitPane();
		scenePanel.setDividerLocation(600);
		scenePanel.setLeftComponent(scrollpane);
		scenePanel.setRightComponent(toolkitPanel);
		return scenePanel;
	}

	/**
	 * @return
	 */
	private Component createOverviewPanel() {
		return null;
	}

	//--------------------- StatusBar -----------------------//
	/**
	 * @return
	 */
	private Component createStatusBar() {
		//StatusBar statusBar = new StatusBar(app, taskMonitor)
		JToolBar statusBar = new JToolBar("Status");
		//statusBar.setLayout(new FlowLayout());
		statusLabels = new  JLabel[4];
		int[] widths = new int[] {300,100,100,100};
		for (int i = 0; i < statusLabels.length; i++) {
			JLabel label = new JLabel();
			Dimension d = new Dimension(widths[i], 20);
			label.setPreferredSize(d);
			label.setSize(d);
			label.setMinimumSize(d);
			label.setMaximumSize(d);
			statusLabels[i] = label;
			statusBar.add(label);
			statusBar.addSeparator();
		}
		
		return statusBar;
	}
	/**
	 * 设置提示信息
	 * @param index
	 * @param msg
	 */
	private void setStatus(int index, Object msg) {
		statusLabels[index].setText(msg.toString());
	}
	
	//------------------------ Actions --------------------------//
	@Action
	public void openMapFile() {
		int rtn = fileChooser.showOpenDialog(mainFrame);
		if(rtn == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			lastOpenDir = file.getParent();
			this.sceneEditor.setMap(file);
			this.sceneEditor.getParent().validate();
		}
	}
	
	/**
	 * 显示、隐藏网格
	 */
	@Action
	public void toggleGrid() {
		this.sceneEditor.toggleGrid();
	}
}
