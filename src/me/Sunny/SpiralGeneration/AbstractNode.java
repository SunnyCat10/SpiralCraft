package me.Sunny.SpiralGeneration;

/**
 * The basis of all node classes.
 * @author Daniel Dovgun
 * @version 9/23/2020
 */
public abstract class AbstractNode {
	
	protected static final String NULL_SIGN = "-";
	protected static final String NODE_PREFIX = "[ ";
	protected static final String NODE_SUFFIX = " ]";
	protected static final String NODE_SPLIT = " ] [ ";
	protected static final String NODE_ADV_SPLIT = " ] : [ ";
	
	protected static final int NO_NODES = 0;
	protected static final int ONE_NODE = 1;
	protected static final int TWO_NODES = 2;
	protected static final int THREE_NODES = 3;
	protected static final int FOUR_NODES = 4; // TODO: Currently unused.
	
	private AbstractNode firstNode;
	private AbstractNode secondNode;
	private AbstractNode thirdNode;
	private AbstractNode fourthNode;
	private String ID;
	
	protected AbstractNode getFirstNode() { return firstNode; }
	protected AbstractNode getSecondNode() { return secondNode; }
	protected AbstractNode getThirdNode() { return thirdNode; }
	protected AbstractNode getFourthNode() { return fourthNode;}
	protected String getID() { return ID; }
	
	protected void setFirstNode(AbstractNode node) { firstNode = node; }
	protected void setSecondtNode(AbstractNode node) { secondNode = node; }
	protected void setThirdNode(AbstractNode node) { thirdNode = node; }
	protected void setFourthNode(AbstractNode node) { fourthNode = node; }
	protected void setID(String string) { ID = string; }
	
	public abstract boolean isLeaf();
	protected abstract int getChildSum();
}