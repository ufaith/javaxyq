/**
 * 
 */
package com.javaxyq.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.javaxyq.model.PlayerVO;

/**
 * @author dewitt
 *
 */
public class PlayerPropertyCalculator {
	
	private static String[] �������� = { "���ƹٸ�", "����ɽ" ,"������", "Ů����"};
	private static String[] ħ������ = {  "���ܵظ�", "ħ��կ", "ʨ����", "��˿�� "};
	private static String[] �������� = {"�칬" ,"����", "��ׯ��", "����ɽ"};

	private static String[] ���� = {"0001","0002","0003","0004"};
	private static String[] ħ�� = {"0005","0006","0007","0008"};
	private static String[] ���� = {"0000","0010","0011","0012"};
	/**
	 * �ж�����array�Ƿ����ֵvalue
	 * @return
	 */
	private static boolean inArray(String[] array, String value) {
		for (int i = 0; i < array.length; i++) {
			if(array[i].equals(value))return true;
		}
		return false;
	}
	                            
	public static int calc_�ٶ�(PlayerVO attrs) {
		return (int) (attrs.physique*0.1 + attrs.durability*0.1 + attrs.strength*0.1 + attrs.agility*0.7 + attrs.magic*0);
	}

	public static int calc_����(PlayerVO attrs) {
		return (int) (attrs.physique*0.3 + attrs.magic*0.7 + attrs.durability*0.2 + attrs.strength*0.4 + attrs.agility*0);
	}

	public static int calc_���(PlayerVO attrs) {
		//FIXME ��ܼ��㹫ʽ��
		return attrs.agility*1 + 10;
	}
	public static int calc_stamina(PlayerVO attrs) {
		return attrs.level*10 + 50;
	}
	public static int calc_energy(PlayerVO attrs) {
		return attrs.level*10 + 50;
	}

	public static int calc_����(PlayerVO attrs) {
		if(inArray(����,attrs.character)) {			
			return attrs.strength*2+30;
		}else if(inArray(ħ��,attrs.character)) {
			return (int) (attrs.strength*2.3+27);
		}else if(inArray(����,attrs.character)) {
			return (int) (attrs.strength*1.7+30);
		}
		return -1;
	}


	public static int calc_�˺�(PlayerVO attrs) {
		if(inArray(����,attrs.character)) {			
			return (int) (attrs.strength*0.7+34) ;
		}else if(inArray(ħ��,attrs.character)) {
			return (int) (attrs.strength*0.8+34) ;
		}else if(inArray(����,attrs.character)) {
			return (int) (attrs.strength*0.6+40) ;
		}
		return -1;		
	}

	public static int calc_����(PlayerVO attrs) {
		if(inArray(����,attrs.character)) {			
			return (int) (attrs.durability*1.5) ;
		}else if(inArray(ħ��,attrs.character)) {
			return (int) (attrs.durability*1.3) ;
		}else if(inArray(����,attrs.character)) {
			return (int) (attrs.durability*1.6) ;
		}
		return -1;
	}

	public static int calc_��Ѫ(PlayerVO attrs) {
		if(inArray(����,attrs.character)) {			
			return attrs.physique*5 + 100 ;
		}else if(inArray(ħ��,attrs.character)) {
			return attrs.physique*6 + 100 ;
		}else if(inArray(����,attrs.character)) {
			return (int) (attrs.physique*4.5 + 100) ;
		}
		return -1;
	}
	
	public static int calc_ħ��(PlayerVO attrs) {
		if(inArray(����,attrs.character)) {			
			return attrs.magic*3+80 ;
		}else if(inArray(ħ��,attrs.character)) {
			return (int) (attrs.magic*2.5+80) ;
		}else if(inArray(����,attrs.character)) {
			return (int) (attrs.magic*3.5+80) ;
		}
		return -1;
	}
	/**
	 * invoke a  method
	 * @param mName method name
	 * @param arg argument 
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	public static Object invokeMethod(String mName, Object arg) {
		try {
			Method m = PlayerPropertyCalculator.class.getMethod(mName, arg.getClass());
			return m.invoke(PlayerPropertyCalculator.class, arg);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
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
