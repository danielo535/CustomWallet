package pl.danielo535.customwallet.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.danielo535.customwallet.CustomWallet;
import pl.danielo535.customwallet.config.ConfigStorage;
import pl.danielo535.customwallet.manager.WalletManager;

public class UpdateMoneyTask {
    public BukkitRunnable task;
    private final WalletManager walletManager;
    private final CustomWallet plugin;
    public UpdateMoneyTask(WalletManager walletManager, CustomWallet plugin) {
        this.walletManager = walletManager;
        this.plugin = plugin;
    }

    public void startTask() {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    String playerName = player.getName();
                    double walletMoney = walletManager.checkWalletMoney(player);
                    walletManager.walletCache.put(playerName, walletMoney);
                }

            }
        };
        task.runTaskTimer(plugin, 0L, 20L * ConfigStorage.SETTINGS_UPDATE$VALUE);
    }
}
