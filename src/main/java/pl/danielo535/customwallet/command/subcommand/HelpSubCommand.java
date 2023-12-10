package pl.danielo535.customwallet.command.subcommand;

import org.bukkit.command.CommandSender;
import pl.danielo535.customwallet.utils.TextUtils;

import java.util.Arrays;
import java.util.List;

import static pl.danielo535.customwallet.command.WalletCommand.*;

public class HelpSubCommand {
    //HELP MESSAGES
    public static List<String> help_messages = Arrays.asList(
            "&7===== CustomWallet Help v2 =====",
            "&7 -> &aAvailable Subcommands &7<-",
            "",
            "&7/wallet add <player> <amount> &7 - &eAdd money to a player's wallet.",
            "&7/wallet set <player> <amount> &7 - &eSet player's wallet balance.",
            "&7/wallet remove <player> <amount> &7 - &eRemove money from a player's.",
            "&7/wallet check <player> &f - &eCheck player's wallet balance.",
            "&7/wallet pay <player> <amount> &7 - &ePay money to another player.",
            "&7/wallet reload &7 - &eReload the plugin configuration.",
            "",
            "&7===========================",
            "",
            "&7 -> &aPermission for subcommands &7 <-",
            "&7" + PERMISSION_ADD + " &7 - &eAdd money to wallet",
            "&7" + PERMISSION_SET + " &7 - &eSet wallet balance",
            "&7" + PERMISSION_REMOVE + " &7 - &eRemove money from wallet",
            "&7" + PERMISSION_CHECK + " &7 - &eCheck wallet balance",
            "&7" + PERMISSION_RELOAD + " &7 - &eReload the plugin",
            "",
            "&7===========================",
            "",
            "&7 -> &aAvailable Placeholders &7 <-",
            "&7 %wallet_value% &7 - &eTotal wallet value (1000)",
            "&7 %wallet_value-formatted% &7 - &eFormatted wallet value (1k)",
            "",
            "&7==========================="
    );
    public void executeHelp(CommandSender sender) {
        help_messages.forEach(message -> sender.sendMessage(TextUtils.format(message)));
    }
}
