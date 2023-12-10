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

public final class CustomWallet extends JavaPlugin {
    private static CustomWallet instance;

    public static CustomWallet getInstance() {
        return instance;
    }

    public static String database;
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
        instance = this;

        new Metrics(this, 19669);

        databaseManager = new DatabaseManager(this);
        walletManager = new WalletManager(databaseManager);

        updateMoneyTask = new UpdateMoneyTask(walletManager, this);

        addSubCommand = new AddSubCommand(walletManager);
        checkSubCommand = new CheckSubCommand(walletManager);
        helpSubCommand = new HelpSubCommand();
        paySubCommand = new PaySubCommand(walletManager);
        reloadSubCommand = new ReloadSubCommand(databaseManager, updateMoneyTask, this);
        removeSubCommand = new RemoveSubCommand(walletManager);
        setSubCommand = new SetSubCommand(walletManager);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new WalletPlaceholder(walletManager, databaseManager).register();
        }
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this, databaseManager, walletManager), this);
        getCommand("wallet").setExecutor(new WalletCommand(walletManager, addSubCommand, checkSubCommand, helpSubCommand, paySubCommand, reloadSubCommand, removeSubCommand, setSubCommand));
        getCommand("wallet").setTabCompleter(new TabCompleteCommand());

        ConfigStorage.createDefaultFiles(this);
        ConfigStorage.load();

        databaseManager.connect(ConfigStorage.SETTINGS_DATABASE$TYPE);
        getLogger().info("╭─────────────────────────────────────╮");
        getLogger().info("│ CustomWallet Plugin Started         │");
        getLogger().info("│ Version: " + getDescription().getVersion() + "                        │");
        getLogger().info("│ Developer: danielo535               │");
        getLogger().info("│                                     │");
        getLogger().info("│ Thank you for using CustomWallet    │");
        getLogger().info("╰─────────────────────────────────────╯");
        updateCheck();

    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public WalletManager getWalletManager() {
        return walletManager;
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
        databaseManager.disconnect();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new WalletPlaceholder(walletManager, databaseManager).unregister();
        }
    }
}
