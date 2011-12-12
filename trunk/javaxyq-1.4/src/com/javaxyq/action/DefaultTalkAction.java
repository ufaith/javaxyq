/**
 * 
 */
package com.javaxyq.action;

import java.util.List;

import com.javaxyq.config.TalkConfig;
import com.javaxyq.core.GameMain;
import com.javaxyq.core.ScriptManager;
import com.javaxyq.data.DataStore;
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
		List tasks = TaskManager.instance.getTasksFor(player.getName());
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
				if(TaskManager.instance.process(task))return;
			}
		}
		//npc�¼�
		PlayerListener listener = ScriptManager.getInstance().findListener(npcId);
		if(listener!=null) {//�����¼�
			listener.talk(evt);
		}else {//û���¼�����Ĭ�϶Ի�
			String chat = DataStore.findChat(npcId);
			if (chat != null) {
				GameMain.doTalk(player, chat);
			}
		}
	}
}
