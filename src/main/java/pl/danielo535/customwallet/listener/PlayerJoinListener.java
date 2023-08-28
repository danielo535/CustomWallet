package pl.danielo535.customwallet.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.danielo535.customwallet.manager.MysqlManager;
import pl.danielo535.customwallet.manager.WalletManager;

import java.sql.SQLException;

public class PlayerJoinListener implements Listener {
    private final MysqlManager mysqlManager;
    private final WalletManager walletManager;
    public PlayerJoinListener(MysqlManager mysqlManager, WalletManager walletManager) {
        this.mysqlManager = mysqlManager;
        this.walletManager = walletManager;
    }
    /**
     * Event handler for player join events. Checks if the player exists in the database and adds them if not.
     *
     * @param event The player join event.
     * @throws SQLException If an SQL-related error occurs.
     */
    @EventHandler
    public void onJoinServer(PlayerJoinEvent event) throws SQLException {
        if (mysqlManager.connection != null || !mysqlManager.connection.isClosed()) {
            Player player = event.getPlayer();
            if (!walletManager.checkPlayerDatabase(player)) {
                walletManager.addPlayerDatabase(player, 0);
            }
        }
    }
}
