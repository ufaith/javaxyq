package com.javaxyq.core;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ComponentInputMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import com.javaxyq.action.ActionEvent;
import com.javaxyq.action.BaseAction;
import com.javaxyq.battle.BattleCanvas;
import com.javaxyq.config.TalkConfig;
import com.javaxyq.event.Listener;
import com.javaxyq.graph.Canvas;
import com.javaxyq.graph.LoadingCanvas;
import com.javaxyq.graph.Panel;
import com.javaxyq.graph.SceneCanvas;
import com.javaxyq.graph.TalkPanel;
import com.javaxyq.graph.Window;
import com.javaxyq.widget.Cursor;
import com.javaxyq.widget.Player;
import com.javaxyq.widget.TileMap;

/**
 * JavaXYQ 游戏入口类
 * 
 * @author 龚德伟
 * @history 2008-6-7 龚德伟 新建
 */
public final class GameMain {

	/** global action map */
	private static ActionMap actionMap = new ActionMap();

	private static InputMap inputMap = new InputMap();

	/**
	 * 动画播放每帧的间隔(ms)
	 */
	public static final int ANIMATION_INTERVAL = 100;

	private static String applicationName = "JavaXYQ";

	private static LoadingCanvas loadingCanvas;
	
	private static SceneCanvas sceneCanvas;
	
	private static BattleCanvas battleCanvas;

	private static FontMetrics fontMetrics;

	private static Window gameWindow;

	private static String homeURL;

	public static final Font TEXT_FONT = new Font("宋体", Font.PLAIN, 14);

	public static final Color COLOR_NAME_BACKGROUND = new Color(27, 26, 18);

	public static final Color COLOR_NAME = new Color(118, 229, 128);

	public static final Font TEXT_NAME_FONT = new Font("宋体", Font.PLAIN, 16);

	public static final Color TEXT_NAME_NPC_COLOR = new Color(219, 197, 63);

	private static String version;

	private static DisplayMode displayMode;

	private static boolean isDebug;

	private static Player talker;

	private static boolean showCopyright = true;

	/**
	 * 执行指定ActionCommand的Action
	 * 
	 * @param source
	 *            触发Action的源对象
	 * @param cmd动作的actiomCommand
	 *            ,而非类名
	 */
	public static void doAction(Object source, String actionId, Object[] args) {
		Action action = actionMap.get(actionId);
		if (action == null && actionId.startsWith("com.javaxyq.action.dialog")) {
			action = actionMap.get("com.javaxyq.action.dialog");
		}
		if (action == null) {
			String wildcard = actionId.substring(0, actionId.lastIndexOf('.')) + ".*";
			action = actionMap.get(wildcard);
		}
		if (action == null) {
			return;
		}
		ActionEvent e = new ActionEvent(source, actionId, args);
		if (action instanceof BaseAction) {
			BaseAction a = (BaseAction) action;
			a.doAction(e);
		} else {
			action.actionPerformed(e);
		}
	}

	public static void addWindowListener(WindowListener handler) {
		gameWindow.addWindowListener(handler);
	}

	public static void addWindowStateListener(WindowStateListener handler) {
		gameWindow.addWindowStateListener(handler);
	}

	public static void fullScreen() {
		if (gameWindow.isFullScreen()) {
			gameWindow.restoreScreen();
		} else {
			gameWindow.setFullScreen();
		}
	}

	public static ActionMap getActionMap() {
		return actionMap;
	}

	public static SceneCanvas getSceneCanvas() {
		return sceneCanvas;
	}

	public static void getTarget() {

	}

	public static Window getWindow() {
		return gameWindow;
	}

	public static void loadGame() {
		startLoading();
		// load scripts
		updateLoading("loading resources ...");
		// loadResources();

		// copyright
		if (showCopyright) {
			Image img = SpriteFactory.loadImage("/resources/loading/声明.jpg");
			loadingCanvas.showImage(img, 3000);
			img = SpriteFactory.loadImage("/resources/loading/资源版权.jpg");
			loadingCanvas.showImage(img, 2000);

			img = SpriteFactory.loadImage("/resources/loading/梦想.jpg");
			loadingCanvas.showImage(img, 3000);
			img = SpriteFactory.loadImage("/resources/loading/感谢.jpg");
			loadingCanvas.showImage(img, 3000);
			img = SpriteFactory.loadImage("/resources/loading/version1.3.jpg");
			loadingCanvas.showImage(img, 3000);
		}
	}

	private static void initUI() {
		String[] uiIds = new String[] {"ui.main.dialog1","ui.main.dialog2","ui.main.dialog3"};
   		for(String id : uiIds) {
   			System.out.println("安装UI："+id);
   			Panel dlg = DialogFactory.getDialog(id);
   			GameMain.addUIComponent(dlg);
   		}
	}

	// public static void fadeToMap(String sceneId) {
	// MapManager.getInstance().fadeToMap(sceneId, player.getSceneLocation());
	// }

	public static void fadeToMap(String sceneId, int x, int y) {
		player.stop(true);
		MapManager.getInstance().fadeToMap(sceneId, new Point(x, y));
	}

	public static String getCurrentScene() {
		return MapManager.getInstance().getCurrentScene();
	}

	public static void main(String[] args) throws InterruptedException {
		// System.getProperties().list(System.out);
		init(args);
	}

	public static void init(String[] args) {
		initDisplay(args);
		// loading canvas
		Dimension preferredSize = new Dimension(displayMode.getWidth(), displayMode.getHeight());
		loadingCanvas = new LoadingCanvas();
		loadingCanvas.setPreferredSize(preferredSize);
		//scene canvas
		sceneCanvas = new SceneCanvas();
        sceneCanvas.setPreferredSize(preferredSize);
        sceneCanvas.setSize(preferredSize);
        
		gameWindow = new Window(displayMode);
		gameWindow.setTitle(applicationName +" "+ version);
		gameWindow.setCanvas(loadingCanvas);
		fontMetrics = gameWindow.getFontMetrics(TEXT_NAME_FONT);

		gameWindow.setLocationRelativeTo(null);
		gameWindow.setVisible(true);
		loadGame();
	}

	private static DisplayMode initDisplay(String[] args) {
		int width = 640, height = 480;
		if (args.length == 3) {
			width = Integer.valueOf(args[0]);
			height = Integer.valueOf(args[1]);
			displayMode = new DisplayMode(width, height, Integer.valueOf(args[2]),
					DisplayMode.REFRESH_RATE_UNKNOWN);
		} else {
			displayMode = new DisplayMode(width, height, 16, DisplayMode.REFRESH_RATE_UNKNOWN);
		}
		return displayMode;
	}

	/**
	 * 显示或隐藏指定对话框
	 * 
	 * @param autoSwap
	 */
	public static void showHideDialog(Panel dialog) {
		if (dialog != null) {
			if (dialog.getParent() == getGameCanvas()) {
				hideDialog(dialog);
			} else {
				showDialog(dialog);
			}
		}
	}

	public static void showDialog(Panel dialog) {
		if (dialog != null && dialog.getParent() != getGameCanvas()) {
			String initialAction = dialog.getInitialAction();
			if (initialAction != null && initialAction.length()>0) {
				GameMain.doAction(dialog, initialAction);
			}
			getGameCanvas().add(dialog);
			getGameCanvas().setComponentZOrder(dialog, 0);
		}
	}

	public static void hideDialog(Panel dialog) {
		if (dialog != null) {
			if (dialog.getParent() == getGameCanvas()) {
				getGameCanvas().remove(dialog);
				String action = dialog.getDisposeAction();
				if (action != null && action.length()>0) {
					GameMain.doAction(dialog, action);
				}
			}
		}
	}

	public static TalkPanel getTalkPanel() {
		Component comp = getGameCanvas().getComponent(0);
		if (comp instanceof TalkPanel) {
			return (TalkPanel) comp;
		}
		return null;
	}

	public static InputMap getInputMap() {
		return inputMap;
	}

	private static List<Listener> listeners = new ArrayList<Listener>();

	public static void addListener(String type, Class handler) {
		listeners.add(new Listener(type, handler));
	}

	public static void installListener() {
		String strType;
		Class handler;
		for (Listener l : listeners) {
			strType = l.getType();
			handler = l.getHandler();
			try {
				if ("MouseListener".equals(strType)) {
					sceneCanvas.addMouseListener((MouseListener) handler.newInstance());
				} else if ("MouseMotionListener".equals(strType)) {
					sceneCanvas.addMouseMotionListener((MouseMotionListener) handler.newInstance());
				} else if ("KeyListener".equals(strType)) {
					sceneCanvas.addKeyListener((KeyListener) handler.newInstance());
				} else if ("MouseWheelListener".equals(strType)) {
					sceneCanvas.addMouseWheelListener((MouseWheelListener) handler.newInstance());
				} else if ("WindowListener".equals(strType)) {
					gameWindow.addWindowListener((WindowListener) handler.newInstance());
				} else if ("WindowStateListener".equals(strType)) {
					gameWindow.addWindowStateListener((WindowStateListener) handler.newInstance());
				}
			} catch (Exception e) {
				System.err
						.printf("install listener error: type=%s, handler=%s\n", strType, handler);
			}
		}
	}

	private static List<Panel> uiComponents = new ArrayList<Panel>();

	public static void addUIComponent(Panel dlg) {
		uiComponents.add(dlg);
	}

	private static Player player;

	private static boolean loaded;

	private static long endTime;

	private static long startTime;

	/** 冒泡对话保留时间(ms) */
	public static long CHAT_REMAIND_TIME = 15000;

	private static Player hoverPlayer;

	public static void setPlayer(Player p) {
		player = p;
		sceneCanvas.setPlayer(p);
	}

	public static Player getPlayer() {
		return player;
	}

	public static Point localToPlayer(Point point) {
		Point pl = player.getLocation();
		Point pl2 = new Point(point);
		pl2.translate(-pl.x, -pl.y);
		return pl2;
	}

	public static Dimension getWindowSize() {
		return new Dimension(displayMode.getWidth(), displayMode.getHeight());
	}

	public static String getApplicationName() {
		return applicationName;
	}

	public static void setApplicationName(String applicationName) {
		GameMain.applicationName = applicationName;
	}

	public static FontMetrics getFontMetrics() {
		return fontMetrics;
	}

	public static String getHomeURL() {
		return homeURL;
	}

	public static String getVersion() {
		return version;
	}

	public static DisplayMode getDisplayMode() {
		return displayMode;
	}

	public static void setHomeURL(String homeURL) {
		GameMain.homeURL = homeURL;
	}

	public static void setVersion(String version) {
		GameMain.version = version;
	}

	private static void startLoading() {
		startTime = System.currentTimeMillis();
		Image img = SpriteFactory.loadImage("/resources/loading/cover.jpg");
		loadingCanvas.setLoading("loading ...");
		loadingCanvas.setContent(img);
		loadingCanvas.fadeIn(200);
	}

	public static void stopLoading() {
		loaded = true;
		endTime = System.currentTimeMillis();
		System.out.printf("total cost: %ss\n", (endTime - startTime) / 1000.0);
		initUI();
        gameWindow.setCanvas(sceneCanvas);
        loadingCanvas.dispose();
        installUI();
	}

	public static boolean isLoaded() {
		return loaded;
	}

	private static void updateLoading(String msg) {
		System.out.println(msg);
		loadingCanvas.setLoading(msg);
	}

	public static void pause(long t) {
		try {
			Thread.sleep(t);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void installUI() {
        ComponentInputMap canvasInputMap = new ComponentInputMap(getGameCanvas());
        for (KeyStroke k : inputMap.keys()) {
        	canvasInputMap.put(k, inputMap.get(k));
        }
        getGameCanvas().setInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW, canvasInputMap);
        getGameCanvas().setActionMap(getActionMap());
        
        for (Panel c : uiComponents) {
        	showDialog(c);
        }
        installListener();
	}

	public static boolean isDebug() {
		return isDebug;
	}

	public static void setDebug(boolean isDebug) {
		GameMain.isDebug = isDebug;
	}

	/**
	 * 根据人物的屏幕坐标和地图的viewport位置计算人物在场景中的坐标
	 * 
	 * @param player
	 */
	public static void revisePlayerSceneLocation(Player player) {
		Point p = player.getLocation();
		p = getSceneCanvas().localToScene(p);
		player.setSceneLocation(p.x, p.y);
	}

	public boolean pass(int x, int y) {
		return getSceneCanvas().pass(x, y);
	}

	public static final float NORMAL_SPEED = 0.15f;// 0.1f;

	public static final float BEVEL_SPEED = 0.105f;// 0.071f;

	public static final int STEP_DISTANCE = 20;

	public static final int DOUBLE_STEP_DISTANCE = 2 * STEP_DISTANCE;

	public static final String STATE_NORMAL = "stand";

	public static final String STATE_MOVING = "walk";

	/** 冒泡对话显示的时间 (ms) */
	public static final int TIME_CHAT = 15 * 1000;

	public static final Color COLOR_NAME_HIGHLIGHT = Color.RED;

	public static Point sceneToLocal(int x, int y) {
		return getSceneCanvas().sceneToLocal(new Point(x, y));
	}

	public static boolean isHover(Player player) {
		Point p = getGameCanvas().getMousePosition();
		if (p == null) {
			return false;
		}
		Point vp = localToView(player.getLocation());
		boolean hover = player.contains(p.x - vp.x, p.y - vp.y);
		if(hover) {//TODO 鼠标移开时取消hover对象
			hoverPlayer = player;
		}
		return hover;
	}
	
	public static Player getHoverPlayer() {
		return hoverPlayer;
	}

	public Point localToScene(Point p) {
		return sceneCanvas.localToScene(p);
	}

	public static Point localToView(Point p) {
		return getGameCanvas().localToView(p);
	}

	public static Point sceneToLocal(Point p) {
		return sceneCanvas.sceneToLocal(p);
	}

	public static Point sceneToView(Point p) {
		return sceneCanvas.sceneToView(p);
	}

	public static Point viewToLocal(Point p) {
		return sceneCanvas.viewToLocal(p);
	}

	public static Point viewToScene(Point p) {
		return sceneCanvas.viewToScene(p);
	}

	/**
	 * 触发与npc的对话
	 * 
	 * @param npc
	 */
	public static void doTalk(Player npc, TalkConfig talk) {
		talker = npc;
		TalkPanel dlg = (TalkPanel) DialogFactory.getDialog("com.javaxyq.action.dialog.talk");
		Toolkit.getInstance().createTalk(dlg, talk);
		dlg.setTalker(npc);
		GameMain.showDialog(dlg);
	}

	public static Player getTalker() {
		return talker;
	}

	public static void doAction(Object source, String actionId) {
		doAction(source, actionId, null);
	}

	public static void exit() {
		System.out.println("terminating JavaXYQ ...");
		System.exit(0);
	}

	public static void setCursor(String cursorId) {
		getGameCanvas().setGameCursor(cursorId);
	}

	public static void restoreCursor() {
		getGameCanvas().setGameCursor(Cursor.DEFAULT_CURSOR);
	}

	public static void registerAction(String actionId, final ActionListener al) {
		Action action = new AbstractAction() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				al.actionPerformed(e);
			}
		};
		actionMap.put(actionId, action);
	}

	public static void registerAction(String actionId, Action action) {
		actionMap.put(actionId, action);
	}

	public static Point getMousePosition() {
		Point p = gameWindow.getMousePosition();
		if(p!=null) {
			//SwingUtilities.convertPoint(gameWindow, p, canvas);
			p.y -= 20;
		}
		return p;
	}
	
	public static Canvas getGameCanvas() {
		return gameWindow.getCanvas();
	}
	
	/**
	 * 进入战斗模式
	 */
	public static void enterBattle(List team1,List team2) {
		int width = displayMode.getWidth();
		int height = displayMode.getHeight();
		//background
		BufferedImage bg = new BufferedImage(width, height, BufferedImage.TYPE_USHORT_565_RGB);
		TileMap map = sceneCanvas.getMap();
		Point viewPosition = sceneCanvas.getViewPosition();
		map.draw(bg.getGraphics(), viewPosition.x,viewPosition.y,width,height);
		
		battleCanvas = new BattleCanvas(width,height);
		battleCanvas.setBattleBackground(bg);
		battleCanvas.setOwnsideTeam(team1);
		battleCanvas.setAdversaryTeam(team2);
		battleCanvas.fadeIn(500);
		gameWindow.setCanvas(battleCanvas);
		//installUI();
        getGameCanvas().setActionMap(getActionMap());       
        for (Panel c : uiComponents) {
        	showDialog(c);
        }
        installListener();

		battleCanvas.init();
		
		//TODO
	}
	/**
	 * 退出战斗模式
	 */
	public static void quitBattle() {
		getSceneCanvas().setPlayerSceneLocation(player.getSceneLocation());
		getSceneCanvas().fadeIn(500);
		gameWindow.setCanvas(getSceneCanvas());
		battleCanvas.dispose();
		battleCanvas = null;
		installUI();
		//TODO
	}
	
	public static BattleCanvas getBattleCanvas() {
		return battleCanvas;
	}
}
