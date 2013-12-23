package me.shawshark.spawnkill;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener {
	
	List<Player> pvpstop = new ArrayList<Player>();
	
	public String prefix = ChatColor.GOLD + "[" + ChatColor.RED + "Craftshark" + ChatColor.GOLD + "]";
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		Bukkit.getPluginManager().registerEvents(this, this);	
	}
	
	@Override
	public void onDisable() {
		if(pvpstop.size() > 0) {
			pvpstop.clear();
		}
	}
	
	@EventHandler
	public void pvpevent(EntityDamageByEntityEvent e) {
		if((e.getEntity() instanceof Player) && e.getDamager() instanceof Player) {
			Player p = (Player)e.getEntity();
			Player killer = (Player)e.getDamager();
			if(pvpstop.contains(p) || pvpstop.contains(killer)) {
				killer.sendMessage(prefix + ChatColor.GOLD + " This player is in a non-pvp state for " + ChatColor.GREEN + "2 Minutes!");
				e.setCancelled(true);
			} else {
				e.setCancelled(false);
			}
		}
	}
	
	@EventHandler
	public void Teleport(PlayerTeleportEvent e) {
		Player p = e.getPlayer();
		if(!pvpstop.contains(p)) {
			p.sendMessage(prefix + ChatColor.GREEN + " You are now under 2 minutes of pvp protection!");
			pvpstop.add(p);
			removenonpvp(p);
		}
	}
	
	public void removenonpvp(final Player p ) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				pvpstop.remove(p);
				p.sendMessage(prefix + ChatColor.GREEN + " Your protection is now off, You are able to get killed or pvp others!");
			}
		}, 2000);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(pvpstop.contains(p)) {
			pvpstop.remove(p);
		}
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		Player p = e.getPlayer();
		if(pvpstop.contains(p)) {
			pvpstop.remove(p);
		}
	}
}
