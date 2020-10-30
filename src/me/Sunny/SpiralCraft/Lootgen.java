package me.Sunny.SpiralCraft;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.Sunny.SpiralCraft.Loot.Treasure;
import me.Sunny.SpiralCraft.Loot.Treasure.Rarity;


//TODO: ONLY PROTOTYPING CLASS!!!
public class Lootgen implements CommandExecutor {

	private final JavaPlugin plugin;
	
	public Lootgen(JavaPlugin instance) {
		plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("loot")) {
			if (!(sender instanceof Player)) {
			return true;
			}
			
			Player player = (Player) sender;
			int playerX = player.getLocation().getBlockX();
			int playerY = player.getLocation().getBlockY();
			int playerZ = player.getLocation().getBlockZ();

			Location commonLoc = new Location(SpiralCraftPlugin.getMainWorld(), playerX + 3, playerY, playerZ);
			Location uncommonLoc = new Location(SpiralCraftPlugin.getMainWorld(), playerX , playerY, playerZ);
			Location rareLoc = new Location(SpiralCraftPlugin.getMainWorld(), playerX - 3, playerY, playerZ);

			Treasure commonTreasure = new Treasure(commonLoc, Rarity.COMMON, plugin);
			Treasure uncommonTreasure = new Treasure(uncommonLoc, Rarity.UNCOMMON, plugin);
			Treasure rareTreasure = new Treasure(rareLoc, Rarity.RARE, plugin);
			
			return true;
		}
		return false;
	}

}
