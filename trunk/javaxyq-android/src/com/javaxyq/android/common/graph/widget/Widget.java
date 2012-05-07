package com.javaxyq.android.common.graph.widget;

import java.io.Serializable;

/**
 * ��Ϸ��ʹ�õ�UI�����ӿ�
 * @author chenyang
 *
 */
public interface Widget extends Serializable {
	
	void draw(int x,int y);//TODO ȱ��һ������
	
	void draw(int x,int y, int width , int height);//TODO ȱ��һ������
	
	void draw();//TODO ȱ����������
	
	void fadeIn(long t);

    void fadeOut(long t);

    void dispose();

    int getWidth();

    int getHeight();
    
    boolean contains(int x, int y);
	
}
