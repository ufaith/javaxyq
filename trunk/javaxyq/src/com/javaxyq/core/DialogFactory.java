/*
 *TODO 窗口面板 与 系统的交互设计
 */
package com.javaxyq.core;

import java.util.HashMap;
import java.util.Map;

import com.javaxyq.graph.Panel;

public class DialogFactory {

	private DialogFactory() {
		// cann't new an instance
	}

	/** <dialog id, dialog instance> */
	private static Map<String, Panel> dialogs = new HashMap<String, Panel>();

	/** <dialog id,ui filename> */
	private static Map<String, String> dialogContainer = new HashMap<String, String>();

	private static DialogBuilder builder = null;

	public static Panel getDialog(String id) {
		Panel dialog = null;
		if (id == null || id.length() == 0)
			return null;
		// reuse the dialog
		dialog = dialogs.get(id);
		try {
			if (dialog == null) {
				String filename = dialogContainer.get(id);
				dialog = builder.createDialog(id, filename);
				dialogs.put(id, dialog);
			}
		} catch (Exception e) {
			System.err.println("创建对话框失败！id="+id);
			e.printStackTrace();
		}
		return dialog;
	}

	public static void init(DialogBuilder builder) {
		DialogFactory.builder = builder;
	}

	public static void addDialog(String id, Panel dlg) {
		if (id != null && dlg != null)
			dialogs.put(id, dlg);
	}

	public static void registerDialog(String id, String filename) {
		if (id != null && filename != null)
			dialogContainer.put(id, filename);
	}
}
