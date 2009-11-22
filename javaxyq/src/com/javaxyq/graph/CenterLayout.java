package com.javaxyq.graph;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

public class CenterLayout implements LayoutManager {

	private Component center;

	public void addLayoutComponent(String name, Component comp) {
		synchronized (comp.getTreeLock()) {
			center = comp;
		}
	}

	public void layoutContainer(Container target) {
		if(center==null)return;
		synchronized (target.getTreeLock()) {
			Insets insets = target.getInsets();
			int maxwidth = target.getWidth() - (insets.left + insets.right );
			int maxheight = target.getHeight() - (insets.top + insets.bottom );
			Dimension d = center.getPreferredSize();
			if(d.width>maxwidth)d.width=maxwidth;
			if(d.height>maxheight)d.height=maxheight;
			int x = 0, y = 0 ;
			x=(maxwidth-d.width)/2+insets.left;
			y=(maxheight-d.height)/2+insets.top;
			center.setLocation(x, y);
			center.setSize(d);
		}
	}

	public Dimension minimumLayoutSize(Container target) {
		synchronized (target.getTreeLock()) {
			Component c = target.getComponent(0);
			return c == null ? new Dimension() : c.getMinimumSize();
		}
	}

	public Dimension preferredLayoutSize(Container target) {
		synchronized (target.getTreeLock()) {
			Component c = target.getComponent(0);
			return c == null ? new Dimension() : c.getPreferredSize();
		}
	}

	public void removeLayoutComponent(Component comp) {
		synchronized (comp.getTreeLock()) {
			center = comp;
		}
	}

}
