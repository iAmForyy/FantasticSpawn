package com.iamforyydev.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import com.iamforyydev.FantasticSpawn;

import me.clip.placeholderapi.PlaceholderAPI;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

public class PlayerEvents implements Listener{
	
	private FantasticSpawn plugin;
	public PlayerEvents(FantasticSpawn plugin) {
		this.plugin = plugin;
	}
	
	public static int Join;
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		FileConfiguration config = plugin.getConfig();
		final Player p = e.getPlayer();
		e.setJoinMessage(null);
		
		String titlew = c(config.getString("Welcome.title_welcome").replaceAll("%player%", e.getPlayer().getName()));
		String subtitlew = c(config.getString("Welcome.subtitle_welcome").replaceAll("%player%", e.getPlayer().getName()));
		if(config.isSet("Spawn.x")) {
			final Location l = new Location(Bukkit.getWorld(config.getString("Spawn.world")),
					config.getDouble("Spawn.x"),
					config.getDouble("Spawn.y"),
					config.getDouble("Spawn.z"),
					(float) config.getDouble("Spawn.yaw"),
					(float) config.getDouble("Spawn.pitch"));
			p.teleport(l);
			if(config.getString("Options.msg_tp_onjoin").equals("true")) {
				p.sendMessage(c(config.getString("Messages.spawn_teleport")));
			}
		}else {
			p.sendMessage(c(config.getString("Messages.spawn_not_set")));
			return;
		}
		if(p.hasPermission("fantasticspawn.welcome.rank")) {
			List<String> rank = config.getStringList("Welcome.rank");
			launchFirework(p);
			for(int i=0;i<rank.size();i++) {
				String rankmsg = rank.get(i);
				for(Player online : Bukkit.getOnlinePlayers()) {
					online.sendMessage(c(PlaceholderAPI.setPlaceholders(p, rankmsg).replaceAll("%player%", e.getPlayer().getName())));
				}
			}
		}else {
			if(config.getString("Welcome.default_message").equals("true")) {
				p.sendMessage(c(PlaceholderAPI.setPlaceholders(p, config.getString("Welcome.default").replaceAll("%player%", e.getPlayer().getName()))));
			}
			return;
		}
		if(config.getString("Options.title_welcome").equals("true")) {
			PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a("{\"text\":\""+titlew+"\"}"), 20, 60, 20);
			PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, ChatSerializer.a("{\"text\":\""+subtitlew+"\"}"), 20, 60, 20);
			((CraftPlayer) p.getPlayer()).getHandle().playerConnection.sendPacket(title);
			((CraftPlayer) p.getPlayer()).getHandle().playerConnection.sendPacket(subtitle);
		}
		if(!p.hasPlayedBefore()) {
			Join++;
			for(Player online : Bukkit.getOnlinePlayers()) {
				online.sendMessage(c(config.getString("Welcome.first_join").replaceAll("%player%", p.getName()).replaceAll("%number%", ""+Join)));
			}
		}
			
	}
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		FileConfiguration config = plugin.getConfig();
		e.setQuitMessage(null);
		
		if(p.hasPermission("fantasticspawn.goodbye.rank")) {
			List<String> bye = config.getStringList("Goodbye.rank");
			for(int i=0;i<bye.size();i++) {
				String byem = bye.get(i);
				for(Player online : Bukkit.getOnlinePlayers()) {
					online.sendMessage(c(PlaceholderAPI.setPlaceholders(p, byem).replaceAll("%player%", e.getPlayer().getName())));
				}
			}
		}else {
			if(config.getString("Goodbye.default_message").equals("true")) {
				for(Player online : Bukkit.getOnlinePlayers()) {
					online.sendMessage(c(PlaceholderAPI.setPlaceholders(p, config.getString("Goodbye.default").replaceAll("%player%", e.getPlayer().getName()))));
				}
			}
			return;
		}
	}
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		Entity p = e.getEntity();
		DamageCause dano = e.getCause();
		if(!(p instanceof Player)) return;
		if(dano.equals(EntityDamageEvent.DamageCause.FALL)) {
			e.setCancelled(true);
		}
			
	}
	@EventHandler
	public void onMovement(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Location l = p.getLocation();
		if(l.getBlockY() <= 50) {
			teleportSpawn(p);
		}
		
	}
	public void teleportSpawn(Player player) {
		FileConfiguration config = plugin.getConfig();
		if(config.isSet("Spawn.x")) {
			final Location l = new Location(Bukkit.getWorld(config.getString("Spawn.world")),
					config.getDouble("Spawn.x"),
					config.getDouble("Spawn.y"),
					config.getDouble("Spawn.z"),
					(float) config.getDouble("Spawn.yaw"),
					(float) config.getDouble("Spawn.pitch"));
			player.teleport(l);
			player.sendMessage(c(config.getString("Messages.spawn_teleport")));
		}else {
			player.sendMessage(c(config.getString("Messages.spawn_not_set")));
			return;
		}
		
	}
	public void launchFirework(Player p) {
		FileConfiguration config = plugin.getConfig();
		Firework f = (Firework) p.getWorld().spawn(p.getLocation(), Firework.class);
		FireworkMeta fm = f.getFireworkMeta();
		fm.addEffect(FireworkEffect.builder()
				.flicker(true)
				.trail(true)
				.with(Type.BALL_LARGE)
				.withColor(Color.WHITE, Color.AQUA, Color.LIME).build());
		fm.setPower(config.getInt("Firework.power"));
		f.setFireworkMeta(fm);
	}
	public String c(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}
}
