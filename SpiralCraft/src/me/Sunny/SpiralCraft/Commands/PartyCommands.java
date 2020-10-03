package me.Sunny.SpiralCraft.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Sunny.SpiralCraft.Data.PartyManager;
import me.Sunny.SpiralCraft.Data.SpiralPlayer;
import me.Sunny.SpiralCraft.Data.SpiralPlayerList;
import me.Sunny.SpiralCraft.Utils.Colors;
import net.md_5.bungee.api.ChatColor;

public class PartyCommands implements CommandExecutor {
		
	private final static String PARTY_COMMANDS_HEADER = ChatColor.of(Colors.OXFORD_BLUE) + "" + ChatColor.BOLD + "Party commands:" +
			 ChatColor.RESET + "\n";
	private final static String PARTY_JOIN_INFO = ChatColor.of(Colors.NAPLES_YELLOW) + "" + ChatColor.BOLD + "/party join   " +
			 ChatColor.RESET + "" + ChatColor.of(Colors.CELESTE) + "| Join a random party." + ChatColor.RESET;
	private final static String PARTY_DESCRIPTION_INFO = ChatColor.of(Colors.NAPLES_YELLOW) + "" + ChatColor.BOLD + "/party info   " +
			 ChatColor.RESET + "" + ChatColor.of(Colors.CELESTE) + "| Print your party description." + ChatColor.RESET;
	private final static String PARTY_LEAVE_INFO = ChatColor.of(Colors.NAPLES_YELLOW) + "" + ChatColor.BOLD + "/party leave   " +
			 ChatColor.RESET + "" + ChatColor.of(Colors.CELESTE) + "| Leave your party." + ChatColor.RESET;
	
	private final static String PARTY_COMMANDS_INFO = PARTY_COMMANDS_HEADER + '\n' + PARTY_JOIN_INFO + '\n' +
			PARTY_DESCRIPTION_INFO + '\n' + PARTY_LEAVE_INFO;
	
	public PartyCommands() {}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("party")) {
			if (! (sender instanceof Player)) {
				return true;
			}
			Player player = (Player) sender;
			if (args.length == 0) {
				player.sendMessage(PARTY_COMMANDS_INFO);
				//player.sendMessage("Party commands: /party join /party info /party leave");
				return true;
			}
			if (args[0].equalsIgnoreCase("join")) {
				SpiralPlayer spiralPlayer = SpiralPlayerList.getSpiralPlayer(player.getUniqueId());
				PartyManager.joinParty(spiralPlayer);
				return true;
			}
			if (args[0].equalsIgnoreCase("info")) {
				getInfo(player);
				return true;
			}
			if (args[0].equalsIgnoreCase("leave")) {
				leaveParty(player);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Prints the player`s party info.
	 * @param player The player
	 */
	public static void getInfo(Player player) {
		SpiralPlayer spiralPlayer = SpiralPlayerList.getSpiralPlayer(player.getUniqueId());
		if (spiralPlayer.isInParty()) {
			PartyManager.getParty(spiralPlayer.getPartyID()).getInfo(); }
		else {
			player.sendMessage("You are currenty not inside a party"); }
	}
	
	/**
	 * Exit the player`s current party, if no party found notify the player.
	 * @param player The player.
	 */
	public static void leaveParty(Player player) {
		SpiralPlayer spiralPlayer = SpiralPlayerList.getSpiralPlayer(player.getUniqueId());
		if (spiralPlayer.isInParty()) {
			PartyManager.exitParty(spiralPlayer); }
		else {
			player.sendMessage("You are currenty not inside a party"); }
	}
	
}
