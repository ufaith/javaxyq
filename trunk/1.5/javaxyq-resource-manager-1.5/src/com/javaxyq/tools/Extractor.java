/**
 * 
 */
package com.javaxyq.tools;

import java.io.File;
import java.util.Map;

/**
 * ��Դ��ȡ��
 * @author gongdewei
 * @date 2011-8-1 create
 */
public interface Extractor {

	/**
	 * ��FileObject ��ȡ��ָ��Ŀ¼
	 * @param fileObject ��������
	 * @param dir ��ŵ�Ŀ¼
	 * @param options ����
	 */
	void extract(FileObject fileObject, File dir, Map<String, ?> options);
}
