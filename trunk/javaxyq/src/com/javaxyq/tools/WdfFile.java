package com.javaxyq.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.javaxyq.core.Toolkit;
import com.javaxyq.util.HashUtil;
import com.javaxyq.util.Utils;

/**
 * .WDF�ļ���
 * 
 * @author ����ΰ
 * @history 2008-6-10 ����ΰ �½�
 */
public class WdfFile implements FileSystem {

    /** �ļ���� */
    private RandomAccessFile fileHandler;

    /** ������2/�λ�����/�������� WDF�ļ���� */
    private static final String WDFP_FILE = "PFDW";

    /** ��3/���⴫ */
    private static final String WDFX_FILE = "XFDW";

    /** ��3/���⴫ */
    private static final String WDFH_FILE = "HFDW";

    /** ���ڽ����� */
    private int fileNodeCount;

    /** �ļ�ȫ�� */
    private String filename;

    /** ���ӳ��� */
    private Map<Long, WdfFileNode> fileNodeMap = new HashMap<Long, WdfFileNode>();

    private FileObject rootNode;

    private String fileTag;

    private long[] dh3Keys;

    /**
     * �жϴ򿪵��ļ��Ƿ�ΪWDF�ļ�
     * 
     * @param raf
     * @return
     * @throws IOException
     */
    private boolean isWdfFile(RandomAccessFile raf) throws IOException {
        byte[] buf = new byte[4];
        raf.seek(0);
        raf.read(buf);
        fileTag = new String(buf);
        return fileTag.equals(WDFP_FILE) || fileTag.equals(WDFX_FILE) || fileTag.equals(WDFH_FILE);
    }

    public WdfFile(String filename) {
        try {
            filename = StringUtils.replaceChars(filename, '\\', '/');
            this.filename = filename;
            fileHandler = new RandomAccessFile(filename, "r");
            if (!isWdfFile(fileHandler)) {
                throw new IllegalArgumentException("�������WDF��ʽ���ļ���path=" + filename + ",tag="
                        + fileTag);
            }
            //init key
            if (WDFX_FILE.equals(fileTag) || WDFH_FILE.equals(fileTag)) {
                loadDH3Key("dh3.dat");
            }
            //read
            fileNodeCount = readInt(fileHandler);
            int headerSize = readInt(fileHandler);
            fileHandler.seek(headerSize);
            fileNodeMap.clear();
            //�Ƿ���Ҫ������㣬��ԭidֵ(��3��Դ)
            boolean xor = WDFX_FILE.equals(fileTag) || WDFH_FILE.equals(fileTag);
            int dh3i = 0;
            for (int i = 0; i < fileNodeCount; i++) {
                WdfFileNode node = new WdfFileNode();
                long id = readUnsignInt(fileHandler);
                node.setId(xor ? id ^ dh3Keys[dh3i] : id);
                int offset = readInt(fileHandler);
                node.setOffset((int) (xor ? offset ^ dh3Keys[dh3i + 1] : offset));
                int size = readInt(fileHandler);
                node.setSize((int) (xor ? size ^ dh3Keys[dh3i + 2] : size));
                int space = readInt(fileHandler);
                node.setSpace((int) (xor ? space ^ dh3Keys[dh3i + 3] : space));

                dh3i += 4;
                dh3i %= 0x40;
                node.setFileSystem(this);
                fileNodeMap.put(node.getId(), node);
            }
            rootNode = new WdfDirectoryObject(this);
            String name = StringUtils.substringAfterLast(filename, "/");
            restorePaths(name);

        } catch (Exception e) {
            System.err.println("��WDF�ļ�����" + filename);
            e.printStackTrace();
        }
        System.out.printf("nodeCount=%s, total find:%s\n", fileNodeCount, fileNodes().size());
    }
    
    private Map<Long, String> buildPaths(String filename) {
    	String cmtfile ="resources/names/"+ filename.replaceAll("\\.wd.*", ".cmt");
    	Map<Long, String> map = new HashMap<Long, String>();
        InputStream is = Utils.getResourceAsStream(cmtfile);
        if (is != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String strPath = null;
            try {
                while ((strPath = br.readLine()) != null) {
                	strPath = strPath.trim();
                	if(strPath.length()>0) {
                		String[] strs = strPath.split("=");
                		map.put(Long.parseLong(strs[0],16), strs[1]);
                	}
                }
            } catch (Throwable e) {
            	System.err.println("��ԭ�ļ����б�ʧ��: " + cmtfile);
            	e.printStackTrace();
            }
        } else {
            System.err.println("��ȡ��Դʧ��: " + cmtfile);
        }
    	
		return map;
    }

    private void restorePaths(String name) {
    	Map<Long, String> id2PathMap = buildPaths(name);
//        Map<Long, String> id2PathMap = HashUtil.createId2PathMap("resources/names/"
//                + name.replaceAll("\\.wd.*", ".lst"));
        Set<Entry<Long, WdfFileNode>> entryset = fileNodeMap.entrySet();
        int iCount =0;
        for (Entry<Long, WdfFileNode> entry : entryset) {
            WdfFileNode node = entry.getValue();
            String path = id2PathMap.get(entry.getKey());
            path = StringUtils.replaceChars(path, '\\', '/');
            String strId = Long.toHexString(entry.getKey());
            if (path != null) {
                //System.out.printf("ƥ��: %s=%s\n", strId, path);
                //start with '/'
                if (path.charAt(0) != '/') {
                    path = "/" + path;
                }
                node.setPath(path);
                iCount ++;
            } else {//�Ҳ���path
                node.setPath("/" + strId);
                //System.err.printf("��ƥ����: id=%s\n", strId);
            }
        }
        System.out.printf("�����ļ�%d����ƥ���ļ�%d������ƥ���ļ�%d��\n",fileNodeCount,iCount,(fileNodeCount-iCount));
    }

    private void loadDH3Key(String file) {
        InputStream is = Toolkit.getInputStream("/com/javaxyq/tools/" + file);
        DataInputStream dis = new DataInputStream(is);
        try {
            System.out.println("size=" + dis.available());
            dh3Keys = new long[0x40];
            for (int i = 0; i < dh3Keys.length; i++) {
                dh3Keys[i] = readUnsignInt(dis);
            }
        } catch (IOException e) {
            System.err.println("loadDH3Key error!");
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        if (this.fileHandler != null) {
            this.fileHandler.close();
        }
        this.fileNodeMap.clear();
    }

    private int readInt(DataInput di) throws IOException {
        int ch1, ch2, ch3, ch4;
        ch1 = di.readUnsignedByte();
        ch2 = di.readUnsignedByte();
        ch3 = di.readUnsignedByte();
        ch4 = di.readUnsignedByte();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0));
    }

    private long readUnsignInt(DataInput di) throws IOException {
        long ch1, ch2, ch3, ch4;
        ch1 = di.readUnsignedByte();
        ch2 = di.readUnsignedByte();
        ch3 = di.readUnsignedByte();
        ch4 = di.readUnsignedByte();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0));
    }

    /**
     * ��ȡWdfFile�������ļ���㼯��
     * 
     * @return
     */
    public Collection<WdfFileNode> fileNodes() {
        return fileNodeMap.values();
    }

    public int getFileNodeCount() {
        return fileNodeCount;
    }

    /**
     * ����id��ȡ��Ӧ������
     * 
     * @param nid ���id
     * @return ����һ��������
     * @throws IOException
     */
    public InputStream getNodeAsStream(long nodeId) throws IOException {
        return new ByteArrayInputStream(this.getNodeData(nodeId));
    }

    /**
     * ����id��ȡ��Ӧ������
     * 
     * @param nid
     * @return
     * @throws IOException
     */
    public byte[] getNodeData(long nodeId) throws IOException {
        //����������
        WdfFileNode fnode = fileNodeMap.get(nodeId);
        byte[] data = null;
        if (fnode != null) {
            data = new byte[(int) fnode.getSize()];
            //ƫ�Ƶ�������ݶ�λ��
            fileHandler.seek(fnode.getOffset());
            //��ȡ�������
            fileHandler.readFully(data);
        }
        return data;
    }

    @Override
    public String toString() {
        return "[wdf name=" + filename + "]";
    }

    public String getName() {
        return filename;
    }

    public WdfFileNode findNode(long nodeId) {
        return fileNodeMap.get(nodeId);
    }

    public WdfFileNode findNode(String nodeId) {
        return fileNodeMap.get(Long.parseLong(nodeId, 16));
    }

    /**
     * ���������ļ�
     * 
     * @param filename
     * @throws Exception
     */
    public void loadDescription(InputStream is) {
        if (is != null) {
            Scanner scanner = new Scanner(is);
            scanner.useDelimiter("(\r\n)|(\n\r)|[\n\r=]");
            String tag = scanner.next();
            long uid;
            String str, alias;
            int iCount = 0;
            if (tag.startsWith("[Resource]")) {
                while (scanner.hasNext()) {
                    scanner.skip("(\r\n)|(\n\r)|[\n\r=]");
                    str = scanner.next();
                    uid = Long.parseLong(str, 16);
                    scanner.skip("(\r\n)|(\n\r)|[\n\r=]");
                    alias = scanner.next().trim();
                    WdfFileNode node = fileNodeMap.get(uid);
                    if (node != null) {
                        node.setDescription(alias.replace('\\', '/'));
                        //System.out.println("��Դ:" + Long.toHexString(uid) + "=" + alias);
                    } else {
                        System.out.println("�Ҳ������ڵ���Դ:" + str + "=" + alias);
                    }
                    iCount++;
                }
            }
            System.out.println("total : " + iCount);
            scanner.close();
        }
    }

    /**
     * ȡpath����Ľ���б�
     * 
     * @param path ·��
     * @param subpath �Ƿ������Ŀ¼
     * @return
     */
    public List<WdfFileNode> getNodesUnderPath(String path, boolean subpath) {
        return null;
    }

    /**
     * ������Դ�����ļ�
     * 
     * @param filename
     */
    public void saveDescription(FileOutputStream os) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(os));
            writer.write("[Resource]\r\n");
            List<WdfFileNode> nodeList = getNodesUnderPath("/", false);
            for (WdfFileNode node : nodeList) {
                if (node.getName() != null && node.getName().length() > 0) {
                    writer.write(Long.toHexString(node.getId()).toUpperCase());
                    writer.write('=');
                    writer.write(node.getName());
                    writer.write("\r\n");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * �����Դ������Ϣ
     */
    public void clearDescription() {
        Collection<WdfFileNode> nodes = fileNodes();
        for (WdfFileNode node : nodes) {
            node.setDescription(null);
        }
    }

    public FileObject getRoot() {
        return rootNode;
    }

    public String getType() {
        return "wdf";
    }

    public void load(String filename) {
        this.loadDescription(Utils.getResourceAsStream(filename));
    }

    public void save(String filename) {
        try {
            this.saveDescription(new FileOutputStream(filename));
        } catch (FileNotFoundException e) {
            System.out.println("save description failed! filename=" + filename);
            e.printStackTrace();
        }
    }

    /**
     * ��offset��ʼ��ȡ����Ϊlen�����ݿ�
     * 
     * @param data
     * @param offset
     * @param len
     * @throws IOException
     */
    public void read(byte[] data, int offset, int len) throws IOException {
        fileHandler.seek(offset);
        fileHandler.readFully(data, 0, len);
    }

}
