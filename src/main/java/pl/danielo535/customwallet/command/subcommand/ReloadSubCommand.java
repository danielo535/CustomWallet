package pl.danielo535.customwallet.command.subcommand;

import me.kodysimpson.simpapi.colors.ColorTranslator;
import org.bukkit.command.CommandSender;
import pl.danielo535.customwallet.CustomWallet;
import pl.danielo535.customwallet.config.ConfigStorage;
import pl.danielo535.customwallet.task.UpdateMoneyTask;
import pl.danielo535.customwallet.manager.DatabaseManager;

import java.sql.SQLException;

import static pl.danielo535.customwallet.CustomWallet.*;
import static pl.danielo535.customwallet.command.WalletCommand.PERMISSION_ALL;
import static pl.danielo535.customwallet.command.WalletCommand.PERMISSION_RELOAD;
import static pl.danielo535.customwallet.manager.DatabaseManager.handleSQLException;

public class ReloadSubCommand {
    private final DatabaseManager databaseManager;
    private final UpdateMoneyTask updateMoneyTask;
    private final CustomWallet customWallet;
    public ReloadSubCommand(DatabaseManager databaseManager, UpdateMoneyTask updateMoneyTask, CustomWallet customWallet) {
        this.databaseManager = databaseManager;
        this.updateMoneyTask = updateMoneyTask;
        this.customWallet = customWallet;
    }
    public void executeReload(CommandSender sender) {
        if (sender.hasPermission(PERMISSION_RELOAD) || sender.hasPermission(PERMISSION_ALL)) {
            ConfigStorage.reload();
            if (databaseManager.connection != null) {
                databaseManager.disconnect();
            }
            if (updateMoneyTask.task != null) {
                updateMoneyTask.task.cancel();
            }
            updateMoneyTask.startTask();
            customWallet.setDatabaseConnection();
            databaseManager.connect(type,host,port,database,username,password);
            try {
                if (databaseManager.connection != null && !databaseManager.connection.isClosed()) {
                    databaseManager.createTables();
                }
            } catch (SQLException e) {
                handleSQLException(e);
            }
            sender.sendMessage(ColorTranslator.translateColorCodes(ConfigStorage.MESSAGES_RELOAD));
            try {
                if (databaseManager.connection == null || databaseManager.connection.isClosed()) {
                    sender.sendMessage(ColorTranslator.translateColorCodes("&c✘ Database connection failed!"));
                } else {
                    sender.sendMessage(ColorTranslator.translateColorCodes("&a✔ Connected to the database!"));
                }
            } catch (SQLException e) {
                handleSQLException(e);
            }
        } else {
            sender.sendMessage(ColorTranslator.translateColorCodes(ConfigStorage.MESSAGES$ERROR_NO$PERMISSION)
                    .replace("%permission%", String.valueOf(PERMISSION_RELOAD))
            );
        }
    }
}
