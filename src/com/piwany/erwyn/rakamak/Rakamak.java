package com.piwany.erwyn.rakamak;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Rakamak extends JavaPlugin {
	static String directory = "plugins/Rakamak/";
	static File accounts = new File(directory + "users.rak");

	public String logPrefix = "[Rakamak] ";
	public Logger log = Logger.getLogger("Minecraft");

	private final RakamakPlayerListener playerListener = new RakamakPlayerListener(this);
	private final RakamakEntityListener entityListener = new RakamakEntityListener(this);
	private final HashMap<Player, Location> unlogged = new HashMap<Player, Location>();

	public final HashMap<String, Boolean> pop = new HashMap<String, Boolean>();
	public final HashMap<Player, Integer> essaie = new HashMap<Player, Integer>();
	public final HashMap<String, Boolean> mode = new HashMap<String, Boolean>();

	public SettingsHandler settings;
	public SettingsHandler templates;

	public File pFolder = new File("plugins" + File.separator + "Rakamak");

	public int moveradius;
	public boolean disabledchat;
	public static String wrong_change = "";
	public static String created = "";
	public static String changed = "";
	public String maxWrong = "";
	public String wrong = "";
	public String already_auth = "";
	public String authenticated = "";
	public String not_account = "";
	public String hello_msg = "";
	public String welcome_msg = "";
	public String outside_radius = "";
	public String not_interact = "";
	public String not_chat = "";
	public String not_command = "";
	public String already_password = "";

	public void onDisable() {
		System.out.println(this.logPrefix + "Rakamak plugin disabled.");
	}

	public void onEnable() {
		this.settings = new SettingsHandler("Settings.properties", this.pFolder.getPath() + File.separator + "Config.properties");
		this.templates = new SettingsHandler("Template.properties", this.pFolder.getPath() + File.separator + "Templates.properties");

		this.settings.load();
		this.templates.load();

		if (this.settings.file.exists()) {
			if (this.settings.isValidProperty("moveradius").booleanValue())
				this.moveradius = this.settings.getPropertyInteger("moveradius").intValue();
			if (this.settings.isValidProperty("disabledchat").booleanValue())
				this.disabledchat = this.settings.getPropertyBoolean("disabledchat").booleanValue();
		} else {
			this.log.warning(this.logPrefix + "Check Config.properties");
		}

		if (this.settings.file.exists()) {
			if (this.templates.isValidProperty("hello_msg").booleanValue())
				this.hello_msg = this.templates.getPropertyString("hello_msg");
			if (this.templates.isValidProperty("welcome_msg").booleanValue())
				this.welcome_msg = this.templates.getPropertyString("welcome_msg");
			if (this.templates.isValidProperty("outside_radius").booleanValue())
				this.outside_radius = this.templates.getPropertyString("outside_radius");
			if (this.templates.isValidProperty("not_interact").booleanValue())
				this.not_interact = this.templates.getPropertyString("not_interact");
			if (this.templates.isValidProperty("not_chat").booleanValue())
				this.not_chat = this.templates.getPropertyString("not_chat");
			if (this.templates.isValidProperty("not_command").booleanValue())
				this.not_command = this.templates.getPropertyString("not_command");
			if (this.templates.isValidProperty("already_password").booleanValue())
				this.already_password = this.templates.getPropertyString("already_password");
			if (this.templates.isValidProperty("not_account").booleanValue())
				this.not_account = this.templates.getPropertyString("not_account");
			if (this.templates.isValidProperty("authenticated").booleanValue())
				this.authenticated = this.templates.getPropertyString("authenticated");
			if (this.templates.isValidProperty("already_auth").booleanValue())
				this.already_auth = this.templates.getPropertyString("already_auth");
			if (this.templates.isValidProperty("wrong").booleanValue())
				this.wrong = this.templates.getPropertyString("wrong");
			if (this.templates.isValidProperty("wrong_change").booleanValue())
				wrong_change = this.templates.getPropertyString("wrong_change");
			if (this.templates.isValidProperty("created").booleanValue())
				created = this.templates.getPropertyString("created");
			if (this.templates.isValidProperty("changed").booleanValue())
				changed = this.templates.getPropertyString("changed");
			if (this.templates.isValidProperty("maxwrongkick").booleanValue())
				this.maxWrong = this.templates.getPropertyString("maxwrongkick");
		} else {
			this.log.warning(this.logPrefix + "Check Templates.properties");
		}

		new File(directory).mkdir();

		if (!accounts.exists()) {
			try {
				accounts.createNewFile();
			} catch (IOException e) {
				System.out.println(this.logPrefix + "Problem while trying to create file...");
				e.printStackTrace();
				System.out.println(this.logPrefix + "End of stackTrace");
			}

		}

		PluginDescriptionFile pdf = getDescription();
		System.out.println(this.logPrefix + pdf.getName() + " version " + pdf.getVersion() + " is enabled." + " Plugin provided by " + pdf.getAuthors());

		getServer().getPluginManager().registerEvents(this.playerListener, this);
		getServer().getPluginManager().registerEvents(this.entityListener, this);
	}

	public boolean notlogged(Player player) {
		return this.unlogged.containsKey(player);
	}

	public void togglePlayer(Player player) {
		if (notlogged(player))
			this.unlogged.remove(player);
		else
			this.unlogged.put(player, player.getLocation());
	}

	public Location locate(Player player) {
		return (Location) this.unlogged.get(player);
	}
}

/*
 * Location: /Users/alec/Downloads/Rakamak.jar Qualified Name:
 * com.piwany.erwyn.rakamak.Rakamak JD-Core Version: 0.6.0
 */