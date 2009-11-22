package com.javaxyq.widget;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Frame extends AbstractWidget {
    private BufferedImage image;

    /** 帧的结束时间 */
    private long endTime;

    private int centerX;

    private int centerY;

    public Frame(BufferedImage image, long endTime, int centerX, int centerY) {
        this.image = image;
        this.endTime = endTime;
        this.centerX = centerX;
        this.centerY = centerY;
        setWidth(image.getWidth(null));
        setHeight(image.getHeight(null));
    }

    public Image getImage() {
        return image;
    }

    /**
     * 生成一个新的Image
     * 
     * @return
     */
    public Image newImage() {
        BufferedImage buf = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = buf.getGraphics();
        g.drawImage(image, -centerX, -centerY, null);
        g.dispose();
        return buf;
    }

    @Override
    public void dispose() {
        this.image = null;
    }

    @Override
    protected void doDraw(Graphics2D g2, int x, int y, int width, int height) {
        int x1 = x - this.centerX;
        int y1 = y - this.centerY;
        g2.drawImage(this.image, x1, y1, x1 + width, y1 + height, 0, 0, width, height, null);
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public boolean contains(int x, int y) {

        return false;
    }
}
