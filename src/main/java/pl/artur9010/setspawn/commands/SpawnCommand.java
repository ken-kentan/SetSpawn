package pl.artur9010.setspawn.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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
        if (args.length == 0) {
            if (sender instanceof Player) {
                player = (Player) sender;
                spawnLocation = plugin.getSpawnLocation(spawn);
                if (plugin.spawnExists(spawn)) {
                    if (player.hasPermission("setspawn.nowait") || player.isOp())
                        wait = false;
                    else
                        wait = true;
                    plugin.teleport(spawn, player, wait, true);

                } else {
                    player.sendMessage(plugin.getMessage("messages.error.spawndosentexist"));
                    return true;
                }
            } else {
                sender.sendMessage(plugin.getMessage("messages.error.onlyplayer"));
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                //todo: display list of avaiable spawnpoints
                sender.sendMessage("...");
            } else if (!plugin.bannedSpawnpointNames.contains(args[0].toLowerCase())) {
                if (sender instanceof Player) {
                    player = (Player) sender;
                    spawn = args[0];
                    if (plugin.spawnExists(spawn)) {
                        if (player.hasPermission("setspawn.nowait") || player.isOp())
                            wait = false;
                        else
                            wait = true;
                        plugin.teleport(spawn, player, wait, true);
                    } else {
                        player.sendMessage(plugin.getMessage("messages.error.spawndosentexist"));
                        return true;
                    }
                }
            } else {
                sender.sendMessage("unknown action"); //todo: plugin message
            }
        } else if (args.length == 2) {
            if (sender.hasPermission("setspawn.spawn.other")) {
                player = Bukkit.getPlayer(args[1]);
                if (player != null) {
                    if (plugin.spawnExists(args[0])) {
                        spawn = args[0];
                        plugin.teleport(spawn, player, false, false);
                    } else {
                        sender.sendMessage(plugin.getMessage("messages.error.spawndosentexist"));
                    }
                } else {
                    sender.sendMessage("player is offline"); //todo: plugin message
                }
            }
        }
        return true;
    }
}
