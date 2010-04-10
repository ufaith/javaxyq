package com.javaxyq.action;

import java.util.Map;

import com.javaxyq.data.DataStore;
import com.javaxyq.event.ItemEvent;
import com.javaxyq.event.ItemListener;
import com.javaxyq.model.MedicineItem;
import com.javaxyq.util.MP3Player;
import com.javaxyq.widget.Player;
//potion  
public class MedicineItemAction implements ItemListener{

	@Override
	public void itemUsed(ItemEvent evt) {
		MP3Player.play("resources/sound/use_item.mp3");
		MedicineItem item = (MedicineItem) evt.getItem();
		Player player = evt.getPlayer();
		Map eff = item.getEfficacyParams();
		Integer hpval = (Integer) eff.get("hp");
		Integer mpval = (Integer) eff.get("mp");
		if(hpval!=null && mpval!=null) {
			player.playEffect("add_hpmp");
			DataStore.addHp(player,hpval);		
			DataStore.addMp(player,mpval);		
		}else if(hpval != null) {
			player.playEffect("add_hp");
			DataStore.addHp(player,hpval);		
		}else if(mpval != null) {
			player.playEffect("add_mp");
			DataStore.addMp(player,mpval);
		}
		//�ָ���Ѫ
		//�ָ�����
		//����
		//����Ч������������
		
		item.amount --;
	}
	
	@Override
	public void itemDestroyed(ItemEvent evt) {
	}
	
	@Override
	public void itemInitialized(ItemEvent evt) {
	}
	
}