/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-25
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.soulnew;

import java.awt.Point;
import java.util.List;


/**
 * @author dewitt
 * @date 2009-11-25 create
 */
public interface Searcher {

	public abstract List<AStarNode> findPath(AStarNode startNode, AStarNode goalNode);

	/**
	 * 初始化结点
	 * 
	 * @param maskdata
	 *            地图掩码数据(width*height)
	 */
	public abstract void init(int width, int height, byte[] maskdata);

	/**
	 * 获取某个通行点
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public abstract AStarNode getNode(int x, int y);

	/**
	 * 获得距离该点最近的可通行点
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public abstract AStarNode getNearstNode(int x, int y);

	/**
	 * 是否可以通过该点
	 * 
	 * @return
	 */
	public abstract boolean pass(int x, int y);

	public abstract List<Point> findPath(int x1, int y1, int x2, int y2);

}