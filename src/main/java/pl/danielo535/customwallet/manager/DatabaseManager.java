package pl.danielo535.customwallet.manager;

import org.bukkit.plugin.Plugin;
import pl.danielo535.customwallet.CustomWallet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.bukkit.Bukkit.getLogger;

/**
 * Manages the connection to the MySQL database, creation of tables, and database operations.
 */
public class DatabaseManager {
    public Connection connection;
    private String mysql, sqlite;

    private final CustomWallet plugin;
    public DatabaseManager(CustomWallet plugin) {
        this.plugin = plugin;
    }
    /**
     * Establishes a connection to the MySQL database using configuration parameters.
     */
    public synchronized void connect(String type,String host,Integer port, String database,String username,String password) {
        try {
            if (type.equalsIgnoreCase("Mysql")) {
                mysql = "jdbc:mysql://" + host + ":" + port + "/" + database;
                connection = DriverManager.getConnection(mysql, username, password);
            } else if (type.equalsIgnoreCase("SQlite")) {
                sqlite = plugin.getDataFolder().getAbsolutePath() + "/wallet.db";
                connection = DriverManager.getConnection("jdbc:sqlite:" + sqlite);
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }
    /**
     * Closes the connection to the MySQL database.
     */
    public synchronized void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            handleSQLException(e);
        } finally {
            connection = null;
        }
    }
    /**
     * Creates necessary tables in the database if they don't exist.
     *
     * @throws SQLException If a database error occurs during table creation.
     */
    public void createTables() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS wallet (" +
                "player VARCHAR(64) NOT NULL," +
                "money DOUBLE NOT NULL" +
                ")";
        try (PreparedStatement statement = connection.prepareStatement(createTableSQL)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }
    /**
     * Handles SQLExceptions by providing descriptive error messages based on SQL state codes.
     *
     * @param e The SQLException to handle.
     */
    public static void handleSQLException(SQLException e) {
        String sqlState = e.getSQLState();
        if (sqlState != null) {
            if (sqlState.equals("08001")) {
                getLogger().warning("✘ Unable to establish a database connection. Check the connection configuration.");
            } else if (sqlState.equals("28000")) {
                getLogger().warning("✘ User authentication error. Check the correctness of the username and password.");
            } else if (sqlState.equals("42000")) {
                getLogger().warning("✘ SQL syntax error. Check the correctness of the SQL query construction.");
            } else if (sqlState.equals("08S01")) {
                getLogger().warning("✘ Database connection error. There might be an issue with the database server.");
            } else if (sqlState.equals("3D000")) {
                getLogger().warning("✘ Database error. Check if the database exists.");
            } else if (sqlState.equals("23000")) {
                getLogger().warning("✘ This object already exists in the database.");
            } else if (sqlState.equals("HY000")) {
                getLogger().warning("✘ General database error. Contact the administrator.");
            } else if (sqlState.equals("40001")) {
                getLogger().warning("✘ Transaction error. Attempted an invalid operation on a transaction.");
            } else if (sqlState.equals("42S02")) {
                getLogger().warning("✘ Database error. Table does not exist in the database.");
            } else {
                getLogger().warning("✘ An unknown error occurred with SQL state code: " + sqlState);
            }
        }
    }
}
