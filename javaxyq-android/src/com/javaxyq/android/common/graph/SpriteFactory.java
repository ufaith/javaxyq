package com.javaxyq.android.common.graph;

import java.io.InputStream;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import com.javaxyq.android.common.graph.tcp.WASDecoder;
import com.javaxyq.android.common.graph.widget.Animation;
import com.javaxyq.android.common.graph.widget.Sprite;

/**
 * 精灵工厂类<br>
 * @author chenyang
 *
 */
public class SpriteFactory {
	
	/**
	 * 动画播放每帧的间隔(ms)
	 */
	public static final int ANIMATION_INTERVAL = 100;
	
	public static Sprite getSprite(Activity activity, String id, String action) {
		Sprite sprite = loadSprite(activity, "shape/char/"+id+"/"+action+".tcp", null);
		sprite.setResId(id+"-"+action);
		return sprite;
	}
	
	public static Sprite loadSprite(Activity activity,String filename,int[] colorations) {
		
		if(null == filename || filename.trim().length() == 0) {
			return null;
		}
		
		try{
			WASDecoder decoder = new WASDecoder();
			AssetManager assetManager = activity.getAssets();
			InputStream in = assetManager.open(filename);
			decoder.load(in);
			if(null != colorations) {
				String ppFile = filename.replaceFirst("(\\w)*.tcp", "00.pp");
				decoder.loadColorationProfile(activity, ppFile);
				decoder.coloration(colorations);
			}
			Sprite s = createSprite(decoder);
			if(null != colorations) {
				s.setColorations(colorations);
			}
			return s;
		} catch(Exception e) {
			System.err.println("加载精灵失败:" + filename);
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static Sprite createSprite(WASDecoder decoder) {
		
		int centerX,centerY;
		centerX = decoder.getRefPixelX();
		centerY = decoder.getRefPixelY();
		Sprite sprite = new Sprite(decoder.getWidth(),decoder.getHeight(),centerX,centerY);
		int spriteCount = decoder.getAnimCount();
		int frameCount = decoder.getFrameCount();
		for(int i = 0 ; i < spriteCount ; i ++) {
			Animation anim = new Animation();
			anim.setWidth(decoder.getWidth());
			anim.setHeight(decoder.getHeight());
			for(int j = 0 ; j < frameCount;) {
				try {
					int index = i * frameCount + j;
					Bitmap frame = decoder.getFrame(index);
					int delay = decoder.getDelay(index);
					int duration = delay * ANIMATION_INTERVAL;
					anim.addFrame(frame, duration, centerX, centerY);
					j += delay;
				} catch (Exception e) {
					if (e instanceof IndexOutOfBoundsException) {
						System.err.println("解析精灵出错：frameCount大于实际值  " + frameCount + " > " + anim.getFrames().size());
						break;
					}
					System.err.println("解析精灵资源文件失败！");
					e.printStackTrace();
					j++;
				}
			}
			sprite.addAnimation(anim);
		}
		sprite.setDirection(0);
		return sprite;
	}
}
