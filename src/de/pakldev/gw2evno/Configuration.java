package de.pakldev.gw2evno;

import java.awt.event.KeyEvent;
import java.util.prefs.Preferences;

public class Configuration {

	private static Preferences preference;

	public static int worldIndex = 0;
	public static int mapIndex = 0;
	public static int timeout = 15;
	public static String language = "en";
	public static boolean interestingOnly = false;
	public static int webPort = 8086;
	public static int webSocketPort = 8087;
	public static int hotkeyMod = 2;
	public static int hotkeyKey = KeyEvent.VK_ENTER;
	public static int notificationx = -1;
	public static int notificationy = -1;

	public static void loadConfig() {
		try {
			Configuration.preference = Preferences.userRoot().node("/de/pakldev/gw2evno/Configuration");

			Configuration.worldIndex = Configuration.preference.getInt("worldIndex", 0);
			Configuration.mapIndex = Configuration.preference.getInt("mapIndex", 0);
			Configuration.timeout = Configuration.preference.getInt("timeout", 15);
			Configuration.language = Configuration.preference.get("language", "en");
			Configuration.interestingOnly = Configuration.preference.getBoolean("interestingOnly", false);
			Configuration.webPort = Configuration.preference.getInt("webport", 8086);
			Configuration.webSocketPort = Configuration.preference.getInt("websocketport", 8087);
			Configuration.hotkeyMod = Configuration.preference.getInt("hotkeymod", 2);
			Configuration.hotkeyKey = Configuration.preference.getInt("hotkeykey", KeyEvent.VK_ENTER);
			Configuration.notificationx = Configuration.preference.getInt("notificationx", -1);
			Configuration.notificationy = Configuration.preference.getInt("notificationy", -1);
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
			Configuration.preference.putInt("webport", Configuration.webPort);
			Configuration.preference.putInt("websocketport", Configuration.webSocketPort);
			Configuration.preference.putInt("hotkeymod", Configuration.hotkeyMod);
			Configuration.preference.putInt("hotkeykey", Configuration.hotkeyKey);
			Configuration.preference.putInt("notificationx", Configuration.notificationx);
			Configuration.preference.putInt("notificationy", Configuration.notificationy);
			System.out.println("[Config] Configuration saved with no error.");
		} catch(Exception ex) {
			System.err.println("[Config] Error saving configuration: "+ex.getMessage());
		}
	}

}
