/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.widget;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;

/**
 * ��Ϸ��ʹ�õ�UI�����ӿ�
 * 
 * @author ����ΰ
 * @history 2008-5-29 ����ΰ �½�
 */
public interface Widget extends Serializable {
    void draw(Graphics g, int x, int y);

    void draw(Graphics g, int x, int y, int width, int height);

    void draw(Graphics g, Rectangle rect);

    void fadeIn(long t);

    void fadeOut(long t);

    void dispose();

    int getWidth();

    int getHeight();
    
    boolean contains(int x, int y);

}
