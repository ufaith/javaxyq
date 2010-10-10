package com.javaxyq.tools;

import com.jidesoft.swing.Resizable;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JLayeredPane;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JPanel;



//ͼ�������¼�
class LayerOrderChangeListener implements PropertyChangeListener {
	private UIMaker maker;
	public LayerOrderChangeListener(UIMaker maker) {
		this.maker = maker;
	}
	void propertyChange(PropertyChangeEvent evt) {
		println "change: ${evt.oldValue} ${evt.newValue}"
		def layers = maker.builder.layerlist.model.elements();
		for(LayerObject layerObj in layers) {
			//�ı�dom˳��
			Node node = layerObj.node
			if(node.name() == 'Dialog') {
				continue;
			}
			maker.dialogNode.remove(node)
			maker.dialogNode.append(node);
			
			//�ı�ؼ�˳��
			Component comp = maker.node2comp[node]; 
			if(comp) {//��Щ���û�ж�Ӧ�Ŀؼ�
				maker.dialog.remove(comp);
				maker.dialog.add(comp);
			}
		}
		
	}
}

//�����߳�
class PaintTask extends TimerTask {
	UIMaker maker;
	JPanel canvas;
	private BufferedImage offscreenImage;
	private Graphics2D offscreenGraphics;
	public PaintTask(UIMaker maker) {
		this.maker = maker;
		this.canvas = maker.builder.canvas;
		this.offscreenImage = maker.offscreenImage;
		this.offscreenGraphics = maker.offscreenGraphics;
	}
	void run () {
		try {
			//offscreen painting!
			if(canvas.isShowing()) {
				canvas.paint(this.offscreenGraphics);
				paintMenu()
				canvas.getGraphics().drawImage(this.offscreenImage, 0, 0, null);
			}
		}catch(e) {
			e.printStackTrace()
		}
	}
	//����canvas�ϵ�LayeredPane���ݣ��絯���˵���
	//��ΪCanvas�������ģ�Ӱ����ԭ���Ĳ˵�����
	//FIXME ��canvas�����г�ͻ
	private paintMenu() {
		try {
			def pane = maker.frame.getLayeredPane();
			def comps = pane.getComponentsInLayer(JLayeredPane.POPUP_LAYER);
			//println "popup layer: $comps"
			Graphics g = this.offscreenGraphics.create(0,0,300,150);
			Point p0 = pane.getLocationOnScreen();
			Point p1 = canvas.getLocationOnScreen();
			int dx = p1.x - p0.x;
			int dy = p1.y - p0.y
			g.translate(-dx,-dy);
			//pane.paint(g);
			for(def c in comps) {
				if(c.visible) {
					Graphics g2 = g.create(c.x,c.y,c.width,c.height);
					c.paint(g2);
					g2.dispose();
				}
			}
			g.dispose();
		}catch(e) {}
	}
}


//custom resizable 
class MyResizable extends Resizable {
	private UIMaker maker;
	public MyResizable(JComponent component,UIMaker maker) {
		super(component);
		this.maker = maker;
	}

	public void resizing(int resizeCorner, int newX, int newY, int newW, int newH) {
        Dimension minimumSize = _component.getMinimumSize();
        Dimension maximumSize = _component.getMaximumSize();
        if (newW < minimumSize.width) {
            newW = minimumSize.width;
        }
        if (newH < minimumSize.height) {
            newW = minimumSize.height;
        }
        if (newW > maximumSize.width) {
            newW = maximumSize.width;
        }
        if (newH > maximumSize.height) {
            newH = maximumSize.height;
        }
        _component.setPreferredSize(new Dimension(newW, newH));
		maker.resizeComp(_component,new Dimension(newW, newH))
	};
}