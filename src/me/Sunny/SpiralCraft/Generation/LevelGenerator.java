package me.Sunny.SpiralCraft.Generation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.Sunny.SpiralCraft.Levels.MinMaxBean;
import me.Sunny.SpiralCraft.SpiralCraftPlugin;
import me.Sunny.SpiralCraft.Triggers.FireworkTrigger;
import me.Sunny.SpiralGeneration.Utils.Point;
import org.bukkit.Material;
import org.bukkit.block.Block;
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

	private Vector floorOrigin;
	private final List<MinMaxBean> minMaxBeanList;

	private final com.sk89q.worldedit.world.World worldEditWorld;
	
	public LevelGenerator(Vector floorOrigin) {
		this.floorOrigin = floorOrigin;
		minMaxBeanList = new ArrayList<>();
		worldEditWorld = BukkitAdapter.adapt(SpiralCraftPlugin.getMainWorld());
	}
	
	public Vector getFloorOrigin() { return floorOrigin; }
	
	/**
	 * Generate a level (floor) in the minecraft world.
	 * @param layoutPath Path of the layout file.
	 * @throws FileNotFoundException Schematic of a room was not found.
	 * @throws IOException IO reading failed.
	 * @throws WorldEditException World Edit failed.
	 */
	public RoomNode generateFloor(String layoutPath)
			throws FileNotFoundException, IOException, WorldEditException {
		// Calls the layout generation algorithm:
		LayoutBuilder layoutBuilder = new LayoutBuilder(layoutPath);
		RoomNode root = layoutBuilder.Build();
		
		String schemPath = PathBuilder.build(root);
		
		floorOrigin = ChunkGridUtils.snapOrigin(SpiralCraftPlugin.getMainWorld(), floorOrigin);
		Vector roomOrigin = ChunkGridUtils.snapRoom(floorOrigin, root.getLocation());

		Point chunkLocation = ChunkGridUtils.getChunkLocation(roomOrigin);
		root.setChunkLocation(chunkLocation);
		// TODO: Triggers only for testing:
		root.setSpawnTrigger(new FireworkTrigger(chunkLocation.getX(), chunkLocation.getY()));
		// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

		generateRoom(schemPath, roomOrigin, root.getOrientation());
		addRoom(floorOrigin, root);

		return root;
	}
	
	/**
	 * Adds a room to the minecraft world.
	 * @param floorOrigin Origin point vector of the floor.
	 * @param parentNode Parent room node.
	 * @throws FileNotFoundException Schematic of a room was not found.
	 * @throws IOException IO reading failed.
	 * @throws WorldEditException World Edit failed.
	 */
	private void addRoom(Vector floorOrigin, RoomNode parentNode)
			throws FileNotFoundException, IOException, WorldEditException {	
		// Calls the function on the children of the parent RoomNode:
		for (int i = 0; i <= 4; i++) {
		if (parentNode.getNode(i) != null) {
			RoomNode childNode = parentNode.getNode(i);
			
			String schemPath = PathBuilder.build(childNode);		
			Vector roomOrigin = ChunkGridUtils.snapRoom(floorOrigin, childNode.getLocation());

			Point chunkLocation = ChunkGridUtils.getChunkLocation(roomOrigin);
			childNode.setChunkLocation(chunkLocation);
			// TODO: Triggers only for testing:
			childNode.setSpawnTrigger(new FireworkTrigger(chunkLocation.getX(), chunkLocation.getY()));
			// =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

			generateRoom(schemPath, roomOrigin, childNode.getOrientation());
			addRoom(floorOrigin, childNode);
			}
		}
	}
	
	/**
	 * Generates the given room with world edit API.
	 * @param schemPath The path of the schematic file.
	 * @param roomOrigin The origin point vector of the room.
	 * @param orientation The orientation of the room.
	 * @throws FileNotFoundException Schematic of a room was not found.
	 * @throws IOException IO reading failed.
	 * @throws WorldEditException World Edit failed.
	 */
	@SuppressWarnings("deprecation") //TODO: find the updated method
	private void generateRoom(String schemPath, Vector roomOrigin, RoomNode.Direction orientation)
			throws FileNotFoundException, IOException, WorldEditException {
		File schemFile = new File(schemPath); 

		ClipboardFormat format = ClipboardFormats.findByFile(schemFile);
		ClipboardReader reader = format.getReader(new FileInputStream(schemFile));// Loading the Schematic
		Clipboard clipboard = reader.read();

		// Retrieve the minimum and maximum points of the room:
		minMaxBeanList.add(ChunkGridUtils.getRoomMinMax(roomOrigin, clipboard.getMaximumPoint().getBlockY()));

		// Pasting the Schematic:
		try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(worldEditWorld, -1)) {
			ClipboardHolder clipboardHolder = new ClipboardHolder(clipboard);
			// TODO: Possible to get the min and max points from world edit API.

			// Rotate the clip board holder to the given orientation of the room:
			rotateClipboardHolder(clipboardHolder, orientation);
			roomOrigin = ChunkGridUtils.snapTransformedOrigin(roomOrigin, orientation);

			// Paste schematic into the world with world edit API edit session operation:
			Operation operation = clipboardHolder
					.createPaste(editSession)
					.to(BlockVector3.at(roomOrigin.getBlockX(), roomOrigin.getBlockY(), roomOrigin.getBlockZ()))
					.ignoreAirBlocks(true)
					.build();
			Operations.complete(operation);
		}
	}
	
	/**
	 * Rotate the clip board holder to a given orientation.
	 * @param clipboardHolder clip board holder object that holds the schematic.
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

	/**
	 * Removes a cuboid region.
	 * @param minMaxBean Minimum and Maximum vector points of the region.
	 */
	public void removeRegion(MinMaxBean minMaxBean) {
		Vector minPoint = minMaxBean.getMinPoint();
		Vector maxPoint = minMaxBean.getMaxPoint();

		System.out.println(minPoint.toString() + "\t" + maxPoint.toString());
		for (int y = minPoint.getBlockY(); y <= maxPoint.getBlockY(); y++)
			for (int x = minPoint.getBlockX(); x <= maxPoint.getBlockX(); x++)
				for (int z = minPoint.getBlockZ(); z <= maxPoint.getBlockZ(); z++) {
					Block block = SpiralCraftPlugin.getMainWorld().getBlockAt(x, y, z);
					if (!(block.getType().equals(Material.AIR))) {
						block.setType(Material.AIR);
					}
				}
	}

	/**
	 * Removes the entire generated floor.
	 */
	public void removeGeneration() {
		for (MinMaxBean minMaxBean : minMaxBeanList) {
			removeRegion(minMaxBean);
		}
	}

}