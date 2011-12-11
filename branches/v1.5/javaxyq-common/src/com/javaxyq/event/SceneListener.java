package com.javaxyq.event;



import java.util.EventListener;

/**
 * @author dewitt
 * 
 */
public interface SceneListener extends EventListener {

	/**
	 * ��ʼ������
	 * 
	 * @param e
	 */
	void onInit(SceneEvent e);

	/**
	 * �����������
	 * 
	 * @param e
	 */
	void onLoad(SceneEvent e);

	/**
	 * �˳�����
	 * 
	 * @param e
	 */
	void onUnload(SceneEvent e);
}
