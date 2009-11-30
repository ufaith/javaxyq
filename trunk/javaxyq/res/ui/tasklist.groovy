/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-27
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */

package ui_script;

import java.awt.Color;
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
	
	private String taskItem;
	
	@Override
	public void initial(PanelEvent evt) {
		super.initial(evt);
		taskItem ='school';
		setAutoUpdate(true);
	}
	public void show_storyTask(ActionEvent evt) {
		this.taskItem = 'story';		def lblDetail = panel.findCompByName('lblTaskDetail');
		lblDetail.setText("#B��ǰû�о�������");
		def lblSchoolTask = panel.findCompByName('lblSchoolTask');
		def lblStoryTask = panel.findCompByName('lblStoryTask');
		lblSchoolTask.setForeground(Color.BLACK);
		lblStoryTask.setForeground(Color.BLUE);
	}
	public void show_schoolTask(ActionEvent evt) {
		this.taskItem = 'school';
		def tasks = TaskManager.instance.getTasksOfType(this.taskItem);
		def lblDetail = panel.findCompByName('lblTaskDetail');
		if(tasks && tasks.size()>0) {
			int times = tasks[0].get('times').toInteger();
			int rounds = tasks[0].get('rounds').toInteger();
			def status = tasks[0].finished?"��#R�����#B":'';
			def taskdesc = TaskManager.instance.desc(tasks[0]).replaceAll('#n','#B');
			lblDetail.setText("#B${taskdesc}#r(��ǰ#R${rounds}#B��#R${times}#B��${status})");
		}else {
			lblDetail.setText("#B��ǰû��ʦ����������ʦ����ȡ��");
		}
		def lblSchoolTask = panel.findCompByName('lblSchoolTask');
		def lblStoryTask = panel.findCompByName('lblStoryTask');		lblSchoolTask.setForeground(Color.BLUE);
		lblStoryTask.setForeground(Color.BLACK);	}

	public void update(PanelEvent evt) {
		this.invokeMethod("show_${taskItem}Task", null);
	}
}
