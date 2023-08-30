package pl.danielo535.customwallet.command.subcommand;

import me.kodysimpson.simpapi.colors.ColorTranslator;
import org.bukkit.command.CommandSender;
import pl.danielo535.customwallet.config.ConfigStorage;
import pl.danielo535.customwallet.manager.DatabaseManager;
import pl.danielo535.customwallet.task.UpdateMoneyTask;

import java.sql.SQLException;

import static pl.danielo535.customwallet.command.WalletCommand.PERMISSION_ALL;
import static pl.danielo535.customwallet.command.WalletCommand.PERMISSION_RELOAD;
import static pl.danielo535.customwallet.manager.DatabaseManager.handleSQLException;

public class ReloadSubCommand {
    private static DatabaseManager databaseManager;
    private static UpdateMoneyTask updateMoneyTask;
    public ReloadSubCommand(DatabaseManager databaseManager, UpdateMoneyTask updateMoneyTask) {
        this.databaseManager = databaseManager;
        this.updateMoneyTask = updateMoneyTask;
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
            databaseManager.connect();
            try {
                if (databaseManager.connection != null && !databaseManager.connection.isClosed()) {
                    databaseManager.createTables(databaseManager.connection);
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
