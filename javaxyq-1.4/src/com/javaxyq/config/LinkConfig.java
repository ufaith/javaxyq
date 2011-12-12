/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.config;

/**
 * @author 龚德伟
 * @history 2008-6-8 龚德伟 新建
 */
public class LinkConfig implements Config {
    private String text;

    private String action;

    private String arguments;

    public LinkConfig(String text, String action, String value) {
        this.text = text;
        this.action = action;
        this.arguments = value;
        if (this.arguments == null) {
            this.arguments = "";
        }
    }

    public String getType() {
        return "link";
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String value) {
        this.arguments = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
