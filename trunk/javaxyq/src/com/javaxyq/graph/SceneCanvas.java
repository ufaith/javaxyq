/**
 * 
 */
package com.javaxyq.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;

import com.javaxyq.action.DefaultTalkAction;
import com.javaxyq.config.MapConfig;
import com.javaxyq.core.DataStore;
import com.javaxyq.core.GameMain;
import com.javaxyq.core.ResourceStore;
import com.javaxyq.event.PlayerAdapter;
import com.javaxyq.event.PlayerEvent;
import com.javaxyq.event.PlayerListener;
import com.javaxyq.io.CacheManager;
import com.javaxyq.model.PlayerVO;
import com.javaxyq.model.Task;
import com.javaxyq.search.SearchUtils;
import com.javaxyq.search.Searcher;
import com.javaxyq.task.TaskManager;
import com.javaxyq.trigger.JumpTrigger;
import com.javaxyq.trigger.Trigger;
import com.javaxyq.ui.UIHelper;
import com.javaxyq.util.MP3Player;
import com.javaxyq.widget.Cursor;
import com.javaxyq.widget.Player;
import com.javaxyq.widget.Sprite;
import com.javaxyq.widget.SpriteImage;
import com.javaxyq.widget.TileMap;
import com.soulnew.AStar;

/**
 * @author dewitt
 * 
 */
public class SceneCanvas extends Canvas {

	/** 游戏地图 */
	private TileMap map;

	/**
	 * 地图遮掩层
	 */
	private Image mapMask;

	private Searcher searcher;

	private PlayerListener scenePlayerHandler = new ScenePlayerHandler();

	private Color trackColor = new Color(255, 0, 0, 200);

	private List<Trigger> triggerList;

	/** 当前场景名称 */
	private String sceneName;

	/** 当前场景id */
	private String sceneId;


	private byte[] maskdata;

	private int sceneWidth;

	private int sceneHeight;

	private List<Point> path;

	private String musicfile;

	/**
	 * 创建场景画布实例
	 */
	public SceneCanvas() {
		searcher = new AStar();
		// searcher = new OptimizeAStar();
		// searcher = new Dijkstra();
		// searcher = new BreadthFirstSearcher();
		Thread th = new MovementThread();
		th.start();
	}

	private void drawMap(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		if (map != null) {
			int viewX = getViewportX();
			int viewY = getViewportY();
			map.draw(g, viewX, viewY, getWidth(), getHeight());
		}
	}

	private void drawMask(Graphics g) {
		if (mapMask != null) {
			int viewX = getViewportX();
			int viewY = getViewportY();
			int sx2 = viewX + getWidth();
			int sy2 = viewY + getHeight();
			g.drawImage(mapMask, 0, 0, getWidth(), getHeight(), viewX, viewY, sx2, sy2, null);
		}
	}

	private void drawTrigger(Graphics g, long elapsedTime) {
		if (triggerList == null) {
			return;
		}
		for (int i = 0; i < triggerList.size(); i++) {
			JumpTrigger t = (JumpTrigger) triggerList.get(i);
			Sprite s = t.getSprite();
			s.update(elapsedTime);
			Point p = t.getLocation();
			p = sceneToView(p);
			s.draw(g, p.x, p.y - s.getHeight() / 2 + s.getRefPixelY());
			if(GameMain.isDebug()) {
				g.drawLine(p.x-10, p.y, p.x+10, p.y);
				g.drawLine(p.x, p.y-10, p.x, p.y+10);
			}
		}
	}

	public TileMap getMap() {
		return map;
	}

	public Point getPlayerLocation() {
		return this.getPlayer().getLocation();
	}

	public Point getPlayerSceneLocation() {
		return this.getPlayer().getSceneLocation();
	}


	public Point localToScene(Point p) {
		return new Point(p.x / GameMain.STEP_DISTANCE, (map.getHeight() - p.y) / GameMain.STEP_DISTANCE);
	}

	/**
	 * 判断某点是否可以通行
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean pass(int x, int y) {
		return searcher.pass(x, y);
	}

	private void revisePlayer(Point p) {
		int canvasWidth = getWidth();
		int canvasHeight = getHeight();
		int viewX = getViewportX();
		int viewY = getViewportY();
		if (p.x > viewX + canvasWidth) {
			p.x = viewX + canvasWidth;
		}
		if (p.y > viewY + canvasHeight) {
			p.y = viewY + canvasHeight;
		}
		if (p.x < viewX) {
			p.x = viewX;
		}
		if (p.y < viewY) {
			p.y = viewY;
		}
	}

	public Point sceneToLocal(Point p) {
		return new Point(p.x * GameMain.STEP_DISTANCE, getMaxHeight() - p.y * GameMain.STEP_DISTANCE);
	}

	public Point sceneToView(Point p) {
		return this.localToView(this.sceneToLocal(p));
	}

	/**
	 * 搜索行走路径
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public List<Point> findPath(int x, int y) {
		Point source = getPlayerSceneLocation();
		Point target = new Point(x, y);
		// 计算两点的直线
		List<Point> path = SearchUtils.getLinePath(source.x,source.y,target.x,target.y);
		// 计算两点间的二次曲线
		//path = SearchUtils.getBezierPath(source, target);
		// 修正终点为最近可以到达点
		for (int i = path.size() - 1; i >= 0; i--) {
			Point p = path.get(i);
			if (pass(p.x, p.y)) {
				target = p;
				break;
			}
			path.remove(i);
		}
		// 如果直线上全部点可通行，则返回
		boolean passed = true;
		for (int i = 0; i < path.size(); i++) {
			Point p = path.get(i);
			if (!pass(p.x, p.y)) {
				passed = false;
				break;
			}
		}
		if (passed)
			return path;
		// 否则计算两点间的路径
		path = searcher.findPath(source.x, source.y, target.x, target.y);
		return path;
	}

	public void setMap(TileMap map) {
		if (map == null) {
			return;
		}
		
		setMaxWidth(map.getWidth());
		setMaxHeight(map.getHeight());
		sceneWidth = map.getWidth() / GameMain.STEP_DISTANCE;
		sceneHeight = map.getHeight() / GameMain.STEP_DISTANCE;
		this.map = map;
		MapConfig cfg = map.getConfig();
		this.setSceneId(cfg.getId());
		this.setSceneName(cfg.getName());
		this.triggerList = ResourceStore.getInstance().createTriggers(cfg.getId());
		List<Player> _npcs = ResourceStore.getInstance().createNPCs(cfg.getId());
		clearNPCs();
		for (Player npc : _npcs) {
			Point p = sceneToLocal(npc.getSceneLocation());
			npc.setLocation(p.x, p.y);
			this.addNPC(npc);
		}
		// test! get barrier image
		this.mapMask = new ImageIcon(cfg.getPath().replace(".map", "_bar.png")).getImage();
		maskdata = loadMask(cfg.getPath().replace(".map", ".msk"));
		searcher.init(sceneWidth, sceneHeight, maskdata);
		//play music
		String musicfile = cfg.getPath().replaceAll("\\.map", ".mp3").replaceAll("scene","music");
		try {
			File file = CacheManager.getInstance().getFile(musicfile);
			if(file!=null && file.exists() && !musicfile.equals(this.musicfile)) {
				this.musicfile = file.getAbsolutePath();
				playMusic();
			}
		} catch (Exception e) {
			System.out.println("切换场景音乐失败："+musicfile+", error="+e.getMessage());
			//e.printStackTrace();
		}
	}

	/**
	 * 加载地图的掩码
	 * 
	 * @param filename
	 * @return
	 */
	private byte[] loadMask(String filename) {
		System.out.println("map : " + map.getWidth() + "*" + map.getHeight() + ", scene: " + sceneWidth + "*"
				+ sceneHeight + ", msk: " + filename);
		byte[] maskdata = new byte[sceneWidth * sceneHeight];
		try {
			InputStream in = GameMain.getResourceAsStream(filename);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String str;
			int pos = 0;
			while ((str = reader.readLine()) != null) {
				int len = str.length();
				for (int i = 0; i < len; i++) {
					maskdata[pos++] = (byte) (str.charAt(i) - '0');
				}
			}
		} catch (Exception e) {
			System.out.println("加载地图掩码失败！filename=" + filename);
			e.printStackTrace();
		}
		return maskdata;
	}

	/** 默认人物对话事件 */
	private DefaultTalkAction defaultTalkAction = new DefaultTalkAction();

	public synchronized void addNPC(Player npc) {
		super.addNPC(npc);
		npc.removeAllListeners();
		npc.addPlayerListener(defaultTalkAction);
	}

	@Override
	protected void clearNPCs() {
		List<Player> npclist = getNpcs();
		for (Player player : npclist) {
			player.removePlayerListener(defaultTalkAction);
		}
		super.clearNPCs();
	}

	public void setPlayer(Player player) {
		Player player0 = getPlayer();
		if (player0 != null) {
			player0.stop(false);
			player0.removePlayerListener(scenePlayerHandler);
		}

		player.stop(false);
		super.setPlayer(player);
		if (player != null) {
			player.addPlayerListener(scenePlayerHandler);
			setPlayerSceneLocation(player.getSceneLocation());
		}
	}

	public void setPlayer(Player player, int x, int y) {
		player.setSceneLocation(x, y);
		setPlayer(player);
	}

	public void setPlayerLocation(Point p) {
		this.revisePlayer(p);
		this.getPlayer().setLocation(p.x, p.y);
	}

	/**
	 * 将人物移到场景位置p，同时自动修正地图
	 * 
	 * @param p
	 */
	public void setPlayerSceneLocation(Point p) {
		if (this.map == null) {
			return;
		}
		Point vp = sceneToLocal(p);
		this.setViewPosition(vp.x - 320, vp.y - 240);
		this.setPlayerLocation(vp);
		GameMain.revisePlayerSceneLocation(getPlayer());
	}

	/**
	 * 移动视窗(viewport),跟随人物移动
	 * 
	 * @param increment
	 */
	private void syncSceneAndPlayer(Point increment) {
		synchronized (UPDATE_LOCK) {
			Point p = getPlayerLocation();
			p = this.localToView(p);
			int dx = increment.x;
			int dy = increment.y;
			// System.out.printf("move: dx=%s,dy=%s\n", dx, dy);
			Point vp = getViewPosition();
			// 前进方向移动view
			if (dx < 0) {// move left
				if (p.x < 300) {
					vp.x += dx;
				}
			} else {// move right
				if (p.x > 340) {
					vp.x += dx;
				}
			}
			if (dy < 0) {// move up
				if (p.y < 220) {
					vp.y += dy;
				}
			} else {// move down
				if (p.y > 260) {
					vp.y += dy;
				}
			}
			// 设置视窗(viewport)的位置
			setViewPosition(vp.x, vp.y);
			// System.out.printf("view: (%s,%s)\n",vp.x,vp.y);
		}
	}

	public Point viewToLocal(Point p) {
		return new Point(p.x + getViewportX(), p.y + getViewportY());
	}

	public Point viewToScene(Point p) {
		return localToScene(viewToLocal(p));
	}

	/**
	 * Auto Walk
	 * 
	 * @param x
	 * @param y
	 */
	public void walkTo(int x, int y) {
		if(x<=0 || y<=0 || x> sceneWidth || y>sceneHeight)return;
		Point p = this.getPlayerSceneLocation();
		System.out.printf("walk to:(%s,%s) -> (%s,%s)\n", p.x, p.y, x, y);
		this.path = this.findPath(x, y);
		if (path != null) {
			getPlayer().setPath(path);
			getPlayer().move();
		} else {
			UIHelper.prompt("不能到达那里", 1000);
		}
	}

	public void walkToView(int x, int y) {
		Point p = this.viewToScene(new Point(x, y));
		this.walkTo(p.x, p.y);
	}

	private void drawPath(Graphics g) {
		Player player = getPlayer();
		if (player != null && path != null) {// XXX DEBUG
			// List<Point> path = player.getPath();
			Point p0 = null;
			for (Point p : path) {
				this.drawTrack(g, p0, p);
				p0 = p;
			}
			drawTrack(g, p0,null);
		}
	}

	private void drawTrack(Graphics g, Point p0, Point p) {
		g.setColor(trackColor);
		if (p0 == null) {//画起点
			Point vp = this.sceneToView(p);
			g.fillOval(vp.x - 4, vp.y - 4, 14, 14);
		} else if(p==null){//画终点
			Point vp = this.sceneToView(p0);
			g.fillOval(vp.x - 4, vp.y - 4, 12, 12);
			g.drawOval(vp.x -6, vp.y -6, 16, 16);
			g.drawOval(vp.x -7, vp.y -7, 18, 18);
		} else {
			p0 = sceneToView(p0);
			p = sceneToView(p);
			g.drawLine(p0.x, p0.y, p.x, p.y);
			g.drawLine(p0.x + 1, p0.y + 1, p.x + 1, p.y + 1);
		}
	}

	/**
	 * doclick the canvas
	 * 
	 * @param p
	 *            the click-point of view(canvas)
	 */
	public void click(Point p) {
		final SpriteImage effectSprite = this.getGameCursor().getEffect();
		effectSprite.setVisible(true);
		Point sp = this.viewToScene(p);
		Point vp = this.sceneToView(sp);
		this.getGameCursor().setClick(sp.x, sp.y);
		p.translate(-vp.x, -vp.y);
		this.getGameCursor().setOffset(p.x, p.y);
		if (effectSprite != null) {
			effectSprite.setRepeat(2);
			// 取消点击效果
			new Thread() {
				public void run() {
					while (effectSprite.isVisible()) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
						}
						if (effectSprite.getSprite().getRepeat() == 0) {
							effectSprite.setVisible(false);
							break;
						}
					}
				};
			}.start();
		}
	}

	/**
	 * draw click effect
	 * 
	 * @param elapsedTime
	 */
	private void drawClick(Graphics g, long elapsedTime) {
		// update click effection
		Cursor gameCursor = getGameCursor();
		SpriteImage effectSprite = (gameCursor == null) ? null : gameCursor.getEffect();
		if (effectSprite != null) {
			effectSprite.update(elapsedTime);
			Point p = this.sceneToView(gameCursor.getClickPosition());
			effectSprite.draw(g, p.x + gameCursor.getOffsetX(), p.y + gameCursor.getOffsetY());
		}
	}

	private final class MovementThread extends Thread {
		private long lastTime;
		{
			this.setName("movementThread");
		}

		public void run() {
			while (true) {
				// System.out.println(this.getId()+" "+this.getName());
				synchronized (Canvas.MOVEMENT_LOCK) {
					long t1 = System.currentTimeMillis();
					long currTime = System.currentTimeMillis();
					if (lastTime == 0)
						lastTime = currTime;
					long elapsedTime = currTime - lastTime;
					// update movement
					updateMovements(elapsedTime);
					long t2 = System.currentTimeMillis();
					if (t2 - t1 > 20) {
						System.out.printf("update movement costs: %sms\n", t2 - t1);
					}
					lastTime = currTime;
				}
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 场景的人物事件监听器
	 */
	private final class ScenePlayerHandler extends PlayerAdapter {

		public void walk(PlayerEvent evt) {
			Point coords = evt.getCoords();
			walkTo(coords.x, coords.y);
		}

		public void move(Player player, Point increment) {
			// 1. 更新场景坐标
			syncSceneAndPlayer(increment);
			GameMain.revisePlayerSceneLocation(player);

			// 2. 触发地图跳转
			if (triggerList == null) {
				return;
			}
			Point p = getPlayerSceneLocation();
			for (int i = 0; i < triggerList.size(); i++) {
				Trigger t = triggerList.get(i);
				if (t.hit(p)) {
					t.doAction();
					return;
				}
			}

			// TODO 3. 师门巡逻任务
			Task task = TaskManager.instance.getTaskOfType("school", "patrol");
			if (task != null && !task.isFinished() && sceneId.equals(task.get("sceneId"))) {
				long nowtime = System.currentTimeMillis();
				if (nowtime - lastPatrolTime > patrolInterval) {
					// FIXME 改进巡逻触发战斗机率的判断
					Random rand = new Random();
					if (rand.nextInt(100) < 5) {
						TaskManager.instance.process(task);
					}
				}
			}
		}
	}

	/** 最后一次巡逻时间 */
	private long lastPatrolTime;

	/** 两次巡逻的最小时间间隔 */
	private long patrolInterval = 10000;

	/**
	 * 设置最后一次巡逻时间
	 * 
	 * @param lastPatrolTime
	 */
	public void setLastPatrolTime(long lastPatrolTime) {
		this.lastPatrolTime = lastPatrolTime;
	}

	/**
	 * 设置两次巡逻的时间间隔(ms)
	 * 
	 * @param patrolInterval
	 */
	public void setPatrolInterval(long patrolInterval) {
		this.patrolInterval = patrolInterval;
	}

	/**
	 * @param elapsedTime
	 */
	private void updateMovements(long elapsedTime) {
		Player p = getPlayer();
		if (p != null) {
			p.updateMovement(elapsedTime);
		}
	}

	public synchronized void draw(Graphics g, long elapsedTime) {
		if (g == null) {
			return;
		}
		try {
			g.setColor(Color.BLACK);
			// 场景地图
			drawMap(g);
			// 人物行走路线
			if (GameMain.isDebug()) {
				this.drawPath(g);
			}
			// 场景跳转
			drawTrigger(g, elapsedTime);

			// npcs
			drawNPC(g, elapsedTime);
			// 人物
			drawPlayer(g, elapsedTime);

			// 地图掩码(mask)
			drawMask(g);
			// 鼠标点击效果
			this.drawClick(g, elapsedTime);

			// 游戏UI控件
			drawComponents(g, elapsedTime);

			// 过渡效果：谈出淡入
			if (alpha > 0) {
				g.setColor(new Color(0, 0, 0, alpha));
				g.fillRect(0, 0, getWidth(), getHeight());
			}
			// 内存使用量
			drawDebug(g);
			drawDownloading(g);
		} catch (Exception e) {
			System.out.printf("更新Canvas时失败！\n");
			e.printStackTrace();
		}
	}

	protected String getSceneName() {
		return sceneName;
	}

	protected void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}

	public String getSceneId() {
		return sceneId;
	}

	public void setSceneId(String sceneId) {
		this.sceneId = sceneId;
	}

	public long getLastPatrolTime() {
		return lastPatrolTime;
	}

	public long getPatrolInterval() {
		return patrolInterval;
	}

	public List<Point> getPath() {
		return path;
	}

	public int getSceneWidth() {
		return sceneWidth;
	}

	public int getSceneHeight() {
		return sceneHeight;
	}

	protected String getMusic() {
		return musicfile;		
	}

}
