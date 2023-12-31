package pl.danielo535.customwallet.command.subcommand;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.danielo535.customwallet.config.ConfigStorage;
import pl.danielo535.customwallet.manager.WalletManager;
import pl.danielo535.customwallet.utils.TextUtils;

import static pl.danielo535.customwallet.command.WalletCommand.PERMISSION_ALL;
import static pl.danielo535.customwallet.command.WalletCommand.PERMISSION_CHECK;
import static pl.danielo535.customwallet.manager.WalletManager.roundingWalletMoney;

public class CheckSubCommand {
    private final WalletManager walletManager;
    public CheckSubCommand(WalletManager walletManager) {
        this.walletManager = walletManager;
    }
    public void executeCheck(CommandSender sender, Player player) {
        if (sender.hasPermission(PERMISSION_CHECK) || sender.hasPermission(PERMISSION_ALL)) {
            double saldo = walletManager.checkWalletMoney(player);
            sender.sendMessage(TextUtils.format(ConfigStorage.MESSAGES_CHECK)
                    .replace("%player%", player.getName())
                    .replace("%saldo%", roundingWalletMoney(saldo))
            );
        } else {
            sender.sendMessage(TextUtils.format(ConfigStorage.MESSAGES$ERROR_NO$PERMISSION)
                    .replace("%permission%", PERMISSION_CHECK)
            );
        }
    }
}
