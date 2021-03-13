package me.Sunny.SpiralCraft.Loot;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLib;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import me.Sunny.SpiralCraft.Data.Party;
import me.Sunny.SpiralCraft.Data.SpiralPlayer;
import me.Sunny.SpiralCraft.SpiralCraftPlugin;
import net.minecraft.server.v1_16_R2.PacketPlayOutEntityDestroy;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class TreasureDropManager {

    private TreasureDropManager() {}

    public static void manageDrops(Chest chest, Party party) {
        int partySize = party.getMemberSum();
        List<SpiralPlayer> partyMembers = party.getPartyMembers();

        ItemStack[] lootPool = createLootPool(partySize);
        // Add the loot itself.

        int partyMember = 0;
        for (ItemStack loot : lootPool) {
            String partyMemberUUID = partyMembers.get(partyMember).getPlayer().getUniqueId().toString();

            ItemMeta itemMeta = loot.getItemMeta();
            List<String> loreList = new ArrayList<>();
            loreList.add(partyMemberUUID);
            itemMeta.setLore(loreList);
            loot.setItemMeta(itemMeta);

            chest.getBlockInventory().addItem(loot);
            ++partyMember;
        }
    }

    private static ItemStack[] createLootPool(int partySize) {
        ItemStack[] lootPool = new ItemStack[partySize];
        for (int i = 0; i < lootPool.length; i++) {
            lootPool[i] = new ItemStack(Material.ANVIL);
        }
        return lootPool;
    }
}
