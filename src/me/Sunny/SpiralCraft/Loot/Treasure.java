package me.Sunny.SpiralCraft.Loot;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;

import me.Sunny.SpiralCraft.ItemBuilder;

public class Treasure {
	
	public enum Rarity {
		COMMON,
		UNCOMMON,
		RARE
	}
	
	private final Chest chest;
	private final Rarity rarity;
	
	public Treasure(Location location, Rarity rarity) {
		// Generate the block
		// Generate the loot-table
		// Set break animation
		chest = generateTreasure(location);
		this.rarity = rarity;
		setLootTable();
	}
	
	private Chest generateTreasure(Location location) {
		Block block = location.getWorld().getBlockAt(location);
		block.setType(Material.CHEST);
		return (Chest) block.getState();
	}
	
	private void setLootTable() {
		switch (rarity) {
		case COMMON:
			chest.getInventory().addItem(new ItemBuilder(Material.GRAY_STAINED_GLASS).build());
			break;
		case UNCOMMON:
			chest.getInventory().addItem(new ItemBuilder(Material.GREEN_STAINED_GLASS).build());
			break;
		case RARE:
			chest.getInventory().addItem(new ItemBuilder(Material.BLUE_STAINED_GLASS).build());
			break;
		}
	}
}
