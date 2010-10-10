/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.tools;

import java.util.EventObject;

/**
 * @author ����ΰ
 * @history 2008-6-28 ����ΰ �½�
 */
public class PaginationEvent extends EventObject {

    private int pageNo;

    private int pageSize;

    public PaginationEvent(Object source, int pageNo, int pageSize) {
        super(source);
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }
}
