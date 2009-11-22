/**
 * 
 */
package com.javaxyq.event;

import java.util.EventObject;

import com.javaxyq.model.Item;
import com.javaxyq.widget.Player;

/**
 * @author dewitt
 * 
 */
public class ItemEvent extends EventObject {

	private Player player;
	private Item item;
	private String args;

	public ItemEvent(Player player, Item item, String args) {
		super(player);
		this.player = player;
		this.item = item;
		this.args = args;
	}

	public Player getPlayer() {
		return player;
	}

	public Item getItem() {
		return item;
	}

	public String getArgs() {
		return args;
	}

}
