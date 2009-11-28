/*
 *TODO 窗口面板 与 系统的交互设计
 */
package com.javaxyq.core;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import com.javaxyq.event.PanelListener;
import com.javaxyq.graph.Panel;
import com.javaxyq.ui.XmlDialogBuilder;

public class DialogFactory {

	private DialogFactory() {
		// cann't new an instance
	}
	
	/** Dialog索引表<dialog id,ui filename> */
	private static Map<String, String> dialogIndexTable = new HashMap<String, String>();

	/** Dialog实例表 <dialog id, dialog instance> */
	private static Map<String, Panel> dialogs = new WeakHashMap<String, Panel>();
	
	private static DialogBuilder builder = new XmlDialogBuilder();

	public static Panel getDialog(String id) {
		Panel dialog = null;
		if (id == null || id.length() == 0)
			return null;
		// 检查缓存的实例
		dialog = dialogs.get(id);
		if (dialog == null) {
			try {
				String filename = dialogIndexTable.get(id);
				dialog = builder.createDialog(id, filename);
				dialogs.put(id, dialog);
			} catch (Exception e) {
				System.err.println("创建对话框失败！id=" + id);
				e.printStackTrace();
			}
		}
		return dialog;
	}
	public static Panel createDialog(String id) {
		Panel dialog = null;
		if (id == null || id.length() == 0)
			return null;
		//调试状态每次重新加载
		//FIXME 调试避免每次加载？只更新脚本
		try {
			String filename = dialogIndexTable.get(id);
			dialog = builder.createDialog(id, filename);
			dialogs.put(id, dialog);
		} catch (Exception e) {
			System.err.println("创建对话框失败！id=" + id);
			e.printStackTrace();
		}
		return dialog;
	}

	/**
	 * 改变Dialog生成器
	 * 
	 * @param builder
	 */
	public static void init(DialogBuilder builder) {
		DialogFactory.builder = builder;
	}

	/**
	 * 添加dialog关联到索引表
	 * @param id
	 * @param filename
	 */
	public static void addDialog(String id, String filename) {
		if (id != null && filename != null)
			dialogIndexTable.put(id, filename);
	}
}
