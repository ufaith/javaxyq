package com.javaxyq.ui;

import groovy.util.XmlParser;

import javax.swing.Action;
import javax.swing.AbstractButton;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import com.javaxyq.config.ImageConfig;
import com.javaxyq.core.*;
import com.javaxyq.graph.*;


class XmlDialogBuilder implements DialogBuilder{
	
	
	Panel createDialog(String id, String res) {
		if(!id)throw new IllegalArgumentException('Dialog的id不能为空');
		if(!res)throw new IllegalArgumentException("Dialog[$id]的界面描述文件路径不能为空") ;
		println("createDialog $id in $res");
		def input = new File(res)
		def xml = new XmlParser().parse(input)
		def dlgEl = xml.find {it.@id ==id };
		if(dlgEl) {
			def dialog = processDialog(dlgEl);
			//components
			processComponents(dialog,dlgEl);
			return dialog;
		}
		println("创建Panel失败，找不到Dialog的描述信息：$id in $res")
	}

	
	Panel createDialog(String xml) {
		def dlgEl = new XmlParser().parseText(xml);
		if(dlgEl) {
			def dialog = processDialog(dlgEl);
			//components
			processComponents(dialog,dlgEl);
			return dialog;
		}
		println "创建Panel失败！"
	}

	Panel processDialog(dlgEl) {
		Panel dialog = null;
		int width = dlgEl.@width.toInteger();
		int height = dlgEl.@height.toInteger();
		def isTalk = dlgEl.@talk.equals('true');
		if(isTalk) {
			dialog = new TalkPanel(width,height);
		}else {
			dialog = new Panel(width,height);
		}
		dialog.setName(dlgEl.@id);
		try {
			dialog.setLocation(dlgEl.@x.toInteger(), dlgEl.@y.toInteger())
		}catch(Exception e) {
			try {
				int x = (GameMain.gameCanvas.width - width)/2
				int y = (GameMain.gameCanvas.height - height)/2
				dialog.setLocation( x, y)
			}catch(ex) {}
		}
		dialog.setLayout(null)
		String background = dlgEl.@background;
		if(background) {
			dialog.setBgImage(new ImageConfig(background));
		}
		try {
			dialog.setClosable(dlgEl.@closable.toBoolean());
		}catch(Exception e) {}
		try {
			dialog.setMovable(dlgEl.@movable.toBoolean());
		}catch(Exception e) {}
		dialog.setActionMap(GameMain.getActionMap());
		// 注册监听器
		//dialog.setInitialAction(dlgEl.@initial);
		//dialog.setDisposeAction(dlgEl.@dispose);
		
		//加载面板的脚本
		def listener = GroovyScript.loadUIScript(dlgEl.@id);
		if(listener) {
			dialog.addPanelListener(listener);
		}

		return dialog;
	}
	
	void processComponents(dialog,dlgNode) {
		def nodes = dlgNode.children();
		for(def node in nodes) {
			try {
				def comp = this.invokeMethod ('process'+node.name(),[dialog,node])
				if(comp) {
					//绑定事件
					['mousePressed','mouseReleased','mouseClicked','keyPressed','keyReleased','keyTyped'].each{type ->
						def actionId = node.@"$type"; 
						if(actionId) {
							dialog.bindAction(comp,type,actionId)
							if(type.startsWith('mouse')) {
								comp.delegateMouseEvent();
							}
						}
					}
				}
			}catch(e) {
				println "处理控件失败：${node.name()} ${node.attributes()}"
				e.printStackTrace();
			}
		}		
	}


	AbstractButton processButton(Panel dialog,el) {
		boolean toggle = el.@toggle as Boolean;
		String actionId = el.@actionId;
		AbstractButton btn = null;
		if(toggle) {
			btn = new ToggleButton();
		}else {
			btn = new Button();
		}
		if(actionId) {
//			Action action = dialog.actionMap.get(actionId);
//			if (!action) {
//				try {
//					String wildcard = actionId.substring(0, actionId.lastIndexOf('.')) + ".*";
//					action = dialog.actionMap.get(wildcard);
//				}catch(e) {}
//			}
//			if (!action) {
//				println("Warning: Action not found, actionId=$actionId");
//			}else {
//				btn.setAction(action);
//			}
//			//set cmd after action
			btn.setActionCommand(actionId);
		}
		btn.text = el.@text;
		try {
			btn.setEnabled(el.@enable.toBoolean() )
		}catch(Exception e) {}
		try {
			btn.location = [el.@x.toInteger(),el.@y.toInteger()];
		}catch(e) {}
		btn.name = el.@name;
		btn.init(SpriteFactory.loadSprite(el.@was));
		//btn.setToolTipText(el.@tooltip);
		if(el.@tooltip) {
			btn.setTooltipTpl(el.@tooltip);
		}
		dialog.add(btn);
		return btn;
	}

	Label processText(Panel dialog,el) {
		Label label = new Label(el.@text);
		label.setName(el.@name);
		try {
			label.setLocation(el.@x.toInteger(), el.@y.toInteger());
		}catch(e) {}
		try {
			label.setSize(el.@width.toInteger(), el.@height.toInteger());
		}catch(e) {
			label.setSize(100,20);
		}
		label.setPreferredSize(label.getSize());
		String color = el.@color;
		String align = el.@align;
		if (color) {
			label.setForeground(Color[color]);
		}
		if (align) {
			if (align.equals("center")) {
				label.setHorizontalAlignment(JLabel.CENTER);
			} else if (align.equals("right")) {
				label.setHorizontalAlignment(JLabel.RIGHT);
			}
		}
		if(el.@tooltip)label.setTooltipTpl(el.@tooltip);
		dialog.add(label);
		return label;
	}
	
	ItemLabel processItem(Panel dialog,el) {
		ItemLabel label = new ItemLabel();
		label.setName(el.@name);
		try {
			label.setLocation(el.@x.toInteger(), el.@y.toInteger());
		}catch(e) {}
		try {
			label.setSize(el.@width.toInteger(), el.@height.toInteger());
		}catch(e) {
			label.setSize(100,20);
		}
		label.setPreferredSize(label.getSize());
		if(el.@tooltip)label.setTooltipTpl(el.@tooltip);
		dialog.add(label);
		return label;
	}

	RichLabel processRichText(Panel dialog,el) {
		RichLabel label = new RichLabel(el.@text);
		label.setName(el.@name);
		try {
			label.setLocation(el.@x.toInteger(), el.@y.toInteger());
		}catch(e) {}
		try {
			label.setSize(el.@width.toInteger(), el.@height.toInteger());
		}catch(e) {
			label.setSize(100,20);
		}
		if(el.@tooltip)label.setToolTipText(el.@tooltip);
		
		dialog.add(label);
		return label;
	}
	
	Label processSprite(Panel dialog,el) {
		int index = 0;
		try {
			index = el.@index.toInteger();
		} catch (Exception e) {
		}
		
		Label imgLabel = new Label(SpriteFactory.loadAnimation(el.@path, index));
		try {
			imgLabel.setLocation(el.@x.toInteger(),el.@y.toInteger());
		}catch(Exception e) {}
		try {
			imgLabel.setSize(el.@width.toInteger(),el.@height.toInteger());
		}catch(Exception e) {}
		imgLabel.setName(el.@name);		
		if(el.@tooltip)imgLabel.setTooltipTpl(el.@tooltip);
		dialog.add(imgLabel);
		return imgLabel;
	}
	
	void processImage(Panel dialog,el) {
		ImageConfig cfg = new ImageConfig(el.@path);
		cfg.setId(el.@id);
		try {
			cfg.setX(el.@x.toInteger());
			cfg.setY(el.@y.toInteger());
		}catch(e) {}
		try {
			cfg.setWidth(el.@width.toInteger());
			cfg.setHeight(el.@height.toInteger());
		}catch(Exception e) {}
		dialog.addImage(cfg);
	}
	
	TextField processEditor(Panel dialog,el) {
		TextField editor = new TextField();
		editor.setName(el.@name);
		String actionId = el.@actionId;
		try {
			editor.setLocation(el.@x.toInteger(),el.@y.toInteger());
		}catch(e) {}
		try {
			editor.setSize(el.@width.toInteger(),el.@height.toInteger());
		}catch(e) {
			editor.setSize(100,20);
		}
		if (actionId) {
//			Action action = dialog.actionMap.get(actionId);
//			editor.addActionListener(action);
			editor.setActionCommand(actionId);
		}
		if(el.@foreground) {
			editor.setForeground(Color[el.@foreground]);
		}
		editor.setToolTipText(el.@tooltip);
		
		dialog.add(editor);
		return editor;
	}
	
	void processAction(Panel dialog,el) {
		String actionId = el.@id;
		String className = el.@class;
		Action action = (Action) Class.forName(className).newInstance();
		action.putValue(Action.ACTION_COMMAND_KEY, actionId);
		dialog.actionMap.put(actionId, action);
				
	}
	
}