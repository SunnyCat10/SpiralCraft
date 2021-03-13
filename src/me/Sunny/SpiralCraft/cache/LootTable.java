package me.Sunny.SpiralCraft.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * @Author Daniel Dovgun
 * @Version 19/2/21
 */

/**
 * Container class for catching a representation of a loot table.
 */
public class LootTable {

    /**
     * Record of a weight:itemID pair.
     */
    public record LootPair(int weight, int itemId) {}

    private final int tableTypeId;
    private final int maximalWeight; // Highest weight in the loot table, used by the rolling algorithm.
    private final List<LootPair> lootList;
    private final Random random; // Pseudo random generator for rolling algorithm.

    /**
     * LootTable constructor.
     * @param tableTypeId ID of the loot table.
     * @param lootList List of LootPairs.
     * @param random Random class for pseudo random generation.
     */
    public LootTable(int tableTypeId, ArrayList<LootPair> lootList, Random random) {
        this.tableTypeId = tableTypeId;
        lootList.trimToSize(); // Trims the LootPair list to save on memory space.
        this.lootList = lootList;
        this.maximalWeight = this.lootList.get(this.lootList.size()-1).weight + 1;
        this.random = random;
    }

    /**
     * Rolls an item based on the loot table set probabilities.
     * @return The ID of the drop.
     */
    public int roll() { return binarySearch(random.nextInt(maximalWeight)); }

    /**
     * Binary search implementation for quick range searching in the loot table.
     * @param rollValue The value to find in the loot table`s ranges.
     * @return The ID of the drop.
     */
    private int binarySearch(int rollValue) {
        int left = 0, right = lootList.size() - 1;
        while (left <= right) {
            int middle = left + (right - left) / 2;

            // Check if rollValue is present at the middle.
            if (lootList.get(middle).weight() == rollValue)
                return lootList.get(middle).itemId;

            // If rollValue is greater, ignore left half.
            if (lootList.get(middle).weight() < rollValue)
                left = middle + 1;

                // If x is smaller, ignore right half
            else {
                if (middle - 1 == -1)
                    return lootList.get(middle).itemId();
                else {
                    if (lootList.get(middle - 1).weight() < rollValue)
                        return lootList.get(middle).itemId;
                    else
                        right = middle - 1;
                }
            }
        }

        // if we reach here, then element was
        // not present
        return -1;
    }

    @Override
    public String toString() {
        return "LootTable{" +
                "tableTypeId=" + tableTypeId +
                ", lootList=" + lootList.toString() +
                '}';
    }
}
