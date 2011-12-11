/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.resources;

import java.awt.Image;

import com.javaxyq.core.Pluggable;


/**
 * @author ����ΰ
 * @history 2008-5-22 ����ΰ �½�
 */
public interface ImageProvider extends ResourceProvider<Image>,Pluggable{
    Image getImage(String id);
}
