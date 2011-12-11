package com.javaxyq.core;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;

import java.io.File;
import java.io.IOException;
import java.util.EventListener;

import org.codehaus.groovy.control.CompilationFailedException;

import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.PanelListener;
import com.javaxyq.event.SceneListener;
import com.javaxyq.io.CacheManager;

public class GroovyScript implements ScriptEngine {

	private static GroovyScript instance = new GroovyScript();
	private GroovyClassLoader groovyCl = new GroovyClassLoader(GroovyScript.class.getClassLoader());
	
	private boolean debug;
	public static GroovyScript getInstance() {
		return instance;
	}
	private GroovyScript() {
	}
	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void loadScripts() {

	}

	public <T> T loadClass(String filename,Class<T> clazz) {
		try {
			//�����涯̬���صĽű���
			File file = CacheManager.getInstance().getFile(filename);
			if(file!=null && file.exists()) {
				Class<T> groovyClass = groovyCl.parseClass(new GroovyCodeSource(file),false);
				return groovyClass.newInstance();
			}
		} catch (CompilationFailedException e) {
			System.err.println("Error: �ű�����ʧ�ܣ�"+filename);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Warning: ���ؽű�ʧ�ܣ��Ҳ����ű��ļ�!"+filename);
			//e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Error: ���ؽű�ʧ��! "+filename);
			e.printStackTrace();
		}
		return null;
	}
	
	public Object loadClass(String filename) {
		try {
			//�����涯̬���صĽű���
			File file = CacheManager.getInstance().getFile(filename);
			if(file!=null && file.exists()) {
				Class groovyClass = groovyCl.parseClass(new GroovyCodeSource(file),!isDebug());
				return groovyClass.newInstance();
			}
		} catch (CompilationFailedException e) {
			System.err.println("Error: �ű�����ʧ�ܣ�"+filename);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Warning: ���ؽű�ʧ�ܣ��Ҳ����ű��ļ�!"+filename);
			//e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Error: ���ؽű�ʧ��! "+filename);
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ����UI�ű�
	 * @param id
	 * @return
	 */
	public PanelListener loadUIScript(String id) {
		return (PanelListener) loadClass(String.format("ui/%s.groovy",id));
	}

	public static void main(String[] args) {
		PanelListener listener = GroovyScript.getInstance().loadClass("scripts/ui/system.mainwin.groovy",PanelListener.class);
		listener.initial(null);
		listener.actionPerformed(new ActionEvent(listener, "autoaddhp"));
		listener.dispose(null);
		
	}
	@Override
	public EventListener loadNPCScript(String npcId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public SceneListener loadSceneScript(String npcId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void clearCache() {
		// TODO Auto-generated method stub
		
	}

}