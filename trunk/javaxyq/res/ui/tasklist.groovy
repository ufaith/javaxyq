/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-27
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */

package ui_script;

import java.awt.Desktop;
import java.net.URI;

import com.javaxyq.task.TaskManager;
import com.javaxyq.core.GameMain;
import com.javaxyq.event.*;
import com.javaxyq.ui.*;

/**
 * ������ʾ�Ի���ű�
 * @author dewitt
 * @date 2009-11-27 create
 */
class tasklist extends PanelHandler {
	
	@Override
	public void initial(PanelEvent evt) {
		super.initial(evt);
		
	}
	public void showStoryTask(ActionEvent evt) {
		def lblDetail = panel.findCompByName('lblTaskDetail');		lblDetail.setText("#B��ǰû�о�������");
		
	}
	public void showSchoolTask(ActionEvent evt) {
		def tasks = TaskManager.instance.getTasksOfType('school');
		def lblDetail = panel.findCompByName('lblTaskDetail');
		if(tasks && tasks.size()>0) {
			int times = tasks[0].get('times').toInteger();
			int rounds = tasks[0].get('rounds').toInteger();
			def status = tasks[0].finished?"��#R�����#B":'';
			def taskdesc = TaskManager.instance.desc(tasks[0]).replaceAll('#n','#B');
			lblDetail.setText("#B${taskdesc}#r(��ǰ#R${rounds}#B��#R${times}#B��${status})");
		}else {
			lblDetail.setText("#B��ǰû��ʦ����������ʦ����ȡ��");
		}	}

	
}
