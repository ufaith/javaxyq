/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-27
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */

package ui_script;


import java.util.Timer;
import java.awt.Desktop;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import com.javaxyq.util.ClosureTask;

import com.javaxyq.core.*;
import com.javaxyq.event.*;
import com.javaxyq.ui.*;
import com.javaxyq.graph.*;
import com.javaxyq.widget.*;
/**
 * 场景小地图对话框脚本
 * @author dewitt
 * @date 2009-11-27 create
 */
class scene_map extends PanelHandler implements MouseListener{
	
	private String sceneId;
	private int marginX = 18;
	private int marginY = 12;
	private Animation animPoint = SpriteFactory.loadAnimation('wzife/scene/point.tcp');
	private Animation animWpoint = SpriteFactory.loadAnimation('wzife/scene/wpoint.tcp');
	private Animation animTarget = SpriteFactory.loadAnimation('wzife/scene/target.tcp');
	
	private Label lblPoint = new Label(animPoint);
	private Label lblTarget = new Label(animTarget);
	private List stepPoints = [];
	
	private Timer timer;
	/** 缩略图宽度 */
	private int navWidth;
	/** 缩略图高度 */
	private int navHeight;
	/** 缩小比例 */
	private double rateX;
	private double rateY;
	public void initial(PanelEvent evt) {
		super.initial(evt);
		def canvas = GameMain.getSceneCanvas();
		def path = canvas.getMap().getConfig().getPath();
		path = path.replaceAll('\\.map','.tcp').replaceAll('scene/','smap/');
		//def sprite = SpriteFactory.loadSprite("smap/${sceneId}.tcp");
		def sprite = SpriteFactory.loadSprite(path);
		panel.setBgImage(new SpriteImage(sprite));
		panel.setSize(sprite.getWidth(),sprite.getHeight());
		int x = (640-panel.getWidth())/2;
		int y = (480 - panel.getHeight())/2;
		panel.setLocation(x, y);
		panel.remove(lblPoint);
		panel.remove(lblTarget);
		navWidth = panel.getWidth()-2*marginX;
		navHeight = panel.getHeight() - 2*marginY;
		rateX = 1.0*navWidth /canvas.getSceneWidth()
		rateY = 1.0*navHeight /canvas.getSceneHeight()
		
		timer = new Timer();
		timer.schedule(new ClosureTask({update(null)}),100,500);
		
		panel.removeMouseListener(this);
		panel.addMouseListener(this);
	}

	@Override
	synchronized void update(PanelEvent evt) {
		def canvas = GameMain.getSceneCanvas();
		def player = canvas.getPlayer();
		def playerLoc = player.getSceneLocation();
		def p0 = sceneToLocal(playerLoc);
		//FIXME 修复单帧偏移位置问题
		p0.translate(-animPoint.getRefPixelX(),-animPoint.getRefPixelY());
		lblPoint.setLocation(p0);
		panel.add(lblPoint);
		//移除路线的点
		//TODO 解决闪烁问题
		stepPoints.each{
			panel.remove(it);
		}
		stepPoints.clear();
		def path = player.getPath();
		if(path && path.size()>1) {
			def targetPoint = path[path.size()-1];
			def p = sceneToLocal(targetPoint);
			p.translate(-animTarget.getRefPixelX(),-animTarget.getRefPixelY());
			lblTarget.setLocation(p);
			panel.add(lblTarget);
			
			for(int i=1;i<path.size()-2;i+=5) {
				def lblWpoint = new Label(animWpoint);
				lblWpoint.setLocation(sceneToLocal(path[i]));
				stepPoints << lblWpoint;
				panel.add(lblWpoint);
			}
		}else {
			panel.remove(lblTarget);
		}
	}
	
	private Point sceneToLocal(Point p) {
		return new Point(marginX+(int)(p.@x*rateX), marginY+navHeight-(int)(p.@y*rateY));
	}

	private Point localToScene(Point p) {
		p.translate(-marginX,-marginY);
		return new Point((int)(p.@x/rateX),(int)((navHeight-p.@y)/rateY));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		def target = localToScene(e.getPoint());
		def canvas = GameMain.getSceneCanvas();
		canvas.walkTo(target.@x,target.@y);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
