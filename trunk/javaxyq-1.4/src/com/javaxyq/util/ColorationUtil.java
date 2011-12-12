/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import com.javaxyq.core.GameMain;
import com.javaxyq.core.Toolkit;
import com.javaxyq.widget.Animation;
import com.javaxyq.widget.Sprite;

/**
 * @author ����ΰ
 * @history 2009-10-16 ����ΰ �½�
 */
public class ColorationUtil {

    public static WASDecoder getDecoder(String resId, String profile) throws IllegalStateException, IOException {
        WASDecoder decoder = new WASDecoder();
        InputStream is = Toolkit.getInputStream(resId);
        decoder.load(is);
        decoder.loadColorationProfile(profile);
        return decoder;
    }

    public static void recreate(Sprite sprite, String profile) {
        try {
            WASDecoder decoder = getDecoder(sprite.getResId(), profile);
            //����Ⱦɫ��ʽ
            int partCount = decoder.getSectionCount()-1;
            for (int i = 0; i < partCount; i++) {
                decoder.coloration(i, sprite.getColoration(i));
            }

            int centerX, centerY;
            centerX = decoder.getRefPixelX();
            centerY = decoder.getRefPixelY();
            int s = decoder.getAnimCount();
            int f = decoder.getFrameCount();
            sprite.clearAnimations();
            for (int i = 0; i < s; i++) {
                Animation anim = new Animation();
                for (int j = 0; j < f;) {
                    try {
                        int index = i * f + j;
                        BufferedImage frame = decoder.getFrameImage(index);
                        int delay = decoder.getDelay(index);
                        int duration = delay * GameMain.ANIMATION_INTERVAL;
                        anim.addFrame(frame, duration, centerX, centerY);
                    } catch (Exception e) {
                        //e.printStackTrace();
                        System.out.printf("create anima error: i=%s,j=%s,spriteCount=%s,frameCount=%s,total=%s\n", i,
                            j, s, f, decoder.getFrames().size());
                    }
//                    j += delay;
                    j++;
                }
                sprite.addAnimation(anim);
            }
            sprite.setDirection(sprite.getDirection());
        } catch (IllegalStateException e) {
            System.err.println("Re-Create ����ʧ�ܣ�");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Re-Create ����ʧ�ܣ�");
            e.printStackTrace();
        }
    }

}
