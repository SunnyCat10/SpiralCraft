package me.Sunny.SpiralCraft;

import me.Sunny.SpiralCraft.Levels.LevelManager;
import me.Sunny.SpiralCraft.Triggers.Detector;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import me.Sunny.SpiralCraft.Commands.LevelCommands;
import me.Sunny.SpiralCraft.Commands.PartyCommands;

public class SpiralCraftPlugin extends JavaPlugin {
	
	private static String pluginFolderPath;
	private static World mainWorld;
	private static Detector detector;

	@Override
	public void onEnable() {

		mainWorld = Bukkit.getServer().getWorlds().get(0);
		pluginFolderPath = getDataFolder().getAbsoluteFile().toString();
		detector = new Detector(this);
		detector.run();
		
		Slash slash = new Slash(this);
		this.getCommand("slash").setExecutor(slash);
		getServer().getPluginManager().registerEvents(slash, this);
		
		FloorGenerator floorGenerator = new FloorGenerator(this);
		this.getCommand("generate").setExecutor(floorGenerator);
		
		Lootgen lootGen = new Lootgen(this);
		this.getCommand("loot").setExecutor(lootGen);
		
		PlayerEventListener playerEventListener = new PlayerEventListener();
		getServer().getPluginManager().registerEvents(playerEventListener, this);
		
		PartyCommands pCommands = new PartyCommands();
		this.getCommand("party").setExecutor(pCommands);
		
		LevelCommands lCommmands = new LevelCommands();
		this.getCommand("level").setExecutor(lCommmands);
	}
	
	@Override
	public void onDisable() {
		LevelManager.removeAll(); // Clears all the levels from the map.
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
}
