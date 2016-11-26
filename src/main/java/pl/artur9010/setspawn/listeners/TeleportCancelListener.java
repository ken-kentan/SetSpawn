package pl.artur9010.setspawn.listeners;

/**
 * Created by artur on 26.07.15.
 */

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;
import pl.artur9010.setspawn.SetSpawnPlugin;

//TODO: "little" rewrite ;)

public class TeleportCancelListener implements Listener {
    SetSpawnPlugin plugin;
    public HashMap<Player, BukkitTask> playerTeleportLocation = new HashMap<>();

    public TeleportCancelListener(SetSpawnPlugin plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if ((from.getBlockX() != to.getBlockX()) || (from.getBlockY() != to.getBlockY()) || (from.getBlockZ() != to.getBlockZ()) || (from.getWorld() != to.getWorld())) {
            Player player = event.getPlayer();
            if (this.playerTeleportLocation.get(player) != null) {
                (this.playerTeleportLocation.remove(player)).cancel();
                player.sendMessage(plugin.getMessage("messages.canceled"));
            }


        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageEvent event) {
        if ((event.getEntity() instanceof Player)) {
            Player player = (Player) event.getEntity();
            if (this.playerTeleportLocation.get(player) != null) {
                (this.playerTeleportLocation.remove(player)).cancel();
                player.sendMessage(plugin.getMessage("messages.canceled"));
            }

        }
    }
}
