package com.javaxyq.android.common.graph;

import android.graphics.Point;

/**
 * ��ɫ
 * @author chenyang
 *
 */
public interface Character {
	
	String getId();
	
	boolean isReady();
	
	void initialize();
	
	/**
	 * ���¶���
	 * @param elapsedTime
	 */
	void update(long elapsedTime);
	
	/**
	 * ���Ƶ������ϣ�ȱ�ٲ�����
	 */
	void draw(); 
	// TODO ��Ҫ������
	
	/**
	 * UI��������λ��
	 * @return
	 */
	Point getLocation();
	
	/**
	 * ����ɫ�ƶ���ָ������
	 * @param x
	 * @param y
	 */
	void moveTo(int x,int y);
	
	/**
	 * �ƶ�����
	 * @param x
	 * @param y
	 */
	void moveBy(int x,int y);
	
	/**
	 * ����
	 */
	void walk();
	
	/**
	 * ����
	 */
	void rush();
	
	/**
	 * վ��
	 */
	void stand();
	
	/**
	 * ת��
	 * @param direction
	 */
	void turn(int direction);
	
	void turn();
	
	int getDirection();
	
	/**
	 * �������ﶯ��
	 * @param key
	 */
	void action(String key);
	
	/**
	 * �Ƿ�����ƶ�
	 * @return
	 */
	boolean isMoveOn();
	
	/**
	 * �����Ƿ������ƶ�
	 * @param moveon
	 */
	void setMoveon(boolean moveon);
	
}
