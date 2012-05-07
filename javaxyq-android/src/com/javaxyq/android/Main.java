package com.javaxyq.android;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import com.javaxyq.android.common.graph.Character;
import com.javaxyq.android.common.graph.DefaultCharacter;
import com.javaxyq.android.common.graph.tcp.WASDecoder;

public class Main extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        DefaultCharacter character = new DefaultCharacter(this,"0010");
        character.initialize();
        ImageView image = (ImageView) findViewById(R.id.image);
        Bitmap bitmap = character.sprite.animations.get(4).getFrames().get(4).getImage();
        image.setImageBitmap(bitmap);
        image.setAlpha(255);
        System.out.println("chychy:"+image);
    }
}