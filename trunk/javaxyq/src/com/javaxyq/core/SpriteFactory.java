package com.javaxyq.core;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Map;
import java.util.WeakHashMap;

import com.javaxyq.util.WASDecoder;
import com.javaxyq.widget.Animation;
import com.javaxyq.widget.Sprite;

/**
 * Sprite ������<br>
 * 
 * @author Langlauf
 * @date
 */
public class SpriteFactory {

	/** <sprite id, sprite instance> */
	// private static Map<String, Sprite> sprites = new WeakHashMap<String,
	// Sprite>();

	public static Sprite loadCursor(String filename) {
		return loadSprite("/resources/cursor/" + filename);
	}

	public static Sprite loadSprite(String filename) {
		return loadSprite(filename, null);
	}

	public static Sprite createSprite(InputStream is) throws Exception {
		WASDecoder decoder = new WASDecoder();
		decoder.load(is);
		return createSprite(decoder);
	}

	private static Sprite createSprite(WASDecoder decoder) {
		int centerX, centerY;
		centerX = decoder.getRefPixelX();
		centerY = decoder.getRefPixelY();
		Sprite sprite = new Sprite(decoder.getWidth(), decoder.getHeight(), centerX, centerY);
		int spriteCount = decoder.getAnimCount();
		int frameCount = decoder.getFrameCount();
		for (int i = 0; i < spriteCount; i++) {
			Animation anim = new Animation();
			anim.setWidth(decoder.getWidth());
			anim.setHeight(decoder.getHeight());
			for (int j = 0; j < frameCount;) {
				try {
					int index = i * frameCount + j;
					BufferedImage frame = decoder.getFrame(index);
					int delay = decoder.getDelay(index);
					int duration = delay * GameMain.ANIMATION_INTERVAL;
					anim.addFrame(frame, duration, centerX, centerY);
					j += delay;
				} catch (Exception e) {
					if (e instanceof IndexOutOfBoundsException) {
						System.err.println("�����������frameCount����ʵ��ֵ  " + frameCount + " > " + anim.getFrames().size());
						break;
					}
					System.err.println("����������Դ�ļ�ʧ�ܣ�");
					e.printStackTrace();
					j++;
				}
			}
			sprite.addAnimation(anim);
		}
		sprite.setDirection(0);
		return sprite;
	}

	public static Animation loadAnimation(String filename, int index) {
		Sprite s = loadSprite(filename);
		return (s == null) ? null : s.getAnimation(index);
	}

	public static Animation loadAnimation(String filename) {
		return loadAnimation(filename, 0);
	}

	public static Image loadImage(String filename) {
		if (filename.endsWith(".was")||filename.endsWith(".tcp")) {
			Sprite s = loadSprite(filename);
			return (s == null) ? null : s.getImage();
		}
		return Toolkit.createImageFromResource(filename);
	}

	/**
	 * ������Ӱ�ľ���
	 * 
	 * @return
	 */
	public static Sprite loadShadow() {
		return loadSprite("/shape/char/shadow.tcp");
	}

	public static Sprite loadSprite(String filename, int[] colorations) {
		if (filename == null || filename.trim().length()==0)
			return null;
		try {
			WASDecoder decoder = new WASDecoder();
			InputStream inputStream = Toolkit.getInputStream(filename);
			if(inputStream == null) {
				System.err.println("Warning: �Ҳ����������Դ�ļ�!"+filename);
				return null;
			}
			decoder.load(inputStream);
			if(colorations!=null) {
				String pp = filename.replaceFirst("(\\w)*.tcp", "00.pp");
				System.out.println("pp: "+pp);//XXX
				decoder.loadColorationProfile(pp);
				decoder.coloration(colorations);
			}
			Sprite s = createSprite(decoder);
			if(colorations!=null) {
				s.setColorations(colorations);
			}
			return s;
		} catch (Exception e) {
			System.err.println("���ؾ���ʧ��:" + filename);
			e.printStackTrace();
		}
		return null;
	}

}
