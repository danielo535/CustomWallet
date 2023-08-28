package pl.danielo535.customwallet.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TabCompleteCommand implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            if (sender.hasPermission("MultiWhiteList.*") ||
                    sender.hasPermission("MultiWhiteList.add") ||
                    sender.hasPermission("MultiWhiteList.set") ||
                    sender.hasPermission("MultiWhiteList.remove") ||
                    sender.hasPermission("MultiWhiteList.check")) {
                List<String> tabCompletions = new ArrayList<>();
                String[] subCommands = {"add", "remove", "pay", "set", "check", "reload", "help"};
                for (String subCommand : subCommands) {
                    if (subCommand.startsWith(args[0].toLowerCase())) {
                        tabCompletions.add(subCommand);
                    }
                }
                return tabCompletions;
            } else {
                List<String> tabCompletions = new ArrayList<>();
                String[] subCommands = {"pay", "help"};
                for (String subCommand : subCommands) {
                    if (subCommand.startsWith(args[0].toLowerCase())) {
                        tabCompletions.add(subCommand);
                    }
                }
                return tabCompletions;
            }
        }
        return null;
    }
}