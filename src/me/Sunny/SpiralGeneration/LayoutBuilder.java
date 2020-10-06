package me.Sunny.SpiralGeneration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import me.Sunny.SpiralGeneration.RoomNode.Direction;
import me.Sunny.SpiralGeneration.Utils.Point;

/**
 * Creates the rooms layout with coordinates from abstract layout.
 * @author Daniel Dovgun
 * @version 9/24/2020
 */
public class LayoutBuilder {
	
	private static final String FIND_DIRECTION_ERROR = "ERROR: The input in 'find direction' function is incorect: ";
	
	private final String path;
	private List<Point> usedPoints;
	
	/**
	 * Construct the layout builder.
	 * @param path The path of the layout.
	 */
	public LayoutBuilder(String path) { 
		usedPoints = new ArrayList<Point>();
		this.path = path;
	}
	
	/**
	 * Builds the room node tree.
	 * @return Room node tree.
	 */
	public RoomNode Build()
	{
		// Reads the abstract representation of the layout (without the coordinates):
		LayoutNode template = Deserialization.Deserialize(path);
		
		// Adds the first node the tree:
		RoomNode root = new RoomNode(Point.ORIGIN, template.getID());
		usedPoints.add(Point.ORIGIN);
		
		// Recursively adds the children of the root node:
		root = addRoom(root, template, RoomNode.Direction.NORTH);
		
		//  Calculates each room orientation by preset rules:
		//  (The root node is calculated a bit different as it is the first node in the tree).
		updateOrientation(root);
		updateRootOrientation(root);
		
		return root;
	}

	/**
	 * Add recursively the child nodes to a parent node.
	 * @param parentNode Parent node.
	 * @param template Abstract tree template.
	 * @param previousDirection The direction from the parent of this node to the current one.
	 * @return Child node.
	 */
	private RoomNode addRoom(RoomNode parentNode, LayoutNode template, RoomNode.Direction previousDirection) {
		if (template == null) { 
			return parentNode;
		}
		
		int childSum = template.getChildSum();
		if (childSum == 0) // Leaf node of the tree.
			return parentNode;
		
		List<RoomNode.Direction> dirList = randomizeDirections(parentNode.getLocation(), childSum, previousDirection, usedPoints);
		
		for (int i = 1; i <= childSum; i++) {
			LayoutNode childTemplate = template.getNode(i);
			RoomNode.Direction childDirection = dirList.get(0);
			dirList.remove(0);
			
			// Calculating the child location and adding it to the usedPoints list.
			Point childLocation = findLocation(parentNode.getLocation(), childDirection);	
			usedPoints.add(childLocation);
			
			RoomNode childNode = new RoomNode(childLocation, childTemplate.getID());
			// We set the direction from child to parent while the findDirection method returns parent to child:
			Direction childOrientation = findDirection(parentNode.getLocation(), childNode.getLocation());
			childNode.setParentDirection(RoomNode.getOpposite(childOrientation));
			
			addRoom(childNode, childTemplate, childDirection);
			parentNode.setNode(childDirection, childNode);
		}	
		return parentNode;
	}
	
	/**
	 * Find a location of a point by using another point and a direction vector.
	 * @param location The first point.
	 * @param direrction The direction vector.
	 * @return The resulting point
	 */
	private static Point findLocation(Point location, RoomNode.Direction direrction) {
		switch (direrction) {
		case NORTH:
			return new Point(location.getX(), location.getY() - 1); // In Minecraft the north-south directions are switched.
		case EAST:
			return new Point(location.getX() + 1, location.getY());
		case SOUTH:
			return new Point(location.getX(), location.getY() + 1); // In Minecraft the north-south directions are switched.
		case WEST:
			return new Point(location.getX() - 1, location.getY());
		default:
			return Point.ORIGIN;
		}
	}
	
	/**
	 * Finds the direction of a child node of a parent node.
	 * 	Note: it will only work in a coordinate system where the
	 *  empty space between the child and the parent nodes are set as 1.
	 * @param a The parent node location.
	 * @param b The child node location.
	 * @return The direction of a child node of a parent node.
	 */
	private static RoomNode.Direction findDirection(Point a, Point b) {
		Point directionVector = new Point(b.getX() - a.getX(), b.getY() - a.getY());
		if (directionVector.equals(Point.UP)) { return Direction.SOUTH; } // In Minecraft the north-south directions are switched.
		if (directionVector.equals(Point.LEFT)) { return Direction.WEST; }
		if (directionVector.equals(Point.DOWN)) { return Direction.NORTH; } // In Minecraft the north-south directions are switched.
		if (directionVector.equals(Point.RIGHT)) { return Direction.EAST; }
		
		System.out.println(FIND_DIRECTION_ERROR + a.toString() + '\t' + b.toString());
		return Direction.NORTH; // Default value.
	}

	/**
	 * Checks the surroundings for possible locations for the child room nodes.
	 * @param location The location of the current node.
	 * @param dirList List of directions to check against.
	 * @param ptList List of point coordinates that are already occupied by other nodes.
	 * @return List of possible directions to add the child nodes.
	 */
	private static List<RoomNode.Direction> checkBlockedDirections(Point location, List<RoomNode.Direction> dirList, List<Point> ptList){
		Map<RoomNode.Direction, Point> nearbyPoints = new HashMap<RoomNode.Direction, Point>();
		List<RoomNode.Direction> emptyDirList = new ArrayList<RoomNode.Direction>();
		
		for (RoomNode.Direction direction : dirList) {
			nearbyPoints.put(direction, findLocation(location, direction));
		}
		
		for (RoomNode.Direction direction : dirList) {
			if ( !ptList.contains(nearbyPoints.get(direction)) ) {
				emptyDirList.add(direction);
			}
		}
		
		return emptyDirList;
	}
	
	// TODO: Update the generation script to include re-randomizing on dead-end graphs.
	/**
	 * Randomize the directions of which the child nodes will be added.
	 * @param location The location of the current node.
	 * @param childSum The sum of the node`s children.
	 * @param lastDirection The direction from the parent node to the current node.
	 * @param ptList List of points that are already occupied.
	 * @return The directions of which the child nodes will be added.
	 */
	private static List<RoomNode.Direction> randomizeDirections(Point location, int childSum, RoomNode.Direction lastDirection, List<Point> ptList)
	{
		// Creates a list of all the four possible directions and subtract the one leading to the parent node or that are already blocked:
		List<RoomNode.Direction> dirList = Stream.of(
				RoomNode.Direction.NORTH, RoomNode.Direction.EAST, RoomNode.Direction.SOUTH, RoomNode.Direction.WEST
				).collect(Collectors.toList());
		lastDirection = RoomNode.getOpposite(lastDirection); 
		dirList.remove(lastDirection); 
		dirList = checkBlockedDirections(location, dirList, ptList);
		
		if (childSum > dirList.size()) {
			// TODO: Find a way to restart the generation from here.
			System.out.println("ERROR: There are " +  childSum + " child nodes to add. But only " + dirList.size() + " empty spots.");
			return null;
		}
		Random rand = new Random();
		
		// Return a list of the picked directions depending on the amount of links the node should have:
		switch (childSum) {
		case AbstractNode.ONE_NODE:
			List<RoomNode.Direction> oneDirList = new ArrayList<RoomNode.Direction>();
			oneDirList.add(pickRandDirection(dirList, rand));
			return oneDirList;
		case AbstractNode.TWO_NODES:
			List<RoomNode.Direction> twoDirList = new ArrayList<RoomNode.Direction>();
			twoDirList.add(pickRandDirection(dirList, rand));
			twoDirList.add(pickRandDirection(dirList, rand));
			return twoDirList;
		case AbstractNode.THREE_NODES:
			return dirList;		
		}
		
		return dirList;
	}
	
	/**
	 * Picks a random direction from a direction list.
	 * @param dirList Direction list.
	 * @param rng java.utils Random class.
	 * @return random direction from the direction list.
	 */
	private static RoomNode.Direction pickRandDirection(List<RoomNode.Direction> dirList, Random rng) {
		int index = rng.nextInt(dirList.size());
		RoomNode.Direction direction = dirList.get(index);
		dirList.remove(index);
		return direction;
	}
	
	/**
	 * Recursively updates the orientation of a RoomNode tree.
	 * @param roomNode The tree to traverse and update.
	 */
	private static void updateOrientation(RoomNode parentNode)
	{
		parentNode.setOrientation();
		RoomNode childNode = null;
		
		childNode = parentNode.getNorthNode();
		if (childNode != null)
			updateChildOrientation(parentNode, childNode);
		
		childNode = parentNode.getEastNode();
		if (childNode != null)
			updateChildOrientation(parentNode, childNode);
		
		childNode = parentNode.getSouthNode();
		if (childNode != null) 
			updateChildOrientation(parentNode, childNode);
		
		childNode = parentNode.getWestNode();
		if (childNode != null) 
			updateChildOrientation(parentNode, childNode);
	}
	
	/**
	 * Calculates the updated child orientation.
	 * @param parentNode The parent node.
	 * @param childNode The child node.
	 */
	private static void updateChildOrientation(RoomNode parentNode, RoomNode childNode) {
		if (childNode != null) {
			if (childNode.isLeaf()) {
				childNode.setOrientation(childNode.getParentDirection());
			}
			else
				updateOrientation(childNode); 
		}
	}
	
	/**
	 * Calculates the root orientation.
	 * @param root Root node of the tree.
	 */
	private static void updateRootOrientation(RoomNode root) {
		RoomNode rootChild = null;
		
		for (int i = 1; i <= 4; i++) {
		if (root.getNode(i) != null) 
			rootChild = root.getNode(i);
		}
		
		root.setOrientation(findDirection(root.getLocation(), rootChild.getLocation()));
	}
}
