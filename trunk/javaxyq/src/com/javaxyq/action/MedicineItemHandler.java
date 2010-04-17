package com.javaxyq.action;

import java.util.Map;

import com.javaxyq.data.DataStore;
import com.javaxyq.event.ItemEvent;
import com.javaxyq.event.ItemListener;
import com.javaxyq.model.MedicineItem;
import com.javaxyq.util.MP3Player;
import com.javaxyq.widget.Player;
//potion  
public class MedicineItemHandler implements ItemListener{

	@Override
	public void itemUsed(ItemEvent evt) {
		MP3Player.play("resources/sound/use_item.mp3");
		MedicineItem item = (MedicineItem) evt.getItem();
		Player player = evt.getPlayer();
		if(item.hp!=0 && item.mp!=0) {
			player.playEffect("add_hpmp");
			DataStore.addHp(player,item.hp);		
			DataStore.addMp(player,item.mp);		
		}else if(item.hp != 0) {
			player.playEffect("add_hp");
			DataStore.addHp(player,item.hp);		
		}else if(item.mp != 0) {
			player.playEffect("add_mp");
			DataStore.addMp(player,item.mp);
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