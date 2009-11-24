package com.javaxyq.event;

import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

import com.javaxyq.widget.Player;

/**
 * 人物事件
 * 
 * @author 龚德伟
 * @history 2008-5-11 龚德伟 新建
 */
public class PlayerEvent extends EventObject {

    /** 行走完一步的事件 */
    public static final int STEP_OVER = 0x0001;

    /** 移动事件 */
    public static final int MOVE = 0x0002;

    /** 点击事件 */
    public static final int CLICK = 0x0003;

    public static final int TALK = 0x0004;

    public static final String MOVE_INCREMENT = "move.increment";

    private int id;

    private Map<String, Object> attributes;

    private String arguments;

    private Player player;

    public PlayerEvent(Player player, int id) {
        super(player);
        this.player = player;
        this.id = id;
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

	@Override
	public String toString() {
		return "PlayerEvent [arguments=" + arguments + ", attributes=" + attributes + ", id=" + id + ", player="
				+ player + "]";
	}

}
