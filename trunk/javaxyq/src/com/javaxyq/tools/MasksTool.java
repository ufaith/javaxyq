package com.javaxyq.tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import com.javaxyq.graph.CenterLayout;
import com.javaxyq.util.Utils;

/**
 * ��ͼ���빤��
 * @author dewitt
 * @date 2009-09-21 create
 */
public class MasksTool extends javax.swing.JFrame {

	/**  */
	private static final int CELL_WIDTH = 20;
	private JMenuItem helpMenuItem;
	private JMenu jMenu5;
	private JMenuItem deleteMenuItem;
	private JSeparator jSeparator1;
	private JMenuItem pasteMenuItem;
	private JMenuItem copyMenuItem;
	private JMenuItem cutMenuItem;
	private JMenu jMenu4;
	private JMenuItem exitMenuItem;
	private JSeparator jSeparator2;
	private JMenuItem closeFileMenuItem;
	private JMenuItem saveAsMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem openFileMenuItem;
	private JMenuItem newFileMenuItem;
	private JMenu jMenu3;
	private JMenuBar jMenuBar1;
	private JPanel mainPanel;
	private JMap map;
	private String filename;
	private JScrollPane mapPanel;
	private int sceneWidth;
	private int sceneHeight;
	private String path;
	
	private Color markColor = new Color(255, 0, 0, 100);

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MasksTool inst = new MasksTool();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public MasksTool() {
		super();
		initGUI();
	}

	private void initGUI() {
		try {
			{
				jMenuBar1 = new JMenuBar();
				setJMenuBar(jMenuBar1);
				{
					jMenu3 = new JMenu();
					jMenuBar1.add(jMenu3);
					jMenu3.setText("File");
					{
						openFileMenuItem = new JMenuItem();
						jMenu3.add(openFileMenuItem);
						openFileMenuItem.setText("Open");
						openFileMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								openMap();
							}
						});
					}
					{
						saveMenuItem = new JMenuItem();
						jMenu3.add(saveMenuItem);
						saveMenuItem.setText("Save");
						saveMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								saveMasks();
							}
						});
					}
					{
						saveAsMenuItem = new JMenuItem();
						jMenu3.add(saveAsMenuItem);
						saveAsMenuItem.setText("Save As ...");
					}
					{
						closeFileMenuItem = new JMenuItem();
						jMenu3.add(closeFileMenuItem);
						closeFileMenuItem.setText("Close");
					}
					{
						jSeparator2 = new JSeparator();
						jMenu3.add(jSeparator2);
					}
					{
						exitMenuItem = new JMenuItem();
						jMenu3.add(exitMenuItem);
						exitMenuItem.setText("Exit");
					}
				}
				{
					jMenu4 = new JMenu();
					jMenuBar1.add(jMenu4);
					jMenu4.setText("Edit");
					{
						cutMenuItem = new JMenuItem();
						jMenu4.add(cutMenuItem);
						cutMenuItem.setText("Cut");
					}
					{
						copyMenuItem = new JMenuItem();
						jMenu4.add(copyMenuItem);
						copyMenuItem.setText("Copy");
					}
					{
						pasteMenuItem = new JMenuItem();
						jMenu4.add(pasteMenuItem);
						pasteMenuItem.setText("Paste");
					}
					{
						jSeparator1 = new JSeparator();
						jMenu4.add(jSeparator1);
					}
					{
						deleteMenuItem = new JMenuItem();
						jMenu4.add(deleteMenuItem);
						deleteMenuItem.setText("Delete");
					}
				}
				{
					jMenu5 = new JMenu();
					jMenuBar1.add(jMenu5);
					jMenu5.setText("Help");
					{
						helpMenuItem = new JMenuItem();
						jMenu5.add(helpMenuItem);
						helpMenuItem.setText("Help");
					}
				}
			}
			{
				mainPanel = new JPanel(new BorderLayout());
				map = new JMap() {
					protected void paintComponent(Graphics g) {
						super.paintComponent(g);
						Rectangle rect = getVisibleRect();
						int gx = rect.x - rect.x % CELL_WIDTH;
						int gy = rect.y - rect.y % CELL_WIDTH;
						for (int x = 0, maxX = rect.x + rect.width; x < maxX; x += CELL_WIDTH) {
							// ������
							g.setColor(Color.DARK_GRAY);
							g.drawLine(gx + x, rect.y, gx + x, rect.y + rect.height);
							for (int y = 0, maxY = rect.y + rect.height; y < maxY; y += CELL_WIDTH) {
								// ������
								g.setColor(Color.DARK_GRAY);
								g.drawLine(rect.x, gx + y, rect.x + rect.width, gx + y);
								//��������
								try {
									if(masks!=null && masks[(gx+x)/CELL_WIDTH+(gy+y)/CELL_WIDTH*sceneWidth]!=0) {
										g.setColor(markColor);
										g.fillRect(gx+x, gy+y, CELL_WIDTH, CELL_WIDTH);
										//g.drawOval(gx+x+3, gy+y+3, CELL_WIDTH-4, CELL_WIDTH-4);
										//g.drawOval(gx+x+2, gy+y+2, CELL_WIDTH-3, CELL_WIDTH-3);
									}
								} catch (Exception e) {
									//e.printStackTrace();
								}
							}
						}

					}
				};
				//��갴���¼�
				map.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						if(!e.isAltDown()&& e.getButton()==MouseEvent.BUTTON1) {
							mark(e.getX(), e.getY());
						}else if(e.getButton() == MouseEvent.BUTTON3 || e.isAltDown()) {
							unmark(e.getX(), e.getY());
						}
						//map.repaint();
						map.paintImmediately(e.getX()-CELL_WIDTH,e.getY()-CELL_WIDTH,2*CELL_WIDTH,2*CELL_WIDTH);
					}
				});
				//�����ҷ
				map.addMouseMotionListener(new MouseAdapter() {
					public void mouseDragged(MouseEvent e) {
						int paintX = e.getX()-CELL_WIDTH;
						int paintY = e.getY()-CELL_WIDTH; 
						int paintWidth = 2*CELL_WIDTH;
						int paintHeight = 2*CELL_WIDTH;
						if(e.isAltDown()) {
							unmark(e.getX(), e.getY());
						}else if(e.isControlDown()){
							mark(e.getX()-CELL_WIDTH, e.getY()-CELL_WIDTH);
							mark(e.getX()-CELL_WIDTH, e.getY());
							mark(e.getX(), e.getY());
							mark(e.getX(), e.getY()+CELL_WIDTH);
							mark(e.getX()+CELL_WIDTH, e.getY()+CELL_WIDTH);
							paintX -= CELL_WIDTH;
							paintY -= CELL_WIDTH;
							paintWidth *=2;
							paintHeight *=2;
						}else {
							mark(e.getX(), e.getY());
						}
						map.paintImmediately(paintX,paintY,paintWidth,paintHeight);
					}

//					public void mouseMoved(MouseEvent e) {
//						System.out.println("move");
//					}
				});
				
				JPanel panel = new JPanel(new CenterLayout());
				panel.add("Center", map);
				mapPanel = new JScrollPane(panel);
				mainPanel.add(mapPanel, BorderLayout.CENTER);
				setContentPane(mainPanel);
			}
			setTitle("Masks Builder");
			setSize(800, 600);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���
	 * @param x
	 * @param y
	 */
	private void mark(int x, int y) {
		int sx = x / CELL_WIDTH;
		int sy = y / CELL_WIDTH;
		int pos = sx + sy * sceneWidth;
		//System.out.println("mark: ("+sx+","+sy+")");
		masks[pos] = 1;
	}
	/**
	 * ȡ�����
	 * @param x
	 * @param y
	 */
	private void unmark(int x, int y) {
		int sx = x / CELL_WIDTH;
		int sy = y / CELL_WIDTH;
		int pos = sx + sy * sceneWidth;
		//System.out.println("unmark: ("+sx+","+sy+")");
		masks[pos] = 0;
	}

	private void openMap() {
		File[] files = Utils.showOpenDialog(this, "�򿪵�ͼ", Utils.MAP_FILTER);
		if (files != null) {
			File file = files[0];
			boolean b = map.loadMap(file);
			filename = file.getName();
			path = file.getPath();
			if (b) {
				setTitle("Masks Builder - " + filename);
				JViewport viewport = mapPanel.getViewport();
				viewport.setViewPosition(new Point());
				viewport.setViewSize(map.getSize());
				System.out.println("��ͼ�Ѵ�:" + file.getAbsolutePath());
				Dimension size = map.getSize();
				System.out.println("��ͼ��С��" + size);
				sceneWidth = size.width / CELL_WIDTH;
				sceneHeight = size.height / CELL_WIDTH;
				masks = new byte[sceneWidth * sceneHeight];
				System.out.println("Steps: " + sceneWidth + "*" + sceneHeight);
				String maskfilename = path.replace(".map", ".msk");
				loadMask(maskfilename);
			} else {
				JOptionPane.showMessageDialog(this, "�򿪵�ͼ�ļ�ʧ��:" + filename, "����", JOptionPane.ERROR_MESSAGE);
				System.err.println("��ͼ��ʧ��");
			}
		}
	}

	private byte[] masks;

	private void saveMasks() {
		String maskfilename = path.replace(".map", ".msk");
		try {
			FileOutputStream fos = new FileOutputStream(maskfilename);
			PrintWriter pw = new PrintWriter(fos);
			for (int i = 0; i < masks.length; i++) {
				pw.print(masks[i]==0?'0':'1');
				if(i%sceneWidth==sceneWidth-1) {
					pw.println();
				}
			}
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ���ص�ͼ������
	 * 
	 * @param filename
	 */
	public void loadMask(String filename) {
		System.out.println("map steps: " + sceneWidth + "*" + sceneHeight);
		masks = new byte[sceneWidth * sceneHeight];
		try {
			InputStream in = new FileInputStream(filename);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String str;
			int pos = 0;
			while ((str = reader.readLine()) != null) {
				int len = str.length();
				for (int i = 0; i < len; i++) {
					masks[pos++] = (byte) (str.charAt(i) - '0');
				}
			}
		} catch (UnsupportedEncodingException e) {
			System.out.println("���ص�ͼ����ʧ�ܣ�filename=" + filename);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("���ص�ͼ����ʧ�ܣ�filename=" + filename);
			e.printStackTrace();
		}
	}

}
