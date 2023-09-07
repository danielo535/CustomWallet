package pl.danielo535.customwallet.command.subcommand;

import me.kodysimpson.simpapi.colors.ColorTranslator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.danielo535.customwallet.config.ConfigStorage;
import pl.danielo535.customwallet.manager.WalletManager;

import static pl.danielo535.customwallet.command.WalletCommand.PERMISSION_ADD;
import static pl.danielo535.customwallet.command.WalletCommand.PERMISSION_ALL;
import static pl.danielo535.customwallet.manager.WalletManager.roundingWalletMoney;

public class AddSubCommand {
    private final WalletManager walletManager;
    public AddSubCommand(WalletManager walletManager) {
        this.walletManager = walletManager;
    }
    public void executeAdd(CommandSender sender, Player player, String[] args) {
        if (sender.hasPermission(PERMISSION_ADD) || sender.hasPermission(PERMISSION_ALL)) {
            if (args.length > 2) {
                double number = Double.parseDouble(args[2].replace(",", "."));
                if (number > 0) {
                    double saldoLast = walletManager.checkWalletMoney(player);
                    walletManager.addWalletMoney(player, number);
                    double saldo = walletManager.checkWalletMoney(player);
                    sender.sendMessage(ColorTranslator.translateColorCodes(ConfigStorage.MESSAGES_ADD)
                            .replace("%number%", roundingWalletMoney(number))
                            .replace("%player%", player.getName())
                            .replace("%saldo%", roundingWalletMoney(saldo))
                            .replace("%saldo-last%", roundingWalletMoney(saldoLast))
                    );
                } else {
                    sender.sendMessage(ColorTranslator.translateColorCodes(ConfigStorage.MESSAGES$ERROR_MONEY));
                }
            } else {
                sender.sendMessage(ColorTranslator.translateColorCodes(ConfigStorage.MESSAGES$ERROR_NUMBER));
            }
        } else {
            sender.sendMessage(ColorTranslator.translateColorCodes(ConfigStorage.MESSAGES$ERROR_NO$PERMISSION)
                    .replace("%permission%", String.valueOf(PERMISSION_ADD))
            );
        }
    }
}
