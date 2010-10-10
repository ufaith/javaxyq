/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.widget;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.lang.ref.SoftReference;

import com.javaxyq.config.MapConfig;
import com.javaxyq.resources.DefaultTileMapProvider;

/**
 * @author ����ΰ
 * @history 2008-5-22 ����ΰ �½�
 */
public class TileMap extends AbstractWidget {

    /** ��ͼ�����ؿ�� */
    private static final int MAP_BLOCK_WIDTH = 320;

    /** ��ͼ�����ظ߶� */
    private static final int MAP_BLOCK_HEIGHT = 240;

    private DefaultTileMapProvider provider;

    private SoftReference<Image>[][] blockTable;

    /** ��ͼX������� */
    private int xBlockCount;

    /** ��ͼY������� */
    private int yBlockCount;

    private int width;

    private int height;

    private MapConfig config;

    private int lastCount;

    public TileMap(DefaultTileMapProvider provider, MapConfig cfg) {
        //ˮƽ����w�飬��ֱ����h��
        this.config = cfg;
        this.xBlockCount = provider.getXBlockCount();
        this.yBlockCount = provider.getYBlockCount();
        this.width = provider.getWidth();
        this.height = provider.getHeight();
        blockTable = new SoftReference[this.xBlockCount][this.yBlockCount];
        this.provider = provider;
    }

    @Override
    protected void doDraw(Graphics2D g2, int x, int y, int width, int height) {
        // 1.����Rect���ڵ�ͼ�� 
        Point pFirstBlock = viewToBlock(x, y);
        // 2.�����һ���ͼ���ViewRect��ƫ����,����Graphicsƫ��
        int dx = pFirstBlock.x * MAP_BLOCK_WIDTH - x;
        int dy = pFirstBlock.y * MAP_BLOCK_HEIGHT - y;
        g2.translate(dx, dy);
        //System.out.printf("x=%s,y=%s,dx=%s,dy=%s,block=%s\n", x, y, dx, dy, pFirstBlock);
        // 3.����X��,Y�᷽����Ҫ�ĵ�ͼ������
        int xCount = 1 + (width - dx - 1) / MAP_BLOCK_WIDTH;
        int yCount = 1 + (height - dy - 1) / MAP_BLOCK_HEIGHT;
        //System.out.printf("xCount=%s,yCount=%s\n",xCount,yCount);
        // 4.�ӻ����ȡ��ͼ��,����Graphics��
        for (int i = 0; i < xCount; i++) {
            for (int j = 0; j < yCount; j++) {
                Image b = getBlock(i + pFirstBlock.x, j + pFirstBlock.y);
                g2.drawImage(b, i * MAP_BLOCK_WIDTH, j * MAP_BLOCK_HEIGHT, null);
            }
        }
    }

    /**
     * Ԥ���ش�����ĵ�ͼ��
     * 
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void prepare(int x, int y, int width, int height) {
        // 1.����Rect���ڵ�ͼ�� 
        Point pFirstBlock = viewToBlock(x, y);
        // 2.�����һ���ͼ���ViewRect��ƫ����,����Graphicsƫ��
        int dx = pFirstBlock.x * MAP_BLOCK_WIDTH - x;
        int dy = pFirstBlock.y * MAP_BLOCK_HEIGHT - y;
        //System.out.printf("x=%s,y=%s,dx=%s,dy=%s,block=%s\n", x, y, dx, dy, pFirstBlock);
        // 3.����X��,Y�᷽����Ҫ�ĵ�ͼ������
        int xCount = 1 + (width - dx - 1) / MAP_BLOCK_WIDTH;
        int yCount = 1 + (height - dy - 1) / MAP_BLOCK_HEIGHT;
        //System.out.printf("xCount=%s,yCount=%s\n",xCount,yCount);
        // 4.���������ĵ�ͼ��
        Image[][] images = new Image[xCount][yCount];
        for (int i = 0; i < xCount; i++) {
            for (int j = 0; j < yCount; j++) {
                Image img = getBlock(i + pFirstBlock.x, j + pFirstBlock.y);
                images[i][j] = img;
            }
        }
    }

    private int checkTable() {
        int count = 0;
        int width = this.blockTable.length;
        int height = this.blockTable[0].length;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                SoftReference<Image> reference = this.blockTable[i][j];
                if (reference != null && reference.get() != null) {
                    count++;
                }
            }
        }
//        if (count != lastCount) {
//            System.out.printf("map loaded block count: %s \n", count);
//        }
        lastCount = count;
        return count;
    }

    private Image getBlock(int x, int y) {
        SoftReference<Image> reference = this.blockTable[x][y];
        //����˵�ͼ�黹û����,��ȡ��ͼ�����ݲ�����ͼ��
        //���GC���ڵ��ڴ�,���ͷ�image,��Ҫ����װ��
        if (reference == null || reference.get() == null) {
            reference = new SoftReference<Image>(provider.getBlock(x, y));
            this.blockTable[x][y] = reference;
        }
        this.checkTable();
        return reference.get();
    }

    public int getXBlockCount() {
        return xBlockCount;
    }

    public void setXBlockCount(int blockCount) {
        xBlockCount = blockCount;
    }

    public int getYBlockCount() {
        return yBlockCount;
    }

    public void setYBlockCount(int blockCount) {
        yBlockCount = blockCount;
    }

    /**
     * ����view����vp���Ӧ�ĵ�ͼ���ݿ�λ�� ����vp�������ĸ���ͼ���ϣ�
     * 
     * @param vp view's top left position
     * @return the map block index of the vp
     */
    private Point viewToBlock(int x, int y) {
        Point p = new Point();
        p.x = x / MAP_BLOCK_WIDTH;
        p.y = y / MAP_BLOCK_HEIGHT;
        if (p.x < 0)
            p.x = 0;
        if (p.y < 0)
            p.y = 0;
        return p;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void dispose() {
        this.provider.dispose();
        this.provider = null;
        for (SoftReference<Image>[] refs : this.blockTable) {
            for (SoftReference<Image> ref : refs) {
                if (ref != null) {
                    ref.clear();
                }
            }
        }
        this.blockTable = null;
    }

    public MapConfig getConfig() {
        return config;
    }

    public void setConfig(MapConfig config) {
        this.config = config;
    }

    public boolean contains(int x, int y) {
        return true;
    }

}
