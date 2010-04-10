/**
 * 
 */
package com.javaxyq.action;

import java.util.List;

import com.javaxyq.config.TalkConfig;
import com.javaxyq.core.GameMain;
import com.javaxyq.data.DataStore;
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
		System.out.println("talk: "+evt);
		//任务处理
		List tasks = TaskManager.instance.getTasksFor(player.getName());
		if(tasks!=null && !tasks.isEmpty()) {
			//TODO 多个任务时生成任务选择列表
			if(tasks.size()>1) {
				System.out.println("npc["+player.getName()+"]有"+tasks.size()+"个任务");
				for (int i = 0; i < tasks.size(); i++) {
					System.out.println(tasks.get(i));
				}
			}
			Task task =  (Task) tasks.get(0);
			if(!task.isFinished() && task.isAutoSpark()) {
				task.set("target",player);
				//如果任务处理完成，则返回，否则继续常规对话
				if(TaskManager.instance.process(task))return;
			}
		}
		
		//对话
		String talkId = evt.getArguments();
		if (talkId == null)
			talkId = "default";
		TalkConfig talkCfg = DataStore.getTalk(npcId, talkId);
		if (talkCfg != null) {
			GameMain.doTalk(player, talkCfg);
		}
	}
}
