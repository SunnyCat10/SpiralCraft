package me.Sunny.SpiralCraft.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.bukkit.util.Vector;

public class Party {

	private static final int MAX_MEMBERS = 4;
	private static final String INFO_PREFIX = "'s party: ";
	private static final String SCOREBOARD_TEAM = "partyMembers";
	private static final String SCOREBOARD_OBJECTIVE = "Party";

	private static final int FIRST_MEMBER = 14;
	private static final int SECOND_MEMBER = 13;
	private static final int THIRD_MEMBER = 12;
	private static final int FOURTH_MEMBER = 11;

	private final UUID partyID;
	
	private int levelStage;
	private UUID levelID;
	
	private int memberSum;

	SpiralPlayer partyLeader;
	List<SpiralPlayer> partyMembers;

	// Scoreboard utilities
	private Scoreboard scoreboard;
	private Team membersTeam;
	
	public Party(SpiralPlayer partyLeader) {
		partyID = UUID.randomUUID();
		
		memberSum = 1;
		
		this.partyLeader = partyLeader;
		partyMembers = new ArrayList<>();
		partyMembers.add(partyLeader);
		
		partyLeader.setPartyID(partyID);

		//TODO: FOR TESTING
		createScoreboard();
		partyLeader.getPlayer().setScoreboard(scoreboard);
		membersTeam.addEntry(partyLeader.getPlayer().getName());
		updatePlayersScoreboard();
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

	public void addMember(SpiralPlayer spiralPlayer) {
		partyMembers.add(spiralPlayer);
		memberSum += 1;
		spiralPlayer.setPartyID(partyID);
		
		// If the current player is the only player in the party, promote him to a party leader:
		if (memberSum == 1) {
			partyLeader = partyMembers.get(0);
		}

		//TODO: FOR TESTING
		membersTeam.addEntry(spiralPlayer.getPlayer().getName());
		spiralPlayer.getPlayer().setScoreboard(scoreboard);
		updatePlayersScoreboard();
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
	
	private void createScoreboard(Player player){
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective objective = scoreboard.registerNewObjective(SCOREBOARD_OBJECTIVE, "dummy", "Party");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		membersTeam = scoreboard.registerNewTeam(SCOREBOARD_TEAM);
		//membersTeam.addEntry(player.getName());
		//membersTeam.addEntry("Test");

		Score onlineName = objective.getScore(ChatColor.GRAY + "» Party members:");
		onlineName.setScore(15);

		objective.getScore(player.getName()).setScore(14);
		objective.getScore("Test").setScore(13);

		player.setScoreboard(scoreboard);
	}

	private void createScoreboard(){
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective objective = scoreboard.registerNewObjective(SCOREBOARD_OBJECTIVE, "dummy", "Party");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		membersTeam = scoreboard.registerNewTeam(SCOREBOARD_TEAM);

		Score onlineName = objective.getScore(ChatColor.GRAY + "» Party members:");
		onlineName.setScore(15);
	}

	private void updatePlayersScoreboard() {
		for (SpiralPlayer spiralPlayer : partyMembers) {
			formatEntryDisplay(spiralPlayer.getPlayer());
		}
	}

	private void formatEntryDisplay(Player player) {
		Objective objective = player.getScoreboard().getObjective(SCOREBOARD_OBJECTIVE);
		int formattedPlayers = 0;
		for(SpiralPlayer spiralPlayer : partyMembers) {
			if (spiralPlayer != null) {
				switch (formattedPlayers) {
					case 0 -> objective.getScore(spiralPlayer.getPlayer().getName()).setScore(FIRST_MEMBER);
					case 1 -> objective.getScore(spiralPlayer.getPlayer().getName()).setScore(SECOND_MEMBER);
					case 2 -> objective.getScore(spiralPlayer.getPlayer().getName()).setScore(THIRD_MEMBER);
					case 3 -> objective.getScore(spiralPlayer.getPlayer().getName()).setScore(FOURTH_MEMBER);
				}
				++formattedPlayers;
			}
		}
	}
}
