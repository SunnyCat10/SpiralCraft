package me.Sunny.SpiralCraft.Levels;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import me.Sunny.SpiralCraft.Data.Party;
import me.Sunny.SpiralCraft.Data.PartyManager;
import me.Sunny.SpiralCraft.Data.SpiralPlayer;

/**
 * Manages all the dungeon levels on the server.
 * @author Daniel Dovgun
 * @version 10/3/2020
 */
public class LevelManager {
	
	// Origin point for the first Level to be generated.
	//private static final int X_0RIGIN = -300;
	//private static final int Y_ORIGIN = 33;
	//private static final int Z_ORIGIN = 200;
	
	private static final Vector SPAWN = new Vector(0, 50, 0);
	private static final Vector TEST_ORIGIN = new Vector(-300, 33, 200);
	
	// TODO: Not implemented yet -> offset for further Levels 
	//private static final int X_OFFSET = 0;
	//private static final int Y_OFFSET = 0;
	//private static final int Z_OFFSET = 0;
	
	// The amount of regions in a sector.
	private static final int REGIONS_PER_SECTOR = 1;
	
	private static List<List<Level>> levelMatrix = new ArrayList<>();
	
	/**
	 * Prevents instantiation of the level manager.
	 */
	private LevelManager() {}
	
	/**
	 * Get a level by its stage number and ID.
	 * @param stage Level stage.
	 * @param ID Level ID.
	 * @return Level from the level list.
	 */
	public static Level getLevel(int stage, UUID ID) {
		if (stage > REGIONS_PER_SECTOR) 
			return null;
		if (levelMatrix.get(stage - 1) == null) 
			return null;
		for (Level level : levelMatrix.get(stage - 1)) {
			if (level.getID() == ID)
				return level; 
		}
		return null;
	}
	
	/**
	 * Sends a party to the first level.
	 * @param party Party of players.
	 */
	public static void play(Party party) {
		play(party, 1);
	}

	/**

	 * Sends a party to a specific level.
	 * @param party Party of players.
	 * @param stage Specific stage.
	 */
	public static void play(Party party, int stage) {	
		if (stage > REGIONS_PER_SECTOR) {
			// TODO: temp message -> teleport to hubs or terminals once a set amount of levels have been passed by the party.
			party.sendMessage("You have reached the end of the map!");
		}
		// Searches for empty levels that already were generated
		if (levelMatrix.size() < stage) {
			levelMatrix.add(new ArrayList<Level>());
			
			party.sendMessage("No levels were found, generating level " + stage); 
			
			Level level = new Level(TEST_ORIGIN, party.getPartyMembers().get(0), stage);
			
			party.setLevelID(level.getID());
			party.setLevelStage(stage);
			
			levelMatrix.get(stage - 1).add(level);
			level.start(party);
			
			return;
			}
		
		List<Level> stageLevels = levelMatrix.get(stage - 1);
		List<Level> emptyLevels = new ArrayList<>();
		for (Level level : stageLevels) {
			if (level.isEmpty()) 
				emptyLevels.add(level);
		}
		
		switch (emptyLevels.size()) {
		case 0:
			party.sendMessage("No empty levels were found, generating level " + stage);
			//TODO: generate a new level
			break;
		case 1:
			party.sendMessage("Single empty level was found, strating level " + stage);
			emptyLevels.get(0);
			// TODO: get the only empty level.
			break;
		default: // Anything above 1 level
			party.sendMessage("Serval empty levels were found, starting level " + stage);
			Random random = new Random();
			int bound = emptyLevels.size();
			emptyLevels.get(random.nextInt(bound));
			// TODO: get the randomly selected empty level.
			break;
		}
	}
	
	/**
	 * Exit a level and teleport the player back to spawn.
	 * @param spiralPlayer Player that exits a level.
	 */
	public static void exit(SpiralPlayer spiralPlayer) {
		// Check if the player is the last player in the level -> if so resets the level.
		Party party = PartyManager.getParty(spiralPlayer.getPartyID());
		if (party.getMemberSum() == 1) {
			Level level = getLevel(party.getLevelStage(), party.getLevelID());
			PartyManager.exitParty(spiralPlayer);
			teleportPlayer(spiralPlayer, SPAWN);
			level.reset();
		}
		else {
		PartyManager.exitParty(spiralPlayer);
		teleportPlayer(spiralPlayer, SPAWN);
		}
	}
	
	/**
	 * Teleport a player to a point vector location.
	 * @param spiralPlayer Player to teleport.
	 * @param location location to teleport to.
	 */
	private static void teleportPlayer(SpiralPlayer spiralPlayer, Vector location) {
		spiralPlayer.getPlayer().teleport(new Location(
				spiralPlayer.getPlayer().getWorld(),
				location.getX(),
				location.getY(),
				location.getZ()));
	}
}
