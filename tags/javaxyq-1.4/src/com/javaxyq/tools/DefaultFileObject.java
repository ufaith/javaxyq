/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 龚德伟
 * @history 2008-7-6 龚德伟 新建
 */
public class DefaultFileObject implements FileObject {

    private File file;

    private DefaultFileSystem fileSystem;

    public DefaultFileObject(DefaultFileSystem filesystem, String pathname) {
        this.fileSystem = filesystem;
        this.file = new File(pathname);
    }

    public DefaultFileObject(DefaultFileSystem filesystem, URI uri) {
        this.fileSystem = filesystem;
        this.file = new File(uri);
    }

    public DefaultFileObject(DefaultFileSystem filesystem, File file) {
        this.fileSystem = filesystem;
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public byte[] getData() {
        // TODO LocalFile: getData
        return null;
    }

    public InputStream getDataStream() throws FileNotFoundException {
        return new FileInputStream(file);
    }

    public String getName() {
        return file.getName();
    }

    public String getPath() {
        return file.getPath();
    }

    public String getContentType() {
        return FileUtil.getContentType(this);
    }

    public boolean isDirectory() {
        return file.isDirectory();
    }

    public boolean isFile() {
        return file.isFile();
    }

    public FileObject[] listFiles() {
        File[] allfiles = file.listFiles();
        FileObject[] fileObjects = new FileObject[allfiles.length];
        List<DefaultFileObject> files = new ArrayList<DefaultFileObject>();
        List<DefaultFileObject> dirs = new ArrayList<DefaultFileObject>();

        for (int i = 0; i < allfiles.length; i++) {
            DefaultFileObject fileObj = new DefaultFileObject(fileSystem, allfiles[i]);
            if (allfiles[i].isDirectory()) {
                dirs.add(fileObj);
            } else {
                files.add(fileObj);
            }
        }

        int dirCount = dirs.size();
        for (int i = 0, iSize = dirCount; i < iSize; i++) {
            fileObjects[i] = dirs.get(i);
        }
        for (int i = 0, iSize = files.size(); i < iSize; i++) {
            fileObjects[dirCount + i] = files.get(i);
        }
        return fileObjects;
    }

    public int compareTo(FileObject o) {
    	if(this.isDirectory() && !o.isDirectory()) {
    		return 1;
    	}else if(!this.isDirectory()&& o.isDirectory()) {
    		return -1;
    	}
        return this.getPath().compareTo(o.getPath());
    }

    public FileSystem getFileSystem() {
        return this.fileSystem;
    }

    public FileObject getParent() {
        return new DefaultFileObject(this.fileSystem, this.file.getParent());
    }

    @Override
    public String toString() {
        return this.getName();
    }

	@Override
	public long getSize() {
		return this.file.length();
	}
}
