package pl.artur9010.setspawn;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import pl.artur9010.setspawn.listeners.PlayerJoinListener;
import pl.artur9010.setspawn.listeners.TeleportCancelListener;
import pl.artur9010.setspawn.commands.SpawnCommand;
import pl.artur9010.setspawn.commands.SetSpawnCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by artur on 26.07.15.
 * SetSpawn
 *
 * @author artur9010
 * @url http://artur9010.pl/
 */

public class SetSpawnPlugin extends JavaPlugin {
    protected static SetSpawnPlugin plugin;
    public TeleportCancelListener teleportCancelListener;
    public PlayerJoinListener playerJoinListener;

    public List<String> bannedSpawnpointNames = new ArrayList<>();

    public ConfigsManager cm;

    SpawnCommand spawnCommand;
    SetSpawnCommand setSpawnCommand;

    public void onEnable() {
        plugin = this;
        teleportCancelListener = new TeleportCancelListener(this);
        playerJoinListener = new PlayerJoinListener(this);

        spawnCommand = new SpawnCommand(this);
        setSpawnCommand = new SetSpawnCommand(this);

        bannedSpawnpointNames.add("reload"); //because of /setspawn reload
        bannedSpawnpointNames.add("list"); //because of /spawn list
        bannedSpawnpointNames.add("delete"); //because of /setspawn delete
        bannedSpawnpointNames.add("other"); //because of setspawn.spawn.other

        cm = new ConfigsManager(this);
        cm.registerConfig("config", "config.yml");
        cm.registerConfig("messages", "messages.yml");
        cm.registerConfig("spawn", "spawn.yml");
        cm.loadAll();
        cm.saveAll();
    }

    public void onDisable() {
        cm.saveAll();
        plugin = null;
    }

    public String getMessage(String patch) {
        //return cm.getConfig("messages").getString(patch).replaceAll("&", "" + ChatColor.COLOR_CHAR);
        return ChatColor.translateAlternateColorCodes('&', cm.getConfig("messages").getString(patch));
    }

    public Location getSpawnLocation(String name) {
        ConfigsManager.RConfig spawnCfg = cm.getConfig("spawn");
        if (spawnCfg.getString(name + ".world") == null)
            return null;
        Location spawn = new Location(null, 0, 0, 0);
        spawn.setX(spawnCfg.getDouble(name + ".x"));
        spawn.setY(spawnCfg.getDouble(name + ".y"));
        spawn.setZ(spawnCfg.getDouble(name + ".z"));
        spawn.setWorld(Bukkit.getWorld(spawnCfg.getString(name + ".world")));
        spawn.setYaw(spawnCfg.getInt(name + ".yaw"));
        spawn.setPitch(spawnCfg.getInt(name + ".pitch"));
        return spawn;
    }

    public void setSpawnLocation(String name, String world, double x, double y, double z, float yaw, float pitch) {
        ConfigsManager.RConfig spawnCfg = cm.getConfig("spawn");
        spawnCfg.set(name + ".world", world);
        spawnCfg.set(name + ".x", x);
        spawnCfg.set(name + ".y", y);
        spawnCfg.set(name + ".z", z);
        spawnCfg.set(name + ".yaw", yaw);
        spawnCfg.set(name + ".pitch", pitch);
        try {
            spawnCfg.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void teleport(String spawnpoint, Player player, Boolean wait, Boolean play) {
        //todo: write it from scratch
        /* old copied code */
        Location spawn = plugin.getSpawnLocation(spawnpoint);
        if (wait) {
            if (plugin.cm.getConfig("config").getBoolean("teleport.cooldown_enabled")) {
                player.sendMessage(plugin.getMessage("messages.pleasewait").replaceAll("\\{1\\}", "" + plugin.cm.getConfig("config").getLong("teleport.cooldown")));
                if (plugin.cm.getConfig("config").getBoolean("messages.spawn")) {
                    plugin.teleportPlayerWithDelay(player, plugin.cm.getConfig("config").getLong("teleport.cooldown"), spawn, plugin.getMessage("messages.spawn"), null);
                } else {
                    plugin.teleportPlayerWithDelay(player, plugin.cm.getConfig("config").getLong("teleport.cooldown"), spawn, null, null);
                }
            } else {
                if (plugin.cm.getConfig("config").getBoolean("messages.spawn")) {
                    player.sendMessage(plugin.getMessage("messages.spawn"));
                }
                player.teleport(spawn);
            }
        } else {
            if (plugin.cm.getConfig("config").getBoolean("messages.spawn")) {
                player.sendMessage(plugin.getMessage("messages.spawn"));
            }
            player.teleport(spawn);
        }


        if (play) {
            if (!plugin.cm.getConfig("config").getString("teleport.effect.effect").equalsIgnoreCase("false")) {
                player.playEffect(spawn, Effect.valueOf(plugin.cm.getConfig("config").getString("teleport.effect.effect")), plugin.cm.getConfig("config").getInt("teleport.effect.power"));
            }

            if (!plugin.cm.getConfig("config").getString("teleport.sound.sound").equalsIgnoreCase("false")) {
                player.playSound(spawn, Sound.valueOf(plugin.cm.getConfig("config").getString("teleport.sound.sound")), 1.0F, 0.0F);
            }
        }
    }

    public boolean spawnExists(String spawnpoint) {
        ConfigsManager.RConfig spawnCfg = cm.getConfig("spawn");
        if (spawnCfg.getString(spawnpoint + ".world") == null)
            return false;
        return true;
    }

    //todo: rewrite?
    public static void teleportPlayerWithDelay(final Player player, long l, final Location location, final String messageAfterTp, final Runnable postTeleport) {
        if (plugin.teleportCancelListener.playerTeleportLocation.get(player) != null) {
            plugin.teleportCancelListener.playerTeleportLocation.remove(player);
        }

        BukkitTask task = plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (player.isOnline()) {
                player.teleport(location);
                if (messageAfterTp != null) {
                    player.sendMessage(messageAfterTp);
                }
                if (postTeleport != null) {
                    postTeleport.run();
                }
                if (plugin.teleportCancelListener.playerTeleportLocation.containsKey(player)) {
                    plugin.teleportCancelListener.playerTeleportLocation.remove(player);
                }
            }
        }, l * 20L);
        plugin.teleportCancelListener.playerTeleportLocation.put(player, task);
    }
}
