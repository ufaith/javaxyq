package ui_script;

import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelHandler;
import com.javaxyq.util.AudioPlayer;
import com.javaxyq.util.ClosureTask;
import com.javaxyq.util.MP3Player;
import java.awt.Color;
import java.util.Timer;

import com.javaxyq.core.DialogFactory;
import com.javaxyq.data.DataStore;
import com.javaxyq.core.GameMain;
import com.javaxyq.graph.Button;
import com.javaxyq.graph.Label;
import com.javaxyq.graph.Panel;
import com.javaxyq.model.*;
import com.javaxyq.widget.*;
import com.javaxyq.ui.*;

/**
 * ����״̬����
 * @author dewitt
 * @date 2009-11-26 create
 */
class player_status extends PanelHandler{
	
	private Timer timer ;
	private def template;
	public void actionPerformed(ActionEvent evt) {
		String cmd =evt.getCommand();
		//cmd�ĵ�һ��Ϊ����������������в���
		this.invokeMethod(cmd.split(" ")[0],evt); 
	}
	
	public void initial(PanelEvent evt) {
		super.initial(evt);
		println "initial��player_status "
		this.updateLabels(panel);
		this.timer = new Timer();
		this.timer.schedule(new ClosureTask({
			updateLabels(panel);
		}), 50, 1000);
	}
	
	public void dispose(PanelEvent evt) {
		println "dispose: player_status "
		this.timer.cancel();
	}
	
	public void assignPoints(ActionEvent evt) {
		Button button = (Button) evt.getSource();
		Panel panel = (Panel) button.getParent();
	
		Player player = GameMain.getPlayer()
		PlayerVO vo = player.getData();
		vo.assignPoints.each{ key, value ->
			vo[key] += value;
			vo.assignPoints[key] = 0
			Button btn = panel.findCompByName("-$key");
			btn.setEnabled(false);
			Label label = panel.findCompByName("text$key");
			label.setForeground(Color.BLACK)
		}
		DataStore.recalcProperties(player.getData());
	}

	public void level_up(ActionEvent evt) {
    	Player player = GameMain.getPlayer()
    	PlayerVO vo = player.getData();
		def levelExp = DataStore.getLevelExp(vo.level);
		if(vo.exp >= levelExp) {
			UIHelper.prompt( '��ϲ�㣬������~~���Ͱɣ�', 2000)
			player.playEffect('level_up');
			MP3Player.play("resources/sound/level_up.mp3");
			vo.level += 1
			vo.���� += 1
			vo.ħ�� += 1
			vo.���� += 1
			vo.���� += 1
			vo.���� += 1
			vo.potentiality += 5;
			vo.exp -= levelExp;
			DataStore.recalcProperties(vo);
			
			Button button = (Button) evt.getSource();
			Panel panel = (Panel) button.getParent();
			updateLabels(panel)
		}else {
			//���鲻��
			println "��ľ���û�ﵽ��������ľ���"
			UIHelper.prompt( '��ľ���û�ﵽ��������ľ���', 2000)
			//MP3Player.play()
		}
	}

	/**
	 * �������Ե�
	 * @param evt
	 */
	public void add_point(ActionEvent evt) {
		println "add point: $evt"
		//ȡ���� , like 'add_point ����'
		String attr = evt.command.split()[1];
		Label label = (Label) panel.findCompByName("text$attr");
		PlayerVO vo = GameMain.getPlayer().getData();
		if(vo.potentiality>0) {
			vo.assignPoints[attr] +=1;
			vo.potentiality -=1;
			label.setForeground(Color.RED);
			Button btn = panel.findCompByName("-$attr");
			btn.setEnabled(true)
		}
		updateLabels(panel);
	}
	
	/**
	 * �������Ե�
	 * @param evt
	 */
	public void subtract_point(ActionEvent evt) {
		println "subtract point: $evt"
		//ȡ���� , like 'add_point ����'
		String attr = evt.command.split()[1];
		Label label = (Label) panel.findCompByName("text$attr");
		PlayerVO vo = GameMain.getPlayer().getData();
		if(vo.assignPoints[attr]>0) {
			vo.assignPoints[attr] -= 1;
			vo.potentiality += 1;
		}
		if (vo.assignPoints[attr] == 0) {
			label.setForeground(Color.BLACK);
			Button btn = panel.findCompByName("-$attr");
			btn.setEnabled(false)
		}
		updateLabels(panel);
	}

	public void openSkills(ActionEvent evt) {
		UIHelper.showHideDialog('main_skill')
	}
	
	public void changeTitle(ActionEvent evt) {
		println "��ν"
	}
	
	private void updateLabels(Panel panel) {
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
	
}
