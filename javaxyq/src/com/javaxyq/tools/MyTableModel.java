/*
 * JavaXYQ Source Code 
 * MyTableModel MyTableModel.java
 * by kylixs 2009-10
 * All Rights Reserved.
 * Please see also http://javaxyq.cn or http://kylixs.blog.163.com.
 * Please email to  javaxyq@qq.com.
 */
package com.javaxyq.tools;

import groovy.model.ValueHolder;
import groovy.model.ValueModel;
import groovy.swing.factory.TableModelFactory;
import groovy.util.FactoryBuilderSupport;

import java.util.ArrayList;
import java.util.Map;

import javax.swing.table.TableModel;


//触发更新事件的TableModel
public class MyTableModel extends groovy.model.DefaultTableModel{
	
	public MyTableModel(ValueModel rowsModel, ValueModel rowModel) {
		super(rowsModel, rowModel);
	}
	
	public MyTableModel(ValueModel rowsModel) {
		super(rowsModel);
	}
	
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		super.setValueAt(value, rowIndex, columnIndex);
		fireTableCellUpdated(rowIndex, columnIndex);
	}
}

class MyTableModelFactory extends TableModelFactory{
	public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if (FactoryBuilderSupport.checkValueIsType(value, name, TableModel.class)) {
            return value;
        } else if (attributes.get(name) instanceof TableModel) {
            return attributes.remove(name);
        } else {
            ValueModel model = (ValueModel) attributes.remove("model");
            if (model == null) {
                Object list = attributes.remove("list");
                if (list == null) {
                    list = new ArrayList();
                }
                model = new ValueHolder(list);
            }
            return new MyTableModel(model);
        }
    }

}

