package com.javaxyq.event;

import java.util.EventObject;

public interface EventListener {
    /**
     * �����¼�<br>
     * ע�⣺���������ϵͳ���ã��û���Ӧ��ֱ�ӵ���
     * @param evt
     */
    public void handleEvent(EventObject evt);

}