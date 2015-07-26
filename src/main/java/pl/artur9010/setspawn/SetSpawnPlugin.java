package pl.artur9010.setspawn;

/**
 * Created by artur on 26.07.15.
 */
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import pl.artur9010.setspawn.listeners.TeleportCancelListener;

public class SetSpawnPlugin extends JavaPlugin{
    protected static SetSpawnPlugin plugin;
    public TeleportCancelListener teleportCancelListener;

    private boolean disablePermissions = false;
    private Long cooldown_time = 5L;
    private boolean cooldown_enabled = false;
    private String message_spawn = "Teleported to spawn.";
    private String message_spawn_set = "Spawn set!";
    private String message_please_wait = "Please wait!";
    private String message_teleportation_canceled = "Teleportation canceled.";
    private String message_permissions = "No permissions.";
    private boolean effect_mobspawner = false;
    private boolean sound_level = false;
    private boolean sound_portal = false;
    private boolean sound_enderdragon = false;

    public void onEnable() {
        System.console().printf("===[ SetSpawn v2.0 by artur9010 ]===\n");
        System.console().printf("Thanks for downloading SetSpawn!\n");
        System.console().printf("http://dev.bukkit.org/bukkit-plugins/setspawn\n");
        System.console().printf("=====================================\n");

        saveDefaultConfig();
        this.teleportCancelListener = new TeleportCancelListener(this);
        plugin = this;

        loadConfig();
    }

    public void onDisable(){
        plugin = null;
    }

    public void loadConfig(){
        /* Permissions */
        disablePermissions = getConfig().getBoolean("permissions.disable");

        /* Cooldown */
        cooldown_time = getConfig().getLong("teleport.cooldown");
        cooldown_enabled = getConfig().getBoolean("teleport.cooldown_enabled");

        /* Messages */
        message_spawn = getConfig().getString("messages.spawn").replaceAll("&", "" + ChatColor.COLOR_CHAR);
        message_spawn_set = getConfig().getString("messages.spawnset").replaceAll("&", "" + ChatColor.COLOR_CHAR);
        message_please_wait = getConfig().getString("messages.pleasewait").replaceAll("&", "" + ChatColor.COLOR_CHAR).replaceAll("\\{1\\}", "" + cooldown_time);
        message_teleportation_canceled = getConfig().getString("messages.canceled").replaceAll("&", "" + ChatColor.COLOR_CHAR);
        message_permissions = getConfig().getString("messages.permissions").replaceAll("&", "" + ChatColor.COLOR_CHAR);

        /* Effects */
        effect_mobspawner = getConfig().getBoolean("effects.mobspawner");

        /* Sounds */
        sound_level = getConfig().getBoolean("sound.level");
        sound_portal = getConfig().getBoolean("sound.portal");
        sound_enderdragon = getConfig().getBoolean("sound.enderdragon");
    }

    public static void teleportPlayerWithDelay(final Player player, long l, final Location location, final String messageAfterTp, final Runnable postTeleport){
        if (plugin.teleportCancelListener.playerTeleportLocation.get(player) != null) {
            plugin.teleportCancelListener.playerTeleportLocation.remove(player);
        }

        BukkitTask task = plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){
            public void run(){
                if (player.isOnline()){
                    player.teleport(location);
                    if (messageAfterTp != null){
                        player.sendMessage(messageAfterTp);
                    }
                    if (postTeleport != null){
                        postTeleport.run();
                    }
                }
            }
        }, l * 20L);
        plugin.teleportCancelListener.playerTeleportLocation.put(player, task);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String Label, String[] args){
        if(cmd.getName().equalsIgnoreCase("setspawn")) {
            if (sender instanceof Player){
                Player p = (Player)sender;
                if(disablePermissions || p.hasPermission("setspawn.setspawn")){
                    p.sendMessage(message_spawn_set);
                    Location l = p.getLocation();
                    int x = l.getBlockX();
                    int y = l.getBlockY();
                    int z = l.getBlockZ();
                    p.getWorld().setSpawnLocation(x, y, z);
                }else{
                    p.sendMessage(message_permissions);
                }
            }else{
                sender.sendMessage("[SetSpawn] You must be player to execute this command.");
            }
        }else if(cmd.getName().equalsIgnoreCase("spawn")){
            if(sender instanceof Player){
                Player p = (Player)sender;
                if(disablePermissions || p.hasPermission("setspawn.spawn")){
                    World w = Bukkit.getWorlds().get(0);
                    if(cooldown_enabled){
                        p.sendMessage(message_please_wait);
                        teleportPlayerWithDelay(p, cooldown_time, w.getSpawnLocation(), message_spawn, null);
                    }else{
                        p.sendMessage(message_spawn);
                        p.teleport(w.getSpawnLocation());
                    }
                    Location loc = p.getLocation();
                    if (effect_mobspawner){
                        p.playEffect(loc, Effect.MOBSPAWNER_FLAMES, 6); //TODO: depreacted -,-
                    }
                    if (sound_level){
                        p.playSound(loc, Sound.LEVEL_UP, 1.0F, 0.0F);
                    }
                    if (sound_portal){
                        p.playSound(loc, Sound.PORTAL_TRAVEL, 1.0F, 0.0F);
                    }
                    if (sound_enderdragon){
                        p.playSound(loc, Sound.ENDERDRAGON_DEATH, 1.0F, 0.0F);
                    }
                }else{
                    p.sendMessage(message_permissions);
                }
            }else{
                sender.sendMessage("[SetSpawn] You must be player to execute this command.");
            }
        }else if(cmd.getName().equalsIgnoreCase("rspawn")) {
            if(sender instanceof Player){
                if(disablePermissions || sender.hasPermission("setspawn.rspawn")){
                    Player p = (Player)sender;
                    if(disablePermissions){
                        if(!p.isOp()){
                            p.sendMessage("You must be op to execute /rspawn command.");
                            return true;
                        }
                    }
                    p.sendMessage("[SetSpawn] Configuration reloaded.");
                    loadConfig();
                }else{
                    sender.sendMessage(message_permissions);
                }
            }else{
                sender.sendMessage("[SetSpawn] Configuration reloaded.");
                loadConfig();
            }
        }
        return true;
    }
}