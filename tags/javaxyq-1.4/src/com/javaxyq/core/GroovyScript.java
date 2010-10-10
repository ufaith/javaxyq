package com.javaxyq.core;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;

import java.io.File;
import java.io.IOException;

import org.codehaus.groovy.control.CompilationFailedException;

import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.PanelListener;
import com.javaxyq.io.CacheManager;
import com.javaxyq.model.Task;
import com.javaxyq.task.TaskCoolie;

public class GroovyScript {

	static ClassLoader cl = GroovyScript.class.getClassLoader();
	static GroovyClassLoader groovyCl = new GroovyClassLoader(cl);

	public static void loadScripts() {

	}

	public static <T> T loadClass(String filename,Class<T> clazz) {
		try {
			//不缓存动态加载的脚本类
			File file = CacheManager.getInstance().getFile(filename);
			if(file!=null && file.exists()) {
				Class<T> groovyClass = groovyCl.parseClass(new GroovyCodeSource(file),false);
				return groovyClass.newInstance();
			}
		} catch (CompilationFailedException e) {
			System.err.println("Error: 脚本编译失败！"+filename);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Warning: 加载脚本失败，找不到脚本文件!"+filename);
			//e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Error: 加载脚本失败! "+filename);
			e.printStackTrace();
		}
		return null;
	}
	
	public static Object loadClass(String filename) {
		try {
			//不缓存动态加载的脚本类
			File file = CacheManager.getInstance().getFile(filename);
			if(file!=null && file.exists()) {
				Class groovyClass = groovyCl.parseClass(new GroovyCodeSource(file),!GameMain.isDebug());
				return groovyClass.newInstance();
			}
		} catch (CompilationFailedException e) {
			System.err.println("Error: 脚本编译失败！"+filename);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Warning: 加载脚本失败，找不到脚本文件!"+filename);
			//e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Error: 加载脚本失败! "+filename);
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 加载UI脚本
	 * @param id
	 * @return
	 */
	public static PanelListener loadUIScript(String id) {
		return (PanelListener) loadClass(String.format("ui/%s.groovy",id));
	}

	/**
	 * 加载npc任务脚本
	 * @param id
	 * @return
	 */
//	public static NPCListener loadNPCScript(String id) {
//		
//		return null;
//	}
	
	public static void main(String[] args) {
		PanelListener listener = GroovyScript.loadClass("scripts/ui/system.mainwin.groovy",PanelListener.class);
		listener.initial(null);
		listener.actionPerformed(new ActionEvent(listener, "autoaddhp"));
		listener.dispose(null);
		
		Task task = new Task("simple","say","sender","receiver");
		TaskCoolie taskCoolie = GroovyScript.loadClass("scripts/task/SimpleTask.groovy", TaskCoolie.class);
		taskCoolie.process(task);
		
	}

}