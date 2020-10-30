package me.Sunny.SpiralGeneration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import me.Sunny.SpiralCraft.Triggers.Triggable;
import me.Sunny.SpiralGeneration.Utils.Point;

public class RoomNode extends AbstractNode
{
	private static final String MISSING_LINKS_NODE_ERROR = "ERROR: The current node is not linked to any other node.";

	/**
	 * Represents the four directions.
	 */
	public enum Direction{
		NORTH,
		EAST,
		SOUTH,
		WEST
	}
	
	private final Point location; // The abstract location for the algorithm.
	private Point chunkLocation; // The real location of the room in a chunk map.
	private Direction orientation;
	private Direction parentDirection;
	private boolean isAlternative; // Distinguish between a node with two variants. (Two links node only: L 'variant' and I 'variant').

	private Triggable spawnTrigger;

	/**
	 * Constructs a room node with location and ID.
	 * @param location Abstract location of the node.
	 * @param ID ID of the node.
	 */
	public RoomNode(Point location, String ID) {
		setNorthNode(null);
		setEastNode(null);
		setSouthNode(null);
		setWestNode(null);
		setID(ID);
		this.location = (location == null) ? Point.ORIGIN : location;	
		orientation = Direction.NORTH; // Default input
		isAlternative = false;
	}
	
	public String getID() { return super.getID(); }
	public RoomNode getNorthNode() { return (RoomNode)super.getFirstNode(); }
	public RoomNode getEastNode() { return (RoomNode)super.getSecondNode(); }
	public RoomNode getSouthNode() { return (RoomNode)super.getThirdNode(); }
	public RoomNode getWestNode() { return (RoomNode)super.getFourthNode(); }
	public Point getLocation() { return location; }
	public Point getChunkLocation() { return chunkLocation; }
	public Direction getOrientation() { return orientation; }
	public Direction getParentDirection() { return parentDirection; }
	public boolean getIsAlternative() { return isAlternative; }
	public Triggable getSpawnTrigger() { return spawnTrigger; }

	public void setID(String ID) { super.setID(ID);}
	public void setNorthNode(RoomNode node) { super.setFirstNode(node); }
	public void setEastNode(RoomNode node) { super.setSecondtNode(node); }
	public void setSouthNode(RoomNode node) { super.setThirdNode(node); }
	public void setWestNode(RoomNode node) { super.setFourthNode(node); }
	public void setOrientation() { updateOrientation(); } 
	public void setOrientation(Direction orientation) { this.orientation = orientation; }
	public void setParentDirection(Direction parentDirection) { this.parentDirection = parentDirection; }
	public void setSpawnTrigger(Triggable spawnTrigger) { this.spawnTrigger = spawnTrigger; }
	public void setChunkLocation(Point chunkLocation) { this.chunkLocation = chunkLocation; }

	//TODO: Change index to nodeDirection...
	/**
	 * Get a child node at a specific index.
	 * @param index Index of the child node.
	 * @return Child node.
	 */
	public RoomNode getNode(int index) {
		switch (index) {
		case 1:
			return getNorthNode();
		case 2:
			return getEastNode();
		case 3: 
			return getSouthNode();
		case 4:
			return getWestNode();
		}
		return null;
	}

	/**
	 * Sets a child node for the parent node in a given direction.
	 * @param nodeDirection Direction of the child node from the parent node.
	 * @param roomNode Child node.
	 */
	public void setNode(Direction nodeDirection, RoomNode roomNode) {
		switch (nodeDirection) {
		case NORTH:
			setNorthNode(roomNode);
			break;
		case EAST:
			setEastNode(roomNode);
			break;
		case SOUTH:
			setSouthNode(roomNode);
			break;
		case WEST:
			setWestNode(roomNode);
			break;
		}
	}
	
	/**
	 * Checks if the current node is a leaf node.
	 */
	public boolean isLeaf() {
		return ((super.getFirstNode() == null) && (super.getSecondNode() == null) &&
				(super.getThirdNode() == null) && (super.getFourthNode() == null)); }

	/**
	 * Calculates the opposite direction of the given direction.
 	 * @param direction Given direction.
	 * @return Opposite direction.
	 */
	public static Direction getOpposite(Direction direction) {
		switch (direction) {
		case NORTH:
			return Direction.SOUTH;
		case EAST:
			return Direction.WEST;
		case SOUTH:
			return Direction.NORTH;
		case WEST:
			return Direction.EAST;
		default:
			return null;
		}
	}
	
	/**
	 * Calculates the current node children amount.
	 */
	public int getChildSum() {
		int childSum = 0;
		if (getNorthNode() != null) { ++childSum; }
		if (getEastNode() != null) { ++childSum; }
		if (getSouthNode() != null) { ++childSum; }
		if (getWestNode() != null) { ++childSum; }
		return childSum;
	}

	/**
	 * Updates the default NORTH orientation of the node by checking both the amount of links (child sum) and their location.
	 */
	private void updateOrientation() {
		int linkSum = (parentDirection != null ) ? getChildSum() + 1 : getChildSum();
		switch (linkSum) { //TODO: TODO: TODO: There is also parent direction that should be included.
		case 0: 
			System.out.print(MISSING_LINKS_NODE_ERROR);
			return;
		case 4: // There is no orientation needed in a node with 4 children.
			return;
		case 1: // If a node got only one children it only got a connection with its parent.
			orientation = parentDirection;
			return;
		case 2:
			orientation = getTwoLinkDirection();
			return;
		case 3:
			orientation = getThreeLinkDirection();
			break;
		}
	}
	
	/**
	 * Builds a hash set of the node`s directions to other nodes (Including the parent node).
	 * @return Hash set of the node`s directions to other nodes.
	 */
	private HashSet<Direction> buildDirHashSet() {
		HashSet<Direction> dirHashSet = new HashSet<>();
		
		if (getParentDirection() != null) { dirHashSet.add(parentDirection); }
		if (getNorthNode() != null) { dirHashSet.add(Direction.NORTH); }
		if (getEastNode() != null) { dirHashSet.add(Direction.EAST); }
		if (getSouthNode() != null) { dirHashSet.add(Direction.SOUTH); }
		if (getWestNode() != null) { dirHashSet.add(Direction.WEST); }
	
		return dirHashSet;
	}
		
	/**
	 * Calculates the Orientation of a node with two children.
	 * @return the Orientation of a node with two children.
	 */
	private Direction getTwoLinkDirection() {
		HashSet<Direction> dirHashSet = buildDirHashSet();
		
		// The node can be either in | format or in L format.
	
		// Checks if the node is in | format and if yes, in which:
		if (dirHashSet.contains(Direction.NORTH) && dirHashSet.contains(Direction.SOUTH)) { isAlternative = true; return Direction.NORTH; }
		if (dirHashSet.contains(Direction.EAST) && dirHashSet.contains(Direction.WEST)) { isAlternative = true; return Direction.EAST; }
		
		// Checks if the node is in L format and if yes, in which:
		if (dirHashSet.contains(Direction.NORTH) && dirHashSet.contains(Direction.EAST)) { return Direction.NORTH; }
		if (dirHashSet.contains(Direction.EAST) && dirHashSet.contains(Direction.SOUTH)) { return Direction.EAST; }
		if (dirHashSet.contains(Direction.SOUTH) && dirHashSet.contains(Direction.WEST)) { return Direction.SOUTH; }
		if (dirHashSet.contains(Direction.WEST) && dirHashSet.contains(Direction.NORTH) ) { return Direction.WEST;}
		
		// Default return value.
		return Direction.NORTH;
	}
	
	/**
	 * Calculates the Orientation of a node with three children.
	 * @return the Orientation of a node with three children.
	 */
	private Direction getThreeLinkDirection() {
		HashSet<Direction> dirHashSet = buildDirHashSet();
		
		/* 
		 * The orientation is based on the top link location:
		 *      x
		 *     _|_				x - Is the returned direction.
		 */
		
		if (dirHashSet.contains(Direction.NORTH) && dirHashSet.contains(Direction.EAST) &&
				dirHashSet.contains(Direction.SOUTH)) { return Direction.EAST; }
		if (dirHashSet.contains(Direction.EAST) && dirHashSet.contains(Direction.SOUTH) &&
				dirHashSet.contains(Direction.WEST)) { return Direction.SOUTH; }
		if (dirHashSet.contains(Direction.SOUTH) && dirHashSet.contains(Direction.WEST) &&
				dirHashSet.contains(Direction.NORTH)) { return Direction.WEST; }
		if (dirHashSet.contains(Direction.WEST) && dirHashSet.contains(Direction.NORTH) &&
				dirHashSet.contains(Direction.EAST)) { return Direction.NORTH; }

		// Default return value.
		return Direction.NORTH;
	}

	@Override
	public String toString() {
		String northNode = (getNorthNode() == null) ? AbstractNode.NULL_SIGN : getNorthNode().getID();
		String eastNode = (getEastNode() == null) ? AbstractNode.NULL_SIGN : getEastNode().getID();
		String southNode = (getSouthNode() == null) ? AbstractNode.NULL_SIGN : getSouthNode().getID();
		String westNode = (getWestNode() == null) ? AbstractNode.NULL_SIGN : getWestNode().getID();
		String parentDir = (parentDirection == null) ? AbstractNode.NULL_SIGN : parentDirection.name();
		String nodeOrientation = (orientation == null) ? AbstractNode.NULL_SIGN : orientation.name();

		return AbstractNode.NODE_PREFIX + this.getID() + AbstractNode.NODE_ADV_SPLIT + this.location.toString() +
				AbstractNode.NODE_ADV_SPLIT + northNode + AbstractNode.NODE_SPLIT + eastNode + AbstractNode.NODE_SPLIT +
				southNode + AbstractNode.NODE_SPLIT + westNode + AbstractNode.NODE_ADV_SPLIT + nodeOrientation +
				AbstractNode.NODE_ADV_SPLIT + parentDir + AbstractNode.NODE_SUFFIX;
	}
}