package pl.danielo535.customwallet.manager;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import static pl.danielo535.customwallet.manager.DatabaseManager.handleSQLException;

public class WalletManager {
    private final DatabaseManager databaseManager;
    public final Map<String, Double> walletCache = new HashMap<>();

    public WalletManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Adds a player to the database with a specified amount of money.
     *
     * @param player  The player to be added to the database.
     * @param integer The initial amount of money to allocate to the player.
     */
    public void addPlayerDatabase(Player player, Integer integer) throws SQLException {
        try (final Connection connection = databaseManager.dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO wallet (player, money) VALUES (?, ?)")) {
                statement.setString(1, player.getName());
                statement.setInt(2, integer);
                statement.executeUpdate();
            } catch (SQLException e) {
                handleSQLException(e);
            }
        }
    }

    /**
     * Checks if a player is present in the database.
     *
     * @param player The player to check for in the database.
     * @return True if the player is present in the database, false otherwise.
     * @throws SQLException If a database error occurs during the operation.
     */
    public boolean checkPlayerDatabase(Player player) {
        try (final Connection connection = databaseManager.dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM wallet WHERE player = ?")) {
                statement.setString(1, player.getName());
                ResultSet resultSet = statement.executeQuery();
                boolean status = resultSet.next();
                statement.close();
                resultSet.close();
                return status;
            }
        } catch (SQLException e) {
            handleSQLException(e);
            return false;
        }
    }

    /**
     * Checks the amount of money in a player's wallet.
     *
     * @param player The player whose wallet balance is being checked.
     * @return The amount of money in the player's wallet.
     */
    public double checkWalletMoney(Player player) {
        try (final Connection connection = databaseManager.dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT money FROM wallet WHERE player = ?")) {
                statement.setString(1, player.getName());
                double status = 0.0;
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    status = resultSet.getDouble("money");
                }
                statement.close();
                resultSet.close();
                return status;
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return 0;
    }

    /**
     * Adds an amount of money to a player's wallet.
     *
     * @param player The player whose wallet is being updated.
     * @param number The amount of money to add.
     * @return True if the wallet was successfully updated, false otherwise.
     */
    public boolean addWalletMoney(Player player, Double number) {
        double currentBalance = walletCache.getOrDefault(player.getName(), checkWalletMoney(player));
        walletCache.put(player.getName(), currentBalance + number);
        try (final Connection connection = databaseManager.dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE wallet SET money = ? WHERE player = ?")) {
                if (checkWalletMoney(player) == 0) {
                    statement.setDouble(1, number);
                } else {
                    statement.setDouble(1, checkWalletMoney(player) + number);
                }
                statement.setString(2, player.getName());
                int rowsAffected = statement.executeUpdate();
                statement.close();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            handleSQLException(e);
            return false;
        }
    }

    /**
     * Sets the amount of money in a player's wallet.
     *
     * @param player The player whose wallet balance is being set.
     * @param number The new amount of money.
     * @return True if the wallet was successfully updated, false otherwise.
     */
    public boolean setWalletMoney(Player player, Double number) {
        try (final Connection connection = databaseManager.dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE wallet SET money = ? WHERE player = ?")) {
                statement.setDouble(1, number);
                statement.setString(2, player.getName());
                int rowsAffected = statement.executeUpdate();
                walletCache.put(player.getName(), number);
                statement.close();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            handleSQLException(e);
            return false;
        }
    }

    /**
     * Removes an amount of money from a player's wallet.
     *
     * @param player The player whose wallet is being updated.
     * @param number The amount of money to remove.
     * @return True if the wallet was successfully updated, false otherwise.
     */
    public boolean removeWalletMoney(Player player, Double number) {
        double currentBalance = walletCache.getOrDefault(player.getName(), checkWalletMoney(player));
        if (currentBalance < number) {
            return false;
        }
        walletCache.put(player.getName(), currentBalance - number);
        try (final Connection connection = databaseManager.dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE wallet SET money = ? WHERE player = ?")) {
                if (checkWalletMoney(player) == 0) {
                    return false;
                } else {
                    statement.setDouble(1, checkWalletMoney(player) - number);
                }
                statement.setString(2, player.getName());
                int rowsAffected = statement.executeUpdate();
                statement.close();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            handleSQLException(e);
            return false;
        }
    }

    /**
     * Pays a specific amount of money from one player's wallet to another.
     *
     * @param sender The sender of the command, acting as the payer.
     * @param player The player receiving the payment.
     * @param number The amount of money to be paid.
     * @return True if the payment was successful, false otherwise.
     */
    public boolean payWalletMoney(CommandSender sender, Player player, Double number) {
        double payerBalance = walletCache.getOrDefault(sender.getName(), checkWalletMoney((Player) sender));
        if (payerBalance < number) {
            return false;
        }
        double receiverBalance = walletCache.getOrDefault(player.getName(), checkWalletMoney(player));

        // Updating cached balances
        walletCache.put(sender.getName(), payerBalance - number);
        walletCache.put(player.getName(), receiverBalance + number);

        try (final Connection connection = databaseManager.dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE wallet SET money = ? WHERE player = ?")) {
                statement.setDouble(1, payerBalance - number);
                statement.setString(2, sender.getName());
                statement.executeUpdate();
                statement.setDouble(1, receiverBalance + number);
                statement.setString(2, player.getName());
                statement.executeUpdate();
                statement.close();
                return true;
            }
        } catch (SQLException e) {
            handleSQLException(e);
            return false;
        }
    }

    /**
     * Formats the wallet money value based on the given amount.
     * Values are formatted as K for thousands, M for millions, and B for billions.
     *
     * @param walletMoney The wallet money value to be formatted.
     * @return The formatted wallet money value.
     */
    public static String formatWalletMoney(Double walletMoney) {
        double moneyValue = walletMoney;

        if (moneyValue < 1000 && moneyValue % 1 < 0.01) {
            return roundingWalletMoney(moneyValue);
        } else if (moneyValue < 1_000_000) {
            double formattedValue = moneyValue < 1000 ? moneyValue : moneyValue / 1000.0;
            String unit = moneyValue < 1000 ? "" : "K";
            return formatFormattedValue(formattedValue, unit);
        } else if (moneyValue < 1_000_000_000) {
            double formattedValue = moneyValue / 1_000_000.0;
            return formatFormattedValue(formattedValue, "M");
        } else {
            double formattedValue = moneyValue / 1_000_000_000.0;
            return formatFormattedValue(formattedValue, "B");
        }
    }

    /**
     * Formats the given value and adds the specified unit to it.
     *
     * @param value The value to be formatted.
     * @param unit  The unit to be added (e.g., K for thousands, M for millions, B for billions).
     * @return The formatted value with the unit.
     */
    private static String formatFormattedValue(double value, String unit) {
        String formattedValue = roundingWalletMoney(value);
        return formattedValue + unit;
    }

    /**
     * Rounds the wallet money value to two decimal places and removes trailing ".00".
     *
     * @param walletMoney The wallet money value to be rounded.
     * @return The rounded wallet money value.
     */
    public static String roundingWalletMoney(double walletMoney) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String formattedValue = decimalFormat.format(walletMoney);
        if (walletMoney == 0) {
            return "0";
        } else if (formattedValue.endsWith(".00")) {
            formattedValue = formattedValue.substring(0, formattedValue.length() - 3);
        }
        return formattedValue;
    }
}
