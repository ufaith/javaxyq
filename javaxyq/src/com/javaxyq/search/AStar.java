package com.javaxyq.search;

import java.awt.Point;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

//import org.apache.log4j.Logger;

/**
 * @author SunFruit
 */
public class AStar {
    private Node[][] nodes;

    private int width, height;

    private List<Point> result = new ArrayList<Point>();

    //  Logger logger =  Logger.getLogger(AStar.class);;
    /**
     * Find the shortest path between the two specified Vertices
     * 
     * @param from
     * @param to
     */
    public void computeShortPath(Node from, Node to) {
        this.result.clear();
        if (from == null || to == null) {
            return;
        }
        Node temp = nodes[to.x][to.y];
        if (temp == null || !temp.pass) {
            return;
        }
        
        HashMap openMap = new HashMap();
        HashMap closeMap = new HashMap();
        from.previous = null;
        from.G = 0;
        from.H = 0;
        openMap.put(from.id, from);
        Node cur = from;
        int count = 0;
        do {
            count++;
            String curId = cur.id;
            openMap.remove(curId);
            closeMap.put(curId, cur);
            Vector edges = getEdges(cur);
            if (edges == null)
                continue;
            Iterator it = edges.iterator();
            while (it.hasNext()) {
                Line line = (Line) it.next();
                Node v = line.getToNode();
                String vId = v.id;
                if (closeMap.containsKey(vId)) {
                    continue;
                } else if (openMap.containsKey(vId)) {
                    Node openV = (Node) openMap.get(vId);
                    int G = cur.G + line.getWeight();
                    if (openV.G > G) {
                        openV.previous = cur;
                        openV.G = G;
                    }
                } else {
                    v.previous = cur;
                    v.G = cur.G + line.getWeight();
                    v.H = getH(v, to);
                    openMap.put(vId, v);
                }
            }

            it = openMap.values().iterator();
            Node min = null;

            while (it.hasNext()) {
                Node v = (Node) it.next();
                if (min == null || v.F() < min.F()) {
                    min = v;
                }
            }
            cur = min;
        } while (!openMap.isEmpty() && !(to.id.equals(cur.id)));
        //    logger.debug("loop count:" + count);
        //System.out.println("loop count:"+ count);

        if (cur != null && to.id.equals(cur.id)) {
            createPath(cur);
        } else {
            System.out.println("not found the path from " + from.id + " to " + to.id);
        }

    }

    private int getH(Node v, Node todoV) {
        return 10 * Math.abs(v.x - todoV.x) + 10 * Math.abs(v.y - todoV.y);
    }

    private void createPath(Node v) {
        result.clear();
        while (v != null) {
            result.add(0, new Point(v.x, v.y));
            v = v.previous;
        }
    }

    public Vector getEdges(Node node) {
        Vector vector = new Vector();
        int x = node.x;
        int y = node.y;
        int x1;
        int y1;
        Node point;

        x1 = x - 1;
        y1 = y - 1;
        if (x1 >= 0 && y1 >= 0) {
            point = nodes[x1][y1];
            if (point.pass) {
                vector.add(new Line(node, point));
            }
        }//左上角

        x1 = x;
        y1 = y - 1;
        if (y1 >= 0) {
            point = nodes[x1][y1];
            if (point.pass) {
                vector.add(new Line(node, point));
            }
        }//正上方

        x1 = x + 1;
        y1 = y - 1;
        if (x1 < width && y1 >= 0) {
            point = nodes[x1][y1];
            if (point.pass) {
                vector.add(new Line(node, point));
            }
        }//右上角

        x1 = x - 1;
        y1 = y;
        if (x1 >= 0) {
            point = nodes[x1][y1];
            if (point.pass) {
                vector.add(new Line(node, point));
            }
        }//左边

        x1 = x + 1;
        y1 = y;
        if (x1 < width) {
            point = nodes[x1][y1];
            if (point.pass) {
                vector.add(new Line(node, point));
            }
        }//右边

        x1 = x - 1;
        y1 = y + 1;
        if (x1 >= 0 && y1 < height) {
            point = nodes[x1][y1];
            if (point.pass) {
                vector.add(new Line(node, point));
            }
        }//左下角

        x1 = x;
        y1 = y + 1;
        if (y1 < height) {
            point = nodes[x1][y1];
            if (point.pass) {
                vector.add(new Line(node, point));
            }
        }//正下方

        x1 = x + 1;
        y1 = y + 1;
        if (x1 < width && y1 < height) {
            point = nodes[x1][y1];
            if (point.pass) {
                vector.add(new Line(node, point));
            }
        }//右下角

        return vector;
    }

    private void initNodes(byte[][] map) {
        width = map.length;
        height = map[0].length;
        Node nodes[][] = new Node[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Node node = new Node(i, j);
                node.pass = (map[i][j] == 1) ? true : false;
                nodes[i][j] = node;
            }
        }
        this.nodes = nodes;
    }

    public void init() {
        try {
            InputStream stream = AStar.class.getResourceAsStream("block.txt");
            InputStreamReader reader = new InputStreamReader(stream);
            char[] text = new char[1024000];
            reader.read(text);
            StringBuffer txt = new StringBuffer();
            txt.append(text);
            reader.close();
            txt.trimToSize();
            String string = txt.toString();
            txt = null;
            String[] child = string.split("\n");
            String[] col = child[0].split(" ");
            int line = child.length;
            int column = col.length;
            byte[][] map = new byte[line][column];
            for (int i = 0; i < line; i++) {
                String[] temp = child[i].split(" ");
                for (int j = 0; j < column; j++) {
                    map[i][column - 1 - j] = Byte.valueOf(temp[j].trim());
                }
            }
            initNodes(map);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void main(String[] args) {
        AStar s = new AStar();
        s.init();
        Node from = new Node(10, 20);
        Node to = new Node(30, 54);
        s.computeShortPath(from, to);
        List<Point> path = s.result;
        System.out.printf("path:(%s,%s)->(%s,%s)", from.x, from.y, to.x, to.y);
        for (Point p : path) {
            System.out.printf("[%s,%s]\n", p.x, p.y);
        }
    }

    public List<Point> getResult() {
        return result;
    }

    public boolean pass(int x, int y) {
        return this.nodes[x][y].pass;
    }
}
