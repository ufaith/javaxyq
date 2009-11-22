/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.config;

/**
 * @author 龚德伟
 * @history 2008-6-7 龚德伟 新建
 */
public class CursorConfig implements Config {
    private String id;

    private String pointer;

    private String effect;

    public CursorConfig(String id, String pointer, String effect) {
        this.id = id;
        this.pointer = pointer;
        this.effect = effect;
    }

    public String getType() {
        return "cursor";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPointer() {
        return pointer;
    }

    public void setPointer(String pointer) {
        this.pointer = pointer;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

}
