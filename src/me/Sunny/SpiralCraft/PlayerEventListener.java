package me.Sunny.SpiralCraft;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.Sunny.SpiralCraft.Commands.PartyCommands;
import me.Sunny.SpiralCraft.Data.SpiralPlayerList;

public class PlayerEventListener implements Listener {

	public PlayerEventListener() {}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		// The first function call that should happen in the onJoin event.
		// Only once this function runs, we will have a reference to the SprialPlayer:
		SpiralPlayerList.addPlayer(player.getUniqueId());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		PartyCommands.leaveParty(player);	
		
		// The last function call that should happen in the onQuit event.
		// Once this function runs, we no longer have a reference to the SprialPlayer:
		SpiralPlayerList.removePlayer(player.getUniqueId());
	}
}
