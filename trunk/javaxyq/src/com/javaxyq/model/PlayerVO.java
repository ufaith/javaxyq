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
	//��ŭֵ
	public int sp = 0;
	public int ���� = 10;
	public int max���� = 50;
	public int ���� = 10;
	public int max���� = 50;
	
	public long exp = 56;
	public int money = 0;
	public int deposit = 0;
	public Date createDate = new Date();
	
	public int ���� = 10;
	public int ���� = 10;
	public int ħ�� = 10;
	public int ���� = 10;
	public int ���� = 10;
	/** Ǳ��*/
	public int potentiality = 0;
	
	public int ���� = 62;
	public int �˺� = 50;
	public int ���� = 16;
	public int �ٶ� = 11;
	public int ��� = 21;
	public int ���� = 19;
	public int tmp���� = 0;
	public int tmp�˺� = 0;
	public int tmp���� = 0;
	public int tmp�ٶ� = 0;
	public int tmp��� = 0;
	public int tmp���� = 0;

	//��ν$
	public String title = "���������";
	//����
	public int popularity = 800;
	//����
	public String ���� = "��ң��";
	//����
	public String ���� = "���ƹٸ�";
	//���ɹ���
	public int ���ɹ��� = 0;  
	//���ɹ���
	public int ���ɹ���= 0;

	//���������
	public Map assignPoints = null;//[����:0,ħ��:0,����:0,����:0,����:0];

	//�ɳ���
	public float growthRate; 
	
	public String state;
	public int direction;
	public int[] colorations;
	public Point sceneLocation;
	
	public PlayerVO() {
		assignPoints = new HashMap();
		assignPoints.put("����", 0);
		assignPoints.put("ħ��", 0);
		assignPoints.put("����", 0);
		assignPoints.put("����", 0);
		assignPoints.put("����", 0);
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
		this.���� = data.����;
		this.max���� = data.max����;
		this.���� = data.����;
		this.max���� = data.max����;
		this.exp = data.exp;
		this.money = data.money;
		this.deposit = data.deposit;
		this.createDate = new Date();//create date
		this.���� = data.����;
		this.���� = data.����;
		this.ħ�� = data.ħ��;
		this.���� = data.����;
		this.���� = data.����;
		this.potentiality = data.potentiality;
		this.���� = data.����;
		this.�˺� = data.�˺�;
		this.���� = data.����;
		this.�ٶ� = data.�ٶ�;
		this.��� = data.���;
		this.���� = data.����;
		this.tmp���� = data.tmp����;
		this.tmp�˺� = data.tmp�˺�;
		this.tmp���� = data.tmp����;
		this.tmp�ٶ� = data.tmp�ٶ�;
		this.tmp��� = data.tmp���;
		this.tmp���� = data.tmp����;
		this.title = data.title;
		this.popularity = data.popularity;
		this.���� = data.����;
		this.���� = data.����;
		this.���ɹ��� = data.���ɹ���;
		this.���ɹ��� = data.���ɹ���;
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
		s.writeInt(max����);
		s.writeInt(max����);
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
		s.writeInt(tmp���);
		s.writeInt(tmp����);
		s.writeInt(tmp����);
		s.writeInt(tmp����);
		s.writeInt(tmp�˺�);
		s.writeInt(tmp�ٶ�);

		s.writeUTF(����);
		s.writeInt(���ɹ���);
		s.writeInt(���);
		s.writeInt(����);
		s.writeInt(����);
		s.writeInt(����);
		s.writeInt(����);
		s.writeUTF(����);
		s.writeInt(���ɹ���);
		s.writeInt(����);
		s.writeInt(����);
		s.writeInt(ħ��);
		s.writeInt(����);
		s.writeInt(�˺�);
		s.writeInt(�ٶ�);
		s.writeInt(����);
		s.writeInt(����);
		
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
		max����=s.readInt();
		max����=s.readInt();
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
		tmp���=s.readInt();
		tmp����=s.readInt();
		tmp����=s.readInt();
		tmp����=s.readInt();
		tmp�˺�=s.readInt();
		tmp�ٶ�=s.readInt();

		����=s.readUTF();
		���ɹ���=s.readInt();
		���=s.readInt();
		����=s.readInt();
		����=s.readInt();
		����=s.readInt();
		����=s.readInt();
		����=s.readUTF();
		���ɹ���=s.readInt();
		����=s.readInt();
		����=s.readInt();
		ħ��=s.readInt();
		����=s.readInt();
		�˺�=s.readInt();
		�ٶ�=s.readInt();
		����=s.readInt();
		����=s.readInt();
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
		builder.append(max����);
		builder.append(", max����=");
		builder.append(max����);
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
		builder.append(tmp�˺�);
		builder.append(", tmp����=");
		builder.append(tmp����);
		builder.append(", tmp����=");
		builder.append(tmp����);
		builder.append(", tmp���=");
		builder.append(tmp���);
		builder.append(", tmp�ٶ�=");
		builder.append(tmp�ٶ�);
		builder.append(", tmp����=");
		builder.append(tmp����);
		builder.append(", �˺�=");
		builder.append(�˺�);
		builder.append(", ����=");
		builder.append(����);
		builder.append(", ����=");
		builder.append(����);
		builder.append(", ����=");
		builder.append(����);
		builder.append(", ����=");
		builder.append(����);
		builder.append(", ����=");
		builder.append(����);
		builder.append(", ���ɹ���=");
		builder.append(���ɹ���);
		builder.append(", ����=");
		builder.append(����);
		builder.append(", ����=");
		builder.append(����);
		builder.append(", ����=");
		builder.append(����);
		builder.append(", ����=");
		builder.append(����);
		builder.append(", ���=");
		builder.append(���);
		builder.append(", �ٶ�=");
		builder.append(�ٶ�);
		builder.append(", ����=");
		builder.append(����);
		builder.append(", ���ɹ���=");
		builder.append(���ɹ���);
		builder.append(", ����=");
		builder.append(����);
		builder.append(", ħ��=");
		builder.append(ħ��);
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
	public int get����() {
		return ����;
	}
	public void set����(int ����) {
		this.���� = ����;
	}
	public int getMax����() {
		return max����;
	}
	public void setMax����(int max����) {
		this.max���� = max����;
	}
	public int get����() {
		return ����;
	}
	public void set����(int ����) {
		this.���� = ����;
	}
	public int getMax����() {
		return max����;
	}
	public void setMax����(int max����) {
		this.max���� = max����;
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
	public int get����() {
		return ����;
	}
	public void set����(int ����) {
		this.���� = ����;
	}
	public int get����() {
		return ����;
	}
	public void set����(int ����) {
		this.���� = ����;
	}
	public int getħ��() {
		return ħ��;
	}
	public void setħ��(int ħ��) {
		this.ħ�� = ħ��;
	}
	public int get����() {
		return ����;
	}
	public void set����(int ����) {
		this.���� = ����;
	}
	public int get����() {
		return ����;
	}
	public void set����(int ����) {
		this.���� = ����;
	}
	public int getPotentiality() {
		return potentiality;
	}
	public void setPotentiality(int potentiality) {
		this.potentiality = potentiality;
	}
	public int get����() {
		return ����;
	}
	public void set����(int ����) {
		this.���� = ����;
	}
	public int get�˺�() {
		return �˺�;
	}
	public void set�˺�(int �˺�) {
		this.�˺� = �˺�;
	}
	public int get����() {
		return ����;
	}
	public void set����(int ����) {
		this.���� = ����;
	}
	public int get�ٶ�() {
		return �ٶ�;
	}
	public void set�ٶ�(int �ٶ�) {
		this.�ٶ� = �ٶ�;
	}
	public int get���() {
		return ���;
	}
	public void set���(int ���) {
		this.��� = ���;
	}
	public int get����() {
		return ����;
	}
	public void set����(int ����) {
		this.���� = ����;
	}
	public int getTmp����() {
		return tmp����;
	}
	public void setTmp����(int tmp����) {
		this.tmp���� = tmp����;
	}
	public int getTmp�˺�() {
		return tmp�˺�;
	}
	public void setTmp�˺�(int tmp�˺�) {
		this.tmp�˺� = tmp�˺�;
	}
	public int getTmp����() {
		return tmp����;
	}
	public void setTmp����(int tmp����) {
		this.tmp���� = tmp����;
	}
	public int getTmp�ٶ�() {
		return tmp�ٶ�;
	}
	public void setTmp�ٶ�(int tmp�ٶ�) {
		this.tmp�ٶ� = tmp�ٶ�;
	}
	public int getTmp���() {
		return tmp���;
	}
	public void setTmp���(int tmp���) {
		this.tmp��� = tmp���;
	}
	public int getTmp����() {
		return tmp����;
	}
	public void setTmp����(int tmp����) {
		this.tmp���� = tmp����;
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
	public String get����() {
		return ����;
	}
	public void set����(String ����) {
		this.���� = ����;
	}
	public String get����() {
		return ����;
	}
	public void set����(String ����) {
		this.���� = ����;
	}
	public int get���ɹ���() {
		return ���ɹ���;
	}
	public void set���ɹ���(int ���ɹ���) {
		this.���ɹ��� = ���ɹ���;
	}
	public int get���ɹ���() {
		return ���ɹ���;
	}
	public void set���ɹ���(int ���ɹ���) {
		this.���ɹ��� = ���ɹ���;
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
