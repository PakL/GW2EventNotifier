package de.pakldev.gw2evno;

import java.util.prefs.Preferences;

public class Configuration {

	private static Preferences preference;

	public static int worldIndex = 0;
	public static int mapIndex = 0;
	public static int timeout = 15;
	public static String language = "en";
	public static boolean interestingOnly = false;

	public static void loadConfig() {
		try {
			Configuration.preference = Preferences.userRoot().node("/de/pakldev/gw2evno/Configuration");
			Configuration.worldIndex = Configuration.preference.getInt("worldIndex", 0);
			Configuration.mapIndex = Configuration.preference.getInt("mapIndex", 0);
			Configuration.timeout = Configuration.preference.getInt("timeout", 15);
			Configuration.language = Configuration.preference.get("language", "en");
			Configuration.interestingOnly = Configuration.preference.getBoolean("interestingOnly", false);
			System.out.println("[Config] Configuration loaded with no error.");
		} catch(Exception ex) {
			System.err.println("[Config] Error loading configuration: "+ex.getMessage());
		}
	}

	public static void saveConfig() {
		try {
			Configuration.preference.putInt("worldIndex", Configuration.worldIndex);
			Configuration.preference.putInt("mapIndex", Configuration.mapIndex);
			Configuration.preference.putInt("timeout", Configuration.timeout);
			Configuration.preference.put("language", Configuration.language);
			Configuration.preference.putBoolean("interestingOnly", Configuration.interestingOnly);
			System.out.println("[Config] Configuration saved with no error.");
		} catch(Exception ex) {
			System.err.println("[Config] Error saving configuration: "+ex.getMessage());
		}
	}

}
