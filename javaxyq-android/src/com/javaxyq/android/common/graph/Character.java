package com.javaxyq.android.common.graph;

import android.graphics.Point;

/**
 * 角色
 * @author chenyang
 *
 */
public interface Character {
	
	String getId();
	
	boolean isReady();
	
	void initialize();
	
	/**
	 * 更新动画
	 * @param elapsedTime
	 */
	void update(long elapsedTime);
	
	/**
	 * 绘制到画布上（缺少参数）
	 */
	void draw(); 
	// TODO 需要补参数
	
	/**
	 * UI绘制坐标位置
	 * @return
	 */
	Point getLocation();
	
	/**
	 * 将角色移动到指定坐标
	 * @param x
	 * @param y
	 */
	void moveTo(int x,int y);
	
	/**
	 * 移动增量
	 * @param x
	 * @param y
	 */
	void moveBy(int x,int y);
	
	/**
	 * 行走
	 */
	void walk();
	
	/**
	 * 奔跑
	 */
	void rush();
	
	/**
	 * 站立
	 */
	void stand();
	
	/**
	 * 转向
	 * @param direction
	 */
	void turn(int direction);
	
	void turn();
	
	int getDirection();
	
	/**
	 * 设置人物动作
	 * @param key
	 */
	void action(String key);
	
	/**
	 * 是否继续移动
	 * @return
	 */
	boolean isMoveOn();
	
	/**
	 * 设置是否连续移动
	 * @param moveon
	 */
	void setMoveon(boolean moveon);
	
}
