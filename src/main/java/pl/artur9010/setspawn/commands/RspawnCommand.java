package pl.artur9010.setspawn.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
ale nas zbaw od Pandy.

Enter.

====
#onlydiorite
http://diorite.org/
====
 */

/**
 * Created by artur9010 on 07.05.16.
 */

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
