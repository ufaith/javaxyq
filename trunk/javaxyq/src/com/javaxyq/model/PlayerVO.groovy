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

	public String toString() {
		def props = this.getProperties();
		props.remove('class');
		props.remove('metaClass');
		return props.toString();
	}	
}
