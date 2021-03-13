package me.Sunny.SpiralCraft.Events;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import me.Sunny.SpiralCraft.Data.Party;
import me.Sunny.SpiralCraft.Data.PartyManager;
import me.Sunny.SpiralCraft.Data.SpiralPlayer;
import me.Sunny.SpiralCraft.Data.SpiralPlayerList;
import me.Sunny.SpiralCraft.Loot.TreasureDropManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;

public class DropSpawnEvent implements Listener {

    public DropSpawnEvent(JavaPlugin plugin) { this.plugin = plugin; }

    private final JavaPlugin plugin;

    // TODO: create a utility function:
    public static final String UUID_REGEX = "[a-f0-9]{8}-[a-f0-9]{4}-4[0-9]{3}-[89ab][a-f0-9]{3}-[0-9a-f]{12}";

    @EventHandler
    public void dropSpawn(ItemSpawnEvent event) {
        System.out.println("DROP: event called");
        if (event.getEntity().getItemStack().hasItemMeta()) {
            System.out.println("DROP: meta");
            if (event.getEntity().getItemStack().getItemMeta().hasLore()) {
                System.out.println("DROP: has lore");
                List<String> lore = event.getEntity().getItemStack().getItemMeta().getLore();
                for (String loreLine : lore) {
                    if (loreLine.matches(UUID_REGEX)) {
                        System.out.println("DROP: UUID");
                        UUID itemUUID = UUID.fromString(loreLine);
                        SpiralPlayer spiralPlayer = SpiralPlayerList.getSpiralPlayer(itemUUID);

                        System.out.println(event.getEntity().getEntityId() + " > " + Bukkit.getPlayer(UUID.fromString(event.getEntity().getItemStack().getItemMeta().getLore().get(0))));

                        if (spiralPlayer.isInParty()) {
                            Party party = PartyManager.getParty(spiralPlayer.getPartyID());
                            for (SpiralPlayer partyMember : party.getPartyMembers()) {
                                if (!(partyMember.getPlayer().getUniqueId().equals(itemUUID))) {
                                    // TODO: temp
                                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                        @Override
                                        public void run() {
                                            System.out.println("Sending packet: " + partyMember.getPlayer().getDisplayName() + " | " + event.getEntity().getEntityId());
                                            sendPackets(partyMember.getPlayer(), event.getEntity().getEntityId());
                                        }
                                    }, 0L);
                                    continue;
                                }
                                System.out.println("Item with Player ID was found");
                            }
                        }
                        else {
                            System.out.println("DROP: not inside party");
                            return;
                        }
                    }
                }
            }
        }
    }

    public void sendPackets(Player player, int entityID) {
        PacketContainer destroyItem = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        int[] entitiesID = new int[1];
        entitiesID[0] = entityID;
        destroyItem.getIntegerArrays().write(0, entitiesID);

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, destroyItem);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Cannot send packet " + destroyItem, e);
        }
    }
}
