package pl.artur9010.setspawn.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.artur9010.setspawn.SetSpawnPlugin;

/*
Dioricie nasz,
któryś jest w javie:
święć się bugi Twoje,
przyjdź leaki Twoje,
bądź Twój kod jako na gicie,
tak i na dysku.
Repo naszego powszedniego
daj nam dzisiaj.
I odpuść nam nasze kretynizmy,
jako i my odpuszczamy naszym collobatorom.
I nie wódź nas na memory leaki,
ale nas zbaw od JavaScriptu.

Enter.

====
#onlydiorite
http://diorite.org/
====
 */

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
