package com.javaxyq.model;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PlayerVO  implements Serializable{
	
	private static final long serialVersionUID = 986420253388692309L;
	
	public String id = "Undefined";
	public String name ="player";
	public String character = "0000";
	public int level = 0;
	public int hp = 83;
	public int maxHp = 100;
	public int tmpMaxHp = 100;
	public int mp = 20;
	public int maxMp = 50;
	//愤怒值
	public int sp = 0;
	public int 活力 = 10;
	public int max活力 = 50;
	public int 体力 = 10;
	public int max体力 = 50;
	
	public long exp = 56;
	public int money = 0;
	public int deposit = 0;
	public Date createDate = new Date();
	
	public int 力量 = 10;
	public int 体质 = 10;
	public int 魔力 = 10;
	public int 耐力 = 10;
	public int 敏捷 = 10;
	/** 潜力*/
	public int potentiality = 0;
	
	public int 命中 = 62;
	public int 伤害 = 50;
	public int 防御 = 16;
	public int 速度 = 11;
	public int 躲避 = 21;
	public int 灵力 = 19;
	public int tmp命中 = 0;
	public int tmp伤害 = 0;
	public int tmp防御 = 0;
	public int tmp速度 = 0;
	public int tmp躲避 = 0;
	public int tmp灵力 = 0;

	//称谓$
	public String title = "≮潇洒哥≯";
	//人气
	public int popularity = 800;
	//帮派
	public String 帮派 = "逍遥阁";
	//门派
	public String 门派 = "大唐官府";
	//帮派贡献
	public int 帮派贡献 = 0;  
	//门派贡献
	public int 门派贡献= 0;

	//待分配点数
	public Map assignPoints = null;//[体质:0,魔力:0,力量:0,耐力:0,敏捷:0];

	//成长率
	public float growthRate; 
	
	public String state;
	public int direction;
	public int[] colorations;
	public Point sceneLocation;
	
	public PlayerVO() {
		assignPoints = new HashMap();
		assignPoints.put("体质", 0);
		assignPoints.put("魔力", 0);
		assignPoints.put("力量", 0);
		assignPoints.put("耐力", 0);
		assignPoints.put("敏捷", 0);
	}
	public PlayerVO(String id, String name, String character) {
		this.id = id;
		this.name = name;
		this.character = character;
	}

	/**
	 * @param data
	 */
	public PlayerVO(PlayerVO data) {
		this.id = data.id;
		this.name = data.name;
		this.character = data.character;
		this.level = data.level;
		this.hp = data.hp;
		this.maxHp = data.maxHp;
		this.tmpMaxHp = data.tmpMaxHp;
		this.mp = data.mp;
		this.maxMp = data.maxMp;
		this.sp = data.sp;
		this.活力 = data.活力;
		this.max活力 = data.max活力;
		this.体力 = data.体力;
		this.max体力 = data.max体力;
		this.exp = data.exp;
		this.money = data.money;
		this.deposit = data.deposit;
		this.createDate = new Date();//create date
		this.力量 = data.力量;
		this.体质 = data.体质;
		this.魔力 = data.魔力;
		this.耐力 = data.耐力;
		this.敏捷 = data.敏捷;
		this.potentiality = data.potentiality;
		this.命中 = data.命中;
		this.伤害 = data.伤害;
		this.防御 = data.防御;
		this.速度 = data.速度;
		this.躲避 = data.躲避;
		this.灵力 = data.灵力;
		this.tmp命中 = data.tmp命中;
		this.tmp伤害 = data.tmp伤害;
		this.tmp防御 = data.tmp防御;
		this.tmp速度 = data.tmp速度;
		this.tmp躲避 = data.tmp躲避;
		this.tmp灵力 = data.tmp灵力;
		this.title = data.title;
		this.popularity = data.popularity;
		this.帮派 = data.帮派;
		this.门派 = data.门派;
		this.帮派贡献 = data.帮派贡献;
		this.门派贡献 = data.门派贡献;
		this.assignPoints = new HashMap(data.assignPoints);
		this.growthRate = data.growthRate;
		this.state = data.state;
		this.direction = data.direction;
		this.colorations = data.colorations;
		this.sceneLocation = new Point(data.sceneLocation);
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
		assignPoints=(Map) s.readObject();
		character=s.readUTF();
		colorations=(int[]) s.readObject();
		createDate=(Date) s.readObject();
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
		sceneLocation=(Point) s.readObject();
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
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlayerVO [assignPoints=");
		builder.append(assignPoints);
		builder.append(", character=");
		builder.append(character);
		builder.append(", colorations=");
		builder.append(Arrays.toString(colorations));
		builder.append(", createDate=");
		builder.append(createDate);
		builder.append(", deposit=");
		builder.append(deposit);
		builder.append(", direction=");
		builder.append(direction);
		builder.append(", exp=");
		builder.append(exp);
		builder.append(", growthRate=");
		builder.append(growthRate);
		builder.append(", hp=");
		builder.append(hp);
		builder.append(", id=");
		builder.append(id);
		builder.append(", level=");
		builder.append(level);
		builder.append(", maxHp=");
		builder.append(maxHp);
		builder.append(", maxMp=");
		builder.append(maxMp);
		builder.append(", max体力=");
		builder.append(max体力);
		builder.append(", max活力=");
		builder.append(max活力);
		builder.append(", money=");
		builder.append(money);
		builder.append(", mp=");
		builder.append(mp);
		builder.append(", name=");
		builder.append(name);
		builder.append(", popularity=");
		builder.append(popularity);
		builder.append(", potentiality=");
		builder.append(potentiality);
		builder.append(", sceneLocation=");
		builder.append(sceneLocation);
		builder.append(", sp=");
		builder.append(sp);
		builder.append(", state=");
		builder.append(state);
		builder.append(", title=");
		builder.append(title);
		builder.append(", tmpMaxHp=");
		builder.append(tmpMaxHp);
		builder.append(", tmp伤害=");
		builder.append(tmp伤害);
		builder.append(", tmp命中=");
		builder.append(tmp命中);
		builder.append(", tmp灵力=");
		builder.append(tmp灵力);
		builder.append(", tmp躲避=");
		builder.append(tmp躲避);
		builder.append(", tmp速度=");
		builder.append(tmp速度);
		builder.append(", tmp防御=");
		builder.append(tmp防御);
		builder.append(", 伤害=");
		builder.append(伤害);
		builder.append(", 体力=");
		builder.append(体力);
		builder.append(", 体质=");
		builder.append(体质);
		builder.append(", 力量=");
		builder.append(力量);
		builder.append(", 命中=");
		builder.append(命中);
		builder.append(", 帮派=");
		builder.append(帮派);
		builder.append(", 帮派贡献=");
		builder.append(帮派贡献);
		builder.append(", 敏捷=");
		builder.append(敏捷);
		builder.append(", 活力=");
		builder.append(活力);
		builder.append(", 灵力=");
		builder.append(灵力);
		builder.append(", 耐力=");
		builder.append(耐力);
		builder.append(", 躲避=");
		builder.append(躲避);
		builder.append(", 速度=");
		builder.append(速度);
		builder.append(", 门派=");
		builder.append(门派);
		builder.append(", 门派贡献=");
		builder.append(门派贡献);
		builder.append(", 防御=");
		builder.append(防御);
		builder.append(", 魔力=");
		builder.append(魔力);
		builder.append("]");
		return builder.toString();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCharacter() {
		return character;
	}
	public void setCharacter(String character) {
		this.character = character;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public int getMaxHp() {
		return maxHp;
	}
	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}
	public int getTmpMaxHp() {
		return tmpMaxHp;
	}
	public void setTmpMaxHp(int tmpMaxHp) {
		this.tmpMaxHp = tmpMaxHp;
	}
	public int getMp() {
		return mp;
	}
	public void setMp(int mp) {
		this.mp = mp;
	}
	public int getMaxMp() {
		return maxMp;
	}
	public void setMaxMp(int maxMp) {
		this.maxMp = maxMp;
	}
	public int getSp() {
		return sp;
	}
	public void setSp(int sp) {
		this.sp = sp;
	}
	public int get活力() {
		return 活力;
	}
	public void set活力(int 活力) {
		this.活力 = 活力;
	}
	public int getMax活力() {
		return max活力;
	}
	public void setMax活力(int max活力) {
		this.max活力 = max活力;
	}
	public int get体力() {
		return 体力;
	}
	public void set体力(int 体力) {
		this.体力 = 体力;
	}
	public int getMax体力() {
		return max体力;
	}
	public void setMax体力(int max体力) {
		this.max体力 = max体力;
	}
	public long getExp() {
		return exp;
	}
	public void setExp(long exp) {
		this.exp = exp;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getDeposit() {
		return deposit;
	}
	public void setDeposit(int deposit) {
		this.deposit = deposit;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public int get力量() {
		return 力量;
	}
	public void set力量(int 力量) {
		this.力量 = 力量;
	}
	public int get体质() {
		return 体质;
	}
	public void set体质(int 体质) {
		this.体质 = 体质;
	}
	public int get魔力() {
		return 魔力;
	}
	public void set魔力(int 魔力) {
		this.魔力 = 魔力;
	}
	public int get耐力() {
		return 耐力;
	}
	public void set耐力(int 耐力) {
		this.耐力 = 耐力;
	}
	public int get敏捷() {
		return 敏捷;
	}
	public void set敏捷(int 敏捷) {
		this.敏捷 = 敏捷;
	}
	public int getPotentiality() {
		return potentiality;
	}
	public void setPotentiality(int potentiality) {
		this.potentiality = potentiality;
	}
	public int get命中() {
		return 命中;
	}
	public void set命中(int 命中) {
		this.命中 = 命中;
	}
	public int get伤害() {
		return 伤害;
	}
	public void set伤害(int 伤害) {
		this.伤害 = 伤害;
	}
	public int get防御() {
		return 防御;
	}
	public void set防御(int 防御) {
		this.防御 = 防御;
	}
	public int get速度() {
		return 速度;
	}
	public void set速度(int 速度) {
		this.速度 = 速度;
	}
	public int get躲避() {
		return 躲避;
	}
	public void set躲避(int 躲避) {
		this.躲避 = 躲避;
	}
	public int get灵力() {
		return 灵力;
	}
	public void set灵力(int 灵力) {
		this.灵力 = 灵力;
	}
	public int getTmp命中() {
		return tmp命中;
	}
	public void setTmp命中(int tmp命中) {
		this.tmp命中 = tmp命中;
	}
	public int getTmp伤害() {
		return tmp伤害;
	}
	public void setTmp伤害(int tmp伤害) {
		this.tmp伤害 = tmp伤害;
	}
	public int getTmp防御() {
		return tmp防御;
	}
	public void setTmp防御(int tmp防御) {
		this.tmp防御 = tmp防御;
	}
	public int getTmp速度() {
		return tmp速度;
	}
	public void setTmp速度(int tmp速度) {
		this.tmp速度 = tmp速度;
	}
	public int getTmp躲避() {
		return tmp躲避;
	}
	public void setTmp躲避(int tmp躲避) {
		this.tmp躲避 = tmp躲避;
	}
	public int getTmp灵力() {
		return tmp灵力;
	}
	public void setTmp灵力(int tmp灵力) {
		this.tmp灵力 = tmp灵力;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getPopularity() {
		return popularity;
	}
	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}
	public String get帮派() {
		return 帮派;
	}
	public void set帮派(String 帮派) {
		this.帮派 = 帮派;
	}
	public String get门派() {
		return 门派;
	}
	public void set门派(String 门派) {
		this.门派 = 门派;
	}
	public int get帮派贡献() {
		return 帮派贡献;
	}
	public void set帮派贡献(int 帮派贡献) {
		this.帮派贡献 = 帮派贡献;
	}
	public int get门派贡献() {
		return 门派贡献;
	}
	public void set门派贡献(int 门派贡献) {
		this.门派贡献 = 门派贡献;
	}
	public Map getAssignPoints() {
		return assignPoints;
	}
	public void setAssignPoints(Map assignPoints) {
		this.assignPoints = assignPoints;
	}
	public float getGrowthRate() {
		return growthRate;
	}
	public void setGrowthRate(float growthRate) {
		this.growthRate = growthRate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public int[] getColorations() {
		return colorations;
	}
	public void setColorations(int[] colorations) {
		this.colorations = colorations;
	}
	public Point getSceneLocation() {
		return sceneLocation;
	}
	public void setSceneLocation(Point sceneLocation) {
		this.sceneLocation = sceneLocation;
	}

}
