package pl.danielo535.customwallet.command.subcommand;

import me.kodysimpson.simpapi.colors.ColorTranslator;
import org.bukkit.command.CommandSender;
import pl.danielo535.customwallet.config.ConfigStorage;
import pl.danielo535.customwallet.manager.MysqlManager;

import java.sql.SQLException;

import static pl.danielo535.customwallet.command.WalletCommand.PERMISSION_ALL;
import static pl.danielo535.customwallet.command.WalletCommand.PERMISSION_RELOAD;
import static pl.danielo535.customwallet.manager.MysqlManager.handleSQLException;

public class ReloadSubCommand {
    private static MysqlManager mysqlManager;
    public ReloadSubCommand(MysqlManager mysqlManager) {
        this.mysqlManager = mysqlManager;
    }
    public void executeReload(CommandSender sender) {
        if (sender.hasPermission(PERMISSION_RELOAD) || sender.hasPermission(PERMISSION_ALL)) {
            ConfigStorage.reload();
            if (mysqlManager.connection != null) {
                mysqlManager.disconnect();
            }
            mysqlManager.connect();
            try {
                if (mysqlManager.connection != null && !mysqlManager.connection.isClosed()) {
                    mysqlManager.createTables(mysqlManager.connection);
                }
            } catch (SQLException e) {
                handleSQLException(e);
            }
            sender.sendMessage(ColorTranslator.translateColorCodes(ConfigStorage.MESSAGES_RELOAD));
            try {
                if (mysqlManager.connection == null || mysqlManager.connection.isClosed()) {
                    sender.sendMessage(ColorTranslator.translateColorCodes("&c✘ Database connection failed!"));
                } else {
                    sender.sendMessage(ColorTranslator.translateColorCodes("&a✔ Connected to the database!"));
                }
            } catch (SQLException e) {
                handleSQLException(e);
            }
        } else {
            sender.sendMessage(ColorTranslator.translateColorCodes(ConfigStorage.MESSAGES$ERROR_NO$PERMISSION)
                    .replace("%permission%", PERMISSION_RELOAD)
            );
        }
    }
}
