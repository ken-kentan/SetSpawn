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

    public ConfigsManager cm;

    SpawnCommand spawnCommand;
    SetSpawnCommand setSpawnCommand;
    RspawnCommand rspawnCommand;

    public void onEnable() {
        System.console().printf("===[ SetSpawn v2.2.2 by artur9010 ]===\n");
        System.console().printf("Thanks for downloading SetSpawn!\n");
        System.console().printf("http://dev.bukkit.org/bukkit-plugins/setspawn\n");
        System.console().printf("=====================================\n");

        plugin = this;
        teleportCancelListener = new TeleportCancelListener(this);
        playerJoinListener = new PlayerJoinListener(this);

        spawnCommand = new SpawnCommand(this);
        setSpawnCommand = new SetSpawnCommand(this);
        rspawnCommand = new RspawnCommand(this);

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

    public Location getSpawnLocation(){
        ConfigsManager.RConfig spawnCfg = cm.getConfig("spawn");
        if(spawnCfg.getString("world") == null)
            return null;
        Location spawn = new Location(null, 0, 0, 0);
        spawn.setX(spawnCfg.getDouble("x"));
        spawn.setY(spawnCfg.getDouble("y"));
        spawn.setZ(spawnCfg.getDouble("z"));
        spawn.setWorld(Bukkit.getWorld(spawnCfg.getString("world")));
        spawn.setYaw(spawnCfg.getInt("yaw"));
        spawn.setPitch(spawnCfg.getInt("pitch"));
        return spawn;
    }

    public void setSpawnLocation(String world, double x, double y, double z, float yaw, float pitch){
        ConfigsManager.RConfig spawnCfg = cm.getConfig("spawn");
        spawnCfg.set("world", world);
        spawnCfg.set("x", x);
        spawnCfg.set("y", y);
        spawnCfg.set("z", z);
        spawnCfg.set("yaw", yaw);
        spawnCfg.set("pitch", pitch);
        try {
            spawnCfg.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
