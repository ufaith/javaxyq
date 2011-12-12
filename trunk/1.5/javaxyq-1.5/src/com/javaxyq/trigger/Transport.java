/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.trigger;

import java.awt.Point;

/**
 * @author ����ΰ
 * @history 2008-5-31 ����ΰ �½�
 */
public class Transport {

    /** ��㳡�� */
    private String originScene;

    /** ���λ�� */
    private Point originPosition;

    /** �յ㳡�� */
    private String goalScene;

    /** �յ�λ�� */
    private Point goalPositoin;

    public Transport(String scene1, Point p1, String scene2, Point p2) {
        this.originScene = scene1;
        this.originPosition = p1;
        this.goalScene = scene2;
        this.goalPositoin = p2;
    }

    public Transport(String originId, int originX, int originY, String goalId, int goalX, int goalY) {
        this(originId, new Point(originX, originY), goalId, new Point(goalX, goalY));
    }

    public String getOriginScene() {
        return originScene;
    }

    public void setOriginScene(String originScene) {
        this.originScene = originScene;
    }

    public Point getOriginPosition() {
        return originPosition;
    }

    public void setOriginPosition(Point originPosition) {
        this.originPosition = originPosition;
    }

    public String getGoalScene() {
        return goalScene;
    }

    public void setGoalScene(String goalScene) {
        this.goalScene = goalScene;
    }

    public Point getGoalPositoin() {
        return goalPositoin;
    }

    public void setGoalPositoin(Point goalPositoin) {
        this.goalPositoin = goalPositoin;
    }

}
