package com.foryydev.software.listeners;

import com.foryydev.software.FantasticSpawn;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.List;

public class PlayerJoinListener implements Listener {

    private FantasticSpawn plugin;

    public PlayerJoinListener(FantasticSpawn plugin) {
        this.plugin = plugin;
    }

    public static int Join;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        FileConfiguration config = plugin.getConfig();
        final Player p = e.getPlayer();
        e.setJoinMessage(null);

        String titlew = (config.getString("Welcome.title_welcome").replaceAll("%player%", e.getPlayer().getName()));
        String subtitlew = (config.getString("Welcome.subtitle_welcome").replaceAll("%player%", e.getPlayer().getName()));
        if (config.isSet("Spawn.x")) {
            final Location l = new Location(Bukkit.getWorld(config.getString("Spawn.world")),
                    config.getDouble("Spawn.x"),
                    config.getDouble("Spawn.y"),
                    config.getDouble("Spawn.z"),
                    (float) config.getDouble("Spawn.yaw"),
                    (float) config.getDouble("Spawn.pitch"));
            p.teleport(l);
            if (config.getString("Options.msg_tp_onjoin").equals("true")) {
                p.sendMessage((config.getString("Messages.spawn_teleport")));
            }
        } else {
            p.sendMessage((config.getString("Messages.spawn_not_set")));
            return;
        }
        if (p.hasPermission("fantasticspawn.welcome.rank")) {
            List<String> rank = config.getStringList("Welcome.rank");
            launchFirework(p);
            for (String rankmsg : rank) {
                for (Player online : Bukkit.getOnlinePlayers()) {
                    online.sendMessage((PlaceholderAPI.setPlaceholders(p, rankmsg).replaceAll("%player%", e.getPlayer().getName())));
                }
            }
        } else {
            if (config.getString("Welcome.default_message").equals("true")) {
                p.sendMessage((PlaceholderAPI.setPlaceholders(p, config.getString("Welcome.default").replaceAll("%player%", e.getPlayer().getName()))));
            }
            return;
        }
        if (config.getString("Options.title_welcome").equals("true")) {
            p.sendTitle((titlew), (subtitlew), 10, 20, 10);
        }
        if (!p.hasPlayedBefore()) {
            Join++;
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.sendMessage((config.getString("Welcome.first_join").replaceAll("%player%", p.getName()).replaceAll("%number%", "" + Join)));
            }
        }

    }

    public void launchFirework(Player p) {
        FileConfiguration config = plugin.getConfig();
        Firework f = p.getWorld().spawn(p.getLocation(), Firework.class);
        FireworkMeta fm = f.getFireworkMeta();
        fm.addEffect(FireworkEffect.builder()
                .flicker(true)
                .trail(true)
                .with(FireworkEffect.Type.BALL_LARGE)
                .withColor(Color.WHITE, Color.AQUA, Color.LIME).build());
        fm.setPower(config.getInt("Firework.power"));
        f.setFireworkMeta(fm);
    }
}
