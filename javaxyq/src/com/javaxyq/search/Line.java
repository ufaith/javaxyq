package com.javaxyq.search;

/**
 * ��·�࣬����㡢�յ㡢Ȩ�����
 * @author SunFruit
 *
 */
public class Line {
	private Node from;
	private Node to;
	
	//Ȩ�أ���ǰ��·�ĳ��ȡ���·״��ȶ�������ΪȨ�ص�һ���֣�Ŀǰֻ�ǳ���?
	private int weight;
	
	public Line()
	{
		this(null,null,0);
	}
	
	public Line(Node from,Node to)
	{
		this.from=from;
		this.to=to;
                setWeight();
	}
	
	public Line(Node from,Node to,int weight)
	{
		this.from=from;
		this.to=to;
	    this.weight=weight;
    }

    public Node getFromNode() {
        return from;
    }

    public void setFromNode(Node from) {
        this.from = from;
    }

    public Node getToNode() {
        return to;
    }

    public void setToNode(Node to) {
        this.to = to;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight() {
        int stepx=from.x-to.x; int stepy=from.y-to.y;
        this.weight = (int)(10*Math.sqrt(stepx*stepx+stepy*stepy));
    }
/*   public String toString() {
        return from.toString()+" "+to.toString()+" "+String.valueOf(weight);
            
    }  
  */   
}
