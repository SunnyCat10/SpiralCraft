package me.Sunny.SpiralCraft.Data;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Container class for player interface with added functionality.
 * @author Daniel Dovgun
 * @version 9/27/2020
 */
public class SpiralPlayer {
	
	private Player player; // Used for effecting the player.
	
	private double health;
	
	private UUID partyID;
	
	public SpiralPlayer(UUID playerID) {
		player = Bukkit.getPlayer(playerID);
		setHealth(10);
		
		partyID = null;
	}

	public Player getPlayer() { return player; }
	public double getHealth() { return health; }
	public UUID getPartyID() { return partyID; }
	
	public void setHealth(double health) {
		this.health = health;
		player.setHealth(health);
	}
	public void setPartyID(UUID partyID) { this.partyID = partyID; }
	
	public boolean isInParty() {
		return (partyID != null);
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SpiralPlayer))
			return false;
		else {
			SpiralPlayer other = (SpiralPlayer) o;
			return ( player.getUniqueId().equals(other.getPlayer().getUniqueId()) );
		}
	}
}
