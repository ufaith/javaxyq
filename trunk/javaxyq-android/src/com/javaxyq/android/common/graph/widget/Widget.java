package com.javaxyq.android.common.graph.widget;

import java.io.Serializable;

/**
 * 游戏中使用的UI构建接口
 * 
 * @author chenyang
 * 
 */
public interface Widget extends Serializable {

	void draw(int x, int y);// TODO 缺少一个参数

	void draw(int x, int y, int width, int height);// TODO 缺少一个参数

	void draw();// TODO 缺少两个参数

	void fadeIn(long t);

	void fadeOut(long t);

	void dispose();

	int getWidth();

	int getHeight();

	boolean contains(int x, int y);

}
