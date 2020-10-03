package me.Sunny.SpiralCraft.Levels;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.util.Vector;

import com.sk89q.worldedit.WorldEditException;

import me.Sunny.SpiralCraft.Data.Party;
import me.Sunny.SpiralCraft.Data.SpiralPlayer;
import me.Sunny.SpiralCraft.Generation.LevelGenerator;
import me.Sunny.SpiralCraft.Utils.ChunkGridUtils;

public class Level {

	private int stage;
	private UUID ID;
	
	private boolean isEmpty = true;
	private Party party;
	private Vector spawnPoint;
	
	// Level generation manager
	// Monsters manager
	// Chest manager
	
	// x = -300
	// y = 33
	// z = 200
	
	public Level(Vector floorOrigin, SpiralPlayer spiralPlayer, int stage) {
		ID = UUID.randomUUID();
		generate(floorOrigin, spiralPlayer);
		this.stage = stage;
		setup();
	}
	
	public UUID getID() { return ID; }
	public boolean isEmpty() { return isEmpty; }	
	public Vector getSpawnPoint() { return spawnPoint; }
	
	// Generating the level
	private void generate(Vector floorOrigin, SpiralPlayer spiralPlayer) {
		LevelGenerator levelGenerator = new LevelGenerator(floorOrigin);
		
		System.out.print("Generating the level...");
		
		// TODO: put all this exception handling inside room generation!!!
		try {
			levelGenerator.generateFloor(spiralPlayer);
		} catch (FileNotFoundException e) {
			System.out.println("The node schematic was not found.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO system error.");
			e.printStackTrace();
		} catch (WorldEditException e) {
			System.out.println("World edit failed to print the schematic.");
			e.printStackTrace();
		}
		
		// Sets the spawn point of the level:
		spawnPoint = ChunkGridUtils.getSpawnPoint(levelGenerator.getFloorOrigin());
	}
	
	// TODO: Clear the state of the level
	public void setup() {
		System.out.print("Setting up the level...");
	}
	
	/**
	 * Starts a dungeon run with the inputed party.
	 * @param party Party that will start the dungen run.
	 */
	public void start(Party party) {
		isEmpty = false; // This should be the first function.
		this.party = party;
		party.teleportPlayers(spawnPoint);
	}
	
	/**
	 * Finish the dungeon run of the current party.
	 */
	public void finish() {
		LevelManager.play(party, ++stage);
		reset();
	}
	
	/**
	 * Resets the dungeon so it can be playable by another party.
	 */
	public void reset() {
		party = null;
		setup();
		isEmpty = true;
	}
}
