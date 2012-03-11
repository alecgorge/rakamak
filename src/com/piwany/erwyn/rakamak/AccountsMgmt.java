package com.piwany.erwyn.rakamak;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AccountsMgmt {
	public Rakamak plugin;

	public AccountsMgmt(Rakamak instance) {
		this.plugin = instance;
	}

	public static boolean hasAccount(Player player, File file) {
		Properties properties = new Properties();
		String playerName = player.getName();
		String name = playerName.toLowerCase();
		try {
			FileInputStream in = new FileInputStream(file);
			properties.load(in);

			if (properties.containsKey(name))
				return true;
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}

	public static void addAccount(Player player, String pass, File file) {
		Properties properties = new Properties();
		String playerName = player.getName();
		String name = playerName.toLowerCase();
		try {
			FileInputStream in = new FileInputStream(file);
			properties.load(in);
			properties.setProperty(name, pass);
			properties.store(new FileOutputStream(file), null);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		player.sendMessage(ChatColor.GREEN + Rakamak.created.replace("+pName", playerName));
	}

	public static void chPass(Player player, String pass, String nPass, File file) {
		Properties properties = new Properties();
		String playerName = player.getName();
		String name = playerName.toLowerCase();
		try {
			FileInputStream in = new FileInputStream(file);
			properties.load(in);
			String userpass = properties.getProperty(name);
			if (userpass.equals(pass)) {
				properties.setProperty(name, "");
				properties.setProperty(name, nPass);

				player.sendMessage(ChatColor.DARK_GREEN + Rakamak.changed.replace("+pName", playerName));
			} else {
				player.sendMessage(ChatColor.DARK_RED + Rakamak.wrong_change.replace("+pName", playerName));
			}
			properties.store(new FileOutputStream(file), null);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static boolean verify(Player player, String pass, File file) {
		Properties properties = new Properties();
		String playerName = player.getName();
		String name = playerName.toLowerCase();
		try {
			FileInputStream in = new FileInputStream(file);
			properties.load(in);
			String userpass = properties.getProperty(name);
			return userpass.equals(pass);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}
}

/*
 * Location: /Users/alec/Downloads/Rakamak.jar Qualified Name:
 * com.piwany.erwyn.rakamak.AccountsMgmt JD-Core Version: 0.6.0
 */