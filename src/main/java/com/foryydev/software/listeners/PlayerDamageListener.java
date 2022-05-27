package com.foryydev.software.listeners;

import com.foryydev.software.FantasticSpawn;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener implements Listener {

    private FantasticSpawn plugin;

    public PlayerDamageListener(FantasticSpawn plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        Entity p = e.getEntity();
        EntityDamageEvent.DamageCause causeOfDamage = e.getCause();
        if (!(p instanceof Player)) return;
        if (causeOfDamage.equals(EntityDamageEvent.DamageCause.FALL)) {
            e.setCancelled(true);
        }
    }
}
