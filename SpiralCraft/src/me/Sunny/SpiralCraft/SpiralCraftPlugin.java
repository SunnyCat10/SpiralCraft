package me.Sunny.SpiralCraft;

import org.bukkit.plugin.java.JavaPlugin;

import me.Sunny.SpiralCraft.Commands.LevelCommands;
import me.Sunny.SpiralCraft.Commands.PartyCommands;

public class SpiralCraftPlugin extends JavaPlugin {
	
	private static String pluginFolderPath;
	
	@Override
	public void onEnable() {
		pluginFolderPath = getDataFolder().getAbsoluteFile().toString();
		
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
	}
	
	
	public static String getPluginFolderPath() { return pluginFolderPath; }
	
	

	

}
