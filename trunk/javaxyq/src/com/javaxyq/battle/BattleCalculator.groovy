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
		//按人物速度排序
		cmdlist = cmdlist.sort{cmd -> cmd.source.data.速度 + cmd.source.data.tmp速度}.reverse()
		println "cmdlist: $cmdlist"
		for (int i = 0; i < cmdlist.size(); i++) {
			def cmd = cmdlist[i];
			def result = this.invokeMethod(cmd.cmd,cmd);
			if(result)results <<result;
			if(ownsideTeam.size()==0 || adversaryTeam.size() == 0) {
				//如果一方的成员全部离开战场，则战斗结束
				break;
			}
		}
		return results;
	}
	
	private Command attack(Command cmd) {
		PlayerVO src = datas[cmd.getSource()];
		PlayerVO target = datas[cmd.getTarget()];
		if(!target) {
			println "${cmd.target.name}已经退出战场，攻击无效。"
			return;
		}
		if(src.hp <=0) {
			println "${src.name} 已倒下，无法发动攻击"
			return;
		}else if(target.hp <= 0) {
			println "${target.name}已倒下，${src.name}的攻击无效"
			return;
		}
		int hitpoints = 0;
		//判断是否命中
		float d = src.命中 - target.躲避;
		if(d<0)d = 0;
		boolean hit = (random.nextFloat()<= 0.7+d*1.0/src.命中);
		cmd.add("hit",hit);
		//计算伤害值
		if(hit) {
			d = src.伤害 - target.防御;
			if(d<0)d=0;
			hitpoints = 0.05 * src.伤害 + (0.9+ (random.nextInt(20)-9)*1.0/100)*d;
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
			println "${cmd.target.name}已经退出战场，攻击无效。"
			return;
		}
		if(src.hp <=0) {
			println "${src.name} 已倒下，无法发动攻击"
			return;
		}else if(target.hp <= 0) {
			println "${target.name}已倒下，${src.name}的攻击无效"
			return;
		}else if(src.mp < usedmp) {
			println "${src.name} 法力不足，施放法术失败！"
			return;
		}
		src.mp -= usedmp;
		int hitpoints = 0;
		//判断是否命中
		float d = src.灵力 - target.灵力;
		if(d<0)d = 0;
		boolean hit = (random.nextFloat()<= 0.9+d*1.0/src.灵力);
		cmd.add("hit",hit);
		//TODO 计算法术伤害值
		if(hit) {
			//法术基本伤害系数
			int basehit = cmd.getInt('basehit');
			hitpoints =0.5*src.灵力*basehit+ 0.5*(1+ (random.nextInt(20)-9)*1.0/100)*d* basehit;
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
			println "${cmd.target.name}已经退出战场，操作无效。"
			return;
		}
		if(src.hp <=0) {
			println "${src.name} 已倒下，无法使用道具"
			return;
		}else if(target.hp <= 0) {
			println "${target.name}已倒下，无法接受药物"
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
