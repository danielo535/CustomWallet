package pl.danielo535.customwallet;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.danielo535.customwallet.command.TabCompleteCommand;
import pl.danielo535.customwallet.command.WalletCommand;
import pl.danielo535.customwallet.command.subcommand.*;
import pl.danielo535.customwallet.config.ConfigStorage;
import pl.danielo535.customwallet.listener.PlayerJoinListener;
import pl.danielo535.customwallet.manager.DatabaseManager;
import pl.danielo535.customwallet.manager.WalletManager;
import pl.danielo535.customwallet.placeholder.WalletPlaceholder;
import pl.danielo535.customwallet.task.UpdateMoneyTask;
import pl.danielo535.customwallet.update.CheckUpdate;

import java.sql.SQLException;

import static pl.danielo535.customwallet.manager.DatabaseManager.handleSQLException;

public final class CustomWallet extends JavaPlugin {
    public static CustomWallet getInstance() {
        return instance;
    }

    private static CustomWallet instance;
    public static String host, database, username, password, type;
    public static int port;
    private DatabaseManager databaseManager;
    private WalletManager walletManager;
    private AddSubCommand addSubCommand;
    private CheckSubCommand checkSubCommand;
    private HelpSubCommand helpSubCommand;
    private PaySubCommand paySubCommand;
    private ReloadSubCommand reloadSubCommand;
    private RemoveSubCommand removeSubCommand;
    private SetSubCommand setSubCommand;
    private UpdateMoneyTask updateMoneyTask;

    @Override
    public void onEnable() {
        new Metrics(this,19669);

        databaseManager = new DatabaseManager(this);
        walletManager = new WalletManager(databaseManager);
        updateMoneyTask = new UpdateMoneyTask(walletManager,this);

        addSubCommand = new AddSubCommand(walletManager);
        checkSubCommand = new CheckSubCommand(walletManager);
        helpSubCommand = new HelpSubCommand();
        paySubCommand = new PaySubCommand(walletManager);
        reloadSubCommand = new ReloadSubCommand(databaseManager,updateMoneyTask,this);
        removeSubCommand = new RemoveSubCommand(walletManager);
        setSubCommand = new SetSubCommand(walletManager);

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new WalletPlaceholder(walletManager, databaseManager).register();
        }
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this, databaseManager,walletManager), this);
        getCommand("wallet").setExecutor(new WalletCommand(walletManager,addSubCommand,checkSubCommand,helpSubCommand,paySubCommand,reloadSubCommand,removeSubCommand,setSubCommand));
        getCommand("wallet").setTabCompleter(new TabCompleteCommand());

        ConfigStorage.createDefaultFiles(this);
        ConfigStorage.load();

        try {
            setDatabaseConnection();
            databaseManager.connect(type,host,port,database,username,password);
            if (databaseManager.connection != null) {
                databaseManager.createTables();
            }
            getLogger().info("---------------------------------");
            getLogger().info(" ");
            getLogger().info("✔ CustomWallet enabled...");
            getLogger().info(" ");
            getLogger().info("---------------------------------");
            updateCheck();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }
    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }
    public WalletManager getWalletManager() {
        return this.walletManager;
    }
    public void setDatabaseConnection() {
        host = ConfigStorage.DATABASE_HOST;
        port = ConfigStorage.DATABASE_PORT;
        database = ConfigStorage.DATABASE_DATABASE;
        username = ConfigStorage.DATABASE_USERNAME;
        password = ConfigStorage.DATABASE_PASSWORD;
        type = ConfigStorage.SETTINGS_DATABASE$TYPE;
    }

    private void updateCheck() {
        if (!ConfigStorage.SETTINGS_UPDATE$INFO) return;
        new CheckUpdate(this, 112339).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                this.getLogger().info("There is not a new update available.");
            } else {
                this.getLogger().info("There is a new update available.");
                this.getLogger().info("Your version " + this.getDescription().getVersion() + " new version " + version);
            }
        });
    }

    @Override
    public void onDisable() {
        if (databaseManager.connection != null) {
            databaseManager.disconnect();
            getLogger().info("Database connection closed.");
        }
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new WalletPlaceholder(walletManager, databaseManager).unregister();
        }
    }
}
