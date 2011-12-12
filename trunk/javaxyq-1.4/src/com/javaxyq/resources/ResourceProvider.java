/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.resources;

/**
 * @author ����ΰ
 * @history 2008-5-22 ����ΰ �½�
 */
public interface ResourceProvider<E> {
    E getResource(String resId);
    void dispose();
}
