package com.iamforyydev;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.iamforyydev.commands.Spawn;
import com.iamforyydev.listeners.PlayerEvents;

public class FantasticSpawn extends JavaPlugin{
	

	public void onEnable() {
		this.getConfig().options().copyDefaults(true);
		this.saveDefaultConfig();
		
		this.getCommand("fantasticspawn").setExecutor(new Spawn(this));
		
		PluginManager pluginManager = this.getServer().getPluginManager();
		pluginManager.registerEvents(new PlayerEvents(this), this);
		
		getServer().getLogger().info("");
		getServer().getLogger().info("&aThanks for used my plugin :)");
		getServer().getLogger().info("&aAuthor: iAmForyyDev_");
		getServer().getLogger().info("&aVersion: "+Bukkit.getVersion());
		getServer().getLogger().info("");
	}
	public String c(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}
}
