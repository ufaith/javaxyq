/**
 * 
 */
package com.javaxyq.common.graph;

import java.awt.Image;
import java.io.IOException;

import com.javaxyq.common.io.IOUtils;

/**
 * @author gongdewei
 * @date 2011-8-14 create
 */
public class GraphUtils {

    public static Image createImageFromResource(String filename) {
        byte[] data = null;
        try {
            data = IOUtils.getResourceData(filename);
        } catch (IOException e) {
            System.err.println("create image error!");
            e.printStackTrace();
        }
        if (data == null) {
            return null;
        }
        return java.awt.Toolkit.getDefaultToolkit().createImage(data);
    }

}
