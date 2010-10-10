/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.core;


import com.javaxyq.config.ImageConfig;
import com.javaxyq.widget.Sprite;
import com.javaxyq.widget.SpriteImage;

/**
 * @author ����ΰ
 * @history 2008-5-31 ����ΰ �½�
 */
public class ResourceStore {
	private static ResourceStore store = new ResourceStore();

	private ResourceStore() {
	}

	public static ResourceStore getInstance() {
		return store;
	}

	public SpriteImage createImage(ImageConfig cfg) {
		Sprite s = SpriteFactory.loadSprite(cfg.getPath());
		int width = cfg.getWidth();
		int height = cfg.getHeight();
		if (width > 0 && height > 0) {
			return new SpriteImage(s, cfg.getX(), cfg.getY(), width, height);
		}
		return new SpriteImage(s, cfg.getX(), cfg.getY());
	}

	/**
	 * ��ȡ��ɫ����Ƭ���Ի�ʱʹ�ã�
	 * @param characterId
	 * @return
	 */
	public Sprite findPhoto(String characterId) {
		if(characterId.compareTo("0012")<=0) {			
			return SpriteFactory.loadSprite("/wzife/photo/hero/" + characterId + ".tcp");
		}
		return SpriteFactory.loadSprite("/wzife/photo/npc/" + characterId + ".tcp");
	}

}
