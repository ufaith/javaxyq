/**
 * 
 */
package com.javaxyq.event;

import java.util.EventListener;

/**
 * @author dewitt
 * 
 */
public interface PlayerStateListener extends EventListener {

	/**
	 * ����״̬�ı䣬����Ѫ��ħ��������ֵ���ȼ�
	 * 
	 * @param evt
	 */
	void stateChanged(PlayerStateEvent evt);

}
