/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-4-25
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */

package com.javaxyq.action;

import com.javaxyq.model.Option;
import com.javaxyq.core.*;
import com.javaxyq.event.*;
import com.javaxyq.config.*;


/**
 * @author gongdewei
 * @date 2010-4-25 create
 */
class n1004 extends PlayerAdapter {
	
    public void talk(PlayerEvent evt) {
    	System.out.println("talk: "+this.getClass().getName());
    	Option[] options = new Option[3];
    	options[0] = new Option("��ׯ��","transport","1146 12 11");
    	options[1] = new Option("������","transport","1506 62 21");
    	options[2] = new Option("�һ�����Χ����","close");
    	GameMain.doTalk(evt.getPlayer(), '�͹�,��Ҫ������ȥ��?',options);
    }
	
}
