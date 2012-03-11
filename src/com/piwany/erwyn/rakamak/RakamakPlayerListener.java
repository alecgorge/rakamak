package com.piwany.erwyn.rakamak;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class RakamakPlayerListener implements Listener {
	public Rakamak plugin;
	public SettingsHandler settings;
	public ChatColor $aqua = ChatColor.AQUA;
	public ChatColor $dark_aqua = ChatColor.DARK_AQUA;
	public ChatColor $gold = ChatColor.GOLD;
	public ChatColor $red = ChatColor.RED;
	public ChatColor $dark_red = ChatColor.DARK_RED;
	public ChatColor $green = ChatColor.GREEN;
	public ChatColor $blue = ChatColor.BLUE;
	public ChatColor $black = ChatColor.BLACK;
	public ChatColor $dark_gray = ChatColor.DARK_GRAY;
	public ChatColor $gray = ChatColor.GRAY;
	public ChatColor $dark_green = ChatColor.DARK_GREEN;
	public ChatColor $dark_blue = ChatColor.DARK_BLUE;
	public ChatColor $light_purple = ChatColor.LIGHT_PURPLE;
	public ChatColor $dark_purple = ChatColor.DARK_PURPLE;
	public ChatColor $white = ChatColor.WHITE;
	public ChatColor $yellow = ChatColor.YELLOW;

	public RakamakPlayerListener(Rakamak instance) {
		this.plugin = instance;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String pName = player.getName();
		Location loc = null;
		GameMode game = player.getGameMode();

		player.setNoDamageTicks(1000000000);
		player.setGameMode(GameMode.SURVIVAL);
		this.plugin.essaie.put(player, Integer.valueOf(0));

		if (player.isOp()) {
			this.plugin.pop.put(player.getName(), Boolean.valueOf(true));
			player.setOp(false);
		} else {
			player.setOp(false);
			this.plugin.pop.put(player.getName(), Boolean.valueOf(false));
		}

		if (game == GameMode.CREATIVE)
			this.plugin.mode.put(player.getName(), Boolean.valueOf(true));
		else {
			this.plugin.mode.put(player.getName(), Boolean.valueOf(false));
		}

		if (AccountsMgmt.hasAccount(player, Rakamak.accounts)) {
			player.sendMessage(ChatColor.GOLD + this.plugin.hello_msg.replace("+pName", pName));
			this.plugin.togglePlayer(player);
		} else {
			player.sendMessage(ChatColor.AQUA + this.plugin.welcome_msg.replace("+pName", pName));
			loc = player.getWorld().getSpawnLocation();
			player.teleport(loc);
			this.plugin.togglePlayer(player);
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location loc = player.getLocation();
		int move = this.plugin.moveradius;

		if ((!this.plugin.notlogged(player)) || ((Math.abs(this.plugin.locate(player).getBlockX() - loc.getBlockX()) <= move) && (Math.abs(this.plugin.locate(player).getBlockY() - loc.getBlockY()) <= move) && (Math.abs(this.plugin.locate(player).getBlockZ() - loc.getBlockZ()) <= move))) {
			return;
		}
		player.teleport(this.plugin.locate(player));
		player.sendMessage(ChatColor.RED + this.plugin.outside_radius.replace("+pName", player.getName()));
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (this.plugin.notlogged(player)) {
			player.sendMessage(ChatColor.RED + this.plugin.not_interact.replace("+pName", player.getName()));
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		if (this.plugin.notlogged(p))
			e.setCancelled(true);
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (this.plugin.notlogged(player))
			event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		if (this.plugin.notlogged(player))
			event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (this.plugin.notlogged(player)) {
			player.teleport(this.plugin.locate(player));
			this.plugin.togglePlayer(player);
			if (((Boolean) this.plugin.pop.get(player.getName())).booleanValue())
				player.setOp(true);
			else {
				player.setOp(false);
			}
		}

		this.plugin.essaie.remove(player);
	}

	@EventHandler
	public void onPlayerChat(PlayerChatEvent e) {
		Player player = e.getPlayer();

		if ((this.plugin.notlogged(player)) && (this.plugin.disabledchat)) {
			player.sendMessage(ChatColor.RED + this.plugin.not_chat.replace("+pName", player.getName()));
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		String[] cmd = event.getMessage().split(" ");
		Player player = event.getPlayer();
		String name = player.getName();
		boolean iop = ((Boolean) this.plugin.pop.get(player.getName())).booleanValue();
		boolean moed = ((Boolean) this.plugin.mode.get(player.getName())).booleanValue();
		int nbressaies = ((Integer) this.plugin.essaie.get(player)).intValue();

		if ((this.plugin.notlogged(player)) && (!cmd[0].equalsIgnoreCase("/register")) && (!cmd[0].equalsIgnoreCase("/rs")) && (!cmd[0].equalsIgnoreCase("/login")) && (!cmd[0].equalsIgnoreCase("/lg"))) {
			player.sendMessage(ChatColor.RED + this.plugin.not_command.replace("+pName", name));
			event.setCancelled(true);
		} else {
			if (cmd[0].equalsIgnoreCase("/changepass")) {
				if (cmd.length != 3) {
					player.sendMessage(ChatColor.DARK_GREEN + "/changepass <currentpassword> <newpassword>");
				} else {
					AccountsMgmt.chPass(player, cmd[1], cmd[2], Rakamak.accounts);
					System.out.println("[Rakamak] " + name + " changed his password.");
				}
				event.setCancelled(true);
			}
			if ((cmd.length != 2) && ((cmd[0].equalsIgnoreCase("/register")) || (cmd[0].equalsIgnoreCase("/rs")) || (cmd[0].equalsIgnoreCase("/login")) || (cmd[0].equalsIgnoreCase("/lg")))) {
				player.sendMessage("Rakamak: invalid number of arguments");
			} else if ((cmd[0].equalsIgnoreCase("/register")) || (cmd[0].equalsIgnoreCase("/rs"))) {
				if (AccountsMgmt.hasAccount(player, Rakamak.accounts))
					player.sendMessage(ChatColor.GREEN + this.plugin.already_password.replace("+pName", name));
				else {
					AccountsMgmt.addAccount(player, cmd[1], Rakamak.accounts);
				}
				event.setCancelled(true);
			} else if ((cmd[0].equalsIgnoreCase("/login")) || (cmd[0].equalsIgnoreCase("/lg"))) {
				if (!AccountsMgmt.hasAccount(player, Rakamak.accounts)) {
					player.sendMessage(ChatColor.GREEN + this.plugin.not_account.replace("+pName", name));
				} else if (AccountsMgmt.verify(player, cmd[1], Rakamak.accounts)) {
					if (this.plugin.notlogged(player)) {
						this.plugin.togglePlayer(player);
						player.setNoDamageTicks(0);
						player.sendMessage(ChatColor.GREEN + this.plugin.authenticated.replace("+pName", name));
						System.out.println("[Rakamak] " + name + " is identified.");
						if (iop) {
							player.setOp(true);
						}
						if (moed)
							player.setGameMode(GameMode.CREATIVE);
					} else {
						player.sendMessage(ChatColor.GREEN + this.plugin.already_auth.replace("+pName", name));
					}
				} else {
					player.sendMessage(ChatColor.DARK_RED + this.plugin.wrong.replace("+pName", name));
					this.plugin.essaie.put(player, Integer.valueOf(nbressaies + 1));
					if (nbressaies >= 4) {
						this.plugin.essaie.put(player, Integer.valueOf(0));
						player.kickPlayer(this.plugin.maxWrong.replace("+pName", name));
					}
				}

				event.setCancelled(true);
			}
		}
	}

}

/*
 * Location: /Users/alec/Downloads/Rakamak.jar Qualified Name:
 * com.piwany.erwyn.rakamak.RakamakPlayerListener JD-Core Version: 0.6.0
 */