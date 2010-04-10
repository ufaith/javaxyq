/*
 * JavaXYQ Source Code
 * by kylixs
 * http://javaxyq.googlecode.com
 * kylixs@qq.com
 */

package com.javaxyq.core;

import java.awt.Color;
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ComponentInputMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import com.javaxyq.action.BaseAction;
import com.javaxyq.action.DefaultTransportAction;
import com.javaxyq.action.RandomMovementAction;
import com.javaxyq.battle.BattleCanvas;
import com.javaxyq.config.TalkConfig;
import com.javaxyq.data.DataStore;
import com.javaxyq.data.XmlDataLoader;
import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.Listener;
import com.javaxyq.event.SceneEvent;
import com.javaxyq.event.SceneListener;
import com.javaxyq.graph.Canvas;
import com.javaxyq.graph.DesktopWindow;
import com.javaxyq.graph.GameWindow;
import com.javaxyq.graph.LoadingCanvas;
import com.javaxyq.graph.Panel;
import com.javaxyq.graph.SceneCanvas;
import com.javaxyq.graph.TalkPanel;
import com.javaxyq.io.CacheManager;
import com.javaxyq.task.TaskManager;
import com.javaxyq.ui.UIHelper;
import com.javaxyq.widget.Cursor;
import com.javaxyq.widget.Player;
import com.javaxyq.widget.TileMap;

/**
 * JavaXYQ ��Ϸ�����
 * 
 * @author ����ΰ
 * @history 2008-6-7 ����ΰ �½�
 */
public final class GameMain {

	/** global action map */
	private static ActionMap actionMap = new ActionMap();

	private static InputMap inputMap = new InputMap();

	/**
	 * ��������ÿ֡�ļ��(ms)
	 */
	public static final int ANIMATION_INTERVAL = 100;

	private static String applicationName = "JavaXYQ";

	private static LoadingCanvas loadingCanvas;
	
	private static SceneCanvas sceneCanvas;
	
	private static BattleCanvas battleCanvas;

	private static FontMetrics fontMetrics;

	private static GameWindow gameWindow;

	private static String homeURL;

	public static final Font TEXT_FONT = new Font("����", Font.PLAIN, 14);

	public static final Color COLOR_NAME_BACKGROUND = new Color(27, 26, 18);

	public static final Color COLOR_NAME = new Color(118, 229, 128);

	public static final Font TEXT_NAME_FONT = new Font("����", Font.PLAIN, 16);

	public static final Color TEXT_NAME_NPC_COLOR = new Color(219, 197, 63);

	public static final int APP_APPLET = 1;
	private static final int APP_DESKTOP = 0;

	private static String version;

	private static DisplayMode displayMode;

	private static boolean isDebug;

	private static Player talker;

	private static boolean showCopyright = false;
	
	private static boolean playingMusic = false;

	public static int appType = APP_DESKTOP;

	public static URL base;

	/**
	 * ִ��ָ��ActionCommand��Action
	 * 
	 * @param source
	 *            ����Action��Դ����
	 * @param cmd������actiomCommand
	 *            ,��������
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

	public static GameWindow getWindow() {
		return gameWindow;
	}

	private static void showCopyright() {
		// copyright
		if (showCopyright) {
			Image img = SpriteFactory.loadImage("/resources/loading/����.jpg");
			loadingCanvas.showImage(img, 3000);
			img = SpriteFactory.loadImage("/resources/loading/��Դ��Ȩ.jpg");
			loadingCanvas.showImage(img, 2000);

			img = SpriteFactory.loadImage("/resources/loading/����.jpg");
			loadingCanvas.showImage(img, 3000);
			img = SpriteFactory.loadImage("/resources/loading/��л.jpg");
			loadingCanvas.showImage(img, 3000);
			img = SpriteFactory.loadImage("/resources/loading/version1.3.jpg");
			loadingCanvas.showImage(img, 3000);
		}
	}
	public static void loadGame() {
		startLoading();
		setDebug(false);
		setShowCopyright(false);
		setApplicationName("JavaXYQ ");
		setVersion("1.4 M2");
		setHomeURL("http://javaxyq.googlecode.com/");
		updateLoading("loading game ...");
		
		showCopyright();
		UIHelper.init();
		
		updateLoading("loading groovy ...");
//		try {
//			JarFile jarfile = new JarFile(getFile("lib/groovy-all-1.6.5.jar"));
//			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		updateLoading("loading cursor ...");
		XmlDataLoader.defCursors();
		setCursor(Cursor.DEFAULT_CURSOR);
		
		updateLoading("loading actions ...");
		XmlDataLoader.defActions();
		updateLoading("loading scenes ...");
		XmlDataLoader.defScenes();
		updateLoading("loading talks ...");
		XmlDataLoader.defTalks();
		updateLoading("loading ui ...");
		loadUIs();
		
		updateLoading("loading npcs ...");
		XmlDataLoader.loadNPCs();
		
		registerAction("com.javaxyq.action.transport",new DefaultTransportAction());
		MovementManager.addMovementAction("random", new RandomMovementAction());
		
		//task
		TaskManager.instance.register("school", "com.javaxyq.task.SchoolTaskCoolie");
		
		updateLoading("loading data ...");
		DataStore.init();
		ItemManager.init();
		DataStore.loadData();
		updateLoading("starting game ...");
		stopLoading();
		//setPlayingMusic(false);//debug
	}

	private static void installUI() {
		String[] uiIds = new String[] {"mainwin"};
   		for(String id : uiIds) {
   			System.out.println("��װUI��"+id);
   			Panel dlg = DialogFactory.getDialog(id, true);
   			addUIComponent(dlg);
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
		//init(args);
	}

	public static void init(String[] args) {
		System.getProperties().list(System.out);
		initDisplay(args);
		// loading canvas
		Dimension preferredSize = new Dimension(displayMode.getWidth(), displayMode.getHeight());
		loadingCanvas = new LoadingCanvas();
		loadingCanvas.setPreferredSize(preferredSize);
		//scene canvas
		sceneCanvas = new SceneCanvas();
        sceneCanvas.setPreferredSize(preferredSize);
        sceneCanvas.setSize(preferredSize);
        
		DesktopWindow win = new DesktopWindow(displayMode);
		gameWindow = win;
		win.setTitle(applicationName +" "+ version);
		win.setCanvas(loadingCanvas);
		win.setLocationRelativeTo(null);
		win.setVisible(true);
		fontMetrics = win.getFontMetrics(TEXT_NAME_FONT);
		loadGame();
	}
	
	public static void initApplet(AppletWindow applet, String[] args) {
		System.getProperties().list(System.out);
		System.out.println();
		System.out.println("-------------------------");
		System.out.println("cache dir: "+cacheBase);
		
		gameWindow = applet;
		initDisplay(args);
		Dimension preferredSize = new Dimension(displayMode.getWidth(), displayMode.getHeight());
		// loading canvas
		loadingCanvas = new LoadingCanvas();
		loadingCanvas.setPreferredSize(preferredSize);
		applet.setCanvas(loadingCanvas);
		applet.setSize(preferredSize);
		applet.invalidate();
		//CacheManager.getInstance().addDownloadListener(loadingCanvas);
		
		//scene canvas
		sceneCanvas = new SceneCanvas();
		sceneCanvas.setPreferredSize(preferredSize);
		sceneCanvas.setSize(preferredSize);
		
		fontMetrics =applet.getFontMetrics(TEXT_NAME_FONT);
		
		appType = APP_APPLET;
		base = applet.getDocumentBase();
		
		//Desktop.getDesktop().
		//applet.setLocation();
		//applet.setVisible(true);
		
//		gameWindow = new Window(displayMode);
//		gameWindow.setTitle(applicationName +" "+ version);
//		gameWindow.setCanvas(loadingCanvas);
//		fontMetrics = gameWindow.getFontMetrics(TEXT_NAME_FONT);
		
//		gameWindow.setLocationRelativeTo(null);
//		gameWindow.setVisible(true);
		//loadGame();
	}
	
	private static DisplayMode initDisplay(String[] args) {
		int width = 640, height = 480;
		if (args!=null && args.length == 3) {
			width = Integer.valueOf(args[0]);
			height = Integer.valueOf(args[1]);
			displayMode = new DisplayMode(width, height, Integer.valueOf(args[2]),
					DisplayMode.REFRESH_RATE_UNKNOWN);
		} else {
			displayMode = new DisplayMode(width, height, 16, DisplayMode.REFRESH_RATE_UNKNOWN);
		}
		return displayMode;
	}

	public static InputMap getInputMap() {
		return inputMap;
	}

	private static List<Listener> listeners = new ArrayList<Listener>();

	public static void addListener(String type, Class handler) {
		listeners.add(new Listener(type, handler));
	}

	public static void installListener() {
		//TODO Canvas�л�ʱ�������ظ����Ӽ�����
		for (Listener l : listeners) {
			String strType = l.getType();
			try {
				Object instance = l.getInstance();
				if ("MouseListener".equals(strType)) {
					sceneCanvas.removeMouseListener((MouseListener) instance);
					sceneCanvas.addMouseListener((MouseListener) instance);
				} else if ("MouseMotionListener".equals(strType)) {
					sceneCanvas.removeMouseMotionListener((MouseMotionListener) instance);
					sceneCanvas.addMouseMotionListener((MouseMotionListener) instance);
				} else if ("KeyListener".equals(strType)) {
					sceneCanvas.removeKeyListener((KeyListener) instance);
					sceneCanvas.addKeyListener((KeyListener) instance);
				} else if ("MouseWheelListener".equals(strType)) {
					sceneCanvas.removeMouseWheelListener((MouseWheelListener) instance);
					sceneCanvas.addMouseWheelListener((MouseWheelListener) instance);
				} else if ("WindowListener".equals(strType)) {
					gameWindow.removeWindowListener((WindowListener) instance);
					gameWindow.addWindowListener((WindowListener) instance);
				} else if ("WindowStateListener".equals(strType)) {
					gameWindow.removeWindowStateListener((WindowStateListener) instance);
					gameWindow.addWindowStateListener((WindowStateListener) instance);
				}
			} catch (Exception e) {
				e.printStackTrace();
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

	/** ð�ݶԻ�����ʱ��(ms) */
	public static long CHAT_REMAIND_TIME = 15000;

	private static Player hoverPlayer;

	private static String lastMagic;

	public static String cacheBase = System.getProperty("user.home")+"/javaxyq";

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
		CacheManager.getInstance().addDownloadListener(loadingCanvas);
		startTime = System.currentTimeMillis();
		loadingCanvas.setLoading("start loading ...");
		Image img = SpriteFactory.loadImage("/resources/loading/cover.jpg");
		loadingCanvas.setContent(img);
		loadingCanvas.playMusic();
		loadingCanvas.fadeIn(200);
	}

	public static void stopLoading() {
		loaded = true;
		endTime = System.currentTimeMillis();
		System.out.printf("total cost: %ss\n", (endTime - startTime) / 1000.0);
		installUI();
        gameWindow.setCanvas(sceneCanvas);
        CacheManager.getInstance().removeDownloadListener(loadingCanvas);
		CacheManager.getInstance().addDownloadListener(sceneCanvas);
        loadingCanvas.dispose();
        //loadingCanvas.stopMusic();
        updateUI();
	}

	public static boolean isLoaded() {
		return loaded;
	}

	public static void updateLoading(String msg) {
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

	public static void updateUI() {
        ComponentInputMap canvasInputMap = new ComponentInputMap(getGameCanvas());
        for (KeyStroke k : inputMap.keys()) {
        	canvasInputMap.put(k, inputMap.get(k));
        }
        getGameCanvas().setInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW, canvasInputMap);
        getGameCanvas().setActionMap(getActionMap());
        
        for (int i = 0; i < uiComponents.size(); i++) {
        	Panel c = uiComponents.get(i);
        	UIHelper.showDialog(c);
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
	 * �����������Ļ����͵�ͼ��viewportλ�ü��������ڳ����е�����
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

	/** ð�ݶԻ���ʾ��ʱ�� (ms) */
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
		if(hover) {//TODO ����ƿ�ʱȡ��hover����
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
	 * ������npc�ĶԻ�
	 * 
	 * @param npc
	 */
	public static void doTalk(Player npc, TalkConfig talk) {
		talker = npc;
		TalkPanel dlg = (TalkPanel) DialogFactory.getDialog("npctalk", true);
		Toolkit.getInstance().createTalk(dlg, talk);
		dlg.setTalker(npc);
		UIHelper.showDialog(dlg);
	}
	
	public static void doTalk(Player p,String chat) {
		if(p==null)p = talker;
		TalkConfig cfg = new TalkConfig(chat);
		doTalk(p, cfg);
	}

	public static Player getTalker() {
		return talker;
	}

	public static void doAction(Object source, String actionId) {
		doAction(source, actionId, null);
	}

	public static void exit() {
		System.out.println("terminating JavaXYQ ...");
        //GameMain.doAction(e.getSource(), "com.javaxyq.action.beforeExit");
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
		try {
			Point p = gameWindow.getMousePosition();
//			if(p!=null && gameWindow instanceof JFrame) {
//				//SwingUtilities.convertPoint(gameWindow, p, canvas);
//				p.y -= 20;
//			}
			return p;
		} catch (Exception e) {
			System.out.println("��ȡ���λ��ʧ�ܣ�"+e.getMessage());
			//e.printStackTrace();
		}
		return null;
	}
	
	public static Canvas getGameCanvas() {
		return gameWindow.getCanvas();
	}
	
	/**
	 * ����ս��ģʽ
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
        for (int i = 0; i < uiComponents.size(); i++) {
        	Panel c = uiComponents.get(i);
        	UIHelper.showDialog(c);
        }
        //installListener();
        
		battleCanvas.init();
		battleCanvas.setLastMagic(lastMagic);
		battleCanvas.playMusic();
	}
	/**
	 * �˳�ս��ģʽ
	 */
	public static void quitBattle() {
		getSceneCanvas().setPlayerSceneLocation(player.getSceneLocation());
		getSceneCanvas().fadeIn(500);
		gameWindow.setCanvas(getSceneCanvas());
		getSceneCanvas().playMusic();
		lastMagic = battleCanvas.getLastMagic();
		battleCanvas.dispose();
		battleCanvas = null;
		updateUI();
		//TODO
	}
	
	public static BattleCanvas getBattleCanvas() {
		return battleCanvas;
	}

	public static boolean isPlayingMusic() {
		return playingMusic;
	}

	public static void setPlayingMusic(boolean playingMusic) {
		GameMain.playingMusic = playingMusic;
		if(playingMusic) {
			GameMain.getGameCanvas().playMusic();
		}else {
			GameMain.getGameCanvas().stopMusic();
		}
	}

	public static boolean isShowCopyright() {
		return showCopyright;
	}

	public static void setShowCopyright(boolean showCopyright) {
		GameMain.showCopyright = showCopyright;
	}

	public static InputStream getResourceAsStream(String path) throws IOException {
		File file = CacheManager.getInstance().getFile(path);
		if(file!=null ) {
			return new FileInputStream(file);
		}
		return null;
	}

	/**
	 * @deprecated Use {@link CacheManager#getFile(String)} instead
	 */
	public static File getFile(String filename) {
		return CacheManager.getInstance().getFile(filename);
	}
	
	/**
	 * �����ļ�
	 * @param filename
	 * @return
	 * @throws IOException 
	 * @deprecated Use {@link CacheManager#createFile(String)} instead
	 */
	public static File createFile(String filename) throws IOException {
		return CacheManager.getInstance().createFile(filename);
	}
	
	public static void addListener(String type,String className){
		try {
			GameMain.addListener(type,Class.forName(className));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void loadUIs() {
		File file = GameMain.getFile("ui/list.txt");
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String str = null;
			while((str=br.readLine())!=null) {
				String uifile = "ui/"+str;
				System.out.println("find ui: "+uifile);
				XmlDataLoader.loadUI(uifile);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void setScene(String id, int x, int y){
		if(id == null || id=="null")id="wzg";
		SceneListener action = null;
		try {
		String currentScene = GameMain.getCurrentScene();
		if(currentScene!=null) {
			action = findSceneAction(currentScene);
			if(action!=null)action.onUnload(new SceneEvent(currentScene,-1,-1));
		}
		}catch(Exception e) {e.printStackTrace();}
		System.out.println("�л�������"+id+" ("+x+","+y+")");
		try {
			action =  findSceneAction(id);
			if(action!=null)action.onInit(new SceneEvent(id,x,y));
		}catch(Exception e) {e.printStackTrace();};
	
		GameMain.fadeToMap(id,x,y);
		
		try {
			if(action!=null)action.onLoad(new SceneEvent(id,x,y));
		}catch(Exception e) {e.printStackTrace();};
	}

	public static SceneListener findSceneAction(String id) {
		Object action = GroovyScript.loadClass("scripts/scene/"+id+".groovy");
		return (SceneListener)action;
	}	
}