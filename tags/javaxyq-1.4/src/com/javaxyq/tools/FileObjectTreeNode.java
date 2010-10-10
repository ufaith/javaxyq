/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.tools;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author ����ΰ
 * @history 2008-7-6 ����ΰ �½�
 */
public class FileObjectTreeNode extends DefaultMutableTreeNode {

    private boolean loaded;

    /**
     * @param userObject
     */
    public FileObjectTreeNode(FileObject userObject) {
        super(userObject);
    }

    @Override
    public boolean isLeaf() {
        return ((FileObject) userObject).listFiles().length == 0;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
}
