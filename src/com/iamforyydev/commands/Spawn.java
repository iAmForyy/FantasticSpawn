package com.iamforyydev.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.iamforyydev.FantasticSpawn;

public class Spawn implements CommandExecutor{
	
	private FantasticSpawn plugin;
	public Spawn(FantasticSpawn plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) return false;
		Player player = (Player) sender;
		FileConfiguration config = plugin.getConfig();
		if(args.length <= 1) {
			sendHelpMessage(player, config);
			return true;
		}
		if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("ayuda")) {
			sendHelpMessage(player, config);
		}else if(args[0].equalsIgnoreCase("setspawn")) {
			if(!player.hasPermission("fantasticspawn.command.setspawn")) {
				noPermission(player, config);
				return true;
			}
			Location location = player.getLocation().clone();
			config.set("Spawn.x", location.getX());
			config.set("Spawn.y", location.getY());
			config.set("Spawn.z", location.getZ());
			config.set("Spawn.world", location.getWorld().getName());
			config.set("Spawn.yaw", location.getYaw());
			config.set("Spawn.pitch", location.getPitch());
			plugin.saveConfig();
			plugin.reloadConfig();
			player.sendMessage(c(config.getString("Messages.spawn_set")));
			return true;
		}else if(args[0].equalsIgnoreCase("spawn")) {
			if(config.isSet("Spawn.x")) {
				if(!player.hasPermission("fantasticspawn.command.spawn")) {
					noPermission(player, config);
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
				player.sendMessage(c(config.getString("Messages.spawn_teleport")));
				return true;
			}
		}else {
			sendHelpMessage(player, config);
		}
		return true;
	}
	public void sendHelpMessage(Player player, FileConfiguration config) {
		List<String> helpMessage = config.getStringList("Messages.help");
		for(String s : helpMessage) {
			player.sendMessage(s);
		}
	}
	public void noPermission(Player player, FileConfiguration config) {
		player.sendMessage(c(config.getString("Messages.no_permission")));
		player.playSound(player.getLocation(), Sound.valueOf(config.getString("Sounds.no_permission").toUpperCase()), 10F, 100F);
	}
	public String c(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}
}
