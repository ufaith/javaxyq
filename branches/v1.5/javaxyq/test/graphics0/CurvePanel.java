package graphics0;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import java.awt.geom.*;

/**
 * <P>
 * Title: computer graphics
 * </P>
 * <P>
 * Description: homeworks
 * </P>
 * <P>
 * Copyright: Copyright (c) 2004
 * </P>
 * <P>
 * Company: shu
 * </P>
 * 
 * @author dxf
 * @version 1.0
 */

public class CurvePanel extends JPanel {
	Point[] x = new Point[4];
	int i, j = 0;

	public CurvePanel() {
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Graphics g = getGraphics();
				if (j < 4) {
					x[j] = new Point(e.getX(), e.getY());
					g.fillOval(x[j].x - 3, x[j].y - 3, 6, 6);
					if (j >= 1) {
						g.drawLine(x[j - 1].x, x[j - 1].y, x[j].x, x[j].y);
					}
					j++;
				}
				if (j == 4) {
					draw_Bezier(g, x);
					j = 0;
					for (i = 0; i < 4; i++) {
						x[i].x = 0;
						x[i].y = 0;
					}
				}
			}
		});
	}

	void draw_Bezier(Graphics g, Point[] p) {
		Point[][] result = new Point[2][4];
		Point[] q = new Point[4];
		Point[] r = new Point[4];
		if (Math.max(distance(p[1], p[0], p[3]), distance(p[2], p[0], p[3])) <= 1.0d)
			g.drawLine(p[0].x, p[0].y, p[3].x, p[3].y);
		else {
			result = curve_split(p);
			for (i = 0; i < 4; i++) {
				q[i] = new Point(result[0][i].x, result[0][i].y);
				r[i] = new Point(result[1][i].x, result[1][i].y);
			}
			draw_Bezier(g, q);
			draw_Bezier(g, r);
		}
	}

	Point[][] curve_split(Point[] p) {
		Point[][] result = new Point[2][4];
		Point[] q = new Point[4];
		Point[] r = new Point[4];
		System.arraycopy(p, 0, q, 0, p.length);
		for (i = 1; i < 4; i++) {
			r[4 - i] = new Point(q[3].x, q[3].y);
			for (j = 3; j >= i; j--) {
				q[j].x = (q[j].x + q[j - 1].x) >> 1;
				q[j].y = (q[j].y + q[j - 1].y) >> 1;
			}

		}
		r[0] = new Point(q[3].x, q[3].y);
		for (i = 0; i < 4; i++) {
			result[0][i] = new Point(q[i].x, q[i].y);
			result[1][i] = new Point(r[i].x, r[i].y);
		}
		return result;
	}

	double distance(Point p1, Point p2, Point p3) {
		double d;
		Line2D.Float line = new Line2D.Float(p2.x, p2.y, p3.x, p3.y);
		d = line.ptLineDist(p1);
		return d;
	}
	
	public static void main(String[] args) {
		JFrame f = new JFrame("Curve");
		f.setSize(400, 300);
		f.add(new CurvePanel());
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
