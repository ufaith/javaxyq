/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-3-4
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.event;

import java.util.EventListener;

/**
 * ������Դ���ؼ�����
 * @author gongdewei
 * @date 2010-3-4 create
 */
public interface DownloadListener extends EventListener {

	/**
	 * ���ؿ�ʼ
	 * @param e
	 */
	void downloadStarted(DownloadEvent e);
	/**
	 * �������
	 * @param e
	 */
	void downloadCompleted(DownloadEvent e);
	/**
	 * �����ж�
	 * @param e
	 */
	void downloadInterrupted(DownloadEvent e);
	/**
	 * ���ؽ��ȸ���
	 * @param e
	 */
	void downloadUpdate(DownloadEvent e);
}
