<div align="center">
  
  ![CustomWallet Logo](https://imgur.com/ZzNyzrY.png)

  # CustomWallet Plugin
  **Api Usage info**: [Api CustomWallet](https://github.com/danielo535/CustomWallet/wiki/CustomWallet-Api)<br />
  **Softdepend**: [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)

  [![](https://jitpack.io/v/danielo535/CustomWallet.svg)](https://jitpack.io/#danielo535/CustomWallet)
  ![](https://img.shields.io/github/v/release/danielo535/customwallet.svg)
  ![](https://img.shields.io/github/last-commit/danielo535/customwallet.svg)

  <a href="/#"><img src="https://raw.githubusercontent.com/intergrav/devins-badges/v2/assets/compact/supported/spigot_46h.png" height="35"></a>
  <a href="/#"><img src="https://raw.githubusercontent.com/intergrav/devins-badges/v2/assets/compact/supported/paper_46h.png" height="35"></a>
  <a href="/#"><img src="https://raw.githubusercontent.com/intergrav/devins-badges/v2/assets/compact/supported/purpur_46h.png" height="35"></a>

</div>

## Description

CustomWallet is a powerful Minecraft plugin that provides an advanced currency management platform for your server's economy. 
With an intuitive interface and versatile commands, CustomWallet allows you to create complex economic systems, rewards, and other mechanics related to in-game currency. 
Easily manage player wallets, monitor account balances, and configure permissions to control access to various economic features.

## Features

- **Easy to Use**: CustomWallet is designed with simplicity in mind. With its intuitive interface and clear documentation, installing and configuring the plugin is a breeze.

- **Flexible Permissions**: The plugin offers a flexible permission system, allowing you to precisely control access to different commands for players. Use predefined permissions such as CustomWallet.add, CustomWallet.set, CustomWallet.remove, and more to manage economic functionality.

- **Robust MySQL Error Handling**: CustomWallet ensures the reliability of your server's economy by effectively managing errors related to the MySQL database. Whether it's connection issues or query errors, the plugin responds elegantly, providing valuable error information for troubleshooting.

- **Rich Functionality**: From adding and removing funds to checking account balances and facilitating player-to-player payments, CustomWallet offers a wide array of features that give you full control over your server's economy.

## Placeholder

**PlaceholderAPI variables**:
- `%wallet_value%` - returns player balance `(1000)`
- `%wallet_value-formatted%` - returns player balance `(1K)`

## Commands

- `/wallet`: Main plugin command, providing access to various economic functions.
- `/wallet pay [player] [amount]`: Allows players to make payments to each other without special permissions.
- `/wallet add [nickname] [amount]`: Enables administrators to add specific amounts to players' wallets.
- `/wallet set [nickname] [amount]`: Allows administrators to set precise account balances for players.
- `/wallet remove [nickname] [amount]`: Allows administrators to remove specific amounts from players' wallets.
- `/wallet check [nickname]`: Enables administrators to check the account balance of specific players.
- `/wallet help`: Provides information about available plugin functions.

## Permissions

- `CustomWallet.*`: Grants access to all CustomWallet commands.
- `CustomWallet.add`: Allows adding funds to players' wallets.
- `CustomWallet.set`: Enables setting specific account balances for players.
- `CustomWallet.remove`: Allows removing funds from players' wallets.
- `CustomWallet.check`: Enables checking account balances of players.
- `CustomWallet.reload`: Allows reloading the plugin configuration.

## Advanced Value Handling

CustomWallet supports precise value amounts, including decimal values such as 2.50. This allows for accurate financial management on your server.

## Advanced Features

- **Comprehensive Command System**: CustomWallet offers an extensive set of commands, allowing you to comprehensively manage player wallets and configure the plugin directly in the game.

## Links
* [SpigotMC](https://www.spigotmc.org/resources/cw-customwallet-advanced-economic-system.112339/)
* [bStats](https://bstats.org/plugin/bukkit/CustomWallet/19669)
