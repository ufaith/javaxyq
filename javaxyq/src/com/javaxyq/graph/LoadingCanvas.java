/**
 * 
 */
package com.javaxyq.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.util.Random;

import com.javaxyq.core.GameMain;
import com.javaxyq.util.MP3Player;

/**
 * @author dewitt
 *
 */
public class LoadingCanvas extends Canvas {
	private String loadingText = "";
	private Image contentImage;


	public void setContent(Image img) {
		this.contentImage = img;
	}

	public void setLoading(String text) {
		this.loadingText = text;
	}


	public void showImage(Image img, long t) {
		this.fadeOut(300);
		// wait
		waitForFading();
		this.loadingText = "";
		this.setContent(img);
		this.fadeIn(200);
		waitForFading();
		// wait ,delay
		synchronized (FADE_LOCK) {
			try {
				Thread.sleep(t);
			} catch (InterruptedException e) {
			}
		}

	}
	
	public void draw(Graphics g, long elapsedTime) {
		if(contentImage!=null) {
			g.drawImage(contentImage, 0, 0, null);
		}else {
			//g.setColor(Color.LIGHT_GRAY);
			//g.fillRect(0, 0, getWidth(), getHeight());
			g.clearRect(0, 0, getWidth(), getHeight());
		}
		// draw fade
		g.setColor(new Color(0, 0, 0, alpha));
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setFont(GameMain.TEXT_FONT);
		drawCursor(g,elapsedTime);
		drawDebug(g);
		drawDownloading(g);

		g.setColor(Color.WHITE);
		g.setFont(new Font("»ªÎÄÐÂÎº", Font.PLAIN, 20));
		g.drawString(loadingText, 250, 420);
		//System.out.println("draw loading ...");
	}

	protected String getMusic() {
		Random rand = new Random();
		String[] files = new String[] {"1091","1514","1070","1193"};
		return ("music/"+files[rand.nextInt(files.length)]+".mp3");
	}

}
