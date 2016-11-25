package pl.artur9010.setspawn.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.artur9010.setspawn.SetSpawnPlugin;

/**
 * Created by artur on 27.07.15.
 */
public class PlayerJoinListener implements Listener {
    SetSpawnPlugin plugin;

    public PlayerJoinListener(SetSpawnPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player p = event.getPlayer();
        if(p != null){
            if(plugin.cm.getConfig("config").getBoolean("teleport.everyjoin")){
                Location spawn = plugin.getSpawnLocation("default");
                if(spawn == null){
                    p.sendMessage(ChatColor.RED + "SetSpawn Error:");
                    p.sendMessage("SPAWN IS NOT SET!!!");
                    p.sendMessage("If you are Administrator, you can set spawn using /setspawn.");
                }else{
                    p.teleport(spawn);
                }
            }else{
                if(!p.hasPlayedBefore()){
                    Location spawn = plugin.getSpawnLocation("default");
                    if(spawn == null){
                        p.sendMessage(ChatColor.RED + "SetSpawn Error:");
                        p.sendMessage("SPAWN IS NOT SET!!!");
                        p.sendMessage("If you are Administrator, you can set spawn using /setspawn.");
                    }else{
                        p.teleport(spawn);
                    }
                }
            }
        }
    }
}
