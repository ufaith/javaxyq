/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-4-25
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */

package npc;

import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.event.PlayerAdapter;
import com.javaxyq.event.PlayerEvent;
import com.javaxyq.model.Option;


/**
 * @author gongdewei
 * @date 2010-4-25 create
 */
public class n1006 extends PlayerAdapter {
	
    public void talk(PlayerEvent evt) {
    	System.out.println("talk: "+this.getClass().getName());
    	Option[] options = new Option[] {
	    	new Option("����","open","buy"),
	    	new Option("���","close")};
    	ApplicationHelper.getApplication().doTalk(evt.getPlayer(), "С���и��ֲ�ҩ��������������͹������ѡ��",options);
    }
	
}
