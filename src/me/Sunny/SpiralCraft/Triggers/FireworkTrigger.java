package me.Sunny.SpiralCraft.Triggers;

import me.Sunny.SpiralCraft.Levels.LevelManager;
import me.Sunny.SpiralCraft.SpiralCraftPlugin;
import me.Sunny.SpiralCraft.Utils.ChunkGridUtils;
import me.Sunny.SpiralGeneration.Utils.Point;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkTrigger extends AbstractTrigger implements Triggable {

    private static final int CHUNK_SIZE = 16;

    public FireworkTrigger(int chunkX, int chunkY) {
        super(chunkX, chunkY);
    }

    @Override
    public void Trigger() {
        Location location = new Location(
                SpiralCraftPlugin.getMainWorld(),
                (CHUNK_SIZE*chunkX + 8),  // X coordinate.
                ChunkGridUtils.FLOOR_PLANE_HEIGHT,  // Y coordinate.
                (CHUNK_SIZE*chunkY + 8)); // Z coordinate.

        Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        fireworkMeta.setPower(2);
        fireworkMeta.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(Color.NAVY).flicker(true).withTrail().build());

        firework.setFireworkMeta(fireworkMeta);
        firework.detonate();

        System.out.println("Firework Triggered! " + location.getBlockX() + " | " + location.getBlockZ());
    }

    @Override
    public Point getTriggerLocation() {
        return new Point(super.chunkX, super.chunkY);
    }
}
