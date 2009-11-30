package com.javaxyq.model;

import java.awt.Point;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

class PlayerVO  implements Serializable{
	
	private static final long serialVersionUID = 986420253388692309L;
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
	
	String state;
	int direction;
	int[] colorations;
	Point sceneLocation;

	public String toString() {
		def props = this.getProperties();
		props.remove('class');
		props.remove('metaClass');
		return props.toString();
	}

	protected void writeObject(ObjectOutputStream s)
	throws IOException
	{
		s.writeObject(assignPoints);
		s.writeUTF(character);
		s.writeObject(colorations);
		s.writeObject(createDate);
		s.writeInt(deposit);
		s.writeInt(direction);
		s.writeLong(exp);
		s.writeFloat(growthRate);
		s.writeInt(hp);
		s.writeUTF(id);
		s.writeInt(level);
		s.writeInt(maxHp);
		s.writeInt(maxMp);
		s.writeInt(max活力);
		s.writeInt(max体力);
		s.writeInt(money);
		s.writeInt(mp);

		s.writeUTF(name);
		s.writeInt(popularity);
		s.writeInt(potentiality);
		s.writeObject(sceneLocation);
		s.writeInt(sp);
		s.writeUTF(state);
		s.writeUTF(title);
		s.writeInt(tmpMaxHp);
		s.writeInt(tmp躲避);
		s.writeInt(tmp防御);
		s.writeInt(tmp灵力);
		s.writeInt(tmp命中);
		s.writeInt(tmp伤害);
		s.writeInt(tmp速度);

		s.writeUTF(帮派);
		s.writeInt(帮派贡献);
		s.writeInt(躲避);
		s.writeInt(防御);
		s.writeInt(活力);
		s.writeInt(力量);
		s.writeInt(灵力);
		s.writeUTF(门派);
		s.writeInt(门派贡献);
		s.writeInt(敏捷);
		s.writeInt(命中);
		s.writeInt(魔力);
		s.writeInt(耐力);
		s.writeInt(伤害);
		s.writeInt(速度);
		s.writeInt(体力);
		s.writeInt(体质);
		
	}
	
	/**
	 * Reconstitute this object from a stream (i.e., deserialize it).
	 */
	protected void readObject(ObjectInputStream s)
	throws IOException, ClassNotFoundException
	{
		assignPoints=s.readObject();
		character=s.readUTF();
		colorations=s.readObject();
		createDate=s.readObject();
		deposit=s.readInt();
		direction=s.readInt();
		exp=s.readLong();
		growthRate=s.readFloat();
		hp=s.readInt();
		id=s.readUTF();
		level=s.readInt();
		maxHp=s.readInt();
		maxMp=s.readInt();
		max活力=s.readInt();
		max体力=s.readInt();
		money=s.readInt();
		mp=s.readInt();

		name=s.readUTF();
		popularity=s.readInt();
		potentiality=s.readInt();
		sceneLocation=s.readObject();
		sp=s.readInt();
		state=s.readUTF();
		title=s.readUTF();
		tmpMaxHp=s.readInt();
		tmp躲避=s.readInt();
		tmp防御=s.readInt();
		tmp灵力=s.readInt();
		tmp命中=s.readInt();
		tmp伤害=s.readInt();
		tmp速度=s.readInt();

		帮派=s.readUTF();
		帮派贡献=s.readInt();
		躲避=s.readInt();
		防御=s.readInt();
		活力=s.readInt();
		力量=s.readInt();
		灵力=s.readInt();
		门派=s.readUTF();
		门派贡献=s.readInt();
		敏捷=s.readInt();
		命中=s.readInt();
		魔力=s.readInt();
		耐力=s.readInt();
		伤害=s.readInt();
		速度=s.readInt();
		体力=s.readInt();
		体质=s.readInt();
	}
	
}
