# XRayAlerts

**XRayAlerts** is a plugin for Paper/Spigot servers that can alert staff members (or any player with the appropriate permission) whenever a player finds specific ores or items. It supports customizable alerts, configurable monitoring modes, and permission-based access control. The plugin is compatible with both Paper and Spigot servers running Minecraft 1.21+.

---

## Features

- **Configurable Alerts**: Customize the messages sent to staff or to the logs when a player finds a 
  monitored block. Messages support color and formatting codes.
- **Vein Detection Mode**: Choose between two modes:
    - **Block Mode**: Sends an alert for each block mined, indicating the number of items dropped.
    - **Vein Mode**: Sends one alert for the entire vein when the first block in the vein is mined.
- **Permission Control**: Permissions to toggle alerts, receive alerts, and ignore alerts.

## Commands

### `/xrayalerts`
Toggles the x-ray alerts on or off for the player who runs the command.\
**Permission**: `xrayalerts.toggle`\
**Usage**: `/xrayalerts`

## Permissions

- **`xrayalerts.ignore`**: Prevents x-ray alerts from being triggered by the player.
- **`xrayalerts.receive`**: Allows the player to receive x-ray alerts.
- **`xrayalerts.toggle`**: Allows the player to toggle x-ray alerts on or off.

## Configuration

### `config.yml`
```yaml
alert-message: "&c&lX-Ray&r &7%player% found &6%count% %item% at (%blockX%, %blockY%, %blockZ%)."
alert-log: "%player% found %count% %item% at (%blockX%, %blockY%, %blockZ%)."

mode: "BLOCK" # Options: "BLOCK", "VEIN".

# Log X-ray alert notifications.
log: true

monitored-blocks:
  - ANCIENT_DEBRIS
  - DEEPSLATE_DIAMOND_ORE
  - DEEPSLATE_EMERALD_ORE
  - DEEPSLATE_GOLD_ORE
  - DIAMOND_ORE
  - EMERALD_ORE
  - GOLD_ORE
```

---
## License
Copyright © 2025 [Tyler Richards](https://github.com/tjrgg). [MIT License](LICENSE).
