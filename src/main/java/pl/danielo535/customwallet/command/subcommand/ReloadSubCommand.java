package pl.danielo535.customwallet.command.subcommand;

import org.bukkit.command.CommandSender;
import pl.danielo535.customwallet.CustomWallet;
import pl.danielo535.customwallet.config.ConfigStorage;
import pl.danielo535.customwallet.task.UpdateMoneyTask;
import pl.danielo535.customwallet.manager.DatabaseManager;
import pl.danielo535.customwallet.utils.TextUtils;

import static pl.danielo535.customwallet.command.WalletCommand.PERMISSION_ALL;
import static pl.danielo535.customwallet.command.WalletCommand.PERMISSION_RELOAD;

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
            if (updateMoneyTask.task != null) {
                updateMoneyTask.task.cancel();
            }
            updateMoneyTask.startTask();

            sender.sendMessage(TextUtils.format(ConfigStorage.MESSAGES_RELOAD));
        } else {
            sender.sendMessage(TextUtils.format(ConfigStorage.MESSAGES$ERROR_NO$PERMISSION)
                    .replace("%permission%", PERMISSION_RELOAD)
            );
        }
    }
}
