/**
 * 
 */
package com.javaxyq.battle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.event.EventListenerList;

import com.javaxyq.core.DataStore;
import com.javaxyq.core.DialogFactory;
import com.javaxyq.core.GameMain;
import com.javaxyq.core.SpriteFactory;
import com.javaxyq.graph.Canvas;
import com.javaxyq.graph.Label;
import com.javaxyq.model.Item;
import com.javaxyq.model.PlayerVO;
import com.javaxyq.ui.ItemDetailLabel;
import com.javaxyq.ui.ItemLabel;
import com.javaxyq.ui.UIHelper;
import com.javaxyq.widget.Animation;
import com.javaxyq.widget.Player;
import com.javaxyq.widget.Sprite;

/**
 * ��Ϸս��ϵͳ
 * 
 * @author dewitt
 * @date 2009-11-06
 */
public class BattleCanvas extends Canvas implements MouseListener, MouseMotionListener, KeyListener {

	private static final String BATTLE_ROLE_CMD = "battle_role_cmd";
	private static final String BATTLE_WARMAGIC10 = "battle_warmagic10";
	private static final String BATTLE_USEITEM = "battle_useitem";
	private static final String BATTLE_MSG = "battle_msg";
	private EventListenerList listenerList = new EventListenerList();
	private List<Player> ownsideTeam;
	private List<Player> adversaryTeam;
	private Animation battleMask;
	private Image battleBackground;
	private Label lblMsg;
	private Player target;
	private Random random = new Random();

	private boolean selectingTarget;
	/**
	 * ָ�������
	 */
	private CommandManager cmdMan;
	/**
	 * ���˶�ܵ�player
	 */
	private Player backingPlayer;
	/**
	 * ��ǰѡ��ķ���id
	 */
	private String selectedMagic;
	private boolean selectingItem;
	private Item selectedItem;
	private Command lastCmd;

	public BattleCanvas(int width, int height) {
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		setSize(width, height);
		setMaxWidth(width);
		setMaxHeight(height);
		cmdMan = new CommandManager(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
	}

	public void addBattleListener(BattleListener listener) {
		listenerList.add(BattleListener.class, listener);
	}

	public void removeBattleListener(BattleListener listener) {
		listenerList.remove(BattleListener.class, listener);
	}

	protected void fireBattleEvent(BattleEvent evt) {
		GameMain.quitBattle();
		BattleListener[] listeners = listenerList.getListeners(BattleListener.class);
		for (int i = 0; i < listeners.length; i++) {
			switch (evt.getId()) {
			case BattleEvent.BATTLE_WIN:
				listeners[i].battleWin(evt);
				break;
			case BattleEvent.BATTLE_DEFEATED:
				listeners[i].battleDefeated(evt);
				break;
			case BattleEvent.BATTLE_TIMEOUT:
				listeners[i].battleTimeout(evt);
				break;
			case BattleEvent.BATTLE_BREAK:
				listeners[i].battleBreak(evt);
				break;
			}
		}
	}

	/**
	 * ���õз�����
	 * 
	 * @param team
	 */
	public void setAdversaryTeam(List<Player> team) {
		this.adversaryTeam = team;
	}

	/**
	 * �����ҷ�����
	 * 
	 * @param team
	 */
	public void setOwnsideTeam(List<Player> team) {
		this.ownsideTeam = team;
	}

	public void init() {
		battleMask = SpriteFactory.loadAnimation("/addon/battlebg.tcp");
		UIHelper.showDialog(BATTLE_ROLE_CMD);
		UIHelper.showDialog(BATTLE_MSG);
		lblMsg = (Label) super.findCompByName("textս����Ϣ");

		rank();
		cmdIndex = 0;
		this.setPlayer(ownsideTeam.get(cmdIndex));
		waitingCmd = true;
	}

	/**
	 * ѡ��������Ŀ��
	 */
	public void selectTarget() {
		selectingTarget = true;
		UIHelper.hideDialog(BATTLE_ROLE_CMD);
	}

	/**
	 * ѡ��Ҫʩ�ŵķ���
	 */
	public void selectMagic() {
		UIHelper.hideDialog(BATTLE_ROLE_CMD);
		UIHelper.showDialog(BATTLE_WARMAGIC10);
		selectingMagic = true;
	}

	/**
	 * ȡ��ѡ����
	 */
	public void cancelSelectMagic() {
		UIHelper.showDialog(BATTLE_ROLE_CMD);
		UIHelper.hideDialog(BATTLE_WARMAGIC10);
		selectingTarget = false;
		selectingMagic = false;
	}

	/**
	 * ���õ�ǰѡ��ķ���id
	 * 
	 * @param magicId
	 */
	public void setSelectedMagic(String magicId) {
		this.selectedMagic = magicId;
		this.lastMagic = magicId;
		selectingMagic = false;
		UIHelper.hideDialog(BATTLE_WARMAGIC10);
		selectTarget();
	}

	public void selectItem() {
		UIHelper.hideDialog(BATTLE_ROLE_CMD);
		UIHelper.showDialog(BATTLE_USEITEM);
		initItems();
		selectingItem = true;
	}

	public void setSelectedItem(Item item) {
		this.selectedItem = item;
		this.selectingItem = false;
		UIHelper.hideDialog(BATTLE_USEITEM);
		selectTarget();
	}

	/**
	 * ȡ��ѡ�����
	 */
	public void cancelSelectItem() {
		UIHelper.showDialog(BATTLE_ROLE_CMD);
		UIHelper.hideDialog(BATTLE_USEITEM);
		selectingTarget = false;
		selectingMagic = false;
		selectingItem = false;
	}

	public void defendCmd() {
		Player cmdPlayer = ownsideTeam.get(cmdIndex);
		Command cmd = new Command("defend", cmdPlayer, null);
		addCmd(cmd);
	}

	public void runawayCmd() {
		Player cmdPlayer = ownsideTeam.get(cmdIndex);
		Command cmd = new Command("runaway", cmdPlayer, null);
		addCmd(cmd);
	}

	/**
	 * ���͹�������
	 */
	public void attackCmd() {
		if (target == null) {
			target = randomEnemy();
		}
		Player cmdPlayer = ownsideTeam.get(cmdIndex);
		Command cmd = new Command("attack", cmdPlayer, target);
		addCmd(cmd);
	}

	/**
	 * ���ͷ�����������
	 */
	public void magicCmd() {
		if (target == null) {
			target = randomEnemy();
		}
		Player cmdPlayer = ownsideTeam.get(cmdIndex);
		Command cmd = new Command("magic", cmdPlayer, target);
		cmd.add("magic", selectedMagic);
		cmd.add("mp", -25);
		cmd.add("basehit", 8);
		// cmd.add("hitpoints",random.nextInt(20)+120);
		addCmd(cmd);
		selectedMagic = null;
	}

	public void itemCmd() {
		if (target == null) {
			target = randomEnemy();
		}
		Player cmdPlayer = ownsideTeam.get(cmdIndex);
		Command cmd = new Command("item", cmdPlayer, target);
		cmd.add("item", selectedItem);

		addCmd(cmd);
		selectedItem = null;
	}

	/**
	 * ��ǰ���ܵ�Ŀ���
	 * 
	 * @param player
	 * @param x
	 * @param y
	 */
	public void rushForward(Player player, int x, int y) {
		this.targetX = x;
		this.targetY = y;
		this.originX = player.getX();
		this.originY = player.getY();
		this.movingPlayer = player;
		player.setState("rusha");
		long lastTime = System.currentTimeMillis();
		while (!isReach()) {
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			long nowTime = System.currentTimeMillis();
			updateMovement(nowTime - lastTime);
			lastTime = nowTime;
		}
	}

	/**
	 * ����ܵ�Ŀ���
	 * 
	 * @param player
	 * @param x
	 * @param y
	 */
	public void rushBack(Player player, int x, int y) {
		this.targetX = x;
		this.targetY = y;
		this.originX = player.getX();
		this.originY = player.getY();
		this.movingPlayer = player;
		player.setState("rushb");
		long lastTime = System.currentTimeMillis();
		while (!isReach()) {
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			long nowTime = System.currentTimeMillis();
			updateMovement(nowTime - lastTime);
			lastTime = nowTime;
		}
	}

	/**
	 * ���ˣ��㿪������
	 * 
	 * @param player
	 */
	public void backward(Player player) {
		this.backingPlayer = player;
		new BackwardThread().start();
	}

	private int targetX, targetY;
	private int originX, originY;
	private Player movingPlayer;
	private Animation targetAnim;
	private boolean waitingCmd;

	private void updateMovement(long elapsedTime) {
		int dx = 0, dy = 0;
		// ���������Ŀ���Ļ��Ƚ�
		double radian = Math.atan(1.0 * (targetY - movingPlayer.getY()) / (targetX - movingPlayer.getX()));
		// �����ƶ���
		int distance = (int) (GameMain.NORMAL_SPEED * 6 * elapsedTime);
		dx = (int) (distance * Math.cos(radian));
		dy = (int) (distance * Math.sin(radian));
		// �����ƶ�����
		if (targetX > originX) {
			dx = Math.abs(dx);
			dx = Math.min(dx, targetX - movingPlayer.getX());
		} else {
			dx = -Math.abs(dx);
			dx = Math.max(dx, targetX - movingPlayer.getX());
		}
		if (targetY > originY) {
			dy = Math.abs(dy);
			dy = Math.min(dy, targetY - movingPlayer.getY());
		} else {
			dy = -Math.abs(dy);
			dy = Math.max(dy, targetY - movingPlayer.getY());
		}
		movingPlayer.moveBy(dx, dy);
	}

	/**
	 * ��ǰ��λ�Ƿ񵽴�Ŀ���
	 * 
	 * @return
	 */
	private boolean isReach() {
		return Math.abs(targetX - movingPlayer.getX()) <= 2 && Math.abs(targetY - movingPlayer.getY()) <= 2;
	}

	/**
	 * ����˫�������Ա
	 */
	private void rank() {
		// TODO ������������
		int dx = 60, dy = 40;
		int x0 = 340, y0 = 400;
		int x1 = 300, y1 = 80;
		// ���ез���λ
		switch (adversaryTeam.size()) {
		case 1:
			x1 -= 2 * dx;
			y1 += 2 * dy;
			break;
		case 2:
			x1 -= 1.5 * dx;
			y1 += 1.5 * dy;
			break;
		case 3:
			x1 -= 1 * dx;
			y1 += 1 * dy;
			break;
		case 4:
			break;
		default:
			break;
		}
		for (int i = 0; i < adversaryTeam.size(); i++) {
			Player player = adversaryTeam.get(i);
			player.setLocation(x1 - dx * i, y1 + dy * i);
			player.setDirection(Sprite.DIRECTION_BOTTOM_RIGHT);
			addNPC(player);
		}

		// �����ҷ���λ
		switch (ownsideTeam.size()) {
		case 1:
			x0 += 2 * dx;
			y0 -= 2 * dy;
			break;
		case 2:
			x0 += 1.5 * dx;
			y0 -= 1.5 * dy;
			break;
		case 3:
			x0 += 1 * dx;
			y0 -= 1 * dy;
			break;
		case 4:
			break;
		default:
			break;
		}
		for (int i = 0; i < ownsideTeam.size(); i++) {
			Player player = ownsideTeam.get(i);
			player.setLocation(x0 + dx * i, y0 - dy * i);
			player.setDirection(Sprite.DIRECTION_TOP_LEFT);
			addNPC(player);
		}
		ranked = true;
	}

	private ItemDetailLabel detailLabel = new ItemDetailLabel();

	private void initItems() {
		BattleCanvas canvas = GameMain.getBattleCanvas();
		Item[] items = DataStore.getPlayerItems(canvas.getPlayer());
		// ������ʾ�ĵ���
		for (int i = 0; i < items.length; i++) {
			ItemLabel label = (ItemLabel) DialogFactory.getDialog(BATTLE_USEITEM, true).findCompByName("item" + (i + 1));
			label.setItem(items[i]);
			if (!installItemListener) {
				label.addMouseListener(itemMouseHandler);
				label.addMouseMotionListener(itemMouseHandler);
			}
		}
		installItemListener = true;
	}

	private ItemMouseHandler itemMouseHandler = new ItemMouseHandler();
	private boolean installItemListener = false;
	private Animation soltAnim;
	private Animation emptysoltAnim;
	private boolean ranked;

	private class ItemMouseHandler extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			ItemLabel label = (ItemLabel) e.getComponent();
			Item item = label.getItem();
			if (item != null) {
				setSelectedItem(item);
				UIHelper.hideToolTip(detailLabel);
			}
		}

		public void mouseMoved(MouseEvent e) {
			ItemLabel label = (ItemLabel) e.getComponent();
			Item item = label.getItem();
			if (item != null) {
				detailLabel.setItem(item);
				UIHelper.showToolTip(detailLabel, label, e);
			} else {
				UIHelper.hideToolTip(detailLabel);
			}

		}

		public void mouseExited(MouseEvent e) {
			UIHelper.hideToolTip(detailLabel);
		}
	}

	@Override
	public synchronized void draw(Graphics g, long elapsedTime) {
		if (g == null) {
			return;
		}
		try {
			g.setColor(Color.BLACK);
			g.setColor(Color.WHITE);
			g.clearRect(0, 0, getWidth(), getHeight());
			if (battleBackground != null) {
				g.drawImage(battleBackground, 0, 0, null);
			}
			if (battleMask != null) {
				battleMask.draw(g, 0, 0);
			}
			// npcs
			drawNPC(g, elapsedTime);
			drawHpSlot(g, elapsedTime);
			drawCurrentArrow(g, elapsedTime);
			drawPoints(g);
			drawDebug(g);
			// update comps on the canvas
			drawComponents(g, elapsedTime);
			// draw fade
			g.setColor(new Color(0, 0, 0, alpha));
			g.fillRect(0, 0, getWidth(), getHeight());
		} catch (Exception e) {
			System.out.printf("����Canvasʱʧ�ܣ�\n");
			e.printStackTrace();
		}
	}

	/**
	 * ����Ŀ��ָʾ
	 * 
	 * @param g
	 * @param elapsedTime
	 */
	private void drawCurrentArrow(Graphics g, long elapsedTime) {
		if (waitingCmd) {
			if (targetAnim == null) {
				targetAnim = SpriteFactory.loadAnimation("addon/2231ebb4");
			}
			Player waitingPlayer = ownsideTeam.get(cmdIndex);
			targetAnim.update(elapsedTime);
			targetAnim.draw(g, waitingPlayer.getX(), waitingPlayer.getTop() - 20);
		}
	}

	private void drawHpSlot(Graphics g, long elapsedTime) {
		if (!ranked)
			return;
		if (soltAnim == null) {
			soltAnim = SpriteFactory.loadAnimation("/addon/4fd9fff3");
			emptysoltAnim = SpriteFactory.loadAnimation("/addon/4d0a334c");
		}
		int maxWidth = 36;
		for (int i = 0; i < ownsideTeam.size(); i++) {
			Player player = ownsideTeam.get(i);
			PlayerVO data = player.getData();
			int slotx = player.getX() - maxWidth / 2;
			int slotw = data.getHp() * maxWidth / data.getMaxHp();
			emptysoltAnim.draw(g, slotx, player.getTop() - 10);
			soltAnim.setWidth(slotw);
			soltAnim.draw(g, slotx + 1, player.getTop() + 1 - 10);
		}
	}

	/**
	 * @param player
	 * @param value
	 */
	private void drawPoints(Graphics g) {
		// -Ѫ 30f737d8
		// +Ѫ 3cf8f9fe
		Set<Entry<Player, Integer>> entrys = points.entrySet();
		for (Entry<Player, Integer> en : entrys) {
			Player player = en.getKey();
			int value = en.getValue();
			int x = player.getLeft();
			int y = player.getTop() - 10;
			int dx = 0;
			Animation numAnim = SpriteFactory.loadAnimation(value > 0 ? "misc/3cf8f9fe" : "misc/30f737d8");
			String strValue = Integer.toString(Math.abs(value));
			for (int i = 0; i < strValue.length(); i++) {
				int index = strValue.charAt(i) - '0';
				numAnim.setIndex(index);
				numAnim.draw(g, x + dx, y);
				dx += numAnim.getWidth();
			}
		}
	}

	/**
	 * ������ʾ�����ӡ����ĵ���
	 * 
	 * @param player
	 * @param value
	 */
	public void showPoints(Player player, int value) {
		points.put(player, value);
	}

	/**
	 * ���ص���
	 * 
	 * @param player
	 */
	public void hidePoints(Player player) {
		points.remove(player);
	}

	private Map<Player, Integer> points = new HashMap<Player, Integer>();
	private boolean selectingMagic;
	private String lastMagic;

	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		Player clickPlayer = null;
		// �Ƿ����ڵз���λ��
		for (int i = 0; i < adversaryTeam.size(); i++) {
			Player p = adversaryTeam.get(i);
			if (p.contains(x - p.getX(), y - p.getY())) {
				clickPlayer = p;
				break;
			}
		}
		if (clickPlayer == null) {
			// ������ҷ��ĵ�λ
			for (int i = 0; i < ownsideTeam.size(); i++) {
				Player p = ownsideTeam.get(i);
				if (p.contains(x - p.getX(), y - p.getY())) {
					clickPlayer = p;
					break;
				}
			}
		}
		if (waitingCmd && clickPlayer != null) {
			target = clickPlayer;
			if (selectingTarget) {
				selectingTarget = false;
				if (selectedMagic != null) {
					magicCmd();
				} else if (selectedItem != null) {
					itemCmd();
				}
			} else {
				attackCmd();
			}
		} else if (waitingCmd && e.getButton() == MouseEvent.BUTTON3) {
			cancelSelectMagic();
			cancelSelectItem();
		}
	}

	public void mouseEntered(MouseEvent e) {
		setGameCursor(selectingTarget ? "select" : "attack");
	}

	public void mouseExited(MouseEvent e) {
		setGameCursor("default");
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		setGameCursor(selectingTarget ? "select" : "attack");
	}

	public void keyPressed(KeyEvent e) {
		if (e.isAltDown() && waitingCmd) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				if (target == null || target.getData().getHp() == 0 || isOwnside(target)) {
					target = randomEnemy();
				}
				attackCmd();
				break;
			case KeyEvent.VK_S:
				if (lastMagic != null) {
					selectedMagic = lastMagic;
					selectTarget();
				}
				break;
			case KeyEvent.VK_Q:
				// TODO �жϣ�������ҷ������淨��.....
				if (lastMagic != null) {
					if (target == null || target.getData().getHp() == 0 || isOwnside(target)) {
						target = randomEnemy();
					}
					selectedMagic = lastMagic;
					magicCmd();
				}
				break;
			case KeyEvent.VK_R:// �ظ���һ������
				if (lastCmd != null) {
					target = lastCmd.getTarget();
					// ����ϴε�Ŀ���Ѿ��������Զ����ѡ��Ŀ��
					if (target != null && target.getData().getHp() == 0) {
						target = randomEnemy();
					}
					addCmd(lastCmd);
				}
				break;
			case KeyEvent.VK_W: // show magic
				if (selectingMagic) {
					cancelSelectMagic();
				} else {
					selectMagic();
				}
				break;
			case KeyEvent.VK_E: // item
				if (selectingItem) {
					cancelSelectItem();
				} else {
					selectItem();
				}
				break;
			}
		}
	}

	/**
	 * ���ѡ��һ������
	 * 
	 * @return
	 */
	private Player randomEnemy() {
		Player target = null;
		do {
			target = adversaryTeam.get(random.nextInt(adversaryTeam.size()));
		} while (target.getData().getHp() == 0);
		return target;
	}

	/**
	 * �ж϶����Ƿ�Ϊ��ҵĶ���
	 * 
	 * @param p
	 * @return
	 */
	private boolean isOwnside(Player p) {
		return ownsideTeam.contains(p);
	}

	public void keyReleased(KeyEvent e) {

	}

	public void keyTyped(KeyEvent e) {

	}

	protected List<Player> getOwnsideTeam() {
		return ownsideTeam;
	}

	protected List<Player> getAdversaryTeam() {
		return adversaryTeam;
	}

	public void setMsg(String text) {
		this.lblMsg.setText(text);
	}

	/**
	 * ��ǰָ��ս��ָ����������
	 */
	private int cmdIndex;

	public void addCmd(Command cmd) {
		cmdMan.addCmd(cmd);
		lastCmd = cmd;
		if (cmdIndex >= ownsideTeam.size() - 1) {
			turnBattle();
		} else {
			UIHelper.showDialog(BATTLE_ROLE_CMD);
			cmdIndex++;
			this.setPlayer(ownsideTeam.get(cmdIndex));
			waitingCmd = true;
			// �ȴ���һ�������ָ��
		}
	}

	private void turnBattle() {
		// ȫ��ָ����յ������лغ�ս��
		waitingCmd = false;
		UIHelper.hideDialog(BATTLE_ROLE_CMD);
		new Thread("BattleThread") {
			public void run() {
				cmdMan.turnBattle();
			};
		}.start();
	}

	/**
	 * ��ʼ�»غ�
	 */
	public void turnBegin() {
		UIHelper.showDialog(BATTLE_ROLE_CMD);
		cmdIndex = 0;
		this.setPlayer(ownsideTeam.get(cmdIndex));
		waitingCmd = true;
	}

	public void setPlayer(Player player) {
		super.setPlayer(player);
	}

	private class BackwardThread extends Thread {
		public void run() {
			int dist = 15;
			int step = 2;
			if (backingPlayer.getDirection() == Sprite.DIRECTION_BOTTOM_RIGHT) {// �泯����
				step = -step;
			}
			int backingX = backingPlayer.getX();
			int backingY = backingPlayer.getY();
			// ����
			for (int i = 0; i < dist; i++) {
				backingX += step;
				backingY += step;
				backingPlayer.setLocation(backingX, backingY);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// ��ͣ
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// �ָ�
			for (int i = 0; i < dist; i++) {
				backingX -= step;
				backingY -= step;
				backingPlayer.setLocation(backingX, backingY);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			backingPlayer = null;
		}
	}

	public void setBattleBackground(Image battleBackground) {
		this.battleBackground = battleBackground;
	}
	/**
	 * ���һ��ʩ�ŵķ���
	 * @return the lastMagic
	 */
	public String getLastMagic() {
		return lastMagic;
	}
	/**
	 * set value of lastMagic
	 * @param lastMagic 
	 */
	public void setLastMagic(String lastMagic) {
		this.lastMagic = lastMagic;
	}

	protected String getMusic() {
		return "music/2003.mp3";
	}
}
