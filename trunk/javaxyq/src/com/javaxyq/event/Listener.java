package com.javaxyq.event;

/**
 * @author ����ΰ
 * @history 2008-5-10 ����ΰ �½�
 */
public class Listener {
    private String type;
    private Class handler; 
    
    public Listener(String type,Class handler) {
        this.type = type;
        this.handler = handler;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Class getHandler() {
        return handler;
    }

    public void setHandler(Class handler) {
        this.handler = handler;
    }
    
}
