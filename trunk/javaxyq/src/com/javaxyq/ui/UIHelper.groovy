/**
 * 
 */
package com.javaxyq.ui;

import com.javaxyq.graph.LightweightToolTipManager;

import java.util.List;

import java.awt.Color;
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

import com.javaxyq.core.GameMain;
import com.javaxyq.graph.Canvas;

/**
 * @author dewitt
 *
 */
class UIHelper {
	public static List prompts = [];
	
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
	
	public static void showToolTip(JComponent c,JComponent src,MouseEvent e) {
		def canvas = GameMain.getGameCanvas();
		Point p = SwingUtilities.convertPoint(src, src.getToolTipLocation(e), canvas);
		p.translate(20, 30);
		p.x = (p.x+c.width < canvas.width-2)?p.x:(canvas.width-c.width-2);
		p.y = (p.y+c.height < canvas.height - 2)?p.y:(canvas.height-c.height-2);
		c.location = p;
		canvas.add(c,0);
	}
	
	public static void hideToolTip(JComponent c) {
		def canvas = GameMain.getGameCanvas();
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
	
	public static void removeMovingObject() {
		Canvas canvas = GameMain.getGameCanvas();
		canvas.removeMovingObject();
	}
	
}
