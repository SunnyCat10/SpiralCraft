package me.Sunny.SpiralCraft.Commands;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import me.Sunny.SpiralCraft.Data.PartyManager;
import me.Sunny.SpiralCraft.Data.SpiralPlayer;
import me.Sunny.SpiralCraft.Data.SpiralPlayerList;
import me.Sunny.SpiralCraft.Utils.Colors;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

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
			SpiralPlayer spiralPlayer = SpiralPlayerList.getSpiralPlayer(player.getUniqueId());
			
			if (args.length == 0) {
				player.sendMessage(PARTY_COMMANDS_INFO);
				//player.sendMessage("Party commands: /party join /party info /party leave");
				return true;
			}
			if (args[0].equalsIgnoreCase("join")) {
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
			if (args[0].equalsIgnoreCase("invite")) {
				if (args.length == 1) {
					player.sendMessage("Enter a player name to send an invite...");
					return true;
				}
				if (spiralPlayer.getPartyID() == null) {
					player.sendMessage("You are not a member of a party, in order to invite someone create a party first.");
					return true;
				}
				
				String invitedPlayerName = args[1];
				Player invitedPlayer = Bukkit.getPlayer(invitedPlayerName);
				
				// TODO: after debugging disable this:
				if (args.length == 2) {
					sendInvite(spiralPlayer, invitedPlayer);
					return true;
				}
				List<SpiralPlayer> partyMembersList = PartyManager.getParty(spiralPlayer.getPartyID()).getPartyMembers();
				for (SpiralPlayer partyMember : partyMembersList) {
					if (invitedPlayerName.equals(partyMember.getPlayer().getDisplayName())) {
						player.sendMessage("The player is already a member of the party.");
						return true;
					}
				}
				sendInvite(spiralPlayer, invitedPlayer);
				return true;
			}
			
			// Party invitation handling:
			if (args[0].equalsIgnoreCase("accept")) {
				if(args.length == 1 || args.length == 2)
					return true; 
				else {
					Player inviteSender = Bukkit.getPlayer(args[1]);
					if (inviteSender == null) {
						player.sendMessage("The party leader is no longer online.");
						return true;
					}
					inviteSender.sendMessage(player.getDisplayName() + " accepted your invitation.");
					PartyManager.joinParty(spiralPlayer, UUID.fromString(args[2]));
					return true; 
				}
			}
			if (args[0].equalsIgnoreCase("reject")) {
				if(args.length == 1) 
					return true;
				else {
					Bukkit.getPlayer(args[1]).sendMessage(player.getDisplayName() + " rejected your invitation.");	
					return true;
				}
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
	
	private static void sendInvite(SpiralPlayer spiralSender, Player reciver) {
		Player sender = spiralSender.getPlayer();
		sender.sendMessage("Party invitation for " + reciver.getDisplayName() + " was successfully sent.");

		TextComponent prefix = new TextComponent("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n\n");
		prefix.setColor(ChatColor.of(Colors.BLACK_CORAL));
		
		TextComponent playerHeader = new TextComponent(sender.getDisplayName());
		playerHeader.setColor(ChatColor.of(Colors.MANGO_TANGO));
		
		TextComponent header = new TextComponent(" is inviting you to join their party.\n\n|     ");
		header.setColor(ChatColor.of(Colors.BLACK_CORAL));		
	
		TextComponent acceptText = new TextComponent("ACCEPT"); 
		acceptText.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new Text("Click here to accept the inventation.")));
		acceptText.setClickEvent(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/party accept " + sender.getDisplayName() + " " + spiralSender.getPartyID()));
		acceptText.setColor(ChatColor.of(Colors.GREEN_PANTONE));
		
		TextComponent split = new TextComponent("     |     ");
		split.setColor(ChatColor.of(Colors.BLACK_CORAL));
		
		TextComponent rejectText = new TextComponent("REJECT");
		rejectText.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new Text("Click here to reject the inventation.")));
		rejectText.setClickEvent(new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/party reject " + sender.getDisplayName()));
		rejectText.setColor(ChatColor.of(Colors.FIREBRICK));
		
		TextComponent suffix = new TextComponent("     |\n\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		
		prefix.addExtra(playerHeader);
		prefix.addExtra(header);
		prefix.addExtra(acceptText);
		prefix.addExtra(split);
		prefix.addExtra(rejectText);
		prefix.addExtra(suffix);
		
		reciver.spigot().sendMessage(prefix);	
	}
}
