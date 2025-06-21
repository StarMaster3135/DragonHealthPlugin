# DragonHealthPlugin

A simple and lightweight Minecraft plugin for Paper servers that allows you to customize the Ender Dragon's health, making boss fights more challenging and exciting!

## Features

- 🐉 **Customizable Dragon Health** - Set the Ender Dragon's health to any value you want
- ⚡ **Lightweight** - Minimal performance impact on your server
- 🔄 **Auto-Apply** - Works with both natural spawns and respawned dragons
- 🛠️ **Easy Configuration** - Simple config file with reload command
- 🔧 **Compatible** - Works with Paper 1.21+ servers
- 📝 **Permission-Based** - Secure admin commands

## Installation

1. Download the latest `.jar` file from the releases
2. Place it in your server's `plugins/` folder
3. Restart your server
4. Configure the dragon health in `plugins/DragonHealthPlugin/config.yml`

## Configuration

```yaml
# Dragon Health Plugin Configuration
# Set the Ender Dragon's health (default Minecraft value is 200)
dragon-health: 600.0
```

## Commands

- `/dragonhealth` - Show current dragon health setting
- `/dragonhealth reload` - Reload the configuration

## Permissions

- `dragonhealth.admin` - Access to all plugin commands (default: op)

## How It Works

The plugin automatically detects when an Ender Dragon spawns (either naturally or when respawned using End Crystals) and applies the configured health value. The dragon will maintain this health until it's defeated or the server restarts.

## Compatibility

- **Server Software**: Paper 1.21+
- **Java Version**: 17+
- **Dependencies**: None

## Support

If you encounter any issues or have suggestions, please create an issue on the project repository.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
