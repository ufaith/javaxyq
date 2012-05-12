package com.javaxyq.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.javaxyq.android.common.graph.Character;
import com.javaxyq.android.core.GameCanvas;

public class Main extends Activity {
	
	/** 游戏角色 */
	private Character character;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.gameCanvasLayout);
        GameCanvas gameCanvas = new GameCanvas(this);
        gameCanvas.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        linearLayout.addView(gameCanvas);

    }
    
    private Character createCharacter() {
    	return null;
    }
}