package pl.danielo535.customwallet.command.subcommand;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.danielo535.customwallet.config.ConfigStorage;
import pl.danielo535.customwallet.manager.WalletManager;
import pl.danielo535.customwallet.utils.TextUtils;

import static pl.danielo535.customwallet.command.WalletCommand.PERMISSION_ALL;
import static pl.danielo535.customwallet.command.WalletCommand.PERMISSION_REMOVE;
import static pl.danielo535.customwallet.manager.WalletManager.roundingWalletMoney;

public class RemoveSubCommand {
    private final WalletManager walletManager;
    public RemoveSubCommand(WalletManager walletManager) {
        this.walletManager = walletManager;
    }
    public void executeRemove(CommandSender sender, Player player, String[] args) {
        if (sender.hasPermission(PERMISSION_REMOVE) || sender.hasPermission(PERMISSION_ALL)) {
            if (args.length > 2) {
                double number = Double.parseDouble(args[2].replace(",", "."));
                double saldoLast = walletManager.checkWalletMoney(player);
                if (saldoLast - number >= 0) {
                    walletManager.removeWalletMoney(player, number);
                    double saldo = walletManager.checkWalletMoney(player);
                    sender.sendMessage(TextUtils.format(ConfigStorage.MESSAGES_REMOVE)
                            .replace("%number%", roundingWalletMoney(number))
                            .replace("%player%", player.getName())
                            .replace("%saldo%", roundingWalletMoney(saldo))
                            .replace("%saldo-last%", roundingWalletMoney(saldoLast))
                    );
                } else {
                    sender.sendMessage(TextUtils.format(ConfigStorage.MESSAGES$ERROR_REMOVE$MONEY)
                            .replace("%player%", player.getName())
                            .replace("%saldo%", roundingWalletMoney(saldoLast))
                    );
                }
            } else {
                sender.sendMessage(TextUtils.format(ConfigStorage.MESSAGES$ERROR_NUMBER));

            }
        } else {
            sender.sendMessage(TextUtils.format(ConfigStorage.MESSAGES$ERROR_NO$PERMISSION)
                    .replace("%permission%", PERMISSION_REMOVE)
            );
        }
    }
}
