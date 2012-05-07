package com.javaxyq.android.common.graph;

import java.util.LinkedList;

import com.javaxyq.android.common.graph.widget.Sprite;

import android.app.Activity;
import android.graphics.Point;

public class DefaultCharacter implements Character {
	
	private String id;
	
	/** 精灵 *///TODO private
	public Sprite sprite;
	
	/** 角色动作  */
	private String action = CharacterActions.STAND;
	
	private LinkedList<Point> footmark;
	
	private LinkedList<Point> track;
	
	private int direction;
	
	private Activity activity;
	
	public DefaultCharacter(Activity activity,String id) {
		this.activity = activity;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void initialize() {
		refresh();

	}
	
	synchronized public void refresh() {
		String resId = id + "-" + action;
		if(null == sprite || !sprite.getResId().equals(resId)) {
			sprite = SpriteFactory.getSprite(activity, id, action);
		}
		if(null != null && sprite.getDirection() != direction) {
			sprite.setDirection(direction);
			sprite.resetFrames();
		}
 		
	}

	@Override
	public void update(long elapsedTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub

	}

	@Override
	public Point getLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void moveTo(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveBy(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void walk() {
		// TODO Auto-generated method stub

	}

	@Override
	public void rush() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stand() {
		// TODO Auto-generated method stub

	}

	@Override
	public void turn(int direction) {
		// TODO Auto-generated method stub

	}

	@Override
	public void turn() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getDirection() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void action(String key) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isMoveOn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setMoveon(boolean moveon) {
		// TODO Auto-generated method stub

	}

}
