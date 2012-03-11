package com.piwany.erwyn.rakamak;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class RakamakEntityListener implements Listener {
	public Rakamak plugin;

	public RakamakEntityListener(Rakamak instance) {
		this.plugin = instance;
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		Entity victime = e.getEntity();

		if ((victime instanceof Player)) {
			Player p = (Player) victime;

			if (this.plugin.notlogged(p))
				e.setCancelled(true);
		}
	}
}

/*
 * Location: /Users/alec/Downloads/Rakamak.jar Qualified Name:
 * com.piwany.erwyn.rakamak.RakamakEntityListener JD-Core Version: 0.6.0
 */