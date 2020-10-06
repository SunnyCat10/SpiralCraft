package me.Sunny.SpiralCraft.Loot;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;

import me.Sunny.SpiralCraft.ItemBuilder;

public class Tresure {
	
	public enum Rarity {
		COMMON,
		UNCOMMON,
		RARE,	
	}
	
	private Chest chest;
	private Rarity rarity;
	
	public Tresure(Location location, Rarity rarity) {	
		// Generate the block
		// Generate the loot-table
		// Set break animation
		chest = generateTresure(location);
		this.rarity = rarity;
		setLootTable();
	}
	
	private Chest generateTresure(Location location) {
		Block block = location.getWorld().getBlockAt(location);
		block.setType(Material.CHEST);
		Chest chest = (Chest) block.getState();
		return chest;
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
