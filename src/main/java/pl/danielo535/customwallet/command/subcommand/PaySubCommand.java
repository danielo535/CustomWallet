package pl.danielo535.customwallet.command.subcommand;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.danielo535.customwallet.config.ConfigStorage;
import pl.danielo535.customwallet.manager.WalletManager;
import pl.danielo535.customwallet.utils.TextUtils;

import static pl.danielo535.customwallet.manager.WalletManager.roundingWalletMoney;

public class PaySubCommand {
    private final WalletManager walletManager;
    public PaySubCommand(WalletManager walletManager) {
        this.walletManager = walletManager;
    }
    public void executePay(CommandSender sender, Player player, String[] args) {
        if (args.length > 2) {
            if (sender.getName() != player.getName()) {
                double number = Double.parseDouble(args[2].replace(",", "."));
                double saldo = walletManager.checkWalletMoney((Player) sender);
                if (number > 0) {
                    if (saldo >= number) {
                        walletManager.payWalletMoney(sender, player, number);
                        double saldoSender = walletManager.checkWalletMoney((Player) sender);
                        double saldoUser = walletManager.checkWalletMoney(player);
                        sender.sendMessage(TextUtils.format(ConfigStorage.MESSAGES_PAY)
                                .replace("%player%", player.getName())
                                .replace("%saldo%", roundingWalletMoney(saldoSender))
                                .replace("%number%", roundingWalletMoney(number))
                        );
                        player.sendMessage(TextUtils.format(ConfigStorage.MESSAGES_PAY$USER)
                                .replace("%player%", player.getName())
                                .replace("%saldo%", roundingWalletMoney(saldoUser))
                                .replace("%number%", String.valueOf(number))
                        );
                    } else {
                        sender.sendMessage(TextUtils.format(ConfigStorage.MESSAGES$ERROR_PAY$VALUE));
                    }
                } else {
                    sender.sendMessage(TextUtils.format(ConfigStorage.MESSAGES$ERROR_MONEY));
                }
            } else {
                sender.sendMessage(TextUtils.format(ConfigStorage.MESSAGES$ERROR_PAY$MONEY));
            }
        } else {
            sender.sendMessage(TextUtils.format(ConfigStorage.MESSAGES$ERROR_NUMBER));
        }
    }
}
