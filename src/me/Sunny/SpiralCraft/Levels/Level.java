package me.Sunny.SpiralCraft.Levels;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.Sunny.SpiralCraft.Triggers.TriggerBean;
import me.Sunny.SpiralGeneration.RoomNode;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.WorldEditException;

import me.Sunny.SpiralCraft.Data.Party;
import me.Sunny.SpiralCraft.Data.SpiralPlayer;
import me.Sunny.SpiralCraft.Generation.LevelGenerator;
import me.Sunny.SpiralCraft.Utils.ChunkGridUtils;

public class Level {

	// TODO: test path for the current layout
	private static final String TEST_PATH = "C:/Users/Sunny/Dev/TestServer_1.16/plugins/SpiralCraft/layouts/test.txt";

	private int stage;
	private final UUID ID;
	
	private boolean isEmpty = true;
	private Party party;
	private Vector spawnPoint;

	private RoomNode root;
	public List<TriggerBean> triggerList;

	private final LevelGenerator levelGenerator;

	// Level generation manager
	// Monsters manager
	// Chest manager
	
	// x = -300
	// y = 33
	// z = 200
	
	public Level(Vector floorOrigin, SpiralPlayer spiralPlayer, int stage) {
		ID = UUID.randomUUID();
		levelGenerator = new LevelGenerator(floorOrigin);
		this.stage = stage;

		generate();
		setup();
	}

	public UUID getID() { return ID; }
	public boolean isEmpty() { return isEmpty; }	
	public Vector getSpawnPoint() { return spawnPoint; }
	public List<TriggerBean> getTriggerList() { return triggerList; }
	
	// Generating the level
	private void generate() {
		System.out.print("Generating the level...");
		
		// TODO: put all this exception handling inside room generation!!!
		try {
			root = levelGenerator.generateFloor(TEST_PATH);
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
		triggerList = new ArrayList<>();
		loadTriggers();
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

	public void remove() {
		System.out.println("Removing level \t" + stage + "\t" + ID);
		levelGenerator.removeGeneration();
		System.out.println("The level removed...");
		// TODO: handle level removing from level generator to not create a memory leak.
	}

	// Triggers:

	public void loadTriggers() {
		TriggerBean triggerBean = new TriggerBean();
		triggerBean.setTrigger(root.getSpawnTrigger());
		triggerList.add(triggerBean);

		for (int i = 1; i <= 4; i++) {
			RoomNode childNode = root.getNode(i);
			if (childNode != null) {
				TriggerBean childTriggerBean = new TriggerBean();
				childTriggerBean.setTrigger(childNode.getSpawnTrigger());
				triggerList.add(childTriggerBean);
				loadTriggers(childNode);
			}
		}
	}

	public void loadTriggers(RoomNode parentNode) {
		for (int i = 1; i <= 4; i++) {
			RoomNode childNode = parentNode.getNode(i);
			if (childNode != null) {
				TriggerBean childTriggerBean = new TriggerBean();
				childTriggerBean.setTrigger(childNode.getSpawnTrigger());
				triggerList.add(childTriggerBean);
				if (!(childNode.isLeaf()))
					loadTriggers(childNode);
			}
		}
	}
}
