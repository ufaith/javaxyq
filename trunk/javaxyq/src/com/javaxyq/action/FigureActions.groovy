package com.javaxyq.action;

import com.javaxyq.util.AudioPlayer;
import com.javaxyq.util.ClosureTask;
import com.javaxyq.util.MP3Player;
import java.awt.Color;
import java.util.Timer;

import com.javaxyq.core.DialogFactory;
import com.javaxyq.core.DataStore;
import com.javaxyq.core.GameMain;
import com.javaxyq.graph.Button;
import com.javaxyq.graph.Label;
import com.javaxyq.graph.Panel;
import com.javaxyq.model.*;
import com.javaxyq.widget.*;
import com.javaxyq.ui.UIHelper;

public class FigureActions extends BaseAction {
	
	private Timer timer ;
	
	private def template;
	
	public void updateLabels(Panel panel) {
		def labels = panel.getComponents().findAll {it instanceof Label };
		def attrs = GameMain.getPlayer().getData().getProperties();
		attrs['levelExp'] = DataStore.getLevelExp(attrs.level);
		if(!template) {
			def engine = new groovy.text.SimpleTemplateEngine()
			def vars = []
			for(Label label in labels) {
				vars.add(label.textTpl)
			}
			this.template = engine.createTemplate(vars.join(';'))
		}
		def values = template.make(attrs).toString().split(';')
		def i=0;
		for(Label label in labels) {
			label.text = values[i++];
		}
	}
    public void doAction(ActionEvent e) {
        String cmd = e.getCommand();
        Object source = e.getSource();
        System.out.println("FigureActions:" + cmd);
		switch(cmd) {
			case ~/.*\.initial$/:
				final Panel panel = source;
				this.updateLabels(panel);
				this.timer = new Timer();
				this.timer.schedule(new ClosureTask({
					updateLabels(panel);
				}), 50, 1000);
			break;
			case ~/.*\.dispose$/:
				this.timer.cancel();
			break;
			case ~/.*\.��������$/:
				Button button = (Button) source;
				Panel panel = (Panel) button.getParent();
			
				Player player = GameMain.getPlayer()
	   			PlayerVO vo = player.getData();
				vo.assignPoints.each{ key, value ->
					vo[key] += value;
					vo.assignPoints[key] = 0
					Button btn = panel.getComponentByName("-$key");
					btn.setEnabled(false);
					Label label = panel.getComponentByName("text$key");
					label.setForeground(Color.BLACK)
				}
				DataStore.recalcProperties(player.getData());
				break;
			case ~/.*\.����$/:
	        	Player player = GameMain.getPlayer()
	        	PlayerVO vo = player.getData();
				def levelExp = DataStore.getLevelExp(vo.level);
				if(vo.exp >= levelExp) {
					UIHelper.prompt( '��ϲ�㣬������~~���Ͱɣ�', 2000)
					player.playEffect('����');
					MP3Player.play("resources/sound/����.mp3");
					vo.level += 1
					vo.���� += 1
					vo.ħ�� += 1
					vo.���� += 1
					vo.���� += 1
					vo.���� += 1
					vo.potentiality += 5;
					vo.exp -= levelExp;
					DataStore.recalcProperties(vo);
					
					Button button = (Button) source;
					Panel panel = (Panel) button.getParent();
					updateLabels(panel)
				}else {
					//���鲻��
					println "��ľ���û�ﵽ��������ľ���"
					UIHelper.prompt( '��ľ���û�ﵽ��������ľ���', 2000)
					//MP3Player.play()
				}
				break;
			case ~/.*\.\+.*$/: //+����
				String attr = cmd.substring( cmd.length()-2 )
				Button button = (Button) source;
				Panel panel = (Panel) button.getParent();
				Label label = (Label) panel.getComponentByName("text$attr");
				PlayerVO vo = GameMain.getPlayer().getData();
				if(vo.potentiality>0) {
					vo.assignPoints[attr] +=1;
					vo.potentiality -=1;
					label.setForeground(Color.RED);
					Button btn = panel.getComponentByName("-$attr");
					btn.setEnabled(true)
				}
				updateLabels(panel);
				break;
			case ~/.*\.\-.*$/: //-����
				String attr = cmd.substring( cmd.length()-2 )
				Button button = (Button) source;
				Panel panel = (Panel) button.getParent();
				Label label = (Label) panel.getComponentByName("text$attr");
		 		PlayerVO vo = GameMain.getPlayer().getData();
				if(vo.assignPoints[attr]>0) {
		 			vo.assignPoints[attr] -= 1;
		 			vo.potentiality += 1;
				}
				if (vo.assignPoints[attr] == 0) {
		 			label.setForeground(Color.BLACK);
					Button btn = panel.getComponentByName("-$attr");
					btn.setEnabled(false)
				}
				updateLabels(panel);
				break;
		}
		

    }
}

