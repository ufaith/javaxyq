/**
 * 
 */
package com.javaxyq.battle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
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
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;
import javax.swing.event.EventListenerList;

import com.javaxyq.core.DialogFactory;
import com.javaxyq.core.GameMain;
import com.javaxyq.core.SpriteFactory;
import com.javaxyq.data.DataStore;
import com.javaxyq.data.ItemInstance;
import com.javaxyq.graph.Canvas;
import com.javaxyq.graph.Label;
import com.javaxyq.model.Item;
import com.javaxyq.model.Item;
import com.javaxyq.model.PlayerVO;
import com.javaxyq.ui.ItemDetailLabel;
import com.javaxyq.ui.ItemLabel;
import com.javaxyq.ui.UIHelper;
import com.javaxyq.util.MP3Player;
import com.javaxyq.widget.Animation;
import com.javaxyq.widget.Cursor;
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
	private ItemInstance selectedItem;
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
	
	/**
	 * �������npc�Ƴ�ս������
	 * @param p
	 */
	public void removePlayerFromTeam(Player p) {
		this.adversaryTeam.remove(p);
		this.ownsideTeam.remove(p);
		removeNPC(p);
	}

	public void init() {
		battleMask = SpriteFactory.loadAnimation("/addon/battlebg.tcp");
		UIHelper.showDialog(BATTLE_ROLE_CMD);
		UIHelper.showDialog(BATTLE_MSG);
		lblMsg = (Label) super.findCompByName("lbl_battle_msg");

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

	public void setSelectedItem(ItemInstance item) {
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
			player.setDirection(Sprite.DIR_DOWN_RIGHT);
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
			player.setDirection(Sprite.DIR_UP_LEFT);
			addNPC(player);
		}
		ranked = true;
	}

	private ItemDetailLabel detailLabel = new ItemDetailLabel();

	private void initItems() {
		BattleCanvas canvas = GameMain.getBattleCanvas();
		ItemInstance[] items = DataStore.getPlayerItems(canvas.getPlayer());
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
			ItemInstance item = label.getItem();
			if (item != null) {
				setSelectedItem(item);
				UIHelper.hideToolTip(detailLabel);
			}
		}

		public void mouseMoved(MouseEvent e) {
			ItemLabel label = (ItemLabel) e.getComponent();
			ItemInstance item = label.getItem();
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
			// update comps on the canvas
			drawComponents(g, elapsedTime);
			// draw fade
			g.setColor(new Color(0, 0, 0, alpha));
			g.fillRect(0, 0, getWidth(), getHeight());
			
			drawDebug(g);
			drawDownloading(g);
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
			int slotw = data.hp * maxWidth / data.maxHp;
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
		setGameCursor(selectingTarget ? Cursor.SELECT_CURSOR : Cursor.ATTACK_CURSOR);
	}

	public void mouseExited(MouseEvent e) {
		setGameCursor(Cursor.DEFAULT_CURSOR);
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		setGameCursor(selectingTarget ? Cursor.SELECT_CURSOR : Cursor.ATTACK_CURSOR);
	}

	public void keyPressed(KeyEvent e) {
		if (e.isAltDown() && waitingCmd) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				if (target == null || target.getData().hp == 0 || isOwnside(target)) {
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
					if (target == null || target.getData().hp == 0 || isOwnside(target)) {
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
					if (target != null && target.getData().hp == 0) {
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
		} while (target.getData().hp == 0);
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
	/**
	 * @param target2
	 */
	public void cleanPlayer(Player player) {
		try {
			BlinkWorker blinker = new BlinkWorker(player, 400);
			blinker.execute();
			blinker.get();
			FadeOutWorker worker = new FadeOutWorker(player, 200);
			worker.execute();
			worker.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	public void runaway(Player player, boolean success) {
		try {
			RunawayWorker worker = new RunawayWorker(player,success, 2000);
			worker.execute();
			worker.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	private class BackwardThread extends Thread {
		public void run() {
			int dist = 15;
			int step = 2;
			if (backingPlayer.getDirection() == Sprite.DIR_DOWN_RIGHT) {// �泯����
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
	private static class FlyOutWorker extends SwingWorker<Point, Point>{

		private Player player;

		public FlyOutWorker(Player player) {
			this.player = player;
		}
		
		@Override
		protected Point doInBackground() throws Exception {
			
			return null;
		}
		
		@Override
		protected void process(List<Point> chunks) {
			super.process(chunks);
		}
		
	}
	
	private class FadeOutWorker extends SwingWorker<Float, Float>{
		private Player player;
		private long duration;

		public FadeOutWorker(Player player,long duration) {
			this.player = player;
			this.duration = duration;
		}
		
		@Override
		protected Float doInBackground() throws Exception {
			long passTime = 0;
			long interval = 50; 
			float alpha = 1.0f;
			while (passTime < duration) {
				// System.out.println(this.getId()+" "+this.getName());
				passTime += interval;
				alpha = (float) (1 - (1.0 * passTime / duration));
				if (alpha < 0) {
					alpha = 0;
				}
				if (alpha > 1) {
					alpha = 1;
				}
				player.setAlpha(alpha);
				publish(alpha);
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
				}
			}
			removePlayerFromTeam(player);
			player.setAlpha(1.0f);
			System.out.println("��"+player.getName()+"�Ƴ����顣");
			return alpha;
		}
		
		@Override
		protected void done() {
			super.done();
		}
		
		@Override
		protected void process(List<Float> chunks) {
			super.process(chunks);
//			for (Float a : chunks) {
//				System.out.println("player alpha: "+a);
//			}
		}
		
	}
	
	private class BlinkWorker extends SwingWorker{
		private Player player;
		private long duration;
		public BlinkWorker(Player player, long duration) {
			super();
			this.player = player;
			this.duration = duration;
		}
		@Override
		protected Object doInBackground() throws Exception {
			long minShow = 50;
			long interval = (this.duration - minShow*2)/2;
			try {
				this.player.setAlpha(0);
				Thread.sleep(interval);
				this.player.setAlpha(1.0f);
				Thread.sleep(minShow);
				this.player.setAlpha(0);
				Thread.sleep(interval);
				this.player.setAlpha(1.0f);
				Thread.sleep(minShow);
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				this.player.setAlpha(1.0f);
			}
			return null;
		}
	}

	private class RunawayWorker extends SwingWorker{
		private Player player;
		private long duration;
		private boolean success;
		public RunawayWorker(Player player, boolean success, long duration) {
			super();
			this.player = player;
			this.duration = duration;
			this.success = success;
		}
		@Override
		protected Object doInBackground() throws Exception {
			//ת��
			int dir =  player.getDirection();
			player.setDirection(dir-2);
			//�л���rush
			player.setState("rusha");
			Thread.sleep(500);
			if(this.success) {
				MP3Player.play("sound/addon/escape_ok.mp3");
				long interval = 50;
				long t = 0;
				while(t<duration) {
					Thread.sleep(interval);
					// �����ƶ���
					long elapsedTime = interval;
					int distance = (int) (2*GameMain.NORMAL_SPEED * elapsedTime);
					int dx = distance; //����������
					int dy = distance;
					if(player.getDirection() == Sprite.DIR_UP_LEFT) {//����������
						dx = -dx;
						dy = -dy;
					}
					player.moveBy(dx, dy);
					publish(new Point(dx,dy));
					t += interval;
					//����Ƴ���������ֹ����
					if(player.getX()<0 || player.getY()< 0 || player.getX()>BattleCanvas.this.getWidth() 
							||player.getY() > BattleCanvas.this.getHeight()) {
						break;
					}
				}
			}else {				
				UIHelper.prompt("�������ã�����ʧ�ܣ�#83",3000);
			}
			player.setState(Player.STATE_STAND);
			player.setDirection(dir);
			return null;
		}
		
//		protected void process(List chunks) {
//			super.process(chunks);
//			for (Object p : chunks) {
//				System.out.println("moveby: "+p);
//			}
//		}
	}

}