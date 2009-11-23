/**
 * 
 */
package com.javaxyq.action;

import java.util.List;

import com.javaxyq.config.TalkConfig;
import com.javaxyq.core.DataStore;
import com.javaxyq.core.GameMain;
import com.javaxyq.event.PlayerAdapter;
import com.javaxyq.event.PlayerEvent;
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
		//������
		List tasks = TaskManager.instance.getTasksFor(player.getName());
		if(tasks!=null && !tasks.isEmpty()) {
			//TODO �������ʱ��������ѡ���б�
			Task task =  (Task) tasks.get(0);
			task.add("target",player);
			//�����������ɣ��򷵻أ������������Ի�
			if(TaskManager.instance.process(task))return;
		}
		
		//�Ի�
		String talkId = evt.getArguments();
		if (talkId == null)
			talkId = "default";
		TalkConfig talkCfg = DataStore.getTalk(npcId, talkId);
		if (talkCfg != null) {
			GameMain.doTalk(player, talkCfg);
		}
	}
}
