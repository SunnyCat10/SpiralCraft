package me.Sunny.SpiralCraft.Events;

import me.Sunny.SpiralCraft.Loot.Treasure;
import me.Sunny.SpiralCraft.SpiralCraftPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChestBreakEvent implements Listener {

    public ChestBreakEvent() {}

    @EventHandler
    public void chestBreak(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            if (event.getClickedBlock().hasMetadata(Treasure.LOOT_CHEST_METADATA)) {
                Block block = event.getClickedBlock();
                chestBreakAnimation(block, event.getPlayer().getWorld());
            }
        }
    }

    private void chestBreakAnimation(Block chest, World world) {
        Location chestLocation = new Location(
                world,
                chest.getLocation().getBlockX(),
                chest.getLocation().getBlockY() + 1,
                chest.getLocation().getBlockZ());
        world.spawnParticle(Particle.REDSTONE, chestLocation, 30, getRarityColor(chest));
        world.spawnParticle(Particle.BLOCK_CRACK, chestLocation, 30, Material.CHEST.createBlockData());
        chest.setType(Material.AIR);
    }

    private DustOptions getRarityColor(Block chest) {
        if (chest.hasMetadata(Treasure.COMMON_CHEST)) {
            return Treasure.COMMON_PARTICLE;
        }
        else if (chest.hasMetadata(Treasure.UNCOMMON_CHEST)) {
            return Treasure.UNCOMMON_PARTICLE;
        }
        else if (chest.hasMetadata(Treasure.RARE_CHEST)) {
            return Treasure.RARE_PARTICLE;
        }
        else {
            // Default value:
            return Treasure.COMMON_PARTICLE;
        }
    }
}
