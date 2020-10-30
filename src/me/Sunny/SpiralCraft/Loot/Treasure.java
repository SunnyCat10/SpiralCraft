package me.Sunny.SpiralCraft.Loot;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;

import me.Sunny.SpiralCraft.ItemBuilder;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public class Treasure {

	/**
	 * Metadata that is placed upon treasure chests.
	 */
	public static final String LOOT_CHEST_METADATA = "LootChest";
	public static final String COMMON_CHEST = "CommonChest";
	public static final String UNCOMMON_CHEST = "UncommonChest";
	public static final String RARE_CHEST = "RareChest";

	// TODO: all the colors can be custom made from RGB...
	public static final DustOptions COMMON_PARTICLE = new DustOptions(Color.GRAY, 1);
	public static final DustOptions UNCOMMON_PARTICLE = new DustOptions(Color.GREEN, 1);
	public static final DustOptions RARE_PARTICLE = new DustOptions(Color.BLUE, 1);

	
	public enum Rarity {
		COMMON,
		UNCOMMON,
		RARE
	}
	
	private final Chest chest;
	private final Rarity rarity;
	
	public Treasure(Location location, Rarity rarity, JavaPlugin plugin) {
		// Generate the block
		// Generate the loot-table
		// Set break animation
		chest = generateTreasure(location);
		setMetaData(chest, plugin);
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

	// TODO: add local JavaPlugin variable.
	private void setMetaData(Chest chest, JavaPlugin plugin) {
		chest.setMetadata(LOOT_CHEST_METADATA, new FixedMetadataValue(plugin, true));
		/*switch (rarity) {
			case COMMON:
				chest.setMetadata(COMMON_CHEST, new FixedMetadataValue(plugin, true));
				break;
			case UNCOMMON:
				chest.setMetadata(UNCOMMON_CHEST, new FixedMetadataValue(plugin, true));
				break;
			case RARE:
				chest.setMetadata(RARE_CHEST, new FixedMetadataValue(plugin, true));
				break;
		}*/
	}
}
