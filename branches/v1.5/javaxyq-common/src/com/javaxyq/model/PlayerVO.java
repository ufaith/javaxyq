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
	public static final String STATE_STAND = "stand";
	public static final String STATE_WALK = "walk";
	
	public String id = "Undefined";
	public String name ="player";
	public String character = "0000";
	public int level = 0;
	public int hp = 83;
	public int maxHp = 100;
	public int tmpMaxHp = 100;
	public int mp = 20;
	public int maxMp = 50;
	//��ŭֵ
	public int sp = 0;
	/** ���� */
	public int energy = 10;
	public int maxEnergy = 50;
	/** ���� */
	public int stamina = 10;
	public int maxStamina = 50;
	
	public long exp = 56;
	public int money = 0;
	public int deposit = 0;
	public Date createDate = new Date();
	
	/** ���� */
	public int strength = 10;
	/** ����*/
	public int physique = 10;
	/** ħ�� */
	public int magic = 10;
	/** ���� */
	public int durability = 10;
	/** ���� */
	public int agility = 10;
	/** Ǳ��*/
	public int potentiality = 0;
	
	/** ���� */
	public int hitrate = 62;
	/** �˺�*/
	public int harm = 50;
	/** ����*/
	public int defense = 16;
	/** �ٶ�*/
	public int speed = 11;
	/** ���*/
	public int shun = 21;
	/** ����*/
	public int wakan = 19;
	/** tmp����*/
	public int tmpHitrate = 0;
	/** tmp�˺�*/
	public int tmpHarm = 0;
	/** tmp����*/
	public int tmpDefense = 0;
	/** tmp�ٶ�*/
	public int tmpSpeed = 0;
	/** tmp���*/
	public int tmpShun = 0;
	/** tmp����*/
	public int tmpWakan = 0;

	//��ν$
	public String title = "���������";
	//����
	public int popularity = 800;
	//����
	public String faction = "��ң��";
	//����
	public String school = "���ƹٸ�";
	//���ɹ���
	public int factionContribution = 0;  
	//���ɹ���
	public int schoolContribution= 0;

	//���������
	public Map<String,Integer> assignPoints = new HashMap<String, Integer>();//[����:0,ħ��:0,����:0,����:0,����:0];

	//�ɳ���
	public float growthRate; 
	
	public String state = STATE_STAND;
	public int direction;
	public int[] colorations;
	public Point sceneLocation = new Point();
	
	public PlayerVO() {
		assignPoints.put("physique", 0);
		assignPoints.put("magic", 0);
		assignPoints.put("strength", 0);
		assignPoints.put("durability", 0);
		assignPoints.put("agility", 0);
	}
	public PlayerVO(String id, String name, String character) {
		this.id = id;
		this.name = name;
		this.character = character;
		assignPoints.put("physique", 0);
		assignPoints.put("magic", 0);
		assignPoints.put("strength", 0);
		assignPoints.put("durability", 0);
		assignPoints.put("agility", 0);
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
		this.energy = data.energy;
		this.maxEnergy = data.maxEnergy;
		this.stamina = data.stamina;
		this.maxStamina = data.maxStamina;
		this.exp = data.exp;
		this.money = data.money;
		this.deposit = data.deposit;
		this.createDate = new Date();//create date
		this.strength = data.strength;
		this.physique = data.physique;
		this.magic = data.magic;
		this.durability = data.durability;
		this.agility = data.agility;
		this.potentiality = data.potentiality;
		this.hitrate = data.hitrate;
		this.harm = data.harm;
		this.defense = data.defense;
		this.speed = data.speed;
		this.shun = data.shun;
		this.wakan = data.wakan;
		this.tmpHitrate = data.tmpHitrate;
		this.tmpHarm = data.tmpHarm;
		this.tmpDefense = data.tmpDefense;
		this.tmpSpeed = data.tmpSpeed;
		this.tmpShun = data.tmpShun;
		this.tmpWakan = data.tmpWakan;
		this.title = data.title;
		this.popularity = data.popularity;
		this.faction = data.faction;
		this.school = data.school;
		this.factionContribution = data.factionContribution;
		this.schoolContribution = data.schoolContribution;
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
		s.writeInt(maxEnergy);
		s.writeInt(maxStamina);
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
		s.writeInt(tmpShun);
		s.writeInt(tmpDefense);
		s.writeInt(tmpWakan);
		s.writeInt(tmpHitrate);
		s.writeInt(tmpHarm);
		s.writeInt(tmpSpeed);

		s.writeUTF(faction);
		s.writeInt(factionContribution);
		s.writeInt(shun);
		s.writeInt(defense);
		s.writeInt(energy);
		s.writeInt(strength);
		s.writeInt(wakan);
		s.writeUTF(school);
		s.writeInt(schoolContribution);
		s.writeInt(agility);
		s.writeInt(hitrate);
		s.writeInt(magic);
		s.writeInt(durability);
		s.writeInt(harm);
		s.writeInt(speed);
		s.writeInt(stamina);
		s.writeInt(physique);
		
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
		maxEnergy=s.readInt();
		maxStamina=s.readInt();
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
		tmpShun=s.readInt();
		tmpDefense=s.readInt();
		tmpWakan=s.readInt();
		tmpHitrate=s.readInt();
		tmpHarm=s.readInt();
		tmpSpeed=s.readInt();

		faction=s.readUTF();
		factionContribution=s.readInt();
		shun=s.readInt();
		defense=s.readInt();
		energy=s.readInt();
		strength=s.readInt();
		wakan=s.readInt();
		school=s.readUTF();
		schoolContribution=s.readInt();
		agility=s.readInt();
		hitrate=s.readInt();
		magic=s.readInt();
		durability=s.readInt();
		harm=s.readInt();
		speed=s.readInt();
		stamina=s.readInt();
		physique=s.readInt();
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
		builder.append(", max����=");
		builder.append(maxStamina);
		builder.append(", max����=");
		builder.append(maxEnergy);
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
		builder.append(", tmp�˺�=");
		builder.append(tmpHarm);
		builder.append(", tmp����=");
		builder.append(tmpHitrate);
		builder.append(", tmp����=");
		builder.append(tmpWakan);
		builder.append(", tmp���=");
		builder.append(tmpShun);
		builder.append(", tmp�ٶ�=");
		builder.append(tmpSpeed);
		builder.append(", tmp����=");
		builder.append(tmpDefense);
		builder.append(", �˺�=");
		builder.append(harm);
		builder.append(", ����=");
		builder.append(stamina);
		builder.append(", ����=");
		builder.append(physique);
		builder.append(", ����=");
		builder.append(strength);
		builder.append(", ����=");
		builder.append(hitrate);
		builder.append(", ����=");
		builder.append(faction);
		builder.append(", ���ɹ���=");
		builder.append(factionContribution);
		builder.append(", ����=");
		builder.append(agility);
		builder.append(", ����=");
		builder.append(energy);
		builder.append(", ����=");
		builder.append(wakan);
		builder.append(", ����=");
		builder.append(durability);
		builder.append(", ���=");
		builder.append(shun);
		builder.append(", �ٶ�=");
		builder.append(speed);
		builder.append(", ����=");
		builder.append(school);
		builder.append(", ���ɹ���=");
		builder.append(schoolContribution);
		builder.append(", ����=");
		builder.append(defense);
		builder.append(", ħ��=");
		builder.append(magic);
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
	public int getEnergy() {
		return energy;
	}
	public void setEnergy(int energy) {
		this.energy = energy;
	}
	public int getMaxEnergy() {
		return maxEnergy;
	}
	public void setMaxEnergy(int maxEnergy) {
		this.maxEnergy = maxEnergy;
	}
	public int getStamina() {
		return stamina;
	}
	public void setStamina(int stamina) {
		this.stamina = stamina;
	}
	public int getMaxStamina() {
		return maxStamina;
	}
	public void setMaxStamina(int max����) {
		this.maxStamina = max����;
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
	public int getStrength() {
		return strength;
	}
	public void setStrength(int ����) {
		this.strength = ����;
	}
	public int getPhysique() {
		return physique;
	}
	public void setPhysique(int physique) {
		this.physique = physique;
	}
	public int getMagic() {
		return magic;
	}
	public void setMagic(int magic) {
		this.magic = magic;
	}

	public int getDurability() {
		return durability;
	}
	public void setDurability(int durability) {
		this.durability = durability;
	}
	public int getAgility() {
		return agility;
	}
	public void setAgility(int agility) {
		this.agility = agility;
	}
	public int getPotentiality() {
		return potentiality;
	}
	public void setPotentiality(int potentiality) {
		this.potentiality = potentiality;
	}
	public int getHitrate() {
		return hitrate;
	}
	public void setHitrate(int ����) {
		this.hitrate = ����;
	}
	public int getHarm() {
		return harm;
	}
	public void setHarm(int �˺�) {
		this.harm = �˺�;
	}
	public int getDefense() {
		return defense;
	}
	public void setDefense(int ����) {
		this.defense = ����;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int �ٶ�) {
		this.speed = �ٶ�;
	}
	public int getShun() {
		return shun;
	}
	public void setShun(int ���) {
		this.shun = ���;
	}
	public int getWakan() {
		return wakan;
	}
	public void setWakan(int ����) {
		this.wakan = ����;
	}
	public int getTmpHitrate() {
		return tmpHitrate;
	}
	public void setTmpHitrate(int tmp����) {
		this.tmpHitrate = tmp����;
	}
	public int getTmpHarm() {
		return tmpHarm;
	}
	public void setTmpHarm(int tmp�˺�) {
		this.tmpHarm = tmp�˺�;
	}
	public int getTmpDefense() {
		return tmpDefense;
	}
	public void setTmpDefense(int tmp����) {
		this.tmpDefense = tmp����;
	}
	public int getTmpSpeed() {
		return tmpSpeed;
	}
	public void setTmpSpeed(int tmp�ٶ�) {
		this.tmpSpeed = tmp�ٶ�;
	}
	public int getTmpShun() {
		return tmpShun;
	}
	public void setTmpShun(int tmp���) {
		this.tmpShun = tmp���;
	}
	public int getTmpWakan() {
		return tmpWakan;
	}
	public void setTmpWakan(int tmp����) {
		this.tmpWakan = tmp����;
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
	public String getFaction() {
		return faction;
	}
	public void setFaction(String ����) {
		this.faction = ����;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String ����) {
		this.school = ����;
	}
	public int getFactionContribution() {
		return factionContribution;
	}
	public void setFactionContribution(int ���ɹ���) {
		this.factionContribution = ���ɹ���;
	}
	public int getSchoolContribution() {
		return schoolContribution;
	}
	public void setSchoolContribution(int ���ɹ���) {
		this.schoolContribution = ���ɹ���;
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
