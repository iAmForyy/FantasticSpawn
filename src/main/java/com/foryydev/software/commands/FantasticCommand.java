package com.foryydev.software.commands;

import com.foryydev.software.FantasticSpawn;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class FantasticCommand implements CommandExecutor {

    private FantasticSpawn plugin;

    public FantasticCommand(FantasticSpawn plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;
        FileConfiguration config = plugin.getConfig();
        if (args.length <= 1) {
            player.sendMessage(config.getString("Messages.help"));
            return true;
        }
        if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("ayuda")) {
            player.sendMessage(config.getString("Messages.help"));
        } else if (args[0].equalsIgnoreCase("setspawn")) {
            if (!player.hasPermission("fantasticspawn.command.setspawn")) {
                player.sendMessage(config.getString("Messages.no_permission"));
                return true;
            }
            Location location = player.getLocation().clone();
            config.set("Spawn.x", location.getX()); config.set("Spawn.y", location.getY());
            config.set("Spawn.z", location.getZ());
            if (location.getWorld() != null) {
                config.set("Spawn.world", location.getWorld().getName());
            }
            config.set("Spawn.yaw", location.getYaw()); config.set("Spawn.pitch", location.getPitch());
            plugin.saveConfig();
            plugin.reloadConfig();
            player.sendMessage((config.getString("Messages.spawn_set")));
            return true;
        } else if (args[0].equalsIgnoreCase("spawn")) {
            if (config.isSet("Spawn.x")) {
                if (!player.hasPermission("fantasticspawn.command.spawn")) {
                    player.sendMessage(config.getString("Messages_no-permission"));
                    return true;
                }
                final Location location = new Location(Bukkit.getWorld(config.getString("Spawn.world")),
                        config.getDouble("Spawn.x"),
                        config.getDouble("Spawn.y"),
                        config.getDouble("Spawn.z"),
                        (float) config.getDouble("Spawn.yaw"),
                        (float) config.getDouble("Spawn.pitch"));
                player.teleport(location);
                player.playSound(player.getLocation(), Sound.valueOf(config.getString("Sounds.spawn_teleport").toUpperCase()), 1F, 1F);
                player.sendMessage((config.getString("Messages.spawn_teleport")));
                return true;
            }
        } else {
            player.sendMessage(config.getString("Messages.help"));
        }
        return true;
    }
}
