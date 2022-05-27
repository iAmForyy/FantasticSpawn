package com.foryydev.software.listeners;

import com.foryydev.software.FantasticSpawn;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    public FantasticSpawn plugin;

    public PlayerMoveListener(FantasticSpawn plugin) {
        this.plugin = plugin;
    }

    FileConfiguration config = plugin.getConfig();
    @EventHandler
    public void onMovement(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Location l = p.getLocation();
        if(l.getBlockY() <= 50) {
            teleportSpawn(p);
        }
    }

    public void teleportSpawn(Player player) {
        if(config.isSet("Spawn.x")) {
            final Location l = new Location(Bukkit.getWorld(config.getString("Spawn.world")),
                    config.getDouble("Spawn.x"),
                    config.getDouble("Spawn.y"),
                    config.getDouble("Spawn.z"),
                    (float) config.getDouble("Spawn.yaw"),
                    (float) config.getDouble("Spawn.pitch"));
            player.teleport(l);
            player.sendMessage((config.getString("Messages.spawn_teleport")));
        }else {
            player.sendMessage((config.getString("Messages.spawn_not_set")));
        }

    }
}
