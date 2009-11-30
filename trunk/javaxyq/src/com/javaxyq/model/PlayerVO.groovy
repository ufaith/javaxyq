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
	//��ŭֵ
	int sp = 0;
	int ���� = 10;
	int max���� = 50;
	int ���� = 10;
	int max���� = 50;
	
	long exp = 56;
	int money = 0;
	int deposit = 0;
	Date createDate = new Date();
	
	int ���� = 10;
	int ���� = 10;
	int ħ�� = 10;
	int ���� = 10;
	int ���� = 10;
	/** Ǳ��*/
	int potentiality = 0;
	
	int ���� = 62;
	int �˺� = 50;
	int ���� = 16
	int �ٶ� = 11;
	int ��� = 21;
	int ���� = 19;
	int tmp���� = 0;
	int tmp�˺� = 0;
	int tmp���� = 0
	int tmp�ٶ� = 0;
	int tmp��� = 0;
	int tmp���� = 0;

	//��ν$
	String title = "���������";
	//����
	int popularity = 800;
	//����
	String ���� = "��ң��";
	//����
	String ���� = "���ƹٸ�";
	//���ɹ���
	int ���ɹ��� = 0;  
	//���ɹ���
	int ���ɹ���= 0;

	//���������
	Map assignPoints = [����:0,ħ��:0,����:0,����:0,����:0];

	//�ɳ���
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
		max����=s.readInt();
		max����=s.readInt();
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
	
}
