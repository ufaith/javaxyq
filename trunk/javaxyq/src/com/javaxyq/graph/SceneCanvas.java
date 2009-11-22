/**
 * 
 */
package com.javaxyq.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.List;

import javax.swing.ImageIcon;

import com.javaxyq.config.MapConfig;
import com.javaxyq.core.DataStore;
import com.javaxyq.core.GameMain;
import com.javaxyq.core.ResourceStore;
import com.javaxyq.event.PlayerAdapter;
import com.javaxyq.event.PlayerListener;
import com.javaxyq.model.PlayerVO;
import com.javaxyq.search.AStar;
import com.javaxyq.search.Node;
import com.javaxyq.trigger.JumpTrigger;
import com.javaxyq.trigger.Trigger;
import com.javaxyq.widget.Cursor;
import com.javaxyq.widget.Player;
import com.javaxyq.widget.Sprite;
import com.javaxyq.widget.SpriteImage;
import com.javaxyq.widget.TileMap;

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

	private AStar searcher;

	private PlayerListener sceneTransitionHandler = new TransportHandler();

	private PlayerListener spriteMoveHandler = new SpriteMovementHandler();

	private Color trackColor = new Color(128, 0, 0, 200);

	private List<Trigger> triggerList;

	private Label sceneLabel;

	private String sceneName;

	private Label coordinateLabel;

	/**
	 * 
	 */
	public SceneCanvas() {
		searcher = new AStar();
		Thread th= new MovementThread();
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

	private void drawTrack(Graphics g, Point p) {
		Point vp = this.sceneToView(p);
		g.setColor(trackColor);
		g.fillOval(vp.x - 2, vp.y - 2, 16, 16);
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
			s.draw(g, p.x-s.getWidth()/2+s.getCenterX(), p.y-s.getHeight()/2+s.getCenterY());
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

	protected void updatePlayerState() {
		if (this.getPlayer() == null)
			return;
		// 人物坐标
		Point pp = this.getPlayerSceneLocation();
		String strCoordinate = "X:" + pp.x + " Y:" + pp.y;
		if (coordinateLabel == null) {
			coordinateLabel = (Label) findComponentByName("人物坐标");
		}
		if (coordinateLabel != null) {
			coordinateLabel.setText(strCoordinate);
		}
		if (sceneLabel == null) {
			sceneLabel = (Label) findComponentByName("地图名称");
		}
		if (sceneLabel != null) {
			sceneLabel.setText(sceneName);
		}

		PlayerVO playerVO = this.getPlayer().getData();
		int maxLen = 50;
		// 人物气血
		int len = playerVO.getHp() * maxLen / playerVO.getMaxHp();
		Label hpTrough = (Label) this.findComponentByName("人物气血");
		hpTrough.setSize(len, hpTrough.getHeight());
		// 人物魔法
		len = playerVO.getMp() * maxLen / playerVO.getMaxMp();
		Label mpTrough = (Label) this.findComponentByName("人物魔法");
		mpTrough.setSize(len, mpTrough.getHeight());
		// 人物愤怒
		len = playerVO.getSp() * maxLen / 150;
		Label spTrough = (Label) this.findComponentByName("人物愤怒");
		spTrough.setSize(len, spTrough.getHeight());
		// 人物经验
		len = (int) (playerVO.getExp() * maxLen / DataStore.getLevelExp(playerVO.getLevel()));
		Label expTrough = (Label) this.findComponentByName("人物经验");
		expTrough.setSize(len, expTrough.getHeight());

		// TODO 召唤兽状态
		// 召唤兽气血
		// 召唤兽魔法
		// 召唤兽经验
	}

	public Point localToScene(Point p) {
		return new Point(p.x / GameMain.STEP_DISTANCE, (map.getHeight() - p.y) / GameMain.STEP_DISTANCE);
	}

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

	public List<Point> searchPathTo(int x, int y) {
		Point p = getPlayerSceneLocation();
		searcher.computeShortPath(new Node(p.x, p.y), new Node(x, y));
		return searcher.getResult();
	}

	public void setMap(TileMap map) {
		if (map == null) {
			return;
		}
		setMaxWidth(map.getWidth());
		setMaxHeight(map.getHeight());
		this.map = map;
		clearNPCs();
		MapConfig cfg = map.getConfig();
		this.setSceneName(cfg.getName());
		this.triggerList = ResourceStore.getInstance().findTriggers(cfg.getId());
		List<Player> _npcs = ResourceStore.getInstance().findNPCs(cfg.getId());
		for (Player npc : _npcs) {
			Point p = sceneToLocal(npc.getSceneLocation());
			npc.setLocation(p.x, p.y);
			this.addNPC(npc);
		}
		// test! get mask image
		this.mapMask = new ImageIcon(cfg.getPath().replaceAll(".map", "_mask.png")).getImage();
		searcher.init();

	}

	public void setPlayer(Player player) {
		player.stop(false);
		super.setPlayer(player);
		if (player != null) {
			player.addPlayerListener(spriteMoveHandler);
			player.addPlayerListener(sceneTransitionHandler);
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
		Point p = this.getPlayerSceneLocation();
		System.out.printf("walk to:(%s,%s) -> (%s,%s)\n", p.x, p.y, x, y);
		List<Point> path = this.searchPathTo(x, y);
		getPlayer().setPath(path);
		getPlayer().move();
	}

	public void walkToView(int x, int y) {
		Point p = this.viewToScene(new Point(x, y));
		this.walkTo(p.x, p.y);
	}

	private void drawPath(Graphics g) {
		Player player = getPlayer();
		if (player != null) {// XXX DEBUG
			List<Point> path = player.getPath();
			for (Point p : path) {
				this.drawTrack(g, p);
			}
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
			//取消点击效果
			new Thread() {
				public void run() {
					while(effectSprite.isVisible()) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
						}
						if(effectSprite.getSprite().getRepeat()==0) {
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
            	//System.out.println(this.getId()+" "+this.getName());
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

	private final class SpriteMovementHandler extends PlayerAdapter {
		public void move(Player player, Point increment) {
			// FIXME
			syncSceneAndPlayer(increment);
			// 更新场景坐标
			GameMain.revisePlayerSceneLocation(player);
			// System.out.println("player view position: " +
			// localToView(player.getLocation()));
		}
	}

	private final class TransportHandler extends PlayerAdapter {
		public void move(Player player, Point increment) {
			if (triggerList == null) {
				return;
			}
			Point p = getPlayerSceneLocation();
			for (int i = 0; i < triggerList.size(); i++) {
				Trigger t = triggerList.get(i);
				if (t.hit(p)) {
					t.doAction();
					break;
				}
			}
		}
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
			// draw map
			drawMap(g);
			// trigers
			drawTrigger(g, elapsedTime);
			// npcs
			drawNPC(g, elapsedTime);

			// update player
			drawPlayer(g, elapsedTime);
			// draw map's mask
			drawMask(g);
			// draw path
			// XXX this.drawPath(g);
			this.drawClick(g, elapsedTime);

			// update comps on the canvas
			updatePlayerState();
			drawComponents(g, elapsedTime);

			// draw fade
			g.setColor(new Color(0, 0, 0, alpha));
			g.fillRect(0, 0, getWidth(), getHeight());
			
			drawMemory(g);
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

}
