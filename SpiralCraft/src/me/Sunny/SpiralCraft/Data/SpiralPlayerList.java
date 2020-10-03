package me.Sunny.SpiralCraft.Data;

import java.util.Hashtable;
import java.util.UUID;

/**
 * SpiralPlayer HashTable to allow quick access between Player object and SpiralPlayer object.
 * @author Daniel Dovgun
 * @version 9/27/2020
 */
public final class SpiralPlayerList {
	
	/**
	 * Holds all the SpiralPlayers that are currently online.
	 */
	private static Hashtable<UUID, SpiralPlayer> spiralPlayerList = new Hashtable<UUID, SpiralPlayer>();
	
	/**
	 * Error message for when the player was not found in game.
	 */
	private static final String PLAYER_NOT_FOUND = "The current player was no found.";
	
	/**
	 * Private constructor to not allow instantiating this class.
	 */
	private SpiralPlayerList() {}
	
	/**
	 * Returns a SpiralPlayer from a player UUID.
	 * @param playerID Player UUID.
	 * @return SpiralPlayer representation of the Player.
	 */
	public static SpiralPlayer getSpiralPlayer(UUID playerID) {
		return spiralPlayerList.get(playerID);
	}
	
	/**
	 * Create a SpiralPlayer from a player`s UUID and add it to the SpiralPlayer list.
	 * 	Call this function when the player logs in.
	 * @param playerID Player UUID.
	 */
	public static void addPlayer(UUID playerID) {
		
		if (!(spiralPlayerList.containsKey(playerID))) {
			SpiralPlayer spiralPlayer = new SpiralPlayer(playerID);
			spiralPlayerList.put(playerID, spiralPlayer);
		}
		System.out.println(PLAYER_NOT_FOUND);
	}
	
	/**
	 * Removes a SpiralPlayer from the SpiralPlayer list.
	 * 	Call this function when the player logs off.
	 * @param playerID Player UUID.
	 */
	public static void removePlayer(UUID playerID) {
		if (spiralPlayerList.containsKey(playerID)) {
			spiralPlayerList.remove(playerID);
		}
		System.out.println(PLAYER_NOT_FOUND);
	}
}
