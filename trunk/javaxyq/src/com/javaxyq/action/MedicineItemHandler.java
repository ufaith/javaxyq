package com.javaxyq.action;

import java.util.Map;

import com.javaxyq.data.DataStore;
import com.javaxyq.data.ItemInstance;
import com.javaxyq.event.ItemEvent;
import com.javaxyq.event.ItemListener;
import com.javaxyq.data.MedicineItem;
import com.javaxyq.util.MP3Player;
import com.javaxyq.widget.Player;
//potion  
public class MedicineItemHandler implements ItemListener{

	@Override
	public void itemUsed(ItemEvent evt) {
		MP3Player.play("resources/sound/use_item.mp3");
		ItemInstance iteminst = evt.getItem();
		if(iteminst.alterAmount(-1) == -1) {//如果成功删除一个数量
			MedicineItem item = (MedicineItem) iteminst.getItem();
			Player player = evt.getPlayer();
			if(item.getHp()!=0 && item.getMp()!=0) {
				player.playEffect("add_hpmp");
				DataStore.addHp(player,item.getHp());		
				DataStore.addMp(player,item.getMp());		
			}else if(item.getHp() != 0) {
				player.playEffect("add_hp");
				DataStore.addHp(player,item.getHp());		
			}else if(item.getMp() != 0) {
				player.playEffect("add_mp");
				DataStore.addMp(player,item.getMp());
			}
			//疗伤
			//播放效果动画及声音
		}
	}
	
	@Override
	public void itemDestroyed(ItemEvent evt) {
	}
	
	@Override
	public void itemInitialized(ItemEvent evt) {
	}
	
}