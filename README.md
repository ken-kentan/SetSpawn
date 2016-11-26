# SetSpawn 3.0 [indev]
## Commands
| Command | Description | Permission  |
| ------------- |:-------------:| -----:|
| `/spawn` | Teleport yourself to default spawnpoint. | `setspawn.spawn` |
| `---` | Don't wait for teleport :) | `setspawn.nowait` |
| `/spawn [spawnpoint]` | Teleport yourself to spawnpoint named *[spawnpoint]*. | `setspawn.spawn.[spawnpoint]` |
| `/spawn [spawnpoint] [player]` | Teleport other player to diffrent spawnpoint. Use "default" to teleprort to default spawnpoint. | `setspawn.spawn.other` |
| `/spawn list` | List of spawnpoints. | ... |
| `/setspawn` | Set default spawnpoint. | ... |
| `/setspawn [spawnpoint]` | Set spawnpoint named *[spawnpoint]* | ... |
| `/setspawn reload` | Reload SetSpawn plugin configuration. | ... |
| `/setspawn delete [spawnpoint]` | Delete spawnpoint named *[spawnpoint]* | ... |

## Configuration
```yaml
#SetSpawn by artur9010

# Sound list: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Sound.html
# Effect list: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Effect.html
# SOUND/EFFECT - USE "false" TO DISABLE

teleport:
    cooldown: 5
    cooldown_enabled: false
    everyjoin: false
    sound:
      sound: ENTITY_PLAYER_LEVELUP
    effect:
      effect: SMOKE
      power: 6
messages:
    spawn: true
```

## Messages
```yaml
messages:
    spawn: "&7Teleported to spawn"
    spawnset: "&7Spawn set!"
    pleasewait: "&7Please wait &9{1} &7seconds."
    canceled: "&cTeleportation canceled."
    error:
      onlyplayer: "This command can be executed only by player."
      onlyconsole: "This command can be executed only from server console."
```

## Contributing
//todo: write something...