/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.tools;

import java.util.EventListener;

/**
 * @author ����ΰ
 * @history 2008-6-28 ����ΰ �½�
 */
public interface PaginationListener extends EventListener {

    void loadPage(PaginationEvent e);
}
