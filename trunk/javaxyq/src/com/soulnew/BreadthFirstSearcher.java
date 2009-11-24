package com.soulnew;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class BreadthFirstSearcher implements Searcher {

	private int width;
	private int height;

	/**
	 * Construct the path, not including the start node. ���ҵ��յ�,���η����丸�ڵ�,����Ҫ������·��
	 */
	protected List constructPath(AStarNode node) {
		LinkedList path = new LinkedList();
		while (node.pathParent != null) {
			path.addFirst(node);
			node = node.pathParent;
		}
		return path;
	}

	public List<AStarNode> findPath(AStarNode startNode, AStarNode goalNode) {
		// list of visited nodes
		//LinkedList closedList = new LinkedList();// ����Ѿ����ʹ��Ľڵ�,��(FIFO)��
		Set closedList = new HashSet(); // ����Ѿ����ʹ��Ľڵ�,��(FIFO)��

		// list of nodes to visit (sorted)
		LinkedList openList = new LinkedList(); // ����Ѿ������õĽڵ�
		openList.add(startNode);
		startNode.pathParent = null;

		while (!openList.isEmpty()) {
			AStarNode node = (AStarNode) openList.removeFirst();
			if (node == goalNode) {
				// path found!
				return constructPath(goalNode);
			} else {
				closedList.add(node);

				Iterator i = node.neighbors.iterator();
				while (i.hasNext()) {
					AStarNode neighborNode = (AStarNode) i.next();
					if (!closedList.contains(neighborNode) && !openList.contains(neighborNode)) {
						neighborNode.pathParent = node;
						openList.add(neighborNode);
					}
				}
			}
		}

		// no path found
		return null;
	}

	public void init(int width, int height, byte[] maskdata) {
		this.width = width;
		this.height = height;
		nodes = new AStarNode[width * height];
		// ��ʼ�����
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// ����ϵת��
				if (maskdata[x + y * width] > 0) {
					nodes[x + (height - y - 1) * width] = new AStarNode(x, height - y - 1);
					// �ж�8����ĵ��Ƿ񵽴�

				}
			}
		}
		// �ж�8����ĵ��Ƿ񵽴�
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				AStarNode node = getNode(x, y);
				if (node != null) {
					// �� ��
					AStarNode n = getNode(x - 1, y);
					if (n != null) {
						node.neighbors.add(n);
					}
					// ���� �I
					n = getNode(x - 1, y - 1);
					if (n != null) {
						node.neighbors.add(n);
					}
					// �� ��
					n = getNode(x, y - 1);
					if (n != null) {
						node.neighbors.add(n);
					}
					// ���� �J
					n = getNode(x + 1, y - 1);
					if (n != null) {
						node.neighbors.add(n);
					}
					// �� ��
					n = getNode(x + 1, y);
					if (n != null) {
						node.neighbors.add(n);
					}
					// ���� �K
					n = getNode(x + 1, y + 1);
					if (n != null) {
						node.neighbors.add(n);
					}
					// �� ��
					n = getNode(x, y + 1);
					if (n != null) {
						node.neighbors.add(n);
					}
					// ���� �L
					n = getNode(x - 1, y + 1);
					if (n != null) {
						node.neighbors.add(n);
					}
				}
			}
		}
	}

	private AStarNode[] nodes;

	public AStarNode getNode(int x, int y) {
		try {
			return nodes[x + y * width];
		} catch (Exception e) {
		}
		return null;
	}

	public AStarNode getNearstNode(int x, int y) {
		AStarNode node = null;
		try {
			node = nodes[x + y * width];
			while (node == null) {
				// TODO
			}
		} catch (Exception e) {
		}
		return node;
	}

	public boolean pass(int x, int y) {
		return nodes[x + y * width] != null;
	}

	public List<Point> findPath(int x1, int y1, int x2, int y2) {
		AStarNode startNode = getNode(x1, y1);
		AStarNode goalNode = getNode(x2, y2);
		if (goalNode == null) {
			return null;
		}
		List<AStarNode> nodepath = this.findPath(startNode, goalNode);
		List<Point> path = new ArrayList<Point>(nodepath.size());
		for (AStarNode node : nodepath) {
			path.add(new Point(node.x, node.y));
		}
		return path;

	}
}
