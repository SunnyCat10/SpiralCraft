package me.Sunny.SpiralCraft.cache;

/*
 * @Author Daniel Dovgun
 * @Version 20/2/21
 */

/**
 * Utility class for managing database operations.
 */
public class DBManager {
    private static final DBManager instance = new DBManager();

    /**
     * Credentials record used in database connections.
     */
    public record Credentials(String url, String user, String password) {}

    private static final String POSTGRESS_DRIVER_NAME = "org.postgresql.Driver";

    // DB credentials:
    private static final String URL = "jdbc:postgresql://localhost:5433/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "sunny";

    // SQL statements:
    public static final String ITEM_TABLE_QUERY = "SELECT * FROM dev.drop";
    public static final String LOOT_TABLES_QUERY = "SELECT * FROM dev.loot_table";

    // Errors:
    public static final String ERROR_POSTGRESS_DRIVER_NOT_FOUND = "ERROR: Postgres driver was not found.";
    public static final String ERROR_DB_CONNECTION_FAILED = "ERROR: Connection to the database failed.";

    private final Credentials DBCredentials = new Credentials(URL, USER, PASSWORD);

    private DBManager() {
        try { // Checks if postgress driver is installed on the system.
            Class.forName(POSTGRESS_DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            System.out.println(DBManager.ERROR_POSTGRESS_DRIVER_NOT_FOUND);
        }
    }

    public static DBManager getInstance() { return instance; }
    public Credentials getDBCredentials() {return DBCredentials; }
}
