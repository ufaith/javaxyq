package com.javaxyq.android.core;

import com.javaxyq.android.common.graph.Character;
import com.javaxyq.android.common.graph.DefaultCharacter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class GameCanvas extends View {

	/** 游戏角色 */
	private Character character;

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

		Paint paint = new Paint();
		canvas.drawColor(Color.BLACK);
		DefaultCharacter character = new DefaultCharacter(getContext(), "0010");
		character.initialize();
		Bitmap bitmap = character.sprite.animations.get(4).getFrames().get(4)
				.getImage();
		canvas.drawBitmap(bitmap, 300, 500, paint);

	}
}
