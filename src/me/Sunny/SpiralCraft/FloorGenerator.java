package me.Sunny.SpiralCraft;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
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
import me.Sunny.SpiralGeneration.LayoutBuilder;
import me.Sunny.SpiralGeneration.RoomNode;

public class FloorGenerator implements CommandExecutor {
	
	private static final String TEST_PATH = "C:/Users/Sunny/Dev/TestServer_1.16/plugins/SpiralCraft/layouts/test.txt";
	
	private static final int ROOM_SIZE = 16;
	
	private final JavaPlugin plugin;
	
	public FloorGenerator(JavaPlugin instance) {
		plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("generate")) {
			if (!(sender instanceof Player)) {
				return true;
			}
			
			Player player = (Player) sender;
			//player.sendMessage(String.valueOf(player.getLocation().getBlock().getChunk().getX()));
			//player.sendMessage(String.valueOf(player.getLocation().getBlock().getChunk().getZ()));
			
			//player.sendMessage(getOriginChunk(player.getLocation()).toString());
			
			try {
				generateFloor(player, player.getWorld());
			} catch (IOException | WorldEditException e) {
				e.printStackTrace();
			}
			
			
		}
		return false;
	}
	
	private void generateFloor(Player player, org.bukkit.World world)
			throws FileNotFoundException, IOException, WorldEditException {
		LayoutBuilder layoutBuilder = new LayoutBuilder(TEST_PATH);
		RoomNode root = layoutBuilder.Build();
		
		//TODO: Move the pathBuilder to utilities as well.
		String schemPath = PathBuilder.build(root);
		//TODO: Move the translation into utilities and call it from the generator itself.
		// Translating abstract coordinates to Minecraft coordinates:
		
		Vector originPoint = getOriginChunk(player.getLocation());
		
		int abstractX = root.getLocation().getX();
		int abstractZ = root.getLocation().getY();
					
		double roomX = originPoint.getBlockX() + (abstractX * ROOM_SIZE);
		double roomZ = originPoint.getBlockZ() + (abstractZ * ROOM_SIZE);
		//============================================================//
		
		player.sendMessage(String.valueOf(roomX) + " | " + String.valueOf(roomZ));
		generateRoom(player, world, schemPath, roomX, player.getLocation().getY(), roomZ, root.getOrientation());
		
		// DEBUG
		Block testBlock = player.getLocation().getWorld().getBlockAt(originPoint.getBlockX(),
				originPoint.getBlockY(), originPoint.getBlockZ());
		testBlock.setType(Material.PURPLE_STAINED_GLASS);
		
		Block testBlock2 = player.getLocation().getWorld().getBlockAt((int)roomX,
				originPoint.getBlockY(), (int)roomZ);
		testBlock2.setType(Material.RED_STAINED_GLASS);  
		
		loadRoom(player, world, originPoint, root);
	}
	
	private void loadRoom(Player player, org.bukkit.World world, Vector originPoint, RoomNode roomTree)
			throws FileNotFoundException, IOException, WorldEditException {	
		for (int i = 0; i <= 4; i++) {
		if (roomTree.getNode(i) != null) {
			String schemPath = PathBuilder.build(roomTree.getNode(i));
			
			// Translating abstract coordinates to Minecraft coordinates:
			int abstractX = roomTree.getNode(i).getLocation().getX();
			int abstractZ = roomTree.getNode(i).getLocation().getY();
			
			double roomX = originPoint.getBlockX() + (abstractX * ROOM_SIZE);
			double roomZ = originPoint.getBlockZ() + (abstractZ * ROOM_SIZE);
			//============================================================//
			
			generateRoom(player, world, schemPath, roomX, player.getLocation().getY(), roomZ, roomTree.getNode(i).getOrientation());
			
			// DEBUG
			Block testBlock2 = player.getLocation().getWorld().getBlockAt((int)roomX,
					originPoint.getBlockY(), (int)roomZ);
			testBlock2.setType(Material.RED_STAINED_GLASS);
			
			loadRoom(player, world, originPoint, roomTree.getNode(i));
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void generateRoom(Player player, org.bukkit.World world, String schemPath, double roomX, double roomY, double roomZ,
			RoomNode.Direction orientation)
			throws FileNotFoundException, IOException, WorldEditException {
		
		File schemFile = new File(schemPath); // File varible
		/*
		 * System.out.println(schemFile);
		 * C:\Users\Sunny\Dev\TestServer_1.16\plugins\SpiralCraft\FILE_NAME_HERE
		 */
		ClipboardFormat format = ClipboardFormats.findByFile(schemFile); //Clipboard Reader	
		ClipboardReader reader = format.getReader(new FileInputStream(schemFile));// Loading the Schematic	
		Clipboard clipboard = reader.read();
		
		
		// Pasting the Schematic
		com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(world);
		
		try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1)) {
			ClipboardHolder clipboardHolder = new ClipboardHolder(clipboard);
			// Possible to get the min and max points.
			
			
			switch(orientation) {
			case NORTH:
				break;
			case EAST:
				clipboardHolder.setTransform(new AffineTransform().rotateY(270));
				roomX = roomX + (ROOM_SIZE - 1);
				break;
			case SOUTH:
				clipboardHolder.setTransform(new AffineTransform().rotateY(180));
				roomX = roomX + (ROOM_SIZE - 1);
				roomZ = roomZ + (ROOM_SIZE - 1);
				break;
			case WEST:
				clipboardHolder.setTransform(new AffineTransform().rotateY(90));
				roomZ = roomZ + (ROOM_SIZE - 1);
				break;
			} 
			
			Operation operation = clipboardHolder
					.createPaste(editSession)
					.to(BlockVector3.at(roomX, roomY, roomZ))
					.ignoreAirBlocks(false)
					.build();
			Operations.complete(operation);
		}
	}
	private Vector getOriginChunk(Location playerLocation) {
		Chunk originChunk = playerLocation.getBlock().getChunk();
		Block cornerBlock = originChunk.getBlock(0, playerLocation.getBlockY(), 0);
		Vector originPoint = new Vector(cornerBlock.getLocation().getX(),
				cornerBlock.getLocation().getY(),
				cornerBlock.getLocation().getZ());
		// Debug
		/*Block testBlock = playerLocation.getWorld().getBlockAt(originPoint.getBlockX(),
				originPoint.getBlockY(), originPoint.getBlockZ());
		testBlock.setType(Material.PURPLE_STAINED_GLASS);*/
		//
		return originPoint;
	}

}
