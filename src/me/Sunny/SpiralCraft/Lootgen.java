package me.Sunny.SpiralCraft;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import me.Sunny.SpiralCraft.Data.Party;
import me.Sunny.SpiralCraft.Data.PartyManager;
import me.Sunny.SpiralCraft.Data.SpiralPlayer;
import me.Sunny.SpiralCraft.Data.SpiralPlayerList;
import me.Sunny.SpiralCraft.Loot.TreasureDropManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.Sunny.SpiralCraft.Loot.Treasure;
import me.Sunny.SpiralCraft.Loot.Treasure.Rarity;

import java.lang.reflect.InvocationTargetException;


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
			if (args.length == 0) {

				int playerX = player.getLocation().getBlockX();
				int playerY = player.getLocation().getBlockY();
				int playerZ = player.getLocation().getBlockZ();

				Location commonLoc = new Location(SpiralCraftPlugin.getMainWorld(), playerX + 3, playerY, playerZ);
				Location uncommonLoc = new Location(SpiralCraftPlugin.getMainWorld(), playerX, playerY, playerZ);
				Location rareLoc = new Location(SpiralCraftPlugin.getMainWorld(), playerX - 3, playerY, playerZ);

				SpiralPlayer spiralPlayer = SpiralPlayerList.getSpiralPlayer(((Player) sender).getUniqueId());
				Party playerParty = PartyManager.getParty(spiralPlayer.getPartyID());

				Treasure commonTreasure = new Treasure(commonLoc, Rarity.COMMON, plugin, playerParty);
				Treasure uncommonTreasure = new Treasure(uncommonLoc, Rarity.UNCOMMON, plugin, playerParty);
				Treasure rareTreasure = new Treasure(rareLoc, Rarity.RARE, plugin, playerParty);

				return true;
			}
			if ((args.length == 1 || args.length == 2) && args[0].equalsIgnoreCase("kill")) {
				if (args.length == 2) {
					PacketContainer destroyItem = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
					int[] entitiesID = new int[1];
					entitiesID[0] = Integer.parseInt(args[1]);
					destroyItem.getIntegerArrays().write(0, entitiesID);

					try {
						ProtocolLibrary.getProtocolManager().sendServerPacket(player, destroyItem);
					} catch (InvocationTargetException e) {
						throw new RuntimeException("Cannot send packet " + destroyItem, e);
					}
				}
				return true;
			}
			return true;
		}
		return false;
	}

}
