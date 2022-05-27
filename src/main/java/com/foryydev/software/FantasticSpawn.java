package com.foryydev.software;

import com.foryydev.software.commands.FantasticCommand;
import com.foryydev.software.listeners.PlayerDamageListener;
import com.foryydev.software.listeners.PlayerJoinListener;
import com.foryydev.software.listeners.PlayerMoveListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class FantasticSpawn extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getConfig().options().copyDefaults(true); //Copy default config
        this.saveDefaultConfig(); //Save default configuration
        getLogger().info("FantasticSpawn by iForyyDev version" + Bukkit.getVersion());
        commandHandler(); //register commands to the plugin
        eventHandler(); //register events to the plugin
    }

    @Override
    public void onDisable() {
        getLogger().info("FantasticSpawn has been disabled successfully");
    }

    public void commandHandler() {
        getCommand("fantasticspawn").setExecutor(new FantasticCommand(this));
    }

    public void eventHandler() {
        Arrays.asList(
                new PlayerDamageListener(this),
                new PlayerJoinListener(this),
                new PlayerMoveListener(this)
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }
}
