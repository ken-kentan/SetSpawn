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
        Player player = event.getPlayer();
        if(player != null){
            if(plugin.cm.getConfig("config").getBoolean("teleport.everyjoin")){
                Location spawn = plugin.getSpawnLocation("default");
                if(spawn != null){
                    player.teleport(spawn);
                }
            }else{
                if(!player.hasPlayedBefore()){
                    Location spawn = plugin.getSpawnLocation("default");
                    if(spawn == null){
                        player.teleport(spawn);
                    }
                }
            }
        }
    }
}
