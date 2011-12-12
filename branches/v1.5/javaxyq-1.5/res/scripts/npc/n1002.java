/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-4-25
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */

package npc;

import java.util.List;
import java.util.Random;

import com.javaxyq.core.BaseApplication;
import com.javaxyq.core.Context;
import com.javaxyq.core.GameMain;
import com.javaxyq.event.PlayerAdapter;
import com.javaxyq.event.PlayerEvent;
import com.javaxyq.model.Option;
import com.javaxyq.model.Task;
import com.javaxyq.task.TaskManager;
import com.javaxyq.ui.UIHelper;
import com.javaxyq.widget.Player;


/**
 * @author gongdewei
 * @date 2010-4-25 create
 */
public class n1002 extends PlayerAdapter {
	
    public void talk(PlayerEvent evt) {
    	System.out.println("talk: "+this.getClass().getName());
    	String chat = "��ׯ���ǵ��ɷ�Դ�أ�ׯ�ڵ��˲ι������ͬ�٣���������֮��ϡ������#r����Ϊʦ���£�";
    	Option[] options = new Option[3];
    	options[0] = new Option("Ϊʦ��������","applyfor_task");
    	options[1] = new Option("ѧϰ����","learn_skill");
    	options[2] = new Option("ͽ������","close");
    	Option result = doTalk(evt.getPlayer(),chat,options);
		if(result!=null && "applyfor_task".equals(result.getAction())) {
			applyfor_task(evt);
		}else if(result!=null && "learn_skill".equals(result.getAction())) {
			learn_skill(evt);
		}
    	
    }
	
    public void applyfor_task(PlayerEvent evt) {
    	TaskManager taskManager = application.getTaskManager();
		List<Task> currTasks = taskManager.getTasksOfType("school");
		if(currTasks!=null && currTasks.size()>0) {
			Task currTask = currTasks.get(0);
			if(!currTask.isFinished() && !currTask.isAutoSpark() && !currTask.getSubtype().equals("patrol")) {//���Զ�������ɵ�����
				taskManager.process(currTask);
			}
			if(currTask.isFinished()) {//���������
				taskManager.remove(currTask);
				Player player = context.getPlayer();
				dataManager.addMoney(player,currTask.getMoney());
				dataManager.addExp(player, currTask.getExp());
				String chat = "ͽ�������ˣ�Ϊʦ������#R"+currTask.getExp()+"#n�����#R"+currTask.getMoney()+"#n��Ǯ������Ŭ����";
				int times = currTask.getInt("times");
				if(times==10) {
					int rounds = currTask.getInt("rounds");
					//���⽱��
					String[] items = {"�첻��","�����ֻ�","�ɺ���","��¶Ϊ˪","����","�ܵ�"};
					String item = items[new Random().nextInt(items.length)];
					dataManager.addItemToPlayer(player,dataManager.createItem(item));
					chat += "#r����˵�"+rounds+"��ʦ�����񣬶��⽱����һ��#R"+item+"#n��";
				}
				doTalk(context.getTalker(),chat);				
			}else {//����δ���
				String chat ="�㻹���������������ظ���������Ҫȡ����ǰ������";
		    	Option[] options = new Option[2];
		    	options[0] = new Option("�ǵģ���Ҫȡ��","cancel_task");
		    	options[1] = new Option("��ȥ�������","close");
		    	Option result = doTalk(context.getTalker(),chat,options);
				if("cancel_task".equals(result.getAction())) {
					//ȡ��δ�������
					taskManager.remove(currTask);
					doTalk(context.getTalker(),"���������ȡ����");
				}
			}
		}else {//û��δ��ɵ�ʦ������
			Random rand = new Random();
			String[] subtypes = {"deliver","lookfor","patrol"};
			String subtype = subtypes[rand.nextInt(subtypes.length)];
			String sender = "��Ԫ����";
			Task task = taskManager.create("school",subtype, sender);
			taskManager.add(task);
			String desc = taskManager.desc(task);
			doTalk(context.getTalker(),desc);
		}
    	
    }
    
    public void learn_skill(PlayerEvent evt) {
    	Player player = context.getPlayer();
    	if(player.getData().level < 10) {
    		doTalk(context.getTalker(),"��ĸ�����ǳ����Щ����������Ϊʦ�ɣ�");
    	}else {
    		helper.showDialog("main_skill");
    	}
    }
}
