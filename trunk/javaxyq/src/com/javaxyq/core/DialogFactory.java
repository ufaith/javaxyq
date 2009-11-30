/*
 *TODO ������� �� ϵͳ�Ľ������
 */
package com.javaxyq.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.Map.Entry;

import com.javaxyq.event.PanelListener;
import com.javaxyq.graph.Panel;
import com.javaxyq.ui.XmlDialogBuilder;

public class DialogFactory {

	private DialogFactory() {
		// cann't new an instance
	}

	/** Dialog������<dialog id,ui filename> */
	private static Map<String, String> dialogIndexTable = new HashMap<String, String>();

	/** Dialogʵ���� <dialog id, dialog instance> */
	private static Map<String, Panel> dialogs = new HashMap<String, Panel>();
	/** ���رյĶԻ��򣬿����Զ����� */
	private static Map<String, Panel> disposeDialogs = new WeakHashMap<String, Panel>();

	private static DialogBuilder builder = new XmlDialogBuilder();

	/**
	 * ��öԻ���ʵ��
	 * @param id
	 * @param create �Ƿ��Զ�����
	 * @return
	 */
	public static Panel getDialog(String id, boolean create) {
		Panel dialog = null;
		if (id == null || id.length() == 0)
			return null;
		// ��黺���ʵ��
		dialog = dialogs.get(id);
		if(dialog == null) {
			dialog = disposeDialogs.get(id);
		}
		if (dialog == null && create) {
			return createDialog(id);
		}
		return dialog;
	}

	public static Panel createDialog(String id) {
		Panel dialog = null;
		if (id == null || id.length() == 0)
			return null;
		// ����״̬ÿ�����¼���
		// FIXME ���Ա���ÿ�μ��أ�ֻ���½ű�
		try {
			String filename = dialogIndexTable.get(id);
			dialog = builder.createDialog(id, filename);
			dialogs.put(id, dialog);
		} catch (Exception e) {
			System.err.println("�����Ի���ʧ�ܣ�id=" + id);
			e.printStackTrace();
		}
		return dialog;
	}

	/**
	 * �ı�Dialog������
	 * 
	 * @param builder
	 */
	public static void init(DialogBuilder builder) {
		DialogFactory.builder = builder;
	}

	/**
	 * ���dialog������������
	 * 
	 * @param id
	 * @param filename
	 */
	public static void addDialog(String id, String filename) {
		if (id != null && filename != null)
			dialogIndexTable.put(id, filename);
	}
	
	/**
	 * @param dlg
	 */
	public static void dispose(String id,Panel dlg) {
		if(id==null && dlg!=null) {
			Set<Entry<String, Panel>> entres =  dialogs.entrySet();
			for (Entry<String, Panel> entry : entres) {
				if(dlg.equals(entry.getValue())){
					id = entry.getKey();
					break;
				}
			}
		}
		dialogs.remove(id);
		disposeDialogs.put(id, dlg);
	}
}
