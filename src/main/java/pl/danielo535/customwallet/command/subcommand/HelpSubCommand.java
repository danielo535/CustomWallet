package pl.danielo535.customwallet.command.subcommand;

import me.kodysimpson.simpapi.colors.ColorTranslator;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

import static pl.danielo535.customwallet.command.WalletCommand.*;

public class HelpSubCommand {
    //HELP MESSAGES
    public static List<String> help_messages = Arrays.asList(
            "&7===== CustomWallet Help =====",
            "&7- &aAvailable Subcommands:",
            "&e/add <player> <amount> &7- Add money to a player's wallet.",
            "&e/set <player> <amount> &7- Set player's wallet balance.",
            "&e/remove <player> <amount> &7- Remove money from a player's.",
            "&e/check <player> &7- Check player's wallet balance.",
            "&e/pay <player> <amount> &7- Pay money to another player.",
            "&e/reload &7- Reload the plugin configuration.",
            "&7===========================",
            "&7- &aPermission for subcommands:",
            "&e" + PERMISSION_ADD + " &7- Add money to wallet",
            "&e" + PERMISSION_SET + " &7- Set wallet balance",
            "&e" + PERMISSION_REMOVE + " &7- Remove money from wallet",
            "&e" + PERMISSION_CHECK + " &7- Check wallet balance",
            "&e" + PERMISSION_RELOAD + " &7- Reload the plugin",
            "&7===========================",
            "&7- &aAvailable Placeholders:",
            "&e%wallet_value% &7- Total wallet value (1000)",
            "&e%wallet_value-formatted% &7- Formatted wallet value (1k)",
            "&7==========================="
    );
    public void executeHelp(CommandSender sender) {
        for (String message : help_messages) {
            sender.sendMessage(ColorTranslator.translateColorCodes(message));
        }
    }
}
