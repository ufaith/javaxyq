/**
 * 
 */
package com.javaxyq.core;

import com.javaxyq.model.PlayerVO;

/**
 * @author dewitt
 *
 */
class PlayerPropertyCalculator {
	
	private static def �������� = [ '���ƹٸ�', '����ɽ' ,'������', 'Ů����'];
	private static def ħ������ = [  '���ܵظ�', 'ħ��կ', 'ʨ����', '��˿�� '];
	private static def �������� = ['�칬' ,'����', '��ׯ��', '����ɽ'];

	private static def ���� = ['0001','0002','0003','0004'];
	private static def ħ�� = ['0005','0006','0007','0008'];
	private static def ���� = ['0000','0010','0011','0012'];
	                            
	static int calc�ٶ�(PlayerVO attrs) {
		return attrs.����*0.1 + attrs.����*0.1 + attrs.����*0.1 + attrs.����*0.7 + attrs.ħ��*0;
	}

	static int calc����(PlayerVO attrs) {
		return attrs.����*0.3 + attrs.ħ��*0.7 + attrs.����*0.2 + attrs.����*0.4 + attrs.����*0
	}

	static int calc���(PlayerVO attrs) {
		//FIXME ��ܼ��㹫ʽ��
		return attrs.����*1 + 10;
	}
	static int calc����(PlayerVO attrs) {
		return attrs.level*10 + 50;
	}
	static int calc����(PlayerVO attrs) {
		return attrs.level*10 + 50;
	}

	static int calc����(PlayerVO attrs) {
		switch(attrs.character) {
			case ����:
				return attrs.����*2+30
			case ħ��:
				return attrs.����*2.3+27;
			case ����:
				return attrs.����*1.7+30;
		}
	}

	static int calc�˺�(PlayerVO attrs) {
		switch(attrs.character) {
			case ����:
				return attrs.����*0.7+34 
			case ħ��:
				return attrs.����*0.8+34 
			case ����:
				return attrs.����*0.6+40 
		}
		
	}

	static int calc����(PlayerVO attrs) {
		switch(attrs.character) {
			case ����:
				return attrs.����*1.5 
			case ħ��:
				return attrs.����*1.3 
			case ����:
				return attrs.����*1.6 
		}
	}

	static int calc��Ѫ(PlayerVO attrs) {
		switch(attrs.character) {
			case ����:
				return attrs.����*5 + 100 
			case ħ��:
				return attrs.����*6 + 100 
			case ����:
				return attrs.����*4.5 + 100 
		}
	}
	
	static int calcħ��(PlayerVO attrs) {
		switch(attrs.character) {
			case ����:
				return attrs.ħ��*3+80 
			case ħ��:
				return attrs.ħ��*2.5+80 
			case ����:
				return attrs.ħ��*3.5+80 
		}
	}
}

/** 
	 1�����ʣ���������ֵ�Ķ����кܴ�Ӱ�죬�������ʵ������������Ѫ���ޣ�������Щ��������
	 2��ħ���������ߵ͵Ĵ�������ħ��������������ħ�����ޣ������������
	 3������������������������������к��˺���������Щ��������
	 4�����������������ĵֿ���������������������������������������΢����������
	 5�����ݣ��������ݵ���������߶�ܺ��ٶȣ��Ӷ�����������ս�����Ȼ���
	 
	 (һ)�������ʼ����
	 1)ħ��
	 ��12-ħ��11-����11-����8-��8
	 Ѫ172-ħ��ֵ107-����55-�˺�43-����11-�ٶ�8-����18-����17
	 2)����
	 ��10-ħ��10-����10-����10-��10
	 Ѫ150-ħ��ֵ110-����50-�˺�41-����15-�ٶ�10-����30-����16
	 3)����
	 ��12-ħ��5-����11-����12-��10
	 Ѫ154-ħ��ֵ97-����48-�˺�46-����19-�ٶ�10-����20-����13
	 
	 (��)�������Թ�ϵ��
	 �ٶ�=����*0.1+����*0.1+����*0.1+����*0.7+ħ��*0��ħ�������ٶȣ�
	 ����=����*0.3+ħ��*0.7+����*0.2+����*0.4+����*0�����ݲ���������
	 ���=����*1
	 ���У�
	 �ˣ�����*2+30
	 ħ������*2.3+27
	 �ɣ�����*1.7+30
	 �˺���
	 �ˣ�����*0.7+34
	 ħ������*0.8+34
	 �ɣ�����*0.6+40
	 ������
	 �ˣ�����*1.5
	 ħ������*1.3
	 �ɣ�����*1.6
	 ��Ѫ��
	 �ˣ�����*5+100
	 ħ������*6+100
	 �ɣ�����*4.5+100
	 ħ����
	 �ˣ�ħ��*3+80
	 ħ��ħ��*2.5+80
	 �ɣ�ħ��*3.5+80 
 **/
