/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-14
 * http://javaxyq.googlecode.com
 * kylixs@qq.com
 */
package com.javaxyq.battle;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.javaxyq.model.PlayerVO;
import com.javaxyq.util.ClassUtil;
import com.javaxyq.widget.Player;

/**
 * @author dewitt
 * @date 2009-11-14 create
 */
public class BattleCalculator {
	
	private Random random = new Random();
	private Map datas = [:]; 
	private Map defends = [:] ;
	private List ownsideTeam;
	private List adversaryTeam;
	
	public List calc(List cmdlist,List ownsideTeam,List adversaryTeam,Map params) {
		List results = [];
		defends.clear();
		datas.clear();
		this.ownsideTeam = ownsideTeam;
		this.adversaryTeam = adversaryTeam;
		ownsideTeam.each{player ->
			def vo = new PlayerVO();
			ClassUtil.copyFields(player.getData(),vo);
			datas[player] = vo;
		}
		adversaryTeam.each{player ->
			def vo = new PlayerVO();
			ClassUtil.copyFields(player.getData(),vo);
			datas[player] = vo;
		}
		//�������ٶ�����
		cmdlist = cmdlist.sort{cmd -> cmd.source.data.�ٶ� + cmd.source.data.tmp�ٶ�}.reverse()
		println "cmdlist: $cmdlist"
		for (int i = 0; i < cmdlist.size(); i++) {
			def cmd = cmdlist[i];
			def result = this.invokeMethod(cmd.cmd,cmd);
			if(result)results <<result;
			if(ownsideTeam.size()==0 || adversaryTeam.size() == 0) {
				//���һ���ĳ�Աȫ���뿪ս������ս������
				break;
			}
		}
		return results;
	}
	
	private Command attack(Command cmd) {
		PlayerVO src = datas[cmd.getSource()];
		PlayerVO target = datas[cmd.getTarget()];
		if(!target) {
			println "${cmd.target.name}�Ѿ��˳�ս����������Ч��"
			return;
		}
		if(src.hp <=0) {
			println "${src.name} �ѵ��£��޷���������"
			return;
		}else if(target.hp <= 0) {
			println "${target.name}�ѵ��£�${src.name}�Ĺ�����Ч"
			return;
		}
		int hitpoints = 0;
		//�ж��Ƿ�����
		float d = src.���� - target.���;
		if(d<0)d = 0;
		boolean hit = (random.nextFloat()<= 0.7+d*1.0/src.����);
		cmd.add("hit",hit);
		//�����˺�ֵ
		if(hit) {
			d = src.�˺� - target.����;
			if(d<0)d=0;
			hitpoints = 0.05 * src.�˺� + (0.9+ (random.nextInt(20)-9)*1.0/100)*d;
			if(defends[target]) {
				hitpoints *= 0.5;
				cmd.add("defend",true);
			}
			cmd.add("hitpoints",hitpoints);
			target.hp -= hitpoints;
			if(target.hp <0)target.hp = 0;
			cmd.add("die",true);
		}
		return cmd;
	}
	
	private Command magic(Command cmd) {
		PlayerVO src = datas[cmd.getSource()];
		PlayerVO target = datas[cmd.getTarget()];
		int usedmp = cmd.getInt('mp');
		if(!target) {
			println "${cmd.target.name}�Ѿ��˳�ս����������Ч��"
			return;
		}
		if(src.hp <=0) {
			println "${src.name} �ѵ��£��޷���������"
			return;
		}else if(target.hp <= 0) {
			println "${target.name}�ѵ��£�${src.name}�Ĺ�����Ч"
			return;
		}else if(src.mp < usedmp) {
			println "${src.name} �������㣬ʩ�ŷ���ʧ�ܣ�"
			return;
		}
		src.mp -= usedmp;
		int hitpoints = 0;
		//�ж��Ƿ�����
		float d = src.���� - target.����;
		if(d<0)d = 0;
		boolean hit = (random.nextFloat()<= 0.9+d*1.0/src.����);
		cmd.add("hit",hit);
		//TODO ���㷨���˺�ֵ
		if(hit) {
			//���������˺�ϵ��
			int basehit = cmd.getInt('basehit');
			hitpoints =0.5*src.����*basehit+ 0.5*(1+ (random.nextInt(20)-9)*1.0/100)*d* basehit;
			cmd.add("hitpoints",hitpoints);
			target.hp -= hitpoints;
			if(target.hp <0)target.hp = 0;
			cmd.add("die",true);
		}
		return cmd;
	}

	private Command defend(Command cmd) {
		defends[datas[cmd.source]] = true;
		return cmd;
	}
	
	private Command runaway(Command cmd) {
		boolean success = random.nextFloat()<0.9; 
		cmd.add("runaway",success);
		if(success) {
			datas.remove(cmd.source);
			ownsideTeam.remove(cmd.source);
			adversaryTeam.remove(cmd.source);
		}
		return cmd;
	}

	private Command item(Command cmd) {
		PlayerVO src = datas[cmd.getSource()];
		PlayerVO target = datas[cmd.getTarget()];
		if(!target) {
			println "${cmd.target.name}�Ѿ��˳�ս����������Ч��"
			return;
		}
		if(src.hp <=0) {
			println "${src.name} �ѵ��£��޷�ʹ�õ���"
			return;
		}else if(target.hp <= 0) {
			println "${target.name}�ѵ��£��޷�����ҩ��"
			return;
		}		
		def item = cmd.get('item');
		def efficacy = item.getEfficacyParams();
		if(efficacy.hp) {
			target.hp += efficacy.hp;
			cmd.add('hp',efficacy.hp);
		}
		if(efficacy.mp) {
			target.mp += efficacy.mp;
			cmd.add('mp',efficacy.mp);
		}
		return cmd;
	}
	
}
