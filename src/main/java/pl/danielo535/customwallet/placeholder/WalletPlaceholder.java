package pl.danielo535.customwallet.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.danielo535.customwallet.manager.DatabaseManager;
import pl.danielo535.customwallet.manager.WalletManager;

import static pl.danielo535.customwallet.manager.WalletManager.formatWalletMoney;
import static pl.danielo535.customwallet.manager.WalletManager.roundingWalletMoney;

public class WalletPlaceholder extends PlaceholderExpansion {
    private final WalletManager walletManager;
    private final DatabaseManager databaseManager;
    public WalletPlaceholder(WalletManager walletManager, DatabaseManager databaseManager) {
        this.walletManager = walletManager;
        this.databaseManager = databaseManager;
    }

    @NotNull
    public String getIdentifier() {
        return "wallet";
    }

    @NotNull
    public String getAuthor() {
        return "danielo535";
    }

    @NotNull
    public String getVersion() {
        return "1.0";
    }

    @NotNull
    public String getName() {
        return "Wallet";
    }

    public boolean persist() {
        return true;
    }

    public boolean canRegister() {
        return true;
    }

    @Nullable
    public String onPlaceholderRequest(final Player p, @NotNull final String identifier) {
        String noData = "No data";
        if (databaseManager.connection != null) {
            double walletMoney;
            if (walletManager.walletCache.containsKey(p.getName())) {
                walletMoney = walletManager.walletCache.get(p.getName());
            } else {
                walletMoney = walletManager.checkWalletMoney(p);
                walletManager.walletCache.put(p.getName(), walletMoney);
            }
            if (identifier.equals("value")) {
                return roundingWalletMoney(walletMoney);
            }
            if (identifier.equals("value-formatted")) {
                return formatWalletMoney(walletMoney);
            }
        } else {
            return noData;
        }
        return null;
    }
}
