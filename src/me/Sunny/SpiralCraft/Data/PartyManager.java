package me.Sunny.SpiralCraft.Data;

import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

public final class PartyManager {
	
	private static final String JOINED_PARTY_MSG = "You have successfully joined the party! ";
	private static final String PARTY_IN_LOBBY_MSG = "The party is currently in spawn.";
	private static final String PATY_IN_LEVEL_MSG = "The party is currently in a level.";

	/**
	 * Holds all the Parties that are currently being used.
	 */
	private static Hashtable<UUID, Party> partyList = new Hashtable<UUID, Party>();
	
	 /**
	  * Private constructor to not allow instantiating this class.
	  */
	private PartyManager() {}
	
	public static void addParty(SpiralPlayer spiralPlayer) {
		Party party = new Party(spiralPlayer);
		partyList.put(party.getID(), party);
		spiralPlayer.getPlayer().sendMessage("You have created a new party.");
	}
	
	public static void removeParty(UUID partyID) {
		partyList.remove(partyID);
	}
	
	public static void removeAll() {
		partyList = new Hashtable<UUID, Party>();
	}

	
	// Joins a random empty party
	public static void joinParty(SpiralPlayer spiralPlayer) {
		if (partyList.size() == 0) { // No parties found:
			spiralPlayer.getPlayer().sendMessage("No parties have been found, creating a new one...");
			addParty(spiralPlayer);
			return;
		}
		for (Map.Entry<UUID, Party> entry : partyList.entrySet()) { // Searching for an empty party.
			if (!(entry.getValue().isFull())) {
				partyList.get(entry.getKey()).addMember(spiralPlayer);
				spiralPlayer.getPlayer().sendMessage("Joining a party...");
				return;
			}
		} // No empty parties were found:
		spiralPlayer.getPlayer().sendMessage("No empty parties have been found, creating a new one...");
		addParty(spiralPlayer);
	}
	
	public static boolean joinParty(SpiralPlayer spiralPlayer, UUID partyID) {
		Party party = partyList.get(partyID);
		if (party.isFull()) {
			return false;
		}
		else {
			party.addMember(spiralPlayer);
			Player player = spiralPlayer.getPlayer();
			player.sendMessage(JOINED_PARTY_MSG);
			// The party is in lobby:
			if (party.getLevelStage() == 0) { 
				player.sendMessage(PARTY_IN_LOBBY_MSG);
				return true;
			}
			else {
				// Teleport the player to the party leader:
				player.sendMessage(PATY_IN_LEVEL_MSG);
				player.teleport(party.getPartyMembers().get(0).getPlayer().getLocation());
				return true;
			}
		}
	}

	public static void exitParty(SpiralPlayer spiralPlayer) {
		Party party = getParty(spiralPlayer.getPartyID());
		party.removeMember(spiralPlayer);
		// Disband the party if the last player leaves:
		if (party.getMemberSum() == 0) { 
			PartyManager.removeParty(party.getID());
			spiralPlayer.getPlayer().sendMessage("Disbanding the current party.");
		}	
		spiralPlayer.getPlayer().sendMessage("You have left the party.");
	}
		
	public static Party getParty(UUID partyID) {
		return partyList.get(partyID);
	}
	
	
}
