package pl.artur9010.setspawn.commands;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.artur9010.setspawn.SetSpawnPlugin;

/**
 * Created by artur9010 on 07.05.16.
 */

//todo: REWRITE THIS SHIT
public class SpawnCommand implements CommandExecutor {

    SetSpawnPlugin plugin;

    public SpawnCommand(SetSpawnPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("spawn").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player; //player to be teleported
        Location spawnLocation; //location of spawn point
        String spawn = "default";
        boolean wait;
        if(args.length == 0){
            if(sender instanceof Player){
                player = (Player) sender;
                spawnLocation = plugin.getSpawnLocation(spawn);
                if(spawnLocation == null){
                    player.sendMessage(plugin.getMessage("messages.error.spawndosentexist"));
                    return true;
                }

                if(player.hasPermission("setspawn.nowait") || player.isOp())
                    wait = false;
                else
                    wait = true;
                plugin.teleport(spawn, player, wait, true);
            }else{
                sender.sendMessage(plugin.getMessage("messages.error.onlyplayer"));
            }
        }else if(args.length == 1){
            if(args[0].equalsIgnoreCase("list")){
                //todo: display list of avaiable spawnpoints
                sender.sendMessage("...");
            }else if(!plugin.bannedSpawnpointNames.contains(args[0].toLowerCase())){
                if(sender instanceof Player){
                    player = (Player) sender;

                }
            }else{
                sender.sendMessage("unknown action"); //todo: plugin message
            }
        }else if(args.length == 2){
            //spawn spawnpoint player
            //todo: teleport other player to diffrent spawnpoint
        }
        //Some cancer code!
        /*
        Player p = (Player) sender;
        Location spawn = plugin.getSpawnLocation("default");
        if (spawn == null) {
            p.sendMessage("Please set spawn point using /setspawn. Thanks :)");
            return true;
        }
        if (plugin.cm.getConfig("config").getBoolean("teleport.cooldown_enabled")) {
            p.sendMessage(plugin.getMessage("messages.pleasewait").replaceAll("\\{1\\}", "" + plugin.cm.getConfig("config").getLong("teleport.cooldown")));
            if (plugin.cm.getConfig("config").getBoolean("messages.spawn")) {
                plugin.teleportPlayerWithDelay(p, plugin.cm.getConfig("config").getLong("teleport.cooldown"), spawn, plugin.getMessage("messages.spawn"), null);
            } else {
                plugin.teleportPlayerWithDelay(p, plugin.cm.getConfig("config").getLong("teleport.cooldown"), spawn, null, null);
            }
        } else {
            if (plugin.cm.getConfig("config").getBoolean("messages.spawn")) {
                p.sendMessage(plugin.getMessage("messages.spawn"));
            }
            p.teleport(spawn);
        }
        Location loc = p.getLocation();

        if (plugin.cm.getConfig("config").getBoolean("teleport.effect.enabled")) {
            p.playEffect(loc, Effect.valueOf(plugin.cm.getConfig("config").getString("teleport.effect.effect")), plugin.cm.getConfig("config").getInt("teleport.effect.power"));
        }

        if (plugin.cm.getConfig("config").getBoolean("teleport.sound.enabled")) {
            p.playSound(loc, Sound.valueOf(plugin.cm.getConfig("config").getString("teleport.sound.sound")), 1.0F, 0.0F);
        }
        return true;*/

        return true; //todo: remove
    }
}
