package com.javaxyq.core;

import java.util.EventListener;

import com.javaxyq.event.PanelListener;
import com.javaxyq.event.SceneListener;

public interface ScriptEngine {
	public boolean isDebug();

	public void setDebug(boolean debug);
	
	/**
	 * �������
	 */
	public void clearCache();

	public <T> T loadClass(String classname, Class<T> clazz);

	public Object loadClass(String classname);

	/**
	 * ����UI�ű�
	 * @param id
	 * @return
	 */
	public PanelListener loadUIScript(String id);

	public EventListener loadNPCScript(String npcId);
	public SceneListener loadSceneScript(String sceneId);
}