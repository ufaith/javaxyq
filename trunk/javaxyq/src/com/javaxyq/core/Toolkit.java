/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;

import com.javaxyq.config.LabelConfig;
import com.javaxyq.config.LinkConfig;
import com.javaxyq.config.TalkConfig;
import com.javaxyq.model.Option;
import com.javaxyq.ui.Label;
import com.javaxyq.ui.LinkLabel;
import com.javaxyq.ui.OptionLabel;
import com.javaxyq.ui.Panel;
import com.javaxyq.ui.RichLabel;

/**
 * @author 龚德伟
 * @history 2008-5-22 龚德伟 新建
 */
public class Toolkit {
    private static Toolkit instance = new Toolkit();

    private Map<String, Color> colorMap = new HashMap<String, Color>();

    private Toolkit() {
        colorMap.put("white", Color.WHITE);
        colorMap.put("black", Color.BLACK);
        colorMap.put("red", Color.RED);
        colorMap.put("pink", Color.PINK);
        colorMap.put("blue", Color.BLUE);
        colorMap.put("yellow", Color.YELLOW);
        colorMap.put("green", Color.GREEN);
    }

    public static Toolkit getInstance() {
        return instance;
    }


    public void createTalk(Panel dlg, TalkConfig talk) {
        String text = talk.getText();
        dlg.removeAll();
        RichLabel lblText = this.createRichLabel(16, 30, 450, 130, text);
        dlg.add(lblText);
        List<LinkConfig> links = talk.getLinks();
        int i = 0;
        int y0 = lblText.getHeight()+lblText.getY()+10;
        for (LinkConfig link : links) {
            text = "#R" + link.getText();
            LinkLabel label = this.createLinkLabel(20, y0 + i * 20, 450, 18, text,
                link.getAction(), link.getArguments());
            dlg.add(label);
            i++;
        }
    }
    
    public OptionLabel createOptionLabel(int x, int y, int width, int height, Option option) {
    	OptionLabel label = new OptionLabel(option);
    	label.setLocation(x, y);
    	label.setSize(width, height);
    	return label;
    }
    public LinkLabel createLinkLabel(int x, int y, int width, int height, String text,
            String action, String arguments) {
        LinkLabel label = new LinkLabel(text, action, arguments);
        label.setLocation(x, y);
        label.setSize(width, height);
        return label;
    }

    public RichLabel createRichLabel(int x, int y, int width, int height, String text) {
        LabelConfig cfg = new LabelConfig(x, y, width, height, text);
        return this.createRichLabel(cfg);
    }

    public Label createLabel(LabelConfig cfg) {
        Label label = new Label(cfg.getText());
        label.setName(cfg.getName());
        label.setLocation(cfg.getX(), cfg.getY());
        label.setSize(cfg.getWidth(), cfg.getHeight());
        String color = cfg.getColor();
        String align = cfg.getAlign();
        if (color != null) {
            label.setForeground(parseColor(color));
        }
        if (align != null) {
            if (align.equals("center")) {
                label.setHorizontalAlignment(JLabel.CENTER);
            } else if (align.equals("right")) {
                label.setHorizontalAlignment(JLabel.RIGHT);
            }
        }
        return label;
    }

    private Color parseColor(String color) {
        return colorMap.get(color);
    }

    public RichLabel createRichLabel(LabelConfig cfg) {
        RichLabel label = new RichLabel(cfg.getText());
        label.setName(cfg.getName());
        label.setLocation(cfg.getX(), cfg.getY());
        //XXX get prefered size
        Dimension size = label.computeSize(cfg.getWidth());
        label.setSize(cfg.getWidth(), size.height);
        return label;
    }

    public static InputStream getInputStream(String filename) {
        InputStream is = Toolkit.class.getResourceAsStream(filename);
        if (is == null) {
            try {
                if (filename.charAt(0) == '/') {
                    filename = filename.substring(1);
                }
                File file = new File(filename); 
                if(file.exists()) {
                	is = new FileInputStream(filename);
                }else {
                	is = GameMain.getResourceAsStream(filename);
                }
            } catch (FileNotFoundException e) {
            	System.out.println("找不到文件: "+filename);
                //e.printStackTrace();
            } catch (IOException e) {
            	System.out.println("找不到文件: "+filename);
				e.printStackTrace();
			}
        }
        return is;
    }

    public static byte[] getResourceData(String filename) throws IOException {
        InputStream is = getInputStream(filename);
        if (is == null) {
            return null;
        }
        byte[] buf = new byte[is.available()];
        int count = 0;
        while (is.available() > 0) {
            count += is.read(buf, count, is.available());
        }
        return buf;
    }

    public static Image createImageFromResource(String filename) {
        byte[] data = null;
        try {
            data = getResourceData(filename);
        } catch (IOException e) {
            System.err.println("create image error!");
            e.printStackTrace();
        }
        if (data == null) {
            return null;
        }
        return java.awt.Toolkit.getDefaultToolkit().createImage(data);
    }

    public static InputStream getInputStream(Class clazz, String filename) {
        // TODO Toolkit: getInputStream
        return null;
    }
}
