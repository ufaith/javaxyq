package com.javaxyq.widget;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.event.EventListenerList;

import com.javaxyq.core.GameMain;
import com.javaxyq.core.SpriteFactory;
import com.javaxyq.event.EventDispatcher;
import com.javaxyq.event.EventException;
import com.javaxyq.event.EventTarget;
import com.javaxyq.event.PlayerEvent;
import com.javaxyq.event.PlayerListener;
import com.javaxyq.graph.FloatPanel;
import com.javaxyq.model.PlayerVO;
import com.javaxyq.util.Cheat;
import com.javaxyq.util.MP3Player;
import com.javaxyq.util.WASDecoder;

/**
 * ��Ϸ����
 * 
 * @author ����ΰ
 * @history 2008-5-14 ����ΰ ��������������ߵĴ���
 */
public class Player extends AbstractWidget implements EventTarget {

	public static final String STATE_STAND = "stand";

	public static final String STATE_WALK = "walk";

	private String id;

	/** ��ɫ������(����ң��/����/����) */
	private String character;

	/** ��ʷð�ݶԻ� */
	private List<FloatPanel> chatPanels;

	private Sprite person;

	private Sprite weapon;

	private Sprite shadow;

	/** ·������ */
	private Queue<Point> path = new ConcurrentLinkedQueue<Point>();

	private String name;

	private String state = null;

	private int x;

	private int y;

	private int direction;

	private Color nameForeground = GameMain.COLOR_NAME;

	private Color nameBackground = GameMain.COLOR_NAME_BACKGROUND;

	private Color highlightColor = GameMain.COLOR_NAME_HIGHLIGHT;

	private List<String> chatHistory = new ArrayList<String>();

	private Font nameFont;

	private boolean visible = true;

	private boolean moving = false;

	private boolean stepping = false;

	private EventListenerList listenerList = new EventListenerList();

	/** ����X���� */
	private int sceneX;

	/** ����Y���� */
	private int sceneY;

	/** ��ǰ���ƶ���[x,y] */
	private Point nextStep;

	/** ������ǰ�����ƶ� */
	private boolean movingOn = false;

	/** Ⱦɫ���� */
	private String profile;

	private int[] profileData;

	private int[] colorations = null;// new int[3];

	/**
	 * ��������
	 */
	private PlayerVO data;

	/**
	 * ״̬Ч������
	 */
	private Map<String, Animation> stateEffects = new HashMap<String, Animation>();

	/**
	 * ����Ч������
	 */
	private Animation onceEffect = null;

	public Player(String id, String name, String character) {
		this.id = id;
		this.name = name;
		this.character = character;
		shadow = SpriteFactory.loadShadow();
		nameFont = GameMain.TEXT_NAME_FONT;
		chatPanels = new ArrayList<FloatPanel>();
	}

	public String getId() {
		return id;
	}

	public int getHeight() {
		return this.person.getHeight();
	}

	public int getWidth() {
		return this.person.getWidth();
	}

	/**
	 * ȡ����һ�����ƶ���
	 * 
	 * @return
	 */
	private Point popPath() {
		// System.out.println("path count:" + path.size());
		if (this.path != null && !this.path.isEmpty()) {
			Point step = this.path.poll();
			while (step != null && step.x == this.sceneX && step.y == this.sceneY) {
				step = this.path.poll();
			}
			return step;
		}
		return null;
	}

	/**
	 * ����·���Ĳ�����������ƶ�����
	 * 
	 * @param step
	 * @return
	 */
	private int calculateStepDirection(Point step) {
		int dx = step.x - this.sceneX;
		int dy = step.y - this.sceneY;
		int dir = 0;
		if (dx < 0) {
			if (dy < 0) {
				dir = Sprite.DIR_DOWN_LEFT;
			} else if (dy > 0) {
				dir = Sprite.DIR_UP_LEFT;
			} else {
				dir = Sprite.DIR_LEFT;
			}
		} else if (dx > 0) {
			if (dy < 0) {
				dir = Sprite.DIR_DOWN_RIGHT;
			} else if (dy > 0) {
				dir = Sprite.DIR_UP_RIGHT;
			} else {
				dir = Sprite.DIR_RIGHT;
			}
		} else {// x=0
			if (dy < 0) {
				dir = Sprite.DIR_DOWN;
			} else if (dy > 0) {
				dir = Sprite.DIR_UP;
			} else {
				// no move
				dir = -1;
			}
		}

		return dir;
	}

	public void changeDirection(Point mouse) {
		// FIXME ����ת��
		int direction = Sprite.computeDirection(getLocation(), mouse);
		setDirection(direction);
	}

	public boolean contains(int x, int y) {
		boolean b = person.contains(x, y) || shadow.contains(x, y);
		if (weapon != null && !b) {
			b = weapon.contains(x, y);
		}
		return b;
	}

	public Point getLocation() {
		return new Point(x, y);
	}

	public String getName() {
		return name;
	}

	synchronized public void say(String chatText) {
		if (GameMain.isDebug() && chatText.startsWith("!>")) {
			String cmd = chatText.substring(2);
			Cheat.process(cmd);
			return;
		}
		this.chatHistory.add(chatText);
		this.chatPanels.add(new FloatPanel(chatText));
		if (this.chatPanels.size() > 3) {
			this.chatPanels.remove(0);
		}
		System.out.println(name + " ˵: " + chatText);
	}

	public List<String> getChatHistory() {
		return chatHistory;
	}

	public synchronized void move() {
		// TODO
		this.prepareStep();
	}

	/**
	 * ��ĳ�����ƶ�һ��
	 * 
	 * @param direction
	 */
	public void stepTo(int direction) {
		this.clearPath();
		int dx = 0;
		int dy = 0;
		switch (direction) {
		case Sprite.DIR_LEFT:
			dx = -1;
			break;
		case Sprite.DIR_UP:
			dy = 1;
			break;
		case Sprite.DIR_RIGHT:
			dx = 1;
			break;
		case Sprite.DIR_DOWN:
			dy = -1;
			break;
		case Sprite.DIR_DOWN_LEFT:
			dx = -1;
			dy = -1;
			break;
		case Sprite.DIR_UP_LEFT:
			dx = -1;
			dy = 1;
			break;
		case Sprite.DIR_UP_RIGHT:
			dx = -1;
			dy = 1;
			break;
		case Sprite.DIR_DOWN_RIGHT:
			dx = 1;
			dy = -1;
			break;
		default:
			break;
		}
		Point next = new Point(this.sceneX + dx, this.sceneY + dy);
		this.addStep(next);
		// System.out.printf("step to:%s, (%s,%s)\n", direction, next.x,
		// next.y);
		this.prepareStep();
	}

	public void moveBy(int dx, int dy) {
		this.x += dx;
		this.y += dy;
	}

	public void setDirection(int direction) {
		if (this.direction != direction) {
			this.direction = direction;
			// System.out.printf("player.direction=%s\n", this.direction);
			person.setDirection(direction);
			person.resetFrames();
			if (weapon != null) {
				weapon.setDirection(direction);
				weapon.resetFrames();
			}
		} else {
			reviseDirection();
		}
	}

	public void reviseDirection() {
		person.setDirection(direction);
		int index = person.getCurrAnimation().getIndex();
		if (weapon != null) {
			weapon.setDirection(direction);
			weapon.getCurrAnimation().setIndex(index);
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setState(String state) {
		if (this.state != state) {
			this.state = state;
			this.person = createPerson(state);
			this.weapon = createWeapon(state);
			this.person.setDirection(this.direction);
			this.person.resetFrames();
			if (this.weapon != null) {
				this.weapon.setDirection(this.direction);
				this.weapon.resetFrames();
			}
		}
	}

	private Sprite createPerson(String state) {
		Sprite sprite = SpriteFactory.loadSprite("/shape/char/" + this.character + "/" + state + ".tcp", colorations);
		return sprite;
	}

	private Sprite createWeapon(String state) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getState() {
		return state;
	}

	public synchronized void stop(boolean force) {
		if (force) {
			stopAction();
		} else {
			this.movingOn = false;
		}
		// this.setState(GameMain.STATE_NORMAL);
		// System.out.println("stop");
	}

	private synchronized void stopAction() {
		this.moving = false;
		this.movingOn = false;
		this.setState(GameMain.STATE_NORMAL);
		// System.out.println("stop action!");
	}

	public synchronized void update(long elapsedTime) {
		shadow.update(elapsedTime);
		person.update(elapsedTime);
		if (weapon != null) {
			weapon.update(elapsedTime);
		}
		// effect
		Collection<Animation> stateEffs = stateEffects.values();
		for (Animation effect : stateEffs) {
			effect.update(elapsedTime);
		}
		if (this.onceEffect != null) {
			this.onceEffect.update(elapsedTime);
			if (this.onceEffect.getRepeat() == 0) {
				// ������ϣ��Ƴ�����
				this.onceEffect = null;
			}
		}
	}

	public synchronized void updateMovement(long elapsedTime) {
		// ����״̬�ı�player��sprite��character���ܸı䣩
		this.setState(this.isMoving() ? GameMain.STATE_MOVING : this.state);
		if (this.isMoving()) {
			// ����ƶ����,����STEP_OVER��Ϣ
			if (this.isStepOver()) {
				fireEvent(new PlayerEvent(this, PlayerEvent.STEP_OVER));
				prepareStep();
			} else {// �����ƶ���
				Point d = this.calculateIncrement(elapsedTime);
				if (d.x != 0 || d.y != 0) {
					x += d.x;
					y += d.y;
					PlayerEvent evt = new PlayerEvent(this, PlayerEvent.MOVE);
					evt.setAttribute(PlayerEvent.MOVE_INCREMENT, d);
					fireEvent(evt);
					// System.out.printf("pos:(%s,%s)\tmove->:(%s,%s)\n", x, y,
					// d.x, d.y);
				}
			}
		}

	}

	private Point calculateIncrement(long elapsedTime) {
		int dx = 0, dy = 0;
		// �����������Ե����ƶ�
		if (GameMain.getSceneCanvas().pass(this.nextStep.x, this.nextStep.y)) {
			// ���������Ŀ���Ļ��Ƚ�
			double radian = Math.atan(1.0 * (nextStep.y - sceneY) / (nextStep.x - sceneX));
			// �����ƶ���
			int distance = (int) (GameMain.NORMAL_SPEED * elapsedTime);
			dx = (int) (distance * Math.cos(radian));
			dy = (int) (distance * Math.sin(radian));
			// �����ƶ�����
			if (nextStep.x > sceneX) {
				dx = Math.abs(dx);
			} else {
				dx = -Math.abs(dx);
			}
			if (nextStep.y > sceneY) {
				dy = -Math.abs(dy);
			} else {
				dy = Math.abs(dy);
			}
		} else if (this.movingOn) {// �����ϰ���ʱ����ס������ƶ�
			// TODO �����ƶ��ķ���
			// switch (this.direction) {
			// case Sprite.DIRECTION_BOTTOM:
			//				
			// break;
			//
			// default:
			// break;
			// }
		} else if (!this.movingOn) {// �����ϰ���ʱ���ɿ������(û�м����ƶ�)
			stopAction();
		}
		return new Point(dx, dy);
	}

	/**
	 * �Ƿ����һ�����ƶ�<br>
	 * ���ˮƽ���ߴ�ֱ�����ƶ����ڵ��ڲ���,����Ϊ�ƶ����
	 * 
	 * @return
	 */
	private boolean isStepOver() {
		return GameMain.getSceneCanvas().localToScene(new Point(x, y)).equals(nextStep);
	}

	/**
	 * �����¼�
	 * 
	 * @param event
	 */
	public void handleEvent(PlayerEvent event) {
		final PlayerListener[] listeners = listenerList.getListeners(PlayerListener.class);
		switch (event.getId()) {
		case PlayerEvent.STEP_OVER:
			for (PlayerListener listener : listeners) {
				listener.stepOver(this);
			}
			break;
		case PlayerEvent.WALK:
			for (PlayerListener listener : listeners) {
				listener.walk(event);
			}
			break;
		case PlayerEvent.MOVE:
			for (PlayerListener listener : listeners) {
				listener.move(this, (Point) event.getAttribute(PlayerEvent.MOVE_INCREMENT));
			}
			break;
		case PlayerEvent.CLICK:
			for (PlayerListener listener : listeners) {
				listener.click(event);
			}
			break;
		case PlayerEvent.TALK:
			for (PlayerListener listener : listeners) {
				listener.talk(event);
			}
			break;
		default:
			break;
		}
	}

	public void removePlayerListener(PlayerListener l) {
		listenerList.remove(PlayerListener.class, l);
	}

	/**
	 * �Ƿ񵽴�Ŀ�ĵ�
	 * 
	 * @return
	 */
	public boolean isArrived() {
		return false;
	}

	public Color getNameBackground() {
		return nameBackground;
	}

	public void setNameBackground(Color textBackground) {
		this.nameBackground = textBackground;
	}

	public Color getNameForeground() {
		return nameForeground;
	}

	public void setNameForeground(Color textForeground) {
		this.nameForeground = textForeground;
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public boolean isMoving() {
		return moving;
	}

	public void addPlayerListener(PlayerListener l) {
		listenerList.add(PlayerListener.class, l);
	}

	public void clearPath() {
		this.path.clear();
	}

	public void addStep(Point p) {
		this.path.add(p);
	}

	public void setPath(Collection<Point> path) {
		this.path.clear();
		this.path.addAll(path);
		if (path == null || path.isEmpty()) {
			System.out.println("can't find a path.");
		} else {
			// System.out.println("new path:");
			// for (Point p : path) {
			// System.out.printf("(%s,%s)\n", p.x, p.y);
			// }
			// System.out.println();
		}
	}

	public synchronized void draw(Graphics g, int x, int y) {
		shadow.draw(g, x, y);
		person.draw(g, x, y);
		if (weapon != null)
			weapon.draw(g, x, y);
		// draw name
		g.setFont(nameFont);
		int textY = y + 30;
		int textX = x - g.getFontMetrics().stringWidth(name) / 2;
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setColor(nameBackground);
		g2d.drawString(name, textX + 1, textY + 1);
		g2d.setColor(GameMain.isHover(this) ? highlightColor : nameForeground);
		g2d.drawString(name, textX, textY);
		g2d.dispose();

		// ����ð�ݶԻ�����
		int chatY = y - person.getRefPixelY() - 10;
		for (int i = chatPanels.size() - 1; i >= 0; i--) {
			FloatPanel chatPanel = chatPanels.get(i);
			if (shouldDisplay(chatPanel)) {
				int chatX = x - chatPanel.getWidth() / 2;
				chatY -= chatPanel.getHeight() + 2;
				Graphics g2 = g.create(chatX, chatY, chatPanel.getWidth(), chatPanel.getHeight());
				chatPanel.paint(g2);
				g2.dispose();
			} else {
				chatPanels.remove(i);
			}
		}
		// effect
		Collection<Animation> stateEffs = stateEffects.values();
		for (Animation effect : stateEffs) {
			effect.draw(g, x, y);
		}
		if (this.onceEffect != null)
			onceEffect.draw(g, x, y);
		if(GameMain.isDebug()) {
			g.drawLine(x-10, y, x+10, y);
			g.drawLine(x, y-10, x, y+10);
		}
	}

	private boolean shouldDisplay(FloatPanel chatPanel) {
		return System.currentTimeMillis() - chatPanel.getCreateTime() < GameMain.CHAT_REMAIND_TIME;
	}

	@Override
	public void dispose() {
		// TODO Player: dispose

	}

	public Sprite getWeapon() {
		return weapon;
	}

	public void setWeapon(Sprite weapon) {
		this.weapon = weapon;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public int getDirection() {
		return direction;
	}

	public boolean isStepping() {
		return stepping;
	}

	/**
	 * ׼����һ��
	 */
	private synchronized void prepareStep() {
		this.nextStep = this.popPath();
		// ·���Ѿ�Ϊ��,ֹͣ�ƶ�
		if (this.nextStep == null) {
			if (this.movingOn) {
				this.stepTo(direction);
			} else {
				this.stopAction();
			}
		}
		this.stepAction();
	}

	private void stepAction() {
		if (this.nextStep != null) {
			// ������һ���ķ���
			int dir = calculateStepDirection(this.nextStep);
			if (dir != -1) {
				setDirection(dir);
			}
			this.moving = true;
		}
	}

	public int getSceneX() {
		return sceneX;
	}

	public int getSceneY() {
		return sceneY;
	}

	public Point getSceneLocation() {
		return new Point(sceneX, sceneY);
	}

	public void setSceneLocation(int x, int y) {
		this.sceneX = x;
		this.sceneY = y;
	}

	public void setSceneLocation(Point p) {
		setSceneLocation(p.x, p.y);
	}

	public void moveOn() {
		this.movingOn = true;
	}

	@Override
	public String toString() {
		return "[name=" + this.name + ",x=" + this.x + ",y=" + this.y + ",sceenX=" + this.sceneX + ",sceneY="
				+ this.sceneY + "]";
	}

	public List<Point> getPath() {
		Point[] paths = new Point[path.size()];
		path.toArray(paths);
		return Arrays.asList(paths);
	}

	public String getCharacter() {
		return character;
	}

	public boolean handleEvent(EventObject evt) throws EventException {
		if (evt instanceof PlayerEvent) {
			PlayerEvent playerEvt = (PlayerEvent) evt;
			handleEvent(playerEvt);
		}
		return false;
	}

	public void fireEvent(PlayerEvent e) {
		EventDispatcher.getInstance(Player.class, PlayerEvent.class).dispatchEvent(e);
	}

	public Sprite getPerson() {
		return person;
	}

	public Sprite getShadow() {
		return shadow;
	}

	public int[] getColorations() {
		return colorations;
	}

	public void setColorations(int[] colorations, boolean recreate) {
		this.colorations = colorations;
		if (recreate) {
			this.coloring(colorations);
		}
	}

	public void coloring(int[] colorations) {
		// ���¸ı���ɫ��ľ���
		this.person = createPerson(state);
		this.weapon = createWeapon(state);
		this.person.setDirection(this.direction);
		this.person.resetFrames();
		if (this.weapon != null) {
			this.weapon.setDirection(this.direction);
			this.weapon.resetFrames();
		}
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public int getColorationCount(int part) {
		if (this.profileData == null) {
			// ������ɫ����
			WASDecoder decoder = new WASDecoder();
			decoder.loadColorationProfile("shape/char/" + this.character + "/00.pp");
			int partCount = decoder.getSectionCount();
			this.profileData = new int[partCount];
			for (int i = 0; i < partCount; i++) {
				this.profileData[i] = decoder.getSchemeCount(i);
			}
		}
		return this.profileData[part];
	}

	/**
	 * ���β���Ч������
	 * 
	 * @param name
	 */
	public void playEffect(String name) {
		Animation s = SpriteFactory.loadAnimation("/magic/" + name + ".tcp");
		s.setRepeat(1);
		this.onceEffect = s;
		try {
			MP3Player.play("sound/magic/" + name + ".mp3");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���״̬Ч��
	 * 
	 * @param name
	 */
	public void addStateEffect(String name) {
		Animation s = SpriteFactory.loadAnimation("/magic/" + name + ".tcp");
		this.stateEffects.put(name, s);
	}

	/**
	 * ȡ��״̬Ч��
	 * 
	 * @param name
	 */
	public void removeStateEffect(String name) {
		this.stateEffects.remove(name);
	}

	/**
	 * ����ĳ�������Ķ���
	 * 
	 * @param state
	 */
	public void playOnce(String state) {
		this.setState(state);
		this.person.setRepeat(1);
		if (this.weapon != null) {
			this.weapon.setRepeat(1);
		}
		MP3Player.play("sound/char/" + this.character + "/" + state + ".mp3");
	}

	/**
	 * �ȴ���ǰ��������
	 */
	public void waitFor() {
		this.person.getCurrAnimation().waitFor();
	}

	/**
	 * �ȴ�Ч����������
	 * 
	 * @param name
	 */
	public void waitForEffect(String name) {
		if (this.onceEffect != null)
			this.onceEffect.waitFor();
	}

	public int getTop() {
		return y - this.person.getRefPixelY();
	}

	public int getLeft() {
		return x - this.person.getRefPixelX();
	}

	public PlayerVO getData() {
		return data;
	}

	public void setData(PlayerVO data) {
		this.data = data;
	}

	/**
	 * ���ȫ��������
	 */
	public void removeAllListeners() {
		PlayerListener[] listeners = listenerList.getListeners(PlayerListener.class);
		for (int i = 0; i < listeners.length; i++) {
			this.removePlayerListener(listeners[i]);
		}
	}
	
	@Override
	public void setAlpha(float alpha) {
		super.setAlpha(alpha);
		shadow.setAlpha(alpha);
		person.setAlpha(alpha);
		if(this.weapon!=null) {
			this.weapon.setAlpha(alpha);
		}
	}

	@Override
	protected void doDraw(Graphics2D g2, int x, int y, int width, int height) {
	}

}
