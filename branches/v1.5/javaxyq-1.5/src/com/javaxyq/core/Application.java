package com.javaxyq.core;

import java.io.InputStream;

import com.javaxyq.model.Option;
import com.javaxyq.profile.Profile;
import com.javaxyq.profile.ProfileException;
import com.javaxyq.profile.ProfileManager;
import com.javaxyq.task.TaskManager;
import com.javaxyq.ui.UIHelper;
import com.javaxyq.widget.Player;

public interface Application {

	public static final float NORMAL_SPEED = 0.15f;// 0.1f;
	public static final float BEVEL_SPEED = 0.105f;// 0.071f;
	public static final int STEP_DISTANCE = 20;
	public static final int DOUBLE_STEP_DISTANCE = 2 * STEP_DISTANCE;
	/** ð�ݶԻ���ʾ��ʱ�� (ms) */
	public static final int TIME_CHAT = 15 * 1000;
	/**
	 * ��Ϸ״̬
	 */
	public static final int STATE_BATTLE = 0x1;
	public static final int STATE_NORMAL = 0x0;
	/** ð�ݶԻ�����ʱ��(ms) */
	public static long CHAT_REMAIND_TIME = 15000;

	public abstract void startup();

	public abstract void shutdown();

	public abstract Context getContext();

	public abstract java.net.URL getResource(String name);

	public abstract InputStream getResourceAsStream(String name);

	public abstract DataManager getDataManager();

	public abstract ItemManager getItemManager();

	public TaskManager getTaskManager();
	
	public ProfileManager getProfileManager();
	
	public abstract ScriptEngine getScriptEngine();

	/**
	 * ִ��ָ��ActionCommand��Action
	 * 
	 * @param source
	 *            ����Action��Դ����
	 * @param cmd������actiomCommand
	 *            ,��������
	 */
	public abstract void doAction(Object source, String actionId, Object[] args);

	/**
	 * ������npc�ĶԻ�
	 * 
	 * @param npc
	 */
	public abstract void doTalk(Player p, String chat);

	/**
	 * ������npc�ĶԻ�
	 * @param options
	 * @param npc
	 */
	public abstract Option doTalk(Player talker, String chat, Option[] options);

	public abstract void doAction(Object source, String actionId);

	public abstract boolean isDebug();

	public abstract void setDebug(boolean debug);

	public abstract int getState();

	public abstract void setState(int state);

	void playMusic();
	void stopMusic();
	
	GameWindow getWindow();
	GameCanvas getCanvas();
	UIHelper getUIHelper();
	
	/**
	 * ������Ϸ����
	 */
	void enterScene();
	
	/**
	 * ��ȡ��ǰ��Ϸ�浵
	 * @return
	 */
	public Profile getProfile();
	
	/**
	 * ���ش浵
	 * @param profileName
	 * @throws ProfileException 
	 */
	void loadProfile(String profileName) throws ProfileException;
	
	/**
	 * ����浵
	 */
	void saveProfile() throws ProfileException;
	
	/**
	 * ��ǰ��Ϸ�浵���Ϊ..
	 * @param newname
	 * @throws ProfileException
	 */
	public void saveProfileAs(String newname) throws ProfileException;
	
	/**
	 * ��ȡ��ǰ��Ϸ�浵����
	 * @return
	 */
	String getProfileName();
}