/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.action;

/**
 * @author 龚德伟
 * @history 2008-6-9 龚德伟 新建
 */
public class ActionEvent extends java.awt.event.ActionEvent {

    private Object source;

    private String command;

    //private long when;

    private Object[] arguments;

    public ActionEvent(Object source, String command, Object[] args) {
        super(source, java.awt.event.ActionEvent.ACTION_PERFORMED, command, 0, 0);
        this.source = source;
        this.command = command;
        if (args == null) {
            args = new Object[] {};
        }
        this.arguments = args;
    }

//    public ActionEvent(Object source,Object ... args) {
//        
//    }

    public ActionEvent(Object source, String command) {
        this(source, command, null);
    }

    public Object getSource() {
        return source;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public String getArgumentAsString(int i) {
        if (i < 0 || arguments.length < i) {
            return null;
        }
        return (String) arguments[i];
    }

    public String getCommand() {
        return command;
    }

    public int getArgumentAsInt(int i) {
        if (i < 0 || arguments.length <= i) {
            return 0;
        }
        if (arguments[i] instanceof Integer) {
            Integer val = (Integer) arguments[i];
            return val;
        }
        try {
            return Integer.parseInt((String) arguments[i]);
        } catch (Exception e) {
            return 0;
        }
    }

}
