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
			Location playerLoc = player.getLocation();
			
			playerLoc.setX(playerLoc.getX() + 2);
			
			Location common = playerLoc.clone();
			Location uncommon = playerLoc.clone();
			Location rare = playerLoc.clone();
			
			common.setZ(common.getZ() - 2);
			rare.setZ(common.getZ() + 2);
			
			Treasure ct = new Treasure(common, Rarity.COMMON);
			Treasure ut = new Treasure(uncommon, Rarity.UNCOMMON);
			Treasure rt = new Treasure(rare, Rarity.RARE);
			
			return true;
		}
		return false;
	}

}
