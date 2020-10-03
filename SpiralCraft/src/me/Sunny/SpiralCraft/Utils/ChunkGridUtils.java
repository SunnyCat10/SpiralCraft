package me.Sunny.SpiralCraft.Utils;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import me.Sunny.SpiralCraft.Data.SpiralPlayer;
import me.Sunny.SpiralGeneration.RoomNode;
import me.Sunny.SpiralGeneration.Utils.Point;

/**
 * Utility class for chunk grid snapping.
 * @author Daniel Dovgun
 * @version 10/2/2020
 */
public class ChunkGridUtils {

	/**
	 * The current room size used in game:
	 */
	private static final int ROOM_SIZE = 16;
	
	/**
	 * Private constructor
	 */
	private ChunkGridUtils() {}
	
	/**
	 * Utility method to snap the rooms to the tile grid.
	 * For example given the coordinates 100,0,100 the method will return the origin point that will align with the chunks.
	 * @param spiralPlayer SpiralPlayer.
	 * @param coordinates The raw spawn coordinates.
	 * @return Processed spawn coordinates aligned with the chunk grid system.
	 */
	public static Vector snapOrigin(SpiralPlayer spiralPlayer, Vector coordinates) {	
		Chunk originChunk = spiralPlayer.getPlayer().getWorld().getBlockAt(
				coordinates.getBlockX(), coordinates.getBlockY(), coordinates.getBlockZ()).getChunk();
		Block cornerBlock = originChunk.getBlock(0, coordinates.getBlockY(), 0);
		Vector originPoint = new Vector(cornerBlock.getLocation().getX(),
				cornerBlock.getLocation().getY(),
				cornerBlock.getLocation().getZ());
		return originPoint;
	} 
	
	/**
	 * Utility method that converts abstract coordinates from the generation algorithm,
	 * and convert them into coordinates snapped to the minecraft chunk grid.
	 * @param originPoint Origin point of the level.
	 * @param roomLocation Abstract coordinate representation based on the generation algorithm.
	 * @return minecraft Coordinates snapped to the chunk grid.
	 */
	public static Vector snapRoom(Vector floorOrigin, Point roomLocation) {		
		int abstractX = roomLocation.getX();
		int abstractZ = roomLocation.getY();
					
		double updatedX = floorOrigin.getBlockX() + (abstractX * ROOM_SIZE);
		double updatedZ = floorOrigin.getBlockZ() + (abstractZ * ROOM_SIZE);
		
		return new Vector(updatedX , floorOrigin.getY(), updatedZ);
	}
	
	/**
	 * Utility method to snap an origin point that was transformed.
	 * 	When the origin point get transformed it location change and thus an update is required to snap it to the grid correctly.
	 * @param roomOrigin room origin point vector.
	 * @param orientation The orientation of the transformation.
	 * @return origin point vector that is snapped to the chunk grid.
	 */
	public static Vector snapTransformedOrigin(Vector roomOrigin, RoomNode.Direction orientation) {
		switch(orientation) {
		default:
		case NORTH: // Transformation of 0 degrees:
			return roomOrigin;
		case EAST: // Transformation of 270 degrees:
			roomOrigin.setX( roomOrigin.getX() + (ROOM_SIZE - 1) );
			return roomOrigin;
		case SOUTH: // Transformation of 180 degrees:
			roomOrigin.setX( roomOrigin.getX() + (ROOM_SIZE - 1) );
			roomOrigin.setZ( roomOrigin.getZ() + (ROOM_SIZE - 1) );
			return roomOrigin;
		case WEST: // Transformation of 90 degrees:
			roomOrigin.setZ( roomOrigin.getZ() + (ROOM_SIZE - 1) );
			return roomOrigin;
		} 
	}
	
	/**
	 * Calculates the spawn point of the level (floor).
	 * @param floorOrigin Origin point vector of the floor.
	 * @return Spawn point vector of the level (floor).
	 */
	public static Vector getSpawnPoint(Vector floorOrigin) {
		return new Vector(
				(floorOrigin.getBlockX() + (ROOM_SIZE / 2)),
				floorOrigin.getBlockY() + 1,
				floorOrigin.getBlockZ() + (ROOM_SIZE / 2));
	}
}
