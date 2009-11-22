package com.javaxyq.search;

//import java.util.Vector;

/**
 * �ڵ���
 * @author SunFruit
 *
 */
public class Node {
	String id;
	
	int x;
    
    int y;
    
    //�ɵ�ǰ��Ϊ����������·����
//    private Vector lines;
    
    Node previous;
    
    //����㣬���Ų����·��������ǰ�����·��?
    int G;
    
    //�ӵ�ǰ�㵽�յ�B��Ԥ���ƶ��ķѡ�
    int H;
    boolean pass; //pass or not
   // private boolean isLoadEdge = false;
 //   int xmax;int ymax; //the width and height of the map
  //  private Vector around;
    public Node(int x, int y)
    {
        //this.id=id;
        this.x = x;
        this.y = y;this.id=String.valueOf(x)+":"+String.valueOf(y);
        //this.block=pass;
      //  this.xmax=width;this.ymax=height;
    }
    
    /**
     * ����Ըõ�Ϊ��������Line
     * @return
     */
 
    
    public int F() {
        return G + H;
    }
/*    public int hashCode() {
        final int prime = 31;
        int result = 17;
        result = prime * result + ((int) id);
        return result;
    }
 */   
/*    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        
        if (obj == null)
            return false;
        
        if (!(obj instanceof Node)) {
            return false;
        }
        
        final Node other = (Node) obj;
        return id == other.id;
    }
    */
    public String toString() {
        return id;
            
    }
  
}