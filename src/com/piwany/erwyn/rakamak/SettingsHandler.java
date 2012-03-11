package com.piwany.erwyn.rakamak;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Logger;

public class SettingsHandler {
	public File file;
	public String name;
	public String saveFile;
	private HashMap<String, String> FileContents = new HashMap<String, String>();

	public SettingsHandler(String resourcePath, String saveFile) {
		this.saveFile = saveFile;
		this.name = resourcePath;
	}

	private void create(String name) {
		if (getClass().getClassLoader().getResource(name) == null) {
			Logger log = Logger.getLogger("Minecraft");
			log.severe("Couldn't load resource: " + name);
			return;
		}
		InputStream input = getClass().getClassLoader().getResourceAsStream(name);
		if (input != null) {
			FileOutputStream output = null;
			try {
				output = new FileOutputStream(this.file);
				byte[] buf = new byte[8192];
				int length = 0;

				while ((length = input.read(buf)) > 0)
					output.write(buf, 0, length);
			} catch (Exception e) {
				e.printStackTrace();
				try {
					if (input != null)
						input.close();
				} catch (Exception localException1) {
				}
				try {
					if (output != null)
						output.close();
				} catch (Exception localException2) {
				}
			} finally {
				try {
					if (input != null)
						input.close();
				} catch (Exception localException3) {
				}
				try {
					if (output != null)
						output.close();
				} catch (Exception localException4) {
				}
			}
		}
	}

	public Boolean load() {
		if (this.file == null) {
			this.file = new File(this.saveFile);
		}

		if (!this.file.exists()) {
			create(this.name);
		}

		this.FileContents = loadFileContents();
		return true;
	}

	public String getPropertyString(String property) {
		try {
			if (this.FileContents.containsKey(property)) {
				return (String) this.FileContents.get(property);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Boolean getPropertyBoolean(String property) {
		try {
			String result = (String) this.FileContents.get(property);
			if ((result.equalsIgnoreCase("true")) || (result.equalsIgnoreCase("false"))) {
				return Boolean.valueOf(result);
			}
			return Boolean.valueOf(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.valueOf(false);
	}

	public Integer getPropertyInteger(String property) {
		try {
			String result = (String) this.FileContents.get(property);
			return Integer.valueOf(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Integer.valueOf(10);
	}

	public Double getPropertyDouble(String property) {
		try {
			String result = (String) this.FileContents.get(property);
			if (!result.contains("."))
				result = result + ".0";

			return Double.valueOf(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Double.valueOf(-10.0D);
	}

	public Boolean isValidProperty(String property) {
		if (this.FileContents.containsKey(property)) {
			return Boolean.valueOf(true);
		}

		return Boolean.valueOf(false);
	}

	private HashMap<String, String> loadFileContents() {
		HashMap<String, String> result = new HashMap<String, String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.file));
			String word = null;

			while ((word = br.readLine()) != null)
				if ((!word.isEmpty()) && (!word.startsWith("#")) && (word.contains(":"))) {
					String[] args = word.split(":");
					result.put(args[0], args[1]);
				}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return result;
	}
}

/*
 * Location: /Users/alec/Downloads/Rakamak.jar Qualified Name:
 * com.piwany.erwyn.rakamak.SettingsHandler JD-Core Version: 0.6.0
 */