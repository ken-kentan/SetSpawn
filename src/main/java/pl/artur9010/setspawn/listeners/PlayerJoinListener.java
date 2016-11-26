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

    public PlayerJoinListener(SetSpawnPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (event.getPlayer() != null) {
            if (plugin.cm.getConfig("config").getBoolean("teleport.everyjoin")) {
                plugin.teleport("default", event.getPlayer(), false, false);
            } else {
                if (!event.getPlayer().hasPlayedBefore()) {
                    plugin.teleport("default", event.getPlayer(), false, false);
                }
            }
        }
    }
}
