package pl.danielo535.customwallet.command;

import me.kodysimpson.simpapi.colors.ColorTranslator;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.danielo535.customwallet.command.subcommand.*;
import pl.danielo535.customwallet.config.ConfigStorage;
import pl.danielo535.customwallet.manager.WalletManager;

import java.sql.SQLException;

import static pl.danielo535.customwallet.manager.MysqlManager.handleSQLException;

public class WalletCommand implements CommandExecutor {
    private final WalletManager walletManager;
    private final AddSubCommand addSubCommand;
    private final CheckSubCommand checkSubCommand;
    private final HelpSubCommand helpSubCommand;
    private final PaySubCommand paySubCommand;
    private final ReloadSubCommand reloadSubCommand;
    private final RemoveSubCommand removeSubCommand;
    private final SetSubCommand setSubCommand;

    public WalletCommand(WalletManager walletManager, AddSubCommand addSubCommand, CheckSubCommand checkSubCommand, HelpSubCommand helpSubCommand,
                         PaySubCommand paySubCommand, ReloadSubCommand reloadSubCommand, RemoveSubCommand removeSubCommand, SetSubCommand setSubCommand) {
        this.walletManager = walletManager;
        this.addSubCommand = addSubCommand;
        this.checkSubCommand = checkSubCommand;
        this.helpSubCommand = helpSubCommand;
        this.paySubCommand = paySubCommand;
        this.reloadSubCommand = reloadSubCommand;
        this.removeSubCommand = removeSubCommand;
        this.setSubCommand = setSubCommand;
    }
    // ALL PERMISSIONS COMMAND WALLET
    public static final String PERMISSION_ADD = "CustomWallet.add";
    public static final String PERMISSION_SET = "CustomWallet.set";
    public static final String PERMISSION_REMOVE = "CustomWallet.remove";
    public static final String PERMISSION_CHECK = "CustomWallet.check";
    public static final String PERMISSION_RELOAD = "CustomWallet.reload";
    public static final String PERMISSION_ALL = "CustomWallet.*";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ColorTranslator.translateColorCodes(ConfigStorage.MESSAGES_USAGE));
            return true;
        }
        String subCommand = args[0].toLowerCase();

        // WALLET COMMANDS [RELOAD, HELP]
        if (subCommand.equals("reload")) {
            reloadSubCommand.executeReload(sender);
        } else if (subCommand.equals("help")) {
            helpSubCommand.executeHelp(sender);
        } else if (args.length > 1) {
            try {
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(ColorTranslator.translateColorCodes(ConfigStorage.MESSAGES$ERROR_USER));
                    return true;
                } else if (!walletManager.checkPlayerDatabase(player)) {
                    sender.sendMessage(ColorTranslator.translateColorCodes(ConfigStorage.MESSAGES$ERROR_USER$DATABASE));
                    return true;
                }
                if (walletManager.checkPlayerDatabase(player)) {
                    if (subCommand.equals("check")) {
                        checkSubCommand.executeCheck(sender,player);
                    } else if (subCommand.equals("add")) {
                        if (args.length > 2) {
                            addSubCommand.executeAdd(sender, player, args);
                        } else {
                            sender.sendMessage(ColorTranslator.translateColorCodes(ConfigStorage.MESSAGES_USAGE));
                        }
                    } else if (subCommand.equals("set")) {
                        if (args.length > 2) {
                            setSubCommand.executeSet((Player) sender, player, args);
                        } else {
                            sender.sendMessage(ColorTranslator.translateColorCodes(ConfigStorage.MESSAGES_USAGE));
                        }
                    } else if (subCommand.equals("remove")) {
                        if (args.length > 2) {
                            removeSubCommand.executeRemove(sender, player, args);
                        } else {
                            sender.sendMessage(ColorTranslator.translateColorCodes(ConfigStorage.MESSAGES_USAGE));
                        }
                    } else if (subCommand.equals("pay")) {
                        if (args.length > 2) {
                            paySubCommand.executePay(sender, player, args);
                        } else {
                            sender.sendMessage(ColorTranslator.translateColorCodes(ConfigStorage.MESSAGES_USAGE));
                        }
                    }
                } else {
                    sender.sendMessage(ColorTranslator.translateColorCodes(ConfigStorage.MESSAGES$ERROR_USER$DATABASE));
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(ColorTranslator.translateColorCodes(ConfigStorage.MESSAGES$ERROR_USAGE$NUMBER));
            } catch (SQLException e) {
                handleSQLException(e);
            }
        } else {
            sender.sendMessage(ColorTranslator.translateColorCodes(ConfigStorage.MESSAGES_USAGE));
        }
        return true;
    }
}
