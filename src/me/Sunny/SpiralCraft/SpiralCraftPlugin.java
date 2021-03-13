package me.Sunny.SpiralCraft;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.Sunny.SpiralCraft.Data.PartyManager;
import me.Sunny.SpiralCraft.Events.ChestBreakEvent;
import me.Sunny.SpiralCraft.Events.DropSpawnEvent;
import me.Sunny.SpiralCraft.Events.LootPickupEvent;
import me.Sunny.SpiralCraft.Levels.LevelManager;
import me.Sunny.SpiralCraft.Triggers.Detector;
import me.Sunny.SpiralCraft.cache.DBManager;
import me.Sunny.SpiralCraft.cache.ItemTable;
import me.Sunny.SpiralCraft.cache.LootMatrix;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import me.Sunny.SpiralCraft.Commands.LevelCommands;
import me.Sunny.SpiralCraft.Commands.PartyCommands;

public class SpiralCraftPlugin extends JavaPlugin {
	
	private static String pluginFolderPath;
	private static World mainWorld;
	private static Detector detector;

	private static ProtocolManager protocolManager;

	public void onLoad() {
		protocolManager = ProtocolLibrary.getProtocolManager();
	}

	@Override
	public void onEnable() {

		mainWorld = Bukkit.getServer().getWorlds().get(0);
		pluginFolderPath = getDataFolder().getAbsoluteFile().toString();
		detector = new Detector(this);
		detector.run();

		loadCaches();
		setCommandsExecutor();
		registerEvents();
		
		Slash slash = new Slash(this);
		this.getCommand("slash").setExecutor(slash);
		getServer().getPluginManager().registerEvents(slash, this);
	}
	
	@Override
	public void onDisable() {
		LevelManager.removeAll(); // Clears all the levels from the map.
		PartyManager.removeAll(); // Clears all the parties from the cache.
	}

	/**
	 * Get the plugin folder path.
	 * @return Plugin folder path.
	 */
	public static String getPluginFolderPath() { return pluginFolderPath; }

	/**
	 * Get the main world of the server.
	 * @return Main world of the server.
	 */
	public static World getMainWorld() { return mainWorld; }

	public static ProtocolManager getProtocolManager() { return protocolManager; }

	//TODO: Can be tested on another thread.
	/**
	 * Loads the caches that the plugin will use during its lifetime.
	 */
	private void loadCaches() {
		LootMatrix lootMatrix = LootMatrix.getInstance();
		lootMatrix.loadFromDB();
		ItemTable itemTable = ItemTable.getInstance();
		itemTable.loadFromDB();
	}

	private void setCommandsExecutor() {
		FloorGenerator floorGenerator = new FloorGenerator(this);
		this.getCommand("generate").setExecutor(floorGenerator);

		Lootgen lootGen = new Lootgen(this);
		this.getCommand("loot").setExecutor(lootGen);

		PartyCommands pCommands = new PartyCommands();
		this.getCommand("party").setExecutor(pCommands);

		LevelCommands levelCommands = new LevelCommands();
		this.getCommand("level").setExecutor(levelCommands);
	}

	private void registerEvents() {
		PlayerEventListener playerEventListener = new PlayerEventListener();
		getServer().getPluginManager().registerEvents(playerEventListener, this);

		ChestBreakEvent chestBreakEvent = new ChestBreakEvent();
		getServer().getPluginManager().registerEvents(chestBreakEvent, this);

		LootPickupEvent lootPickupEvent = new LootPickupEvent();
		getServer().getPluginManager().registerEvents(lootPickupEvent, this);

		DropSpawnEvent dropSpawnEvent = new DropSpawnEvent(this);
		getServer().getPluginManager().registerEvents(dropSpawnEvent, this);
	}
}
