package com.javaxyq.action;

import com.javaxyq.event.ItemEvent;
import com.javaxyq.event.ItemListener;
import com.javaxyq.model.Item;
import com.javaxyq.util.MP3Player;
import com.javaxyq.core.*;

class MedicineItemAction implements ItemListener{

	@Override
	public void itemUsed(ItemEvent evt) {
		MP3Player.play("resources/sound/使用药品.mp3");
		Item item = evt.getItem();
		def player = evt.getPlayer();
		def eff = item.getEfficacyParams();
		if(eff.hp && eff.mp) {
			player.playEffect("add_hpmp");
			DataStore.addHp(player,eff.hp);		
			DataStore.addMp(player,eff.mp);		
		}else if(eff.hp) {
			player.playEffect("add_hp");
			DataStore.addHp(player,eff.hp);		
		}else if(eff.mp) {
			player.playEffect("add_mp");
			DataStore.addMp(player,eff.mp);
		}
		//恢复气血
		//恢复法力
		//疗伤
		//播放效果动画及声音
		
		item.amount --;
	}
	
	@Override
	public void itemDestroyed(ItemEvent evt) {
	}
	
	@Override
	public void itemInitialized(ItemEvent evt) {
	}
	
}