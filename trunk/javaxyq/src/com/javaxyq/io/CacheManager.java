/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-3-4
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import com.javaxyq.core.GameMain;
import com.javaxyq.event.DownloadEvent;
import com.javaxyq.event.DownloadListener;

/**
 * ���������
 * 
 * @author gongdewei
 * @date 2010-3-4 create
 */
public class CacheManager {
	private static CacheManager instance = new CacheManager();
	private ArrayList<DownloadListener> listeners = new ArrayList<DownloadListener>();

	private CacheManager() {// single-ton
	}

	public static CacheManager getInstance() {
		return instance;
	}

	/**
	 * �����ļ�
	 * 
	 * @param filename
	 * @param url
	 * @return
	 */
	private File download(String filename, URL url) {
		int size = -1;
		int received = 0;
		try {
			fireDownloadStarted(filename);
			File file = createFile(filename);
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			System.out.println("������Դ��" + filename + ", url=" + url);
			//BufferedInputStream bis = new BufferedInputStream(url.openStream());
			InputStream bis = url.openStream();
			byte[] buf = new byte[1024];
			int count = 0;
			long lastUpdate = 0;
			size = bis.available();
			while ((count = bis.read(buf)) != -1) {
				bos.write(buf, 0, count);
				received += count;
				long now = System.currentTimeMillis();
				if ( now - lastUpdate > 500) {
					fireDownloadUpdate(filename, size, received);
					lastUpdate = now;
				}
			}
			bos.close();
			System.out.println("��Դ������ϣ�" + filename);
			fireDownloadCompleted(filename);
			return file;
		} catch (IOException e) {
			System.out.println("������Դʧ�ܣ�" + filename + ", error=" + e.getMessage());
			fireDownloadInterrupted(filename);
			if (!(e instanceof FileNotFoundException)) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * �����ļ�
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public File createFile(String filename) throws IOException {
		File file = new File(GameMain.cacheBase, filename);
		if (!file.exists() || file.length() == 0) {
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
		}
		return file;
	}

	public File getFile(String filename) {
		File file = null;
		try {
			if (filename.charAt(0) == '/') {
				filename = filename.substring(1);
			}
			file = new File(filename);
			if (GameMain.appType == GameMain.APP_APPLET) {
				try {
					URL url = new URL(GameMain.base, filename);
					// try {
					// file = new File(url.toURI());
					// if (file != null && file.exists()) {
					// System.out.println("[local]��" + filename);
					// return file;
					// }
					// } catch (Exception e) {
					// // System.out.println("��ȡ���ػ���ʧ�ܣ�"+filename);
					// // e.printStackTrace();
					// }
					file = new File(GameMain.cacheBase, filename);
					if (!file.exists() || file.length() == 0) {
						file = download(filename, url);
					} else {
						// System.out.println("[cache]��" + filename);
					}
				} catch (MalformedURLException e) {
					System.out.println("��ԴURL��ʽ����ȷ��" + filename);
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			System.out.println("��ȡ�ļ�ʧ�ܣ�" + filename);
			e.printStackTrace();
		}
		return file;
	}

	public void addDownloadListener(DownloadListener listener) {
		listeners.add(listener);
	}

	public void removeDownloadListener(DownloadListener listener) {
		listeners.remove(listener);
	}

	private void fireDownloadStarted(String resource) {
		DownloadEvent e = new DownloadEvent(this, DownloadEvent.DOWNLOAD_STARTED, resource);
		for (int i = 0; i < listeners.size(); i++) {
			DownloadListener listener = listeners.get(i);
			listener.downloadStarted(e);
		}
	}

	private void fireDownloadCompleted(String resource) {
		DownloadEvent e = new DownloadEvent(this, DownloadEvent.DOWNLOAD_COMPLETED, resource);
		for (int i = 0; i < listeners.size(); i++) {
			DownloadListener listener = listeners.get(i);
			listener.downloadCompleted(e);
		}
	}

	private void fireDownloadInterrupted(String resource) {
		DownloadEvent e = new DownloadEvent(this, DownloadEvent.DOWNLOAD_INTERRUPTED, resource);
		for (int i = 0; i < listeners.size(); i++) {
			DownloadListener listener = listeners.get(i);
			listener.downloadInterrupted(e);
		}
	}

	private void fireDownloadUpdate(String resource, int size, int received) {
		final DownloadEvent event = new DownloadEvent(this, DownloadEvent.DOWNLOAD_UPDATE, resource, size, received);
		Runnable updateAction = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < listeners.size(); i++) {
					DownloadListener listener = listeners.get(i);
					listener.downloadUpdate(event);
				}
			}
		};
		SwingUtilities.invokeLater(updateAction);
	}
}
