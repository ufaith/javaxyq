package com.javaxyq.tools;

import java.util.Comparator;

import com.javaxyq.tools.WdfFileNode;

/**
 * ����ƴ������Ƚ���
 */
public class WasFileComparator implements Comparator {

    /**
     * �Ƚ�����
     */
    private static int sortType;

    public static final int SORT_ID = 0;

    public static final int SORT_NAME = 1;

    public static final int SORT_SIZE = 2;

    private static WasFileComparator instance = null;

    private WasFileComparator() {

    }

    /**
     * ��������Ĺؼ���<br>
     * SORT_ID id<BR>
     * SORT_SIZE ��С<BR>
     * SORT_NAME ����<BR>
     * 
     * @param type
     */
    public static void setSortType(int type) {
        sortType = type;
    }

    public static WasFileComparator getInstance() {
        if (instance == null)
            instance = new WasFileComparator();
        return instance;
    }

    /**
     * WASFileNode �ȽϺ���,���ݲ�ͬ�ıȽ����ͷ��ز�ͬ��ֵ
     */
    public int compare(Object o1, Object o2) {
        try {
            WdfFileNode file1 = (WdfFileNode) o1;
            WdfFileNode file2 = (WdfFileNode) o2;
            switch (sortType) {
            case SORT_ID:
                return file1.getId() > file2.getId() ? 1
                        : (file1.getId() == file2.getId() ? 0 : -1);
            case SORT_SIZE:
                return file1.getSize() > file2.getSize() ? 1
                        : (file1.getSize() == file2.getSize() ? 0 : -1);
            case SORT_NAME:
                if (file1.getName() == null && file2.getName() != null)
                    return 1;
                else if (file1.getName() != null && file2.getName() == null)
                    return -1;
                else if (file1.getName() == null && file2.getName() == null)
                    return file1.getId() > file2.getId() ? 1 : (file1.getId() == file2.getId() ? 0
                            : -1);
                // ȡ�ñȽ϶���ĺ��ֱ��룬������ת�����ַ���
                String s1 = new String(file1.getName().getBytes("GB2312"), "ISO-8859-1");
                String s2 = new String(file2.getName().getBytes("GB2312"), "ISO-8859-1");
                // ����String��� compareTo������������������бȽ�
                return s1.compareTo(s2);
            }
        } catch (Exception e) {
        }
        return 0;
    }

}
