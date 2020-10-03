package me.Sunny.SpiralCraft.Generation.Utils;

import me.Sunny.SpiralCraft.SpiralCraftPlugin;
import me.Sunny.SpiralGeneration.RoomNode;

/**
 * Utility class for building a corresponding path of layout node ID to schematic.
 * @author Daniel Dovgun
 * @version 9/26/2020
 */
public final class PathBuilder {
	
	// Schematic file naming options:
	private static final String SCHEM_FILE_PREFIX = "/room_";
   	private static final String SCHEM_FILE_SUFFIX = ".schem";
	
   	/**
   	 * This utility class should not be initiated.
   	 */
	private PathBuilder() {}
	
	/**
	 * Builds a string representation of a path from a RoomNode. 
	 * @param roomNode Room node based on which value the path will be built.
	 * @return String representation of a path from a RoomNode.
	 */
	public static String build(RoomNode roomNode) {
		String roomID = roomNode.getID();
		int childSum = (roomNode.getParentDirection() != null ) ? roomNode.getChildSum() + 1 : roomNode.getChildSum();
		if (childSum == 2) { // Check if the room is | or L pattern.
			if (roomNode.getIsAlternative()) 
				roomID += 'b';
			else
				roomID += 'a';
		}
		String output = SpiralCraftPlugin.getPluginFolderPath() + SCHEM_FILE_PREFIX + roomID + SCHEM_FILE_SUFFIX;
		return output;
	} 
}
