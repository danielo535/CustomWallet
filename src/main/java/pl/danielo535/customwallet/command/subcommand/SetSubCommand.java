package pl.danielo535.customwallet.command.subcommand;

import org.bukkit.entity.Player;
import pl.danielo535.customwallet.config.ConfigStorage;
import pl.danielo535.customwallet.manager.WalletManager;
import pl.danielo535.customwallet.utils.TextUtils;

import static pl.danielo535.customwallet.command.WalletCommand.PERMISSION_ALL;
import static pl.danielo535.customwallet.command.WalletCommand.PERMISSION_SET;
import static pl.danielo535.customwallet.manager.WalletManager.roundingWalletMoney;

public class SetSubCommand {
    private final WalletManager walletManager;
    public SetSubCommand(WalletManager walletManager) {
        this.walletManager = walletManager;
    }
    public void executeSet(Player sender, Player player , String[] args) {
        if (sender.hasPermission(PERMISSION_SET) || sender.hasPermission(PERMISSION_ALL)) {
            if (args.length > 2) {
                double number = Double.parseDouble(args[2].replace(",", "."));
                if (number >= 0) {
                    double saldoLast = walletManager.checkWalletMoney(player);
                    walletManager.setWalletMoney(player, number);
                    double saldo = walletManager.checkWalletMoney(player);
                    sender.sendMessage(TextUtils.format(ConfigStorage.MESSAGES_SET)
                            .replace("%player%", player.getName())
                            .replace("%saldo%", roundingWalletMoney(saldo))
                            .replace("%saldo-last%", roundingWalletMoney(saldoLast))
                    );
                } else {
                    sender.sendMessage(TextUtils.format(ConfigStorage.MESSAGES$ERROR_MONEY));
                }
            } else {
                sender.sendMessage(TextUtils.format(ConfigStorage.MESSAGES$ERROR_NUMBER));
            }
        } else {
            sender.sendMessage(TextUtils.format(ConfigStorage.MESSAGES$ERROR_NO$PERMISSION)
                    .replace("%permission%", PERMISSION_SET)
            );
        }
    }
}
