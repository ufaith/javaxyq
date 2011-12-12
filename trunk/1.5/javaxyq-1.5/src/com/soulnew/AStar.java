/*
 * AStar.java
 *
 * Created on April 1, 2007, 9:48 PM
 *
 
 */

package com.soulnew;

/**
 *@author soulnew@soulnew.com
 */
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.javaxyq.search.Searcher;


public class AStar implements Searcher{

	private int width;
	private int height;
	/** Creates a new instance of AStar */
	public AStar() {
	}

	public static class PriorityList extends LinkedList {

		public void add(Comparable object) {
			for (int i = 0; i < size(); i++) {
				if (object.compareTo(get(i)) <= 0) {
					add(i, object);
					return;
				}
			}
			addLast(object);
		}
	}

	/**
	 * Construct the path, not including the start node.
	 */
	protected List<AStarNode> constructPath(AStarNode node) {
		LinkedList<AStarNode> path = new LinkedList<AStarNode>();
		while (node.pathParent != null) {
			path.addFirst(node);
			node = node.pathParent;
		}
		return path;
	}

	/**
	 * ����������·��
	 * @param startNode
	 * @param goalNode
	 * @return
	 */
	public List<AStarNode> findPath(AStarNode startNode, AStarNode goalNode) {
		LinkedList openList = new LinkedList(); // Ҫ���ʵĽڵ�ŵ������
		//LinkedList closedList = new LinkedList(); // �Ѿ����ʹ��Ľڵ�ŵ������
		Set closedList = new HashSet(); // �Ѿ����ʹ��Ľڵ�ŵ������

		startNode.costFromStart = 0; // ������㵽�Լ��ľ�����0
		startNode.estimatedCostToGoal = startNode.getEstimatedCost(goalNode); // �õ����յ�Ĺ��Ƴɱ�,��ֵ�����
		startNode.pathParent = null; // �͹������һ��pathParent������¼,��һ���ڵ�,ͨ�������Ϳ����ҵ����
		openList.add(startNode); // �������� �������������ı���

		while (!openList.isEmpty()) {
			AStarNode node = (AStarNode) openList.removeFirst(); // ��Ҫ���ʵı���ȡ��һ����
			// if (node == goalNode) {
			// construct the path from start to goal
			// �ҵ��յ���,ֹͣ����,��ʼ����·��
			// }

			List neighbors = node.getNeighbors();
			for (int i = 0; i < neighbors.size(); i++) { // �������е��ھӽڵ�
				AStarNode neighborNode = (AStarNode) neighbors.get(i);
				boolean isOpen = openList.contains(neighborNode);
				// isOpen �����ж��ھӽڵ��ڲ��ڼ������ʵı���
				boolean isClosed = closedList.contains(neighborNode);
				// isClosed �����ж��ھӽڵ��ڲ����Ѿ����ʹ��ı���
				int costFromStart = node.costFromStart + node.getCost(neighborNode); // ��øýڵ�ɱ�

				// check if the neighbor node has not been
				// traversed or if a shorter path to this
				// neighbor node is found.
				if ((!isOpen && !isClosed) || costFromStart < neighborNode.costFromStart)
				// ����ھӽڵ��Ƿ�δ����,���ҵ�������ھӽڵ�ĸ���·��
				{
					neighborNode.pathParent = node;
					neighborNode.costFromStart = costFromStart;
					// neighborNode.estimatedCostToGoal =
					// neighborNode.getEstimatedCost(goalNode);
					// ���Ƶ��ص�ľ���ķ���,��ʹ��A*�ľ��峡��
					if (node != goalNode) {
						if (isClosed) {
							closedList.remove(neighborNode);
							// �ҵ��ýڵ�ĸ���·��,���·�����ѷ��ʹ��ı�������
						}
						if (!isOpen) {
							openList.add(neighborNode);
						}
					}
				}
			}
			closedList.add(node);
		}
		return constructPath(goalNode);

		// no path found
		// return null;
	}

	public static void main(String[] args) {
		AStarNode nodeA = new AStarNode("A", 0, 10);
		AStarNode nodeB = new AStarNode("B", 5, 15);
		AStarNode nodeC = new AStarNode("C", 10, 20);
		AStarNode nodeD = new AStarNode("D", 15, 15);
		AStarNode nodeE = new AStarNode("E", 20, 10);
		AStarNode nodeF = new AStarNode("F", 15, 5);
		AStarNode nodeG = new AStarNode("G", 10, 0);
		AStarNode nodeH = new AStarNode("H", 5, 5);

		nodeA.neighbors.add(nodeF);
		nodeA.neighbors.add(nodeC);
		nodeA.neighbors.add(nodeE);

		nodeC.neighbors.add(nodeA);
		nodeC.neighbors.add(nodeE);

		nodeE.neighbors.add(nodeA);
		nodeE.neighbors.add(nodeC);
		nodeE.neighbors.add(nodeF);

		nodeF.neighbors.add(nodeA);
		nodeF.neighbors.add(nodeE);

		AStar bfs = new AStar();
		System.out.println("From A to F: " + bfs.findPath(nodeA, nodeF).toString());
		System.out.println("From C to F: " + bfs.findPath(nodeC, nodeF).toString());
		System.out.println("From F to C: " + bfs.findPath(nodeF, nodeC));
		// System.out.println("From A to G: " +
		// bfs.findPath(nodeH, nodeG).toString());
		// System.out.println("From A to unknown: " +
		// bfs.findPath(nodeA, new AStarNode("unknown",0,0)).toString());

	}
	
	/**
	 * ��ʼ�����
	 * @param maskdata ��ͼ��������(width*height)
	 */
	public void init(int width,int height,byte[] maskdata) {
		this.width = width;
		this.height = height;
		nodes = new AStarNode[width*height];
		//��ʼ�����
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				//����ϵת��
				if(maskdata[x+y*width]>0) {
					nodes[x+(height-y-1)*width] = new AStarNode(x, height-y-1);
					//�ж�8����ĵ��Ƿ񵽴�
					
				}
			}
		}
		//�ж�8����ĵ��Ƿ񵽴�
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				AStarNode node = getNode(x, y);
				if(node!=null ) {
					//��   ��
					AStarNode n = getNode(x-1, y);
					if(n!=null) {
						node.neighbors.add(n);
					}
					//��  ��
					n = getNode(x+1, y);
					if(n!=null) {
						node.neighbors.add(n);
					}
					//��  ��
					n = getNode(x, y-1);
					if(n!=null) {
						node.neighbors.add(n);
					}
					//�� ��
					n = getNode(x, y+1);
					if(n!=null) {
						node.neighbors.add(n);
					}
					//���� �I
					n = getNode(x-1, y-1);
					if(n!=null) {
						node.neighbors.add(n);
					}
					//����  �J
					n = getNode(x+1, y-1);
					if(n!=null) {
						node.neighbors.add(n);
					}
					//���� �K
					n = getNode(x+1, y+1);
					if(n!=null) {
						node.neighbors.add(n);
					}
					//���� �L
					n = getNode(x-1, y+1);
					if(n!=null) {
						node.neighbors.add(n);
					}
				}
			}
		}
	}
	
	private AStarNode[] nodes;
	/**
	 * ��ȡĳ��ͨ�е�
	 * @param x
	 * @param y
	 * @return
	 */
	public AStarNode getNode(int x,int y) {
		try {
			return nodes[x+y*width];
		}catch(Exception e) {
		}
		return null;
	}
	
	/**
	 * ��þ���õ�����Ŀ�ͨ�е�
	 * @param x
	 * @param y
	 * @return
	 */
	public AStarNode getNearstNode(int x,int y) {
		AStarNode node = null;
		try {
			node = nodes[x+y*width];
			while(node == null) {
				//TODO
			}
		}catch(Exception e) {
		}
		return node;
	}

	/**
	 * �Ƿ����ͨ���õ�
	 * @return
	 */
	public boolean pass(int x,int y) {
		return nodes[x+y*width] != null;
	}
	
	public List<Point> findPath(int x1,int y1,int x2,int y2){
		AStarNode startNode = getNode(x1,y1);
		AStarNode goalNode = getNode(x2,y2);
		if(goalNode ==null) {
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
