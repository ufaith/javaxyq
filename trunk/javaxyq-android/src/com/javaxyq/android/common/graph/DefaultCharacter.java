package com.javaxyq.android.common.graph;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Location;

import com.javaxyq.android.common.graph.widget.Sprite;

public class DefaultCharacter implements Character {
	
	private Object LocationLock = new Object();
	
	private String id;
	
	/** 精灵 *///TODO private
	public Sprite sprite;
	
	/** 角色动作  */
	private String action = CharacterActions.STAND;
	
	private LinkedList<Point> footmark;
	
	private LinkedList<Point> track;
	
	private int direction;
	
	private Context context;
	
	private int x;
	
	private int y;
	
	/** 是否在移动  */
	private boolean moving;
	
	/** 移动速度  */
	private int speed;
	
	/** 是否继续移动 */
	private boolean moveon;
	
	private int lastFrameIndex;
	
	public DefaultCharacter(Context context,String id) {
		this.context = context;
		this.id = id;
		footmark = new LinkedList<Point>();
		track = new LinkedList<Point>();
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isReady() {
		return (null != sprite);
	}

	@Override
	public void initialize() {
		refresh();

	}
	
	synchronized public void refresh() {
		String resId = id + "-" + action;
		if(null == sprite || !sprite.getResId().equals(resId)) {
			sprite = SpriteFactory.getSprite(context, id, action);
		}
		if(null != sprite && sprite.getDirection() != direction) {
			sprite.setDirection(direction);
			sprite.resetFrames();
		}
 		
	}

	@Override
	public void update(long elapsedTime) {
		updateAnimation(elapsedTime);
		updateMovement(elapsedTime);
		
	}
	
	private void updateAnimation(long elapsedTime) {
		if(isReady()) {
			this.sprite.update(elapsedTime);
			if(moving) {
				int findex = this.sprite.getCurrAnimation().getIndex();
				if(lastFrameIndex != findex) {
					
				}
				lastFrameIndex = findex;
			}
		}
	}
	
	private void updateMovement(long elapsedTime) {
		checkMovement();
		if(moving && speed>0) {
			int dx=0, dy=0;
			double unit = 2;
			double q = 0.7;
			switch (this.direction) {
			case Directions.LEFT:
				dx = - (int) (unit * this.speed);
				break;
			case Directions.UP_LEFT:
				dx = - (int) (unit * this.speed * q);
				dy = (int) (unit * this.speed * q);
				break;
			case Directions.UP:
				dy = (int) (unit * this.speed);
				break;
			case Directions.UP_RIGHT:
				dx = (int) (unit * this.speed * q);
				dy = (int) (unit * this.speed * q);
				break;
			case Directions.RIGHT:
				dx = (int) (unit * this.speed);
				break;
			case Directions.DOWN_RIGHT:
				dx = (int) (unit * this.speed * q);
				dy = - (int) (unit * this.speed * q);
				break;
			case Directions.DOWN:
				dy = - (int) (unit * this.speed * q);
				break;
			case Directions.DOWN_LEFT:
				dx = - (int) (unit * this.speed * q);
				dy = - (int) (unit * this.speed * q);
				break;
			default:
				break;
			}
			this.moveBy(dx, -dy);
		}
	}
	
	private void checkMovement() {
		Point last = getLastFootmark();
		float rst[] = new float[1];
		Location.distanceBetween(last.x, last.y, getLocation().x, getLocation().y, rst);
		double distance = rst[0];
		if(distance >= 20) {
			moveArrived();
		}
	}
	
	private void moveArrived() {
		saveFootmark();
		if(!isMoveOn()) {
			moving = false;
			stand();
		}
	}

	@Override
	public void draw(Canvas canvas) {
		if(isReady()) {
			this.sprite.draw(canvas, x, y);
		}

	}

	@Override
	public Point getLocation() {
		synchronized (LocationLock) {
			return new Point(x, y);
		}
	}

	@Override
	public void moveTo(int x, int y) {
		synchronized (LocationLock) {
			this.x = x;
			this.y = y;
			saveFootmark();
		}

	}

	@Override
	public void moveBy(int x, int y) {
		synchronized (LocationLock) {
			this.x += x;
			this.y += y;
			saveTrack();
		}
	}
	
	private void saveTrack() {
		Point last = track.size() > 0 ? track.getLast() : null;
		Point p = new Point(x,y);
		if(last == null || !last.equals(p)) {
			track.add(new Point(x,y));
		}
	}

	@Override
	public void walk() {
		this.action = CharacterActions.WALK;
		this.moving = true;
		this.speed = 1;
		refresh();
		saveFootmark();
	}

	@Override
	public void rush() {
		this.action = CharacterActions.RUSHA;
		this.moving = true;
		this.speed = 2;
		refresh();
		saveFootmark();
	}

	@Override
	public void stand() {
		this.action = CharacterActions.STAND;
		this.moving = false;
		this.speed = 0;
		refresh();
		saveFootmark();

	}

	@Override
	public void turn(int direction) {
		if(direction > 7) {
			direction %= 8;
		}
		if(direction < 0) {
			direction += 8;
		}
		this.direction = direction;
		refresh();
	}

	@Override
	public void turn() {
		if(this.direction < 4) {
			turn(this.direction + 4);
		} else if(this.direction == 7) {
			turn(0);
		} else {
			turn(this.direction - 3);
		}
	}

	@Override
	public int getDirection() {
		return direction;
	}

	@Override
	public void action(String key) {
		this.action = key;
		this.moving = false;
		this.speed = 0;
		refresh();
	}

	@Override
	public boolean isMoveOn() {
		// TODO Auto-generated method stub
		return moveon;
	}

	@Override
	public void setMoveon(boolean moveon) {
		this.moveon = moveon;

	}
	
	private void saveFootmark() {
		Point last = getLastFootmark();
		Point p = new Point(x, y);
		if(last == null || !last.equals(p)) {
			track.add(p);
			footmark.add(p);
		}
	}
	
	private Point getLastFootmark() {
		return footmark.size() > 0 ? footmark.getLast() : null;
	}

}
