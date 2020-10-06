package me.Sunny.SpiralGeneration;

/**
 * Node class to represent the dungeon`s rooms general layout.
 * @author Daniel Dovgun
 * @version 9/17/2020
 */
public class LayoutNode extends AbstractNode {
	
	public LayoutNode(String ID) {
		super.setID(ID);
		setFirstNode(null);
		setSecondNode(null);
		setThirdNode(null);
	}
	
	public String getID() { return super.getID(); }
	public LayoutNode getFirstNode() { return (LayoutNode)super.getFirstNode(); }
	public LayoutNode getSecondNode() { return (LayoutNode)super.getSecondNode(); }
	public LayoutNode getThirdNode() { return (LayoutNode)super.getThirdNode(); }
	
	private void setFirstNode(LayoutNode layoutNode) { super.setFirstNode(layoutNode); } 
	private void setSecondNode(LayoutNode layoutNode) { super.setSecondtNode(layoutNode); }
	private void setThirdNode(LayoutNode layoutNode) { super.setThirdNode(layoutNode); }

	/**
	 * Gets a specific children node by the index.
	 * @param index The index of the node to get (1-3);
	 * @return The specified children node.
	 */
	public LayoutNode getNode(int index) {
		switch (index) {
		case 1:
			return getFirstNode();
		case 2:
			return getSecondNode();
		case 3: 
			return getThirdNode();
		}
		return null;
	}
	
	/**
	 * Adds a node as a children to the current node.
	 * Can add up to 3 children per node. 
	 * @param layoutNode The node to add as a children.
	 * @return Was the node successfully added?
	 */
	public boolean addNode(LayoutNode layoutNode) {
		int fullNodes = getChildSum();
		switch (fullNodes) {
			case 0:
				super.setFirstNode(layoutNode);
				return true;
			case 1:
				super.setSecondtNode(layoutNode);
				return true;
			case 2:
				super.setThirdNode(layoutNode);
				return true;
			case 3:
				return false;
		}
		return false;	
	}

	/**
	 * Counts the number of children of the current node.
	 * @return The number of children of the current node.
	 */
	public int getChildSum() {
		if (getFirstNode() == null)
			return NO_NODES;
		else if (getSecondNode() == null)
			return ONE_NODE;
		else if (getThirdNode() == null)
			return TWO_NODES;
		else return THREE_NODES;
	}
	
	/**
	 * Checks if the current node is a leaf node.
	 */
	public boolean isLeaf() {
		return ( getChildSum() == 0 );
	}
	
	@Override
	public String toString() {
		String firstNode = (getFirstNode() == null) ? "-" : getFirstNode().getID();
		String secondNode = (getSecondNode() == null) ? "-" : getSecondNode().getID();
		String thirdNode = (getThirdNode() == null) ? "-" : getThirdNode().getID();
		
		return "[ " + getID() + " ] : [ " + firstNode + " ] [ " + secondNode + " ] [ " + thirdNode + " ] ";
	}
}
