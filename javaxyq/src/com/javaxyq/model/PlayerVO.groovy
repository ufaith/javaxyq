package com.javaxyq.model;

class PlayerVO {
	public PlayerVO() {
		
	}
	public PlayerVO(String id, String name, String character) {
		this.id = id;
		this.name = name;
		this.character = character;
	}
	
	String id = "Undefined";
	String name ="player";
	String character = "0000";
	int level = 0;
	int hp = 83;
	int maxHp = 100;
	int tmpMaxHp = 100;
	int mp = 20;
	int maxMp = 50;
	//愤怒值
	int sp = 0;
	int 活力 = 10;
	int max活力 = 50;
	int 体力 = 10;
	int max体力 = 50;
	
	long exp = 56;
	int money = 0;
	int deposit = 0;
	Date createDate = new Date();
	
	int 力量 = 10;
	int 体质 = 10;
	int 魔力 = 10;
	int 耐力 = 10;
	int 敏捷 = 10;
	/** 潜力*/
	int potentiality = 0;
	
	int 命中 = 62;
	int 伤害 = 50;
	int 防御 = 16
	int 速度 = 11;
	int 躲避 = 21;
	int 灵力 = 19;
	int tmp命中 = 0;
	int tmp伤害 = 0;
	int tmp防御 = 0
	int tmp速度 = 0;
	int tmp躲避 = 0;
	int tmp灵力 = 0;

	//称谓$
	String title = "≮潇洒哥≯";
	//人气
	int popularity = 800;
	//帮派
	String 帮派 = "逍遥阁";
	//门派
	String 门派 = "大唐官府";
	//帮派贡献
	int 帮派贡献 = 0;  
	//门派贡献
	int 门派贡献= 0;

	//待分配点数
	Map assignPoints = [体质:0,魔力:0,力量:0,耐力:0,敏捷:0];

	//成长率
	float growthRate; 

	public String toString() {
		def props = this.getProperties();
		props.remove('class');
		props.remove('metaClass');
		return props.toString();
	}	
}
