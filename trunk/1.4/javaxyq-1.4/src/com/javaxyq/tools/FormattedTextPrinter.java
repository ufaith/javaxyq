package com.javaxyq.tools;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

//TODO ��˸Ч��?�ָ�����?�Զ�����ɫ?����?����?
/**
 * ��ӡ��ʽ�ı�<br>
 * 
 * ���ָ�ʽ������ʱ����Ϊ��Ϣ�����������ָ�ʽ��ָ��Ϊ��#��ĸ�������ִ�Сд�� <br>
 * #R ��ʾ���������Ϊ��ɫ(red)<br>
 * #G ��ʾ���������Ϊ��ɫ(green)<br>
 * #B ��ʾ���������Ϊ��ɫ(blue)<br>
 * #K ��ʾ���������Ϊ��ɫ(black)<br>
 * #Y ��ʾ���������Ϊ��ɫ(yellow)<br>
 * #W ��ʾ���������Ϊ��ɫ(white)<br>
 * #b ��ʾ���������Ϊ��˸(blink)<br>
 * #c + �������ֻ���A-F��ĸ �Զ�����ɫ�����磺c008000=����ɫ<br>
 * #u + ���� + #u �������»��ߡ�<br>
 * #n ��������״̬�ָ�������<br>
 * #r ���ֻ��С�<br> ## ���һ��#�š�<br>
 * #0-99 ����
 * 
 * @author Langlauf
 * @date
 * @deprecated ������������ replace by GFormattedLabel.java
 */
public class FormattedTextPrinter {

	private static final int LINE_WIDTH = 14;

	private static final int LINE_HEIGHT = 14;

	private static final TreeMap<String, StyleAttribute> styleMap;

	private static final Font NORML_FONT = new Font("����",Font.PLAIN,14);

	static {
		styleMap = new TreeMap<String, StyleAttribute>();
		styleMap.put("#u", new StyleAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON));
		styleMap.put("#R", new StyleAttribute(TextAttribute.FOREGROUND, Color.RED));
		styleMap.put("#G", new StyleAttribute(TextAttribute.FOREGROUND, Color.GREEN));
		styleMap.put("#B", new StyleAttribute(TextAttribute.FOREGROUND, Color.BLUE));
		styleMap.put("#K", new StyleAttribute(TextAttribute.FOREGROUND, Color.BLACK));
		styleMap.put("#Y", new StyleAttribute(TextAttribute.FOREGROUND, Color.YELLOW));
		styleMap.put("#W", new StyleAttribute(TextAttribute.FOREGROUND, Color.WHITE));
		styleMap.put("#n", new StyleAttribute(TextAttribute.FONT, NORML_FONT));
	}

	private static final StyleAttribute Normal_Style=new StyleAttribute(TextAttribute.FOREGROUND,Color.BLACK);
	static class StyleAttribute {
		Attribute attrib;

		Object value;

		int beginIndex;

		public StyleAttribute(Attribute attrib, Object value) {
			this.attrib = attrib;
			this.value = value;
		}

		protected StyleAttribute clone() {
			return new StyleAttribute(attrib,value);
		}
	}

	public static void printText(String text, Graphics2D g) {
		int index = 0, width = 0, x = 20, y = 100;
		String word;
		// if(!text.startsWith("#"))text="#n"+text;
		Pattern pattern =
			Pattern.compile("#([RGBKYWnbur#]|[1-9]\\d|[0-9]|"
				+ "c[0-9A-Fa-f]?[0-9A-Fa-f]?[0-9A-Fa-f]?[0-9A-Fa-f]?[0-9A-Fa-f]?[0-9A-Fa-f]?)|" + "[^#]+");
		Matcher m = pattern.matcher(text);
		StringBuilder builder = new StringBuilder();
		ArrayList<StyleAttribute> attribList = new ArrayList<StyleAttribute>();
		while (m.find()) {
			word = m.group();
			System.out.println(word);
			if (word.startsWith("#")) {
				StyleAttribute attrib = styleMap.get(word);
				if (attrib != null) {
					attrib=attrib.clone();
					attrib.beginIndex = index;
					attribList.add(attrib);
				}else if(word.startsWith("#c")){
					attrib=Normal_Style.clone();
					attrib.value = Color.decode("0x" + word.substring(2));
					attrib.beginIndex = index;
					attribList.add(attrib);
				}else if(word.startsWith("#n")){
					
				}else {
					builder.append(word);
					index += word.length();
				}
			} else {
				builder.append(word);
				index += word.length();
			}
		}
		String printText = builder.toString();
		int len = printText.length();
		AttributedString attribStr = new AttributedString(printText);
		attribStr.addAttribute(TextAttribute.FONT, NORML_FONT);
		attribStr.addAttribute(TextAttribute.FOREGROUND, Color.WHITE);
		for (StyleAttribute attrib : attribList) {
			attribStr.addAttribute(attrib.attrib, attrib.value, attrib.beginIndex, len);
		}
		g.drawString(attribStr.getIterator(), x, y);

	}

	public static void main(String[] args) {
		// String text="#ufsdfd\nsf#��#2.2";
		final String text = "����һ��#R���λ����Ρ�#Y�����ʽ�ı�:#G���#c008000����#B#u��!";
		JFrame frame = new JFrame() {
			private Image img=new ImageIcon("/resources/map/���ƹ���.jpg").getImage();

			@Override
			public void paint(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.drawImage(img, 0, 30,  null);
				g2d.setColor(Color.WHITE);
				g2d.drawRoundRect(9, 39, getWidth()-19, getHeight()-49, 15, 15);
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
				g2d.setColor(Color.BLACK);
				g2d.fillRoundRect(10, 40, getWidth()-20, getHeight()-50, 15, 15);
				g2d.setComposite(AlphaComposite.SrcOver);
				FormattedTextPrinter.printText(text, g2d);
			}
		};
		// frame.setBackground(Color.BLACK);
		frame.setTitle("FormattedText");
		frame.setSize(400, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
