/**
 * 
 */
package com.javaxyq.action;

import java.util.List;

import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.core.DataManager;
import com.javaxyq.event.PlayerAdapter;
import com.javaxyq.event.PlayerEvent;
import com.javaxyq.event.PlayerListener;
import com.javaxyq.model.Task;
import com.javaxyq.task.TaskManager;
import com.javaxyq.widget.Player;

/**
 * @author dewitt
 * 
 */
public class DefaultTalkAction extends PlayerAdapter {

	@Override
	public void talk(PlayerEvent evt) {
		Player player = evt.getPlayer();
		String npcId = player.getId();
		System.out.println("talk: "+evt);
		//������
		TaskManager taskManager = ApplicationHelper.getApplication().getTaskManager();
		List tasks =  taskManager.getTasksFor(player.getName());
		if(tasks!=null && !tasks.isEmpty()) {
			//TODO �������ʱ��������ѡ���б�
			if(tasks.size()>1) {
				System.out.println("npc["+player.getName()+"]��"+tasks.size()+"������");
				for (int i = 0; i < tasks.size(); i++) {
					System.out.println(tasks.get(i));
				}
			}
			Task task =  (Task) tasks.get(0);
			if(!task.isFinished() && task.isAutoSpark()) {
				task.set("target",player);
				//�����������ɣ��򷵻أ������������Ի�
				if(taskManager.process(task))return;
			}
		}
		//npc�¼�
		PlayerListener listener = (PlayerListener) ApplicationHelper.getApplication().getScriptEngine().loadNPCScript(npcId);
		if(listener!=null) {//�����¼�
			listener.talk(evt);
		}else {//û���¼�����Ĭ�϶Ի�
			DataManager dataManager = ApplicationHelper.getApplication().getDataManager();
			String chat = dataManager.findChat(npcId);
			if (chat != null) {
				ApplicationHelper.getApplication().doTalk(player, chat);
			}
		}
	}
}
