/**
 * 
 */
package com.javaxyq.core;

import com.javaxyq.graph.Panel;

/**
 * @author dewitt
 *
 */
public interface DialogBuilder {

	/**
	 * �����Ի���ʵ��
	 * @param id �Ի���id
	 * @param res ui��Դ�����ļ�
	 * @return
	 */
	Panel createDialog(String id, String res);
}
