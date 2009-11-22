/******************************************************************************
 * Copyright (C) 2007 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * �����Ϊ���ڿ����տ������ơ�δ������˾��ʽ����ͬ�⣬�����κθ��ˡ����岻��ʹ�á����ơ�
 * �޸Ļ򷢲������.
 *****************************************************************************/

package com.javaxyq.widget;

import java.awt.Graphics;
import java.awt.Point;

import com.javaxyq.config.CursorConfig;

/**
 * ��Ϸָ��
 * 
 * @author ����ΰ
 * @history 2008-6-8 ����ΰ �½�
 */
public class Cursor {
    public static final String DEFAULT_CURSOR = "default";

    public static final String ATTACK_CURSOR = "attack";

    public static final String EXCHANGE_CURSOR = "exchange";

    public static final String GIVE_CURSOR = "give";

    public static final String TALK_CURSOR = "talk";

    public static final String PICKUP_CURSOR = "pickup";

    public static final String FRIEND_CURSOR = "friend";

    public static final String SELECT_CURSOR = "select";

    public static final String TEAM_CURSOR = "team";

    public static final String FORBID_CURSOR = "forbid";

    public static final String TEXT_CURSOR = "text";

    private CursorConfig config;

    private SpriteImage pointer;

    private SpriteImage effect;

    private int x;

    private int y;

    /** ����ĳ������� */
    private int clickX;

    private int clickY;

    /** xƫ����(�����clickX,Ϊ�˾�ȷ��ʾ�����Ч��) */
    private int offsetX;

    private int offsetY;

    public Cursor(CursorConfig cfg, SpriteImage pointer) {
        this(cfg, pointer, null);
    }

    public Cursor(CursorConfig cfg, SpriteImage pointer, SpriteImage effect) {
        this.config = cfg;
        this.pointer = pointer;
        this.effect = effect;
        if (this.effect != null) {
            this.effect.setVisible(false);
        }
    }

    public SpriteImage getPointer() {
        return pointer;
    }

    public SpriteImage getEffect() {
        return effect;
    }

    public void setClick(int x, int y) {
        this.clickX = x;
        this.clickY = y;
        this.effect.setVisible(true);
    }

    public int getClickX() {
        return clickX;
    }

    public int getClickY() {
        return clickY;
    }

    public Point getClickPosition() {
        return new Point(this.clickX, this.clickY);
    }

    public void setOffset(int x, int y) {
        this.offsetX = x;
        this.offsetY = y;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * ����ָ������Ļ��λ��
     * @param x
     * @param y
     */
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public CursorConfig getConfig() {
        return config;
    }

    public void draw(Graphics g) {
        this.pointer.draw(g, x, y);
    }

    public void update(long elapsedTime) {
        this.pointer.update(elapsedTime);
    }

}