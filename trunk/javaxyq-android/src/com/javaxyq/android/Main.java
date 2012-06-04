package com.javaxyq.android;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.javaxyq.android.common.graph.Character;
import com.javaxyq.android.common.graph.DefaultCharacter;
import com.javaxyq.android.core.GameCanvas;

public class Main extends Activity {
	
	/** 游戏角色 */
	private Character character;
	
	/** 游戏角色ID */
	private String characterId = "0010";
	
	private boolean moveon = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.gameCanvasLayout);
        
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        GameCanvas gameCanvas = new GameCanvas(this);
        gameCanvas.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 1200));
        this.createCharacter();
        gameCanvas.setCharacter(character);
        linearLayout.addView(gameCanvas);
        LinearLayout toolBar = new LinearLayout(this);
        toolBar.setOrientation(LinearLayout.HORIZONTAL);
        toolBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 80));
        toolBar.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout.addView(toolBar);
        CheckBox walking = new CheckBox(this);
        walking.setTextColor(Color.BLACK);
        walking.setText("连续行走");
        walking.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        toolBar.addView(walking);
        Button walkButton = new Button(this);
        walkButton.setText("行走");
        walkButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        walkButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				character.walk();
				character.setMoveon(moveon);
				
			}
		});
        toolBar.addView(walkButton);
        Button stopButton = new Button(this);
        stopButton.setText("停止");
        stopButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        stopButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				character.setMoveon(false);
			}
		});
        toolBar.addView(stopButton);
        Button changeDirectionButton = new Button(this);
        changeDirectionButton.setText("转向");
        changeDirectionButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        toolBar.addView(changeDirectionButton);
        changeDirectionButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				character.turn();
				System.out.println("hello");
			}
		});
        Button roundButton = new Button(this);
        roundButton.setText("旋转");
        roundButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        toolBar.addView(roundButton);

    }
    
    private Character createCharacter() {
    	character = new DefaultCharacter(this, characterId);
    	character.initialize();
    	character.moveTo(100, 150);
    	return character;
    }
}