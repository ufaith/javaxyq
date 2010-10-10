/**
 * 
 */
package com.javaxyq.event;

import java.util.EventListener;

import com.javaxyq.model.Item;

/**
 * ��Ʒ�¼�������
 * @author dewitt
 *
 */
public interface ItemListener extends EventListener {

	/**
	 * ��Ʒ����ʼ��
	 * @param evt
	 */
	void itemInitialized(ItemEvent evt);
	
	/**
	 * ��Ʒ��ʹ��
	 * @param evt
	 */
	void itemUsed(ItemEvent evt);
	
	/**
	 * ��Ʒ������
	 * @param evt
	 */
	void itemDestroyed(ItemEvent evt);
}
