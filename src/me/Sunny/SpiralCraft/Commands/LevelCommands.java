package me.Sunny.SpiralCraft.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Sunny.SpiralCraft.Data.Party;
import me.Sunny.SpiralCraft.Data.PartyManager;
import me.Sunny.SpiralCraft.Data.SpiralPlayer;
import me.Sunny.SpiralCraft.Data.SpiralPlayerList;
import me.Sunny.SpiralCraft.Levels.LevelManager;

public class LevelCommands implements CommandExecutor {

	public LevelCommands() {}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("level")) {
			if (! (sender instanceof Player)) {
				return true;
			}
			Player player = (Player) sender;
			SpiralPlayer spiralPlayer = SpiralPlayerList.getSpiralPlayer(player.getUniqueId());
			if (args.length == 0) {
				player.sendMessage("Level Commands: /level join");
				return true;
			}
			if (args[0].equalsIgnoreCase("join")) {
				if (spiralPlayer.getPartyID() == null) { 
					PartyManager.joinParty(spiralPlayer);
				}
				Party playerParty = PartyManager.getParty(spiralPlayer.getPartyID());
				LevelManager.play(playerParty);
				return true;
			}
			if (args[0].equalsIgnoreCase("exit")) {
				if (spiralPlayer.getPartyID() == null) {
					player.sendMessage("You are currently not inside a level.");
					return true;
				}
				LevelManager.exit(spiralPlayer);
				return true;
			}
			if (args[0].equalsIgnoreCase("finish")) { // TODO: FOR DEBUG ONLY
				player.sendMessage("NOT IMPLEMENTED YET");
				return true;
			}
			
			
			return true;
		}
		return false;
	}
}
