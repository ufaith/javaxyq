/**
 * 
 */
package com.javaxyq.ui;

import com.javaxyq.event.PanelEvent;
import com.javaxyq.graph.LightweightToolTipManager;
import com.javaxyq.graph.Panel;
import com.javaxyq.graph.TalkPanel;

import java.util.List;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JToolTip;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.border.EmptyBorder;

import com.javaxyq.util.ClosureTask;
import com.javaxyq.widget.Animation;

import javax.swing.border.CompoundBorder;

import com.javaxyq.core.DialogFactory;
import com.javaxyq.core.GameMain;
import com.javaxyq.core.GroovyScript;
import com.javaxyq.graph.Canvas;

/**
 * ��ϷUI������
 * @author dewitt
 */
public class UIHelper {
	public static List prompts = [];
	
	/**
	 * ������ʾ��Ϣ
	 * @param text ��ʾ����
	 * @param delay ��ʱ�ر�ʱ��(ms)
	 */
	public static void prompt(String text,long delay) {
		PromptLabel label = new PromptLabel(text);
		int offset = prompts.size()*15
		label.setLocation( (640-label.getWidth()).intdiv(2)+offset, 180+offset);
		Canvas canvas = GameMain.getGameCanvas();
		canvas.add(label,0);
		prompts.add(label);
		new Timer().schedule(new ClosureTask({
			canvas.remove(label)
			prompts.set(prompts.indexOf(label),null)
			
			if(!prompts.any {it != null }) {
				prompts.clear();
			}
			
		}), delay)
	}
	
	/**
	 * ��ʼ��UI����
	 */
	public static void init() {
		LightweightToolTipManager.sharedInstance().setInitialDelay(100);
		UIManager.put("ToolTip.border", new BorderUIResource(new CompoundBorder(
				new RoundLineBorder(Color.WHITE,1, 8, 8),new EmptyBorder(3, 3, 3, 3))));
		UIManager.put("ToolTip.foreground", new ColorUIResource(Color.WHITE));
		//		UIManager.put("ToolTip.background", new ColorUIResource(new Color(255,
		//				255, 224)));
		UIManager.put('ToolTip.font', new FontUIResource(GameMain.TEXT_FONT));
		//UIManager.put("ToolTipUI", "com.javaxyq.ui.TranslucentTooltipUI");
		
		UIManager.put("GameLabelUI", "com.javaxyq.graph.GameLabelUI");
		UIManager.put("GameButtonUI", "com.javaxyq.graph.GameButtonUI");
	}		
	
	/**
	 * ��ʾtooltip
	 * @param c
	 * @param src
	 * @param e
	 */
	public static void showToolTip(JComponent c,JComponent src,MouseEvent e) {
		def canvas = GameMain.getGameCanvas();
		Point p = SwingUtilities.convertPoint(src, src.getToolTipLocation(e), canvas);
		p.translate(20, 30);
		p.x = (p.x+c.width < canvas.width-2)?p.x:(canvas.width-c.width-2);
		p.y = (p.y+c.height < canvas.height - 2)?p.y:(canvas.height-c.height-2);
		c.location = p;
		canvas.add(c,0);
	}
	
	/**
	 * ����tooltip
	 * @param c
	 */
	public static void hideToolTip(JComponent c) {
		def canvas = GameMain.getGameCanvas();
		canvas.remove(c);		
	}
	
	/**
	 * ���ø������һ���ƶ��Ķ���
	 * @param c
	 * @param offset ���������λ��
	 */
	public static void setMovingObject(Animation anim,Point offset) {
		Canvas canvas = GameMain.getGameCanvas();
		canvas.setMovingObject(anim, offset);
	}
	
	/**
	 * ɾ������涯����
	 */
	public static void removeMovingObject() {
		Canvas canvas = GameMain.getGameCanvas();
		canvas.removeMovingObject();
	}
	
	/**
	 * ������������
	 * @param c
	 * @return
	 */
	public static Panel findPanel(Component c) {
		for(;c!=null;) {
			if (c instanceof Panel) {
				break;
			}
			c=c.parent
		} 
		return c;
	}
	/**
	 * ��ʾ������ָ���Ի���
	 * 
	 * @param autoSwap
	 */
	public static void showHideDialog(Panel dialog) {
		if (dialog != null) {
			if (dialog.getParent() == GameMain.getGameCanvas()) {
				hideDialog(dialog);
			} else {
				showDialog(dialog);
			}
		}
	}
	
	/**
	 * ��ʾ���
	 * @param dialog
	 */
	public static void showDialog(Panel dialog) {
		def canvas = GameMain.getGameCanvas();
		if (dialog != null && dialog.getParent() != canvas) {
			//����ִ�г�ʼ���¼�
			dialog.handleEvent(new PanelEvent(dialog,"initial"));
			canvas.add(dialog);
			canvas.setComponentZOrder(dialog, 0);
		}
	}
	
	/**
	 * �������
	 * @param dialog
	 */
	public static void hideDialog(Panel dialog) {
		if (dialog != null) {
			if (dialog.getParent() == GameMain.getGameCanvas()) {
				GameMain.getGameCanvas().remove(dialog);
				dialog.fireEvent(new PanelEvent(dialog,"dispose"));
				DialogFactory.dispose(dialog.getName(),dialog);
			}
		}
	}
	
	/**
	 * ��ʾ���
	 * @param id
	 */
	public static void showDialog(String id) {
		Panel dlg = null;
		if(GameMain.isDebug()) {//����ǵ���״̬��ÿ�ζ�̬����
			dlg = DialogFactory.createDialog(id);
			def listener = GroovyScript.loadUIScript(id);
			if(listener) {
				dlg.removeAllPanelListeners();
				dlg.addPanelListener(listener);
			}
		}else {
			dlg = DialogFactory.getDialog(id,true);
		}
		if(dlg)showDialog(dlg);
	}
	
	public static void showHideDialog(String id) {
		Panel dlg = DialogFactory.getDialog(id,false);
		println "showHideDialog: $id, $dlg"
		if(dlg && dlg.isShowing()) {
			hideDialog(dlg);
		}else {
			showDialog(id);
		}
	}
	
	/**
	 * �������
	 * @param id
	 */
	public static void hideDialog(String id) {
		if(!id) return;
		Panel dlg = DialogFactory.getDialog(id);
		if(dlg)hideDialog(dlg);
	}
	
	/**
	 * ��õ�ǰ��talkPanel
	 * @return
	 */
	public static TalkPanel getTalkPanel() {
		Component comp = GameMain.getGameCanvas().getComponent(0);
		if (comp instanceof TalkPanel) {
			return (TalkPanel) comp;
		}
		return null;
	}
			
}
