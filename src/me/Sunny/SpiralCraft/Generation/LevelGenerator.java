package me.Sunny.SpiralCraft.Generation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.util.Vector;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;

import me.Sunny.SpiralCraft.Data.SpiralPlayer;
import me.Sunny.SpiralCraft.Generation.Utils.PathBuilder;
import me.Sunny.SpiralCraft.Utils.ChunkGridUtils;
import me.Sunny.SpiralGeneration.LayoutBuilder;
import me.Sunny.SpiralGeneration.RoomNode;

/**
 * Generates a level into the minecraft world.
 * @author Daniel Dovgun
 * @version 10/2/2020
 */
public class LevelGenerator {
	
	// Rotation degrees constants:
	private static final int ROTATE_WEST = 90;
	private static final int ROTATE_SOUTH = 180;
	private static final int ROTATE_EAST = 270;
	
	// TODO: test path for the current layout
	private static final String TEST_PATH = "C:/Users/Sunny/Dev/TestServer_1.16/plugins/SpiralCraft/layouts/test.txt";
	
	private Vector floorOrigin;
	
	public LevelGenerator(Vector floorOrigin) {
		this.floorOrigin = floorOrigin;
	}
	
	public Vector getFloorOrigin() { return floorOrigin; }
	
	/**
	 * Generate a level (floor) in the minecraft world.
	 * @param spiralPlayer Spiral player.
	 * @param floorOrigin Origin point vector of the floor.
	 * @throws FileNotFoundException Schematic of a room was not found.
	 * @throws IOException IO reading failed.
	 * @throws WorldEditException World Edit failed.
	 */
	public void generateFloor(SpiralPlayer spiralPlayer)
			throws FileNotFoundException, IOException, WorldEditException {
		// Calls the layout generation algorithm:
		LayoutBuilder layoutBuilder = new LayoutBuilder(TEST_PATH);
		RoomNode root = layoutBuilder.Build();
		
		String schemPath = PathBuilder.build(root);
		
		floorOrigin = ChunkGridUtils.snapOrigin(spiralPlayer, floorOrigin);
		Vector roomOrigin = ChunkGridUtils.snapRoom(floorOrigin, root.getLocation());
		
		generateRoom(spiralPlayer, schemPath, roomOrigin, root.getOrientation());	
		addRoom(spiralPlayer, floorOrigin, root);
	}
	
	/**
	 * Adds a room to the minecraft world.
	 * @param spiralPlayer Spiral player.
	 * @param originPoint Origin point vector of the floor.
	 * @param parentNode Parent room node.
	 * @throws FileNotFoundException Schematic of a room was not found.
	 * @throws IOException IO reading failed.
	 * @throws WorldEditException World Edit failed.
	 */
	private void addRoom(SpiralPlayer spiralPlayer, Vector floorOrigin, RoomNode parentNode)
			throws FileNotFoundException, IOException, WorldEditException {	
		// Calls the function on the children of the parent RoomNode:
		for (int i = 0; i <= 4; i++) {
		if (parentNode.getNode(i) != null) {
			RoomNode childNode = parentNode.getNode(i);
			
			String schemPath = PathBuilder.build(childNode);		
			Vector roomOrigin = ChunkGridUtils.snapRoom(floorOrigin, childNode.getLocation());
			
			generateRoom(spiralPlayer, schemPath, roomOrigin, childNode.getOrientation());
			addRoom(spiralPlayer, floorOrigin, childNode);
			}
		}
	}
	
	/**
	 * Generates the given room with world edit API.
	 * @param spiralPlayer Spiral player.
	 * @param schemPath The path of the schematic file.
	 * @param roomOrigin The origin point vector of the room.
	 * @param orientation The orientation of the room.
	 * @throws FileNotFoundException Schematic of a room was not found.
	 * @throws IOException IO reading failed.
	 * @throws WorldEditException World Edit failed.
	 */
	@SuppressWarnings("deprecation") //TODO: find the updated method
	private void generateRoom(SpiralPlayer spiralPlayer, String schemPath, Vector roomOrigin, RoomNode.Direction orientation)
			throws FileNotFoundException, IOException, WorldEditException {
		File schemFile = new File(schemPath); 

		ClipboardFormat format = ClipboardFormats.findByFile(schemFile); 
		ClipboardReader reader = format.getReader(new FileInputStream(schemFile));// Loading the Schematic	
		Clipboard clipboard = reader.read();

		com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(spiralPlayer.getPlayer().getWorld());
		// Pasting the Schematic:
		try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1)) {
			ClipboardHolder clipboardHolder = new ClipboardHolder(clipboard);
			// TODO: Possible to get the min and max points from world edit API.
			
			// Rotate the clip board holder to the given orientation of the room:
			rotateClipboardHolder(clipboardHolder, orientation);
			roomOrigin = ChunkGridUtils.snapTransformedOrigin(roomOrigin, orientation);
			
			// Paste schematic into the world with world edit API edit session operation:
			Operation operation = clipboardHolder
					.createPaste(editSession)
					.to(BlockVector3.at(roomOrigin.getBlockX(), roomOrigin.getBlockY(), roomOrigin.getBlockZ()))
					.ignoreAirBlocks(false)
					.build();
			Operations.complete(operation);
		}
	}
	
	/**
	 * Rotate the clip board holder to a given orientation.
	 * @param clipboardHolder clip board holder object that holds the schematic.
	 * @param roomOrigin Origin point vector of the room.
	 * @param orientation Orientation to rotate to.
	 */
	private void rotateClipboardHolder(ClipboardHolder clipboardHolder, RoomNode.Direction orientation) {
		switch(orientation) {
		case NORTH:
			break;
		case EAST:
			clipboardHolder.setTransform(new AffineTransform().rotateY(ROTATE_EAST));
			break;
		case SOUTH:
			clipboardHolder.setTransform(new AffineTransform().rotateY(ROTATE_SOUTH));
			break;
		case WEST:
			clipboardHolder.setTransform(new AffineTransform().rotateY(ROTATE_WEST));
			break;
		} 
	}
}