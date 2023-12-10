package pl.danielo535.customwallet.manager;

import com.zaxxer.hikari.HikariDataSource;
import pl.danielo535.customwallet.CustomWallet;
import pl.danielo535.customwallet.config.ConfigStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.bukkit.Bukkit.getLogger;

/**
 * Manages the connection to the MySQL database, creation of tables, and database operations.
 */
public class DatabaseManager {
    public HikariDataSource dataSource;

    private static CustomWallet plugin;

    public DatabaseManager(CustomWallet plugin) {
        this.plugin = plugin;
    }

    /**
     * Establishes a connection to the MySQL database using configuration parameters.
     */
    public void connect(String type) {
        if (type.equalsIgnoreCase("Mysql")) {
            this.dataSource = new HikariDataSource();
            this.dataSource.setJdbcUrl("jdbc:mysql://" + ConfigStorage.DATABASE_HOST + ":" + ConfigStorage.DATABASE_PORT + "/" + ConfigStorage.DATABASE_DATABASE);
            this.dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            this.dataSource.setPoolName("CustomWallet-HikariPool");
            this.dataSource.setUsername(ConfigStorage.DATABASE_USERNAME);
            this.dataSource.setPassword(ConfigStorage.DATABASE_PASSWORD);
            this.dataSource.addDataSourceProperty("cachePrepStmts", true);
            this.dataSource.addDataSourceProperty("prepStmtCacheSize", 250);
            this.dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
            this.dataSource.addDataSourceProperty("useServerPrepStmts", true);
            this.dataSource.addDataSourceProperty("useLocalSessionState", true);
            this.dataSource.addDataSourceProperty("rewriteBatchedStatements", true);
            this.dataSource.addDataSourceProperty("cacheResultSetMetadata", true);
            this.dataSource.addDataSourceProperty("cacheServerConfiguration", true);
            this.dataSource.addDataSourceProperty("elideSetAutoCommits", true);
            this.dataSource.addDataSourceProperty("maintainTimeStats", false);
            this.dataSource.addDataSourceProperty("autoReconnect", true);
            this.dataSource.setMaxLifetime(1800000);
            this.dataSource.setConnectionTimeout(5000);
            this.dataSource.setValidationTimeout(3000);
            this.dataSource.setMaximumPoolSize(16);
        } else if (type.equalsIgnoreCase("SQlite")) {
            this.dataSource = new HikariDataSource();
            this.dataSource.setJdbcUrl("jdbc:sqlite:" + plugin.getDataFolder().getAbsolutePath() + "/wallet.db");
        }
        createTables();
    }

    /**
     * Closes the connection to the MySQL database.
     */
    public void disconnect() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            getLogger().info("Database connection closed.");
        }
    }

    /**
     * Creates necessary tables in the database if they don't exist.
     *
     * @throws SQLException If a database error occurs during table creation.
     */
    public void createTables() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS wallet (" +
                "player VARCHAR(64) NOT NULL," +
                "money DOUBLE NOT NULL" +
                ")";
        try (final Connection connection = this.dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(createTableSQL);
            statement.executeUpdate();
            statement.close();
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
                getLogger().warning("[" + plugin.getName() + "] ✘ Unable to establish a database connection. Check the connection configuration.");
            } else if (sqlState.equals("28000")) {
                getLogger().warning("[" + plugin.getName() + "] ✘ User authentication error. Check the correctness of the username and password.");
            } else if (sqlState.equals("42000")) {
                getLogger().warning("[" + plugin.getName() + "] ✘ SQL syntax error. Check the correctness of the SQL query construction.");
            } else if (sqlState.equals("08S01")) {
                getLogger().warning("[" + plugin.getName() + "] ✘ Database connection error. There might be an issue with the database server.");
            } else if (sqlState.equals("3D000")) {
                getLogger().warning("[" + plugin.getName() + "] ✘ Database error. Check if the database exists.");
            } else if (sqlState.equals("23000")) {
                getLogger().warning("[" + plugin.getName() + "] ✘ This object already exists in the database.");
            } else if (sqlState.equals("HY000")) {
                getLogger().warning("[" + plugin.getName() + "] ✘ General database error. Contact the administrator.");
            } else if (sqlState.equals("40001")) {
                getLogger().warning("[" + plugin.getName() + "] ✘ Transaction error. Attempted an invalid operation on a transaction.");
            } else if (sqlState.equals("42S02")) {
                getLogger().warning("[" + plugin.getName() + "] ✘ Database error. Table does not exist in the database.");
            } else {
                getLogger().warning("[" + plugin.getName() + "] ✘ An unknown error occurred with SQL state code: " + sqlState);
            }
        }
    }
}
