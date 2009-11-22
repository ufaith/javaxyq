/**
 * 
 */
package com.javaxyq.action;

import com.javaxyq.config.TalkConfig;
import com.javaxyq.core.DataStore;
import com.javaxyq.core.GameMain;
import com.javaxyq.event.PlayerAdapter;
import com.javaxyq.event.PlayerEvent;
import com.javaxyq.widget.Player;

/**
 * @author dewitt
 * 
 */
public class DefaultTalkAction extends PlayerAdapter {

	@Override
	public void talk(PlayerEvent evt) {
		String talkId = evt.getArguments();
		if (talkId == null)
			talkId = "default";
		Player player = evt.getPlayer();
		TalkConfig talkCfg = DataStore.getTalk(player.getId(), talkId);
		if (talkCfg != null) {
			GameMain.doTalk(player, talkCfg);
		}
	}
}
