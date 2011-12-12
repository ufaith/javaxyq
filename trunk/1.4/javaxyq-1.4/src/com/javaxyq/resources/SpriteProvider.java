/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.resources;

import com.javaxyq.core.Pluggable;
import com.javaxyq.widget.Sprite;

/**
 * @author ����ΰ
 * @history 2008-5-22 ����ΰ �½�
 */
public interface SpriteProvider extends ResourceProvider<Sprite>,Pluggable{
    Sprite getSprite(String id);
}
