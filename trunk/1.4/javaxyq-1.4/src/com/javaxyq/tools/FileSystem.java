/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.tools;

/**
 * 
 * @author ����ΰ
 * @history 2008-7-6 ����ΰ �½�
 */
public interface FileSystem {

    FileObject getRoot();

    String getType();

    /**
     * save desc
     * @param filename
     */
    void save(String filename);

    /**
     * load desc
     * @param filename
     */
    void load(String filename);
}
