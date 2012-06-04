package com.javaxyq.android.common.graph.widget;

import java.io.Serializable;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * 游戏中使用的UI构建接口
 * 
 * @author chenyang
 * 
 */
public interface Widget extends Serializable {

	void draw(Canvas canvas,int x, int y);

	void draw(Canvas canbas,int x, int y, int width, int height);

	void draw(Canvas canvas,Rect rect);

	void fadeIn(long t);

	void fadeOut(long t);

	void dispose();

	int getWidth();

	int getHeight();

	boolean contains(int x, int y);

}
