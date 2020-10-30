package me.Sunny.SpiralCraft.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Party {

	private static final int MAX_MEMBERS = 4;
	private static final String INFO_PREFIX = "'s party: ";
	
	private final UUID partyID;
	
	private int levelStage;
	private UUID levelID;
	
	private int memberSum;

	SpiralPlayer partyLeader;
	List<SpiralPlayer> partyMembers;
	
	public Party(SpiralPlayer partyLeader) {
		partyID = UUID.randomUUID();
		
		memberSum = 1;
		
		this.partyLeader = partyLeader;
		partyMembers = new ArrayList<>();
		partyMembers.add(partyLeader);
		
		partyLeader.setPartyID(partyID);
	}
	
	public List<SpiralPlayer> getPartyMembers() { return partyMembers; }
	public int getMemberSum() { return memberSum; } 
	public UUID getID() { return partyID; } //TODO RENAME TO PARTY IDD

	public UUID getLevelID() { return levelID; }
	public int getLevelStage() { return levelStage; }
	
	public void setLevelID(UUID levelID) { this.levelID = levelID; }
	public void setLevelStage(int levelStage) { this.levelStage = levelStage; }
	
	public boolean isFull() { return (memberSum == MAX_MEMBERS); } 
	public boolean isPlaying() { return (levelStage != 0); }

	public void addMember(SpiralPlayer player) {
		partyMembers.add(player);
		memberSum += 1;
		player.setPartyID(partyID);	
		
		// If the current player is the only player in the party, promote him to a party leader:
		if (memberSum == 1) {
			partyLeader = partyMembers.get(0);
		}
	}
	
	public void removeMember(SpiralPlayer player) {
		partyMembers.remove(player);
		player.setPartyID(null);
		memberSum -= 1;
		
		// If the leader of the party exits, replace him with the next player:
		if ((memberSum >= 1) && (partyLeader.equals(player))) { 
			partyLeader = partyMembers.get(0);
		}
	}
	
	public void getInfo() {
		String partyLeaderName = partyLeader.getPlayer().getDisplayName();
		String output = partyLeaderName + INFO_PREFIX + "[ " + partyLeaderName + " ] ";
		
		int partySize = partyMembers.size();
		for (int i = 1; i < partySize; i++) {
			output += "[ " + partyMembers.get(i).getPlayer().toString() + " ] ";
		}
		int emptySlots = MAX_MEMBERS - partySize;
		for (int i = 0; i < emptySlots; i++) {
			output += "[ - ] ";
		}
		
		for (SpiralPlayer spiralPlayer : partyMembers) {
			spiralPlayer.getPlayer().sendMessage(output);
		}
	}
	
	/**
	 * Sends a specific message to all the party members.
	 * @param msg The message contained in a string.
	 */
	public void sendMessage(String msg) {
		for (SpiralPlayer spiralPlayer : partyMembers) {
			spiralPlayer.getPlayer().sendMessage(msg);
		}
	}
	
	/**
	 * Teleport all the party members to a specific point.
	 * @param tpPoint Location to teleport into.
	 */
	public void teleportPlayers(Vector tpPoint) {
		for (SpiralPlayer spiralPlayer : partyMembers) {
			Player player = spiralPlayer.getPlayer();
			player.teleport(new Location(
					player.getWorld(),
					tpPoint.getX(), tpPoint.getY(), tpPoint.getZ()));
		}
	}	
	
	
}
