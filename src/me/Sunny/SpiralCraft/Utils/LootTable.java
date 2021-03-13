package me.Sunny.SpiralCraft.Utils;


import java.util.Random;

public class LootTable {

    private int ID;
    private int sumWeight;
    private LootBean[] lootTable;

    public LootTable(int ID) {
        this.ID = ID;
    }

    public void load() {
        if (!(isInBuffer())) {
            lootTable = readFromDB(ID);
            writeToBuffer(lootTable);
        }
        else {
            lootTable = readFromBuffer(ID);
        }
        setSumWeight();
    }

    public void roll() {
        Random rand = new Random();
        int roll = rand.nextInt(sumWeight);


    }

    private int setSumWeight() {
        int sumWeight = 0;
        for (int i = 0; i < lootTable.length; i++) {
            sumWeight += lootTable[i].getWeight();
        }
        return sumWeight;
    }

    private int binarySearch(int value) {
        boolean wasFound = false;
        while (!(wasFound)) {
            int middle = lootTable[lootTable.length / 2].getWeight();
            if (middle > value) {

            }

        }
        return 0;
    }


    private boolean isInBuffer() { return true; }
    private LootBean[] readFromDB(int ID) { return null; }
    private LootBean[] readFromBuffer(int ID) { return null; }
    private void writeToBuffer(LootBean[] lootTable) {}
}
