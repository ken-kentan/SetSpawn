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

    //todo: multispawns support
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) { //Reload
            //todo: permissions
            if (!(sender instanceof Player) || sender.hasPermission("setspawn.reload") || sender.isOp()) {
                sender.sendMessage("[SetSpawn] Configuration reloaded.");
                plugin.cm.save("spawn");
                plugin.cm.loadAll();
                return true;
            }
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command can be executed only by player.");
                return true;
            }

            Player p = (Player) sender;
            Location l = p.getLocation();
            String spawnName;
            if(args.length == 1){
                spawnName = args[0].toLowerCase();
            }else{
                spawnName = "default";
            }
            plugin.setSpawnLocation(spawnName, l.getWorld().getName(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
            p.sendMessage(plugin.getMessage("messages.spawnset"));
            return true;
        }

        return true;
    }
}
