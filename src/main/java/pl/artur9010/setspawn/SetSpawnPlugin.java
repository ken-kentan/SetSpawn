package pl.artur9010.setspawn;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import pl.artur9010.setspawn.listeners.PlayerJoinListener;
import pl.artur9010.setspawn.listeners.TeleportCancelListener;
import pl.artur9010.setspawn.commands.SpawnCommand;
import pl.artur9010.setspawn.commands.SetSpawnCommand;
import pl.artur9010.setspawn.commands.RspawnCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by artur on 26.07.15.
 * SetSpawn
 * @author artur9010
 * @url http://artur9010.pl/
 */

public class SetSpawnPlugin extends JavaPlugin{
    protected static SetSpawnPlugin plugin;
    public TeleportCancelListener teleportCancelListener;
    public PlayerJoinListener playerJoinListener;

    public List<String> bannedSpawnpointNames = new ArrayList<>();

    public ConfigsManager cm;

    SpawnCommand spawnCommand;
    SetSpawnCommand setSpawnCommand;
    //RspawnCommand rspawnCommand; //replaced with /setspawn reload

    public void onEnable() {
        plugin = this;
        teleportCancelListener = new TeleportCancelListener(this);
        playerJoinListener = new PlayerJoinListener(this);

        spawnCommand = new SpawnCommand(this);
        setSpawnCommand = new SetSpawnCommand(this);
        //rspawnCommand = new RspawnCommand(this);

        bannedSpawnpointNames.add("reload"); //because of /setspawn reload
        bannedSpawnpointNames.add("list"); //because of /spawn list
        bannedSpawnpointNames.add("delete"); //because of /setspawn delete

        cm = new ConfigsManager(this);
        cm.registerConfig("config", "config.yml");
        cm.registerConfig("messages", "messages.yml");
        cm.registerConfig("spawn", "spawn.yml");
        cm.loadAll();
        cm.saveAll();
    }

    public void onDisable(){
        cm.saveAll();
        plugin = null;
    }

    public String getMessage(String patch){
        return cm.getConfig("messages").getString(patch).replaceAll("&", "" + ChatColor.COLOR_CHAR);
    }

    public Location getSpawnLocation(String name){
        ConfigsManager.RConfig spawnCfg = cm.getConfig("spawn");
        if(spawnCfg.getString(name + ".world") == null)
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

    public void setSpawnLocation(String name, String world, double x, double y, double z, float yaw, float pitch){
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

    //todo: rewrite?
    public static void teleportPlayerWithDelay(final Player player, long l, final Location location, final String messageAfterTp, final Runnable postTeleport){
        if (plugin.teleportCancelListener.playerTeleportLocation.get(player) != null) {
            plugin.teleportCancelListener.playerTeleportLocation.remove(player);
        }

        BukkitTask task = plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (player.isOnline()){
                player.teleport(location);
                if(messageAfterTp != null){
                    player.sendMessage(messageAfterTp);
                }
                if(postTeleport != null){
                    postTeleport.run();
                }
                if(plugin.teleportCancelListener.playerTeleportLocation.containsKey(player)){
                    plugin.teleportCancelListener.playerTeleportLocation.remove(player);
                }
            }
        }, l * 20L);
        plugin.teleportCancelListener.playerTeleportLocation.put(player, task);
    }
}
