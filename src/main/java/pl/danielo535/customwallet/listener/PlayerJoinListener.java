package pl.danielo535.customwallet.listener;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.danielo535.customwallet.CustomWallet;
import pl.danielo535.customwallet.config.ConfigStorage;
import pl.danielo535.customwallet.update.CheckUpdate;
import pl.danielo535.customwallet.manager.DatabaseManager;
import pl.danielo535.customwallet.manager.WalletManager;
import pl.danielo535.customwallet.utils.TextUtils;

import java.sql.SQLException;

public class PlayerJoinListener implements Listener {
    private final CustomWallet plugin;
    private final DatabaseManager databaseManager;
    private final WalletManager walletManager;

    public PlayerJoinListener(CustomWallet plugin, DatabaseManager databaseManager, WalletManager walletManager) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.walletManager = walletManager;
    }

    /**
     * Event handler for player join events. Checks if the player exists in the database and adds them if not.
     * Also checks for plugin updates and informs operators about available updates.
     *
     * @param event The player join event.
     * @throws SQLException If an SQL-related error occurs.
     */
    @EventHandler
    public void onJoinServer(PlayerJoinEvent event) throws SQLException {
        Player player = event.getPlayer();
        if (!walletManager.checkPlayerDatabase(player)) {
            walletManager.addPlayerDatabase(player, 0);
        } else {
            walletManager.walletCache.put(player.getName(), walletManager.checkWalletMoney(player));
        }
        if (ConfigStorage.SETTINGS_UPDATE$INFO && player.isOp()) {
            new CheckUpdate(plugin, 112339).getVersion(version -> {
                if (!(plugin.getDescription().getVersion().equals(version))) {
                    player.sendMessage(TextUtils.format("&7[&6CustomWallet&7] &aThere is a new update available."));
                    TextComponent message = new TextComponent(TextUtils.format("&7[&6CustomWallet&7] Your version &c" + plugin.getDescription().getVersion() + "&7 new version &c" + version));
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "https://www.spigotmc.org/resources/112339"));
                    message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Copy download link").create()));
                    player.spigot().sendMessage(message);
                }
            });
        }
    }
}
