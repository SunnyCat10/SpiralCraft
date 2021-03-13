package me.Sunny.SpiralCraft.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import java.util.List;
import java.util.UUID;

public class LootPickupEvent implements Listener {

    public LootPickupEvent() {}

    // TODO: create a utility function:
    public static final String UUID_REGEX = "[a-f0-9]{8}-[a-f0-9]{4}-4[0-9]{3}-[89ab][a-f0-9]{3}-[0-9a-f]{12}";

    @EventHandler
    public void lootPickup(EntityPickupItemEvent event) {
        if (event.getItem().getItemStack().hasItemMeta()) {
            System.out.println("DEBUG: The item has meta");
            if (event.getItem().getItemStack().getItemMeta().hasLore()) { // Items without lore cannot be plugin drops.
                System.out.println("DEBUG: The item has lore");
                List<String> lore = event.getItem().getItemStack().getItemMeta().getLore();
                for (String loreLine : lore) {
                    if (loreLine.matches(UUID_REGEX)) { // Items with UUID registered are plugin drops.
                        System.out.println("DEBUG: The item has UUID");
                        if (!(event.getEntity() instanceof Player)) { // Cancel the ability of mobs to pick the drop.
                            event.setCancelled(true);
                            return;
                        }
                        System.out.println("DEBUG: The picker is a Player");
                        UUID dropOwnerUUID = UUID.fromString(loreLine);
                        Player player = (Player) event.getEntity();
                        if (player.getUniqueId().equals(dropOwnerUUID)) {
                            System.out.println("DEBUG: The picker UUID matches");
                            return;
                        }
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }

    }

}
