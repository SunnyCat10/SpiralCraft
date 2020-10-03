package me.Sunny.SpiralCraft;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {
	
	private ItemStack item;
	private ItemMeta itemMeta;
	
	public ItemBuilder(Material material) {
		item = new ItemStack(material);
		itemMeta = item.getItemMeta();
	}
	
	public ItemBuilder setName(String name) {
		itemMeta.setDisplayName(name);
		return this;
	}
	
	public ItemBuilder setAmount(int amount) {
		item.setAmount(amount);
		return this;
	}
	
	public ItemBuilder setLore(String... lore) {
		itemMeta.setLore(Arrays.asList(lore));
		return this;
	}
	
	/**
	 * Builds the ItemStack with the custom ItemMeta.
	 * @return The ItemStack.
	 */
	public ItemStack build() {
		item.setItemMeta(itemMeta);
		return item;
	}
	


}
