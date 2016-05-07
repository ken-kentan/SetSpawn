package pl.artur9010.setspawn.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.artur9010.setspawn.SetSpawnPlugin;

/**
 * Created by artur9010 on 07.05.16.
 */

//todo: rewrite...
public class SetSpawnCommand implements CommandExecutor {

    SetSpawnPlugin plugin;

    public SetSpawnCommand(SetSpawnPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("setspawn").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("This command can be executed only by player.");
            return true;
        }

        Player p = (Player) sender;
        p.sendMessage(plugin.getMessage("messages.spawnset"));
        Location l = p.getLocation();
        plugin.setSpawnLocation(l.getWorld().getName(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
        return true;
    }
}
