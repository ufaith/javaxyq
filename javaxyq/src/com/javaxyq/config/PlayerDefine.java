/*
 * JavaXYQ 
 * 
 * javaxyq 2008 all rights. http://www.javaxyq.com
 */

package com.javaxyq.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 龚德伟
 * @history 2008-5-21 龚德伟 新建
 */
public class PlayerDefine implements Config {

	private String character;

	private List<StateConfig> states = new ArrayList<StateConfig>();

	private String profile;

	public void addState(StateConfig stateCfg) {
		states.add(stateCfg);
	}

	public List<StateConfig> getStates() {
		return states;
	}

	public void setStates(List<StateConfig> states) {
		this.states = states;
	}

	public String getType() {
		return character;
	}

	public String getCharacter() {
		return character;
	}

	public void setCharacter(String character) {
		this.character = character;
	}

	public void setType(String type) {
		this.character = type;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getProfile() {
		return profile;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("PlayerConfig[");
		sb.append("character=");
		sb.append(this.character);
		sb.append(",profile=");
		sb.append(this.profile);
		sb.append("]");
		return sb.toString();
	}
}
