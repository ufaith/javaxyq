/**
 * 
 */
package com.javaxyq.common.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


/**
 * @author gongdewei
 * @date 2011-8-14 create
 */
public class IOUtils {

    public static InputStream getInputStream(String filename) {
        InputStream is = IOUtils.class.getResourceAsStream(filename);
        if (is == null) {
            try {
                if (filename.charAt(0) == '/') {
                    filename = filename.substring(1);
                }
                File file = new File(filename); 
                if(file.exists()) {
                	is = new FileInputStream(filename);
                }else {
                	is = CacheManager.getInstance().getResourceAsStream(filename);
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

}
