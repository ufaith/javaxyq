/**
 * 
 */
package com.javaxyq.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

import com.javaxyq.core.DialogFactory;
import com.javaxyq.core.GameMain;
import com.javaxyq.core.GroovyScript;
import com.javaxyq.event.Conditional;
import com.javaxyq.event.EventDelegator;
import com.javaxyq.event.EventDispatcher;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelListener;
import com.javaxyq.widget.Animation;
import com.javaxyq.widget.Cursor;

/**
 * 游戏UI帮助类
 * @author dewitt
 */
public class UIHelper {
	public static List prompts = new ArrayList();
	private static Map<String, Color> colors = new HashMap<String, Color>();
	private static Map<String, Cursor>cursors = new HashMap<String, Cursor>();
	/**
	 * 初始化UI参数
	 */
	public static void init() {
		LightweightToolTipManager.sharedInstance().setInitialDelay(100);
		UIManager.put("ToolTip.border", new BorderUIResource(new CompoundBorder(
				new RoundLineBorder(Color.WHITE,1, 8, 8),new EmptyBorder(3, 3, 3, 3))));
		UIManager.put("ToolTip.foreground", new ColorUIResource(Color.WHITE));
		//		UIManager.put("ToolTip.background", new ColorUIResource(new Color(255,
		//				255, 224)));
		UIManager.put("ToolTip.font", new FontUIResource(GameMain.TEXT_FONT));
		//UIManager.put("ToolTipUI", "com.javaxyq.ui.TranslucentTooltipUI");
		
		UIManager.put("GameLabelUI", "com.javaxyq.ui.GameLabelUI");
		UIManager.put("GameButtonUI", "com.javaxyq.ui.GameButtonUI");
		
		colors.put("black", Color.black);
		colors.put("blue", Color.blue);
		colors.put("cyan", Color.cyan);
		colors.put("darkGray", Color.darkGray);
		colors.put("gray", Color.gray);
		colors.put("green", Color.green);
		colors.put("lightGray", Color.lightGray);
		colors.put("magenta", Color.magenta);
		colors.put("orange", Color.orange);
		colors.put("pink", Color.pink);
		colors.put("red", Color.red);
		colors.put("white", Color.white);
		colors.put("yellow", Color.yellow);
	}		
	/**
	 * 弹出提示信息
	 * @param text 提示内容
	 * @param delay 延时关闭时间(ms)
	 */
	public static void prompt(String text,long delay) {
		final PromptLabel label = new PromptLabel(text);
		int offset = prompts.size()*15;
		label.setLocation( (640-label.getWidth())/2+offset, 180+offset);
		Canvas canvas = GameMain.getGameCanvas();
		canvas.add(label,0);
		prompts.add(label);
		
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Canvas canvas = GameMain.getGameCanvas();
				canvas.remove(label);
				prompts.set(prompts.indexOf(label),null);
				boolean empty = true;
				for (int i = 0; i < prompts.size(); i++) {
					if(prompts.get(i)!=null) {
						empty = false;
					}
				}
				if(empty) {
					prompts.clear();
				}
			}
		};
		new Timer().schedule(task, delay);
	}
	
	/**
	 * 显示tooltip
	 * @param c
	 * @param src
	 * @param e
	 */
	public static void showToolTip(JComponent c,JComponent src,MouseEvent e) {
		Canvas canvas = GameMain.getGameCanvas();
		Point p = SwingUtilities.convertPoint(src, src.getToolTipLocation(e), canvas);
		p.translate(20, 30);
		p.x = (p.x+c.getWidth() < canvas.getWidth()-2)?p.x:(canvas.getWidth()-c.getWidth()-2);
		p.y = (p.y+c.getHeight() < canvas.getHeight() - 2)?p.y:(canvas.getHeight()-c.getHeight()-2);
		c.setLocation(p);
		canvas.add(c,0);
	}
	
	/**
	 * 隐藏tooltip
	 * @param c
	 */
	public static void hideToolTip(JComponent c) {
		Canvas canvas = GameMain.getGameCanvas();
		canvas.remove(c);		
	}
	
	/**
	 * 设置跟随鼠标一起移动的对象
	 * @param c
	 * @param offset 与鼠标的相对位置
	 */
	public static void setMovingObject(Animation anim,Point offset) {
		Canvas canvas = GameMain.getGameCanvas();
		canvas.setMovingObject(anim, offset);
	}
	
	/**
	 * 删除鼠标随动对象
	 */
	public static void removeMovingObject() {
		Canvas canvas = GameMain.getGameCanvas();
		canvas.removeMovingObject();
	}
	
	/**
	 * 查找最近的面板
	 * @param c
	 * @return
	 */
	public static Panel findPanel(Component c) {
		for(;c!=null;) {
			if (c instanceof Panel) {
				break;
			}
			c=c.getParent();
		} 
		return (Panel) c;
	}
	/**
	 * 显示或隐藏指定对话框
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
	 * 显示面板
	 * @param dialog
	 */
	public static void showDialog(Panel dialog) {
		Canvas canvas = GameMain.getGameCanvas();
		if (dialog != null && dialog.getParent() != canvas) {
			//阻塞执行初始化事件
			dialog.handleEvent(new PanelEvent(dialog,"initial"));
			canvas.add(dialog,0);
		}
	}
	public static void showModalDialog(final Panel dialog) {
		System.out.println("showModalDialog: "+Thread.currentThread().getName());
		Canvas canvas = GameMain.getGameCanvas();
		if (dialog != null && dialog.getParent() != canvas) {
			//阻塞执行初始化事件
			dialog.handleEvent(new PanelEvent(dialog,"initial"));
			canvas.add(dialog);
			canvas.setComponentZOrder(dialog, 0);
			EventDispatcher.pumpEvents(Thread.currentThread(), new Conditional() {
				@Override
				public boolean evaluate() {
					return (dialog.getParent()!=null && dialog.isVisible());
				}
			});
			//TODO need to interrupt pump after close the panel!
		}
		System.out.println("exit showModalDialog: "+Thread.currentThread().getName());
	}
	
	/**
	 * 隐藏面板
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
	 * 显示面板
	 * @param id
	 */
	public static void showDialog(String id) {
		Panel dlg = null;
		if(GameMain.isDebug()) {//如果是调试状态，每次动态加载
			dlg = DialogFactory.createDialog(id);
			PanelListener listener = GroovyScript.loadUIScript(id);
			if(listener!=null) {
				dlg.removeAllPanelListeners();
				dlg.addPanelListener(listener);
			}
		}else {
			dlg = DialogFactory.getDialog(id,true);
		}
		if(dlg!=null)showDialog(dlg);
	}
	
	public static void showHideDialog(String id) {
		Panel dlg = DialogFactory.getDialog(id,false);
		System.out.println("showHideDialog: "+id+", "+dlg);
		if(dlg!=null && dlg.isShowing()) {
			hideDialog(dlg);
		}else {
			showDialog(id);
		}
	}
	
	/**
	 * 隐藏面板
	 * @param id
	 */
	public static void hideDialog(String id) {
		if(id!=null) {
			Panel dlg = DialogFactory.getDialog(id,false);
			if(dlg!=null)hideDialog(dlg);
		}
	}
	
	/**
	 * 获得当前的talkPanel
	 * @return
	 */
	public static TalkPanel getTalkPanel() {
		Component comp = GameMain.getGameCanvas().getComponent(0);
		if (comp instanceof TalkPanel) {
			return (TalkPanel) comp;
		}
		return null;
	}
	
	/**
	 * @param color
	 * @return
	 */
	public static Color getColor(String color) {
		Color c = colors.get(color);
		if(c == null) {
			c = Color.getColor(color, Color.white);
		}
		return c;
	}

	public static Cursor getCursor(String cursorId) {
		Cursor cursor = cursors.get(cursorId);
		if(cursor==null) {
			boolean effect = Cursor.DEFAULT_CURSOR.equals(cursorId);
			cursor = new Cursor(cursorId, effect);
			cursors.put(cursorId, cursor);
		}
		return cursor;
	}
	
}
