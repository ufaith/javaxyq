/**
 * 
 */
package com.javaxyq.widget;

import java.awt.Graphics;
import java.awt.Point;

/**
 * ��ɫ
 * @author gongdewei
 * @date 2011-7-24 create
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
	 * ���Ƶ�������
	 * @param g
	 * @param x
	 * @param y
	 */
	void draw(Graphics g);
	
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
	void moveTo(int x, int y);
	
	/**
	 * �ƶ�����
	 * @param x
	 * @param y
	 */
	void moveBy(int x, int y);
	
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
