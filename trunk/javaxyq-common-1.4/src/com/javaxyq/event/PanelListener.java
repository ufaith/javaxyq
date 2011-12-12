/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-26
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.event;

import java.util.EventListener;

/**
 * ����¼�������
 * 
 * @author dewitt
 * @date 2009-11-26 create
 */
public interface PanelListener extends EventListener {

	/**
	 * ��ʼ����ÿ����ʾʱ���ã�
	 * 
	 * @param evt
	 */
	void initial(PanelEvent evt);

	/**
	 * ע�����ر�ʱ���ã�
	 * 
	 * @param evt
	 */
	void dispose(PanelEvent evt);

	/**
	 * ������������
	 * 
	 * @param evt
	 */
	void update(PanelEvent evt);
	/**
	 * �رմ���
	 * @param evt
	 */
	void close(ActionEvent evt);
	/**
	 * ��ʾ������Ϣ
	 * @param evt
	 */
	void help(ActionEvent evt);

	/**
	 * ��ť���¡����Label���¼�
	 */
	void actionPerformed(ActionEvent evt);

}
