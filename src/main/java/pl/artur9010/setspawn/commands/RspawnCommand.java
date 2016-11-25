package pl.artur9010.setspawn.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.artur9010.setspawn.SetSpawnPlugin;

/**
 * Created by artur9010 on 07.05.16.
 */

//todo: replace this shit with /setspawn reload
public class RspawnCommand implements CommandExecutor {

    SetSpawnPlugin plugin;

    public RspawnCommand(SetSpawnPlugin plugin)
    {
        this.plugin = plugin;
        plugin.getCommand("rspawn").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        sender.sendMessage("[SetSpawn] Configuration reloaded.");
        plugin.cm.save("spawn");
        plugin.cm.loadAll();
        return true;
    }
}
