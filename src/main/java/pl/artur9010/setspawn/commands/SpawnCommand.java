package pl.artur9010.setspawn.commands;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
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

//todo: REWRITE THIS SHIT
public class SpawnCommand implements CommandExecutor {

    SetSpawnPlugin plugin;

    public SpawnCommand(SetSpawnPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("spawn").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can be executed only by player.");
            return true;
        }

        Player p = (Player) sender;
        Location spawn = plugin.getSpawnLocation();
        if (spawn == null) {
            p.sendMessage("[Error] Spawn is not set.");
            p.sendMessage("If you are Administrator, you can set spawn using /setspawn.");
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
        return true;
    }
}
