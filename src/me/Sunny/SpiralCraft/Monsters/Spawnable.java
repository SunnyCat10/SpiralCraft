package me.Sunny.SpiralCraft.Monsters;

import org.bukkit.util.Vector;

/**
 * All custom mobs should implement this interface.
 * * Used in MonsterGenerator to spawn the monsters correctly.
 */
public interface Spawnable {

    public void Spawn(Vector Location);
}
