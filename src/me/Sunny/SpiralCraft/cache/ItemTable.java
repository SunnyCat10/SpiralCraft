package me.Sunny.SpiralCraft.cache;

/*
 * @Author Daniel Dovgun
 * @Version 20/2/21
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemTable {

    //TODO: Move to drop factory
    public enum ItemRarity{
        _1_STAR,
        _2_STARS,
        _3_STARS,
        _4_STARS,
        _5_STARS,
        DEVELOPMENT
    }

    private record Item(int id, String itemName, String material, String lore, ItemRarity itemRarity){}

    private static final ItemTable instance = new ItemTable();

    private List<Item> itemList = new ArrayList<>();

    /**
     * Private constructor to limit initiation of the class.
     */
    private ItemTable() {}

    /**
     * Returns Instance of ItemTable class.
     * @return Instance of ItemTable class.
     */
    public static ItemTable getInstance() { return instance; }

    /**
     * Loads the item list from the database to the cache.
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
            resultSet = statement.executeQuery(DBManager.ITEM_TABLE_QUERY);
            convertFromDB(resultSet);
        } catch (SQLException e) {
            System.out.println(DBManager.ERROR_DB_CONNECTION_FAILED);
        } finally {
            try { resultSet.close(); } catch (Exception ignored) {}
            try { statement.close(); } catch (Exception ignored) {}
            try { connection.close(); } catch (Exception ignored) {}
        }
    }

    /**
     * Convert result set of a query to a data format that can be stored inside the cache.
     * @param resultSet Result set of the database query.
     */
    private void convertFromDB(ResultSet resultSet) {
        try {
            if (!resultSet.isBeforeFirst()) { // Checks if the resultSet is empty.
                System.out.println("ERROR: The resultSet from drop table in the database is empty."); //<--TODO: Check if it is working...
                return;
            }
            while (resultSet.next()) {
                // The values we read from each of the dev.drop rows:
                int itemID = resultSet.getInt("id");
                String itemName = resultSet.getString("drop_name");
                String itemMaterial = resultSet.getString("mc_material_enum");
                String itemLore = resultSet.getString("drop_lore");
                int rarity = resultSet.getInt("rarity");

                itemList.add(new Item(itemID, itemName, itemMaterial, itemLore, convertRarity(rarity)));
            }

            // Trims LootTables list to save on memory space.
            ArrayList<Item> arr = (ArrayList<Item>) itemList;
            arr.trimToSize();
        } catch (SQLException e) {
            System.out.println("ERROR: ResultSet.next() failed while converting ResultSet to LootPairs.");
        }
    }

    /**
     * Converts from rarity ID to rarity enum.
     * @param rarityID Rarity ID.
     * @return Rarity enum.
     */
    private ItemRarity convertRarity(int rarityID){
        return switch (rarityID) {
            case 1 -> ItemRarity._1_STAR;
            case 2 -> ItemRarity._2_STARS;
            case 3 -> ItemRarity._3_STARS;
            case 4 -> ItemRarity._4_STARS;
            case 5 -> ItemRarity._5_STARS;
            default -> ItemRarity.DEVELOPMENT;
        };
    }

    /**
     * Returns an item with given ID.
     * @param id ID of the item to be fetched from the cache.
     * @return Item template.
     */
    public Item getItem(int id) {
        int index = id - 1;
        return (index > itemList.size() || index < 0) ? null : itemList.get(index);
    }

    @Override
    public String toString() {
        return "ItemTable{" +
                "itemList=" + itemList +
                '}';
    }
}
