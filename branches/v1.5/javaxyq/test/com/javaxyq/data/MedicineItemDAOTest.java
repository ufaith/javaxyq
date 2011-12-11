package com.javaxyq.data;

import java.sql.SQLException;
import java.util.List;

import com.javaxyq.data.MedicineItem;
import com.javaxyq.data.MedicineItemDAO;
import com.javaxyq.data.MedicineItemException;
import com.javaxyq.data.impl.MedicineItemDAOImpl;
import com.javaxyq.model.ItemTypes;
import com.javaxyq.util.DBToolkit;

/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-4-17
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */

/**
 * @author gongdewei
 * @date 2010-4-17 create
 */
public class MedicineItemDAOTest {

	/**
	 * @param args
	 * @throws SQLException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws MedicineItemException, SQLException {
		//set db home dir
		System.out.println("Test JavaDB ..");
		String dbdir = System.getProperty("user.home")+"/javaxyq";
		System.setProperty("derby.system.home",dbdir);
		
		long startTime = System.currentTimeMillis();
		System.out.println("start at: "+new java.util.Date());
		DBToolkit.getDataSource().getConnection();
		System.out.println("DB inited at: "+new java.util.Date());
		//test
		testFindByName("血色茶花");
		testFindByName("四叶花");
		testFindByName("人参");
		testFindByName("白玉骨头");
		testFindByName("血色茶花");
		testFindByName("六道轮回");
		testFindByName("风水混元丹");
		testFindByName("蛇蝎美人");
		testFindByName("地狱灵芝");
		testFindById(16);
		testFindById(18);
		testFindById(1);
		testFindById(31);
		testFindById(41);
		testFindByType(ItemTypes.TYPE_MEDICINE_HP);
		testFindByType(ItemTypes.TYPE_MEDICINE_MP);
		testFindAll();
		long endTime = System.currentTimeMillis();
		System.out.println("total cost time: "+(endTime - startTime)/1000.0);
	}

	private static void testFindAll() throws MedicineItemException {
		System.out.println("findItemMedicineEntities");
		long startTime = System.currentTimeMillis();
        MedicineItemDAO instance = getMedicineItemDAO();
        List result = instance.findMedicineItemEntities();
        System.out.println("result:");
        for (Object item : result) {
			System.out.println("  "+item);
		}
        long endTime = System.currentTimeMillis();
        System.out.println("cost time: "+(endTime-startTime));
	}
	private static void testFindByName(String name) throws MedicineItemException {
		System.out.println("findItemMedicineByName");
		long startTime = System.currentTimeMillis();
		MedicineItemDAO instance = getMedicineItemDAO();
		MedicineItem result = instance.findMedicineItemByName(name);
		System.out.println("result: "+result);
        long endTime = System.currentTimeMillis();
        System.out.println("cost time: "+(endTime-startTime));
	}
	private static void testFindById(long id) throws MedicineItemException {
		System.out.println("testFindById");
		long startTime = System.currentTimeMillis();
		MedicineItemDAO instance = getMedicineItemDAO();
		MedicineItem result = instance.findMedicineItem(id);
		System.out.println("result: "+result);
		long endTime = System.currentTimeMillis();
		System.out.println("cost time: "+(endTime-startTime));
	}
	private static void testFindByType(int type) throws MedicineItemException {
		System.out.println("findItemMedicineByType: "+Integer.toHexString(type));
		long startTime = System.currentTimeMillis();
		MedicineItemDAO instance = getMedicineItemDAO();
		List<MedicineItem> result = instance.findMedicineItemsByType(type);
        System.out.println("result:");
        for (Object item : result) {
			System.out.println("  "+item);
		}
        long endTime = System.currentTimeMillis();
        System.out.println("cost time: "+(endTime-startTime));
	}

	private static MedicineItemDAO getMedicineItemDAO() {
		MedicineItemDAO instance = new  MedicineItemDAOImpl();
//		MedicineItemDAO instance = new MedicineItemJpaController();
		return instance;
	}

}
