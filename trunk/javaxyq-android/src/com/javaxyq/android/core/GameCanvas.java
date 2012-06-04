package com.javaxyq.android.core;

import java.security.acl.LastOwnerException;
import java.util.Timer;
import java.util.TimerTask;

import com.javaxyq.android.common.graph.Character;
import com.javaxyq.android.common.graph.DefaultCharacter;
import com.javaxyq.android.common.graph.widget.Animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.view.View;

public class GameCanvas extends View {

	/** 游戏角色 */
	private Character character;
	
	private Canvas canvas;
	
	private long lastTime;
	
	private int n = 0;
	
	private Handler handler;

	public Character getCharacter() {
		return character;
	}

	public void setCharacter(Character character) {
		this.character = character;
	}

	public GameCanvas(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		if(this.canvas == null) {
			this.canvas = canvas;
		}
		canvas.drawColor(Color.BLACK);
		character.draw(canvas);
		if(null == handler) {
			handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					if(msg.what == 0x123) {
						drawCanvas();
					}
					invalidate();
					
				}
			};
			new Timer().schedule(new TimerTask() {
				
				@Override
				public void run() {
					handler.sendEmptyMessage(0x123);
					
				}
			}, 0, 300);
		}

	}
	
	private synchronized void drawCanvas() {
		long currTime = System.currentTimeMillis();
		if(lastTime == 0) {
			lastTime = currTime;
		}
		long elapsedTime = currTime - lastTime;
		lastTime = currTime;
		if(null != this.canvas ) {//TODO 添加双缓冲
			this.draw(this.canvas, elapsedTime);
		}
				
	}
	
	public synchronized void draw(Canvas canvas,long elapsedTime) {
		//TODO clear
		if(character != null) {
			character.update(elapsedTime);
			character.draw(canvas);
		}
	}
}
