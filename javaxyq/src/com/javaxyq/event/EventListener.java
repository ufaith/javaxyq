package com.javaxyq.event;

import java.util.EventObject;

public interface EventListener {
    /**
     * 处理事件<br>
     * 注意：这个方法由系统调用，用户不应该直接调用
     * @param evt
     */
    public void handleEvent(EventObject evt);

}