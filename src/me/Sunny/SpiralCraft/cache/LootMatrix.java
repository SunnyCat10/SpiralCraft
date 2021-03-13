package me.Sunny.SpiralCraft.cache;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * @Author Daniel Dovgun
 * @Version 20/2/21
 */

/**
 * Container class for catching all the loot tables.
 * This class follows singleton pattern as only one copy should exist at runtime.
 */
public class LootMatrix {

    private static final LootMatrix instance = new LootMatrix();
    private final List<LootTable> lootTables = new ArrayList<>();
    private final Random random = new Random();

    /**
     * Private constructor to limit initiation of the class.
     */
    private LootMatrix() {}

    /**
     * Returns Instance of LootMatrix class.
     * @return Instance of LootMatrix class.
     */
    public static LootMatrix getInstance() { return instance; }

    /**
     * Returns a loot table by the ID specified.
     * @param id ID of the loot table.
     * @return immutable loot table.
     */
    public LootTable getLootTable(int id) {
        int index = id - 1;
        return (index > lootTables.size() || index < 0) ? null : lootTables.get(index);
    }

    //TODO: Can be later moved to special DB utilities.
    /**
     * Loads the LootTables from the Database.
     */
    public void loadFromDB() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(
                    DBManager.getInstance().getDBCredentials().url(),
                    DBManager.getInstance().getDBCredentials().user(),
                    DBManager.getInstance().getDBCredentials().password());
            statement = connection.createStatement();
            resultSet = statement.executeQuery(DBManager.LOOT_TABLES_QUERY);
            convertToLootPairs(resultSet);
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println(DBManager.ERROR_DB_CONNECTION_FAILED);
        } finally {
            try { resultSet.close(); } catch (Exception ignored) {}
            try { statement.close(); } catch (Exception ignored) {}
            try { connection.close(); } catch (Exception ignored) {}
        }
    }

    /**
     * Converts ResultSet from DB query into LootPair records and adds them to the LootMatrix.
     * @param resultSet ResultSet from DB query.
     */
    private void convertToLootPairs(ResultSet resultSet) {
        int tableIndex = 1; // The table indexing starts at 1.
        ArrayList<LootTable.LootPair> lootPairs = new ArrayList<>();

        try {
            if (!resultSet.isBeforeFirst()) { // Checks if the resultSet is empty.
                System.out.println("ERROR: The resultSet from lootTables table in the database is empty."); //<--TODO: Check if it is working...
                return;
            }
            // The rolling algorithm is based on modified weight system,
            // the following variable is used to track the current sum of all the weights in the loot table.
            int totalWeight = 0;

            while (resultSet.next()) {
                // The values we read from each of the dev.loot_table rows:
                int tableTypeId = resultSet.getInt("table_type_id");
                int dropId = resultSet.getInt("drop_id");
                int weight = resultSet.getInt("weight");

                // If the table type ID increased we started reading from a new loot table,
                // We add the current loot table to the matrix, and reassign all the variables to receive the next loot table.
                if (tableTypeId != tableIndex) {
                    lootTables.add(new LootTable(tableIndex, lootPairs, random));
                    ++tableIndex;
                    lootPairs = new ArrayList<>();
                    totalWeight = 0;
                }

                // Crete a new LootPair from received DB row.
                lootPairs.add(new LootTable.LootPair(totalWeight += weight, dropId));
            }
            // Adds the last loot table to the matrix.
            lootTables.add(new LootTable(tableIndex, lootPairs, random));

            // Trims LootTables list to save on memory space.
            ArrayList<LootTable> arr = (ArrayList<LootTable>) lootTables;
            arr.trimToSize();
        } catch (SQLException e) {
            System.out.println("ERROR: ResultSet.next() failed while converting ResultSet to LootPairs.");
        }
    }

    @Override
    public String toString() {
        return "LootMatrix{" +
                "lootTables=" + lootTables.toString() +
                '}';
    }
}
