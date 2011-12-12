package com.javaxyq.event;


import java.awt.Point;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import com.javaxyq.widget.Player;

/**
 * �����¼�
 * 
 * @author ����ΰ
 * @history 2008-5-11 ����ΰ �½�
 */
public class PlayerEvent extends EventObject {

    /** ������һ�����¼� */
    public static final int STEP_OVER = 1;

    /** �ƶ��¼� */
    public static final int MOVE = 2;

    /** ����¼� */
    public static final int CLICK = 3;

    public static final int TALK = 4;

    public static final int WALK = 5;

    public static final String MOVE_INCREMENT = "move.increment";

    private int id;

    private Map<String, Object> attributes;

    private String arguments;

    private Player player;

	/** �������ߵ���Ŀ������ */
	private Point coords;

    public PlayerEvent(Player player, int id) {
        super(player);
        this.player = player;
        this.id = id;
    }
    public PlayerEvent(Player player, int id,Point coords) {
    	super(player);
    	this.player = player;
    	this.id = id;
    	this.coords = coords;
    }

    public PlayerEvent(Player player, int id, String args) {
        super(player);
        this.player = player;
        this.id = id;
        this.arguments = args;
    }

    public void setAttribute(String name, Object value) {
        if (attributes == null) {
            attributes = new HashMap<String, Object>();
        }
        attributes.put(name, value);
    }

    public Object getAttribute(String name) {
        return (attributes == null) ? null : attributes.get(name);
    }

    @Override
    public Player getSource() {
        return (Player) super.getSource();
    }

    public int getId() {
        return id;
    }

    public String getArguments() {
        return arguments;
    }

    public Player getPlayer() {
        return player;
    }

	public Point getCoords() {
		return coords;
	}
	@Override
	public String toString() {
		return "PlayerEvent [arguments=" + arguments + ", attributes=" + attributes + ", coords=" + coords + ", id="
				+ id + ", player=" + player + "]";
	}

}
