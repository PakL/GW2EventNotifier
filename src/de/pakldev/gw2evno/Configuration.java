package de.pakldev.gw2evno;

import java.io.*;

public class Configuration {

	public static int worldIndex = 0;
	public static int mapIndex = 0;
	public static int timeout = 15;
	public static String language = "en";
	public static boolean interestingOnly = false;

	public static void loadConfig() {
		File configFile = new File("gw2evno.cfg");
		String config = "";
		if( configFile.exists() ) {
			try {
				InputStream is = new FileInputStream(configFile);
				int r = -1;
				while((r = is.read()) >= 0) {
					int a = is.available();
					byte[] b = new byte[a+1];
					b[0] = (byte)r;
					is.read(b, 1, a);
					config += new String(b);
				}
				is.close();
				System.out.println("[Config] Configuration loaded with no error.");
			} catch (Exception e) {
				System.err.println("[Config] Error loading configuration: "+e.getMessage());
			}
		} else {
			System.out.println("[Config] Default configuration loaded.");
		}
		String[] c = config.split("\n");
		if( c.length == 5 ) {
			try {
				Configuration.worldIndex = Integer.parseInt(c[0]);
				Configuration.mapIndex = Integer.parseInt(c[1]);
				Configuration.timeout = Integer.parseInt(c[2]);
				if( Configuration.timeout < 10 ) Configuration.timeout = 10;
				Configuration.language = c[3];
				if( c[4].equalsIgnoreCase("true") ) Configuration.interestingOnly = true;
			} catch(Exception e) {}
		}
	}

	public static void saveConfig() {
		File configFile = new File("gw2evno.cfg");
		String	config  = Configuration.worldIndex + "\n";
				config += Configuration.mapIndex + "\n";
				config += Configuration.timeout + "\n";
				config += Configuration.language + "\n";
				config += (Configuration.interestingOnly ? "true" : "false");

		try {
			OutputStream os = new FileOutputStream(configFile);
			os.write(config.getBytes());
			os.flush();
			os.close();

			System.out.println("[Config] Configuration saved with no error.");
		} catch(Exception e) {
			System.err.println("[Config] Error saving configuration: "+e.getMessage());
		}
	}

}
