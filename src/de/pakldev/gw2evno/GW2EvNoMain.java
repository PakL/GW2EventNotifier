package de.pakldev.gw2evno;


import de.pakldev.gw2evno.gui.StartupFrame;
import de.pakldev.gw2evno.gw2api.EventNames;
import de.pakldev.gw2evno.gw2api.Events;
import de.pakldev.gw2evno.gw2api.MapNames;
import de.pakldev.gw2evno.gw2api.WorldNames;
import de.pakldev.gw2evno.web.WebInterface;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.swing.*;
import java.io.*;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class GW2EvNoMain {

	public final StartupFrame sf;

	public WorldNames worlds;
	public MapNames maps;
	public EventNames events;
	public WebInterface web;

	public GW2EvNoMain(boolean startuppaused, boolean noweb) {
		System.out.println("[System] Start up GUI");
		sf = new StartupFrame(this, startuppaused);

		System.out.println("[System] Loading event names to guess the related icon");
		EventNames.loadEventIcons();

		System.out.println("[System] Starting web interface");
		web = new WebInterface(this);
		if( !noweb )
			web.start(Configuration.webPort, Configuration.webSocketPort);

		sf.setWebinterfaceEnabled(true);
		sf.setWebportEnabled(true);

		this.loadLanguage(Configuration.language);
	}

	public void loadLanguage(final String language) {
		Configuration.language = language;
		Configuration.saveConfig();
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("[System] Loading language files for '" + language + "'");
				sf.loading();

				sf.setStatusAndProgress("Loading worlds...", 0);
				worlds = new WorldNames(language);
				if( worlds.getWorlds().size() <= 0 ){ sf.setStatusAndProgress("API offline?", 0); return; }
				System.out.println("[System] World names loaded");
				sf.setStatusAndProgress("Loading map names...", 1);
				maps = new MapNames(language);
				if( maps.getMaps().size() <= 0 ){ sf.setStatusAndProgress("API offline?", 0); return; }
				System.out.println("[System] Map names loaded");
				sf.setStatusAndProgress("Loading event names...", 1);
				events = new EventNames(language);
				if( events.getEvents().size() <= 0 ){ sf.setStatusAndProgress("API offline?", 0); return; }
				System.out.println("[System] Event names loaded");
				sf.setStatusAndProgress("Done!", 3);
				sf.ready();
			}
		}).start();
	}

	public static void main(String[] args) throws Exception {
		System.out.println("[System] Forcing default file encoding to utf-8");
		System.setProperty("file.encoding", "UTF-8");
		Field charset = Charset.class.getDeclaredField("defaultCharset");
		charset.setAccessible(true);
		charset.set(null,null);

		try {
			System.out.println("[System] Setting system look and feel");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception ex) {}

		System.out.println("[System] Loading configuration");
		Configuration.loadConfig();
		System.out.println("[System] Loading InterestingEvents");
		InterestingEvents.loadInterestingEvents();
		System.out.println("[System] Loading images");
		Events.loadImages();

		boolean startuppaused = false;
		boolean noweb = false;
		for( String arg : args ) {
			if( arg.equalsIgnoreCase("-startuppaused") ) startuppaused = true;
			if( arg.equalsIgnoreCase("-noweb") ) noweb = true;
		}

		new GW2EvNoMain(startuppaused, noweb);
	}

	/*public static String loadURL(String urlStr) throws MalformedURLException {
		URL url = new URL(urlStr);
		String result = "";
		try {
			URLConnection conn = url.openConnection();
			if( conn instanceof HttpsURLConnection ) {
				((HttpsURLConnection)conn).setHostnameVerifier(new HostnameVerifier() {
					@Override
					public boolean verify(String s, SSLSession sslSession) {
						return true;
					}
				});
			}
			InputStream is = conn.getInputStream();
			System.out.println("[HTTP] Loading " + urlStr + "...");
			int r = -1;
			while((r = is.read()) >= 0) {
				int a = is.available();
				byte[] b = new byte[a+1];
				b[0] = (byte)r;
				is.read(b, 1, a);
				result += new String(b);
			}
			is.close();
			System.out.println("[HTTP] Downloading file successfull: " + result.getBytes().length + " bytes");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.toString(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
			//System.exit(0);
		}

		return result;
	}*/

	public static Reader readURL(String urlStr) throws MalformedURLException {
		URL url = new URL(urlStr);
		Reader result = null;
		try {
			URLConnection conn = url.openConnection();
			if( conn instanceof HttpsURLConnection ) {
				((HttpsURLConnection)conn).setHostnameVerifier(new HostnameVerifier() {
					@Override
					public boolean verify(String s, SSLSession sslSession) {
						return true;
					}
				});
			}
			InputStream is = conn.getInputStream();
			System.out.println("[HTTP] Opening " + urlStr + "...");

			return new InputStreamReader(is);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.toString(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
		}

		return result;
	}



	public static void openUrl(String url) {
		try {
			if(java.awt.Desktop.isDesktopSupported() ) {
				java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

				if(desktop.isSupported(java.awt.Desktop.Action.BROWSE) ) {
					java.net.URI uri = new java.net.URI(url);
					desktop.browse(uri);
				}
			}
		} catch(Exception e) {}
	}

	public static void restartApplication() {
		try {
			final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
			final File currentJar = new File(GW2EvNoMain.class.getProtectionDomain().getCodeSource().getLocation().toURI());

			if(!currentJar.getName().endsWith(".jar"))
				return;

			final ArrayList<String> command = new ArrayList<String>();
			command.add(javaBin);
			command.add("-jar");
			command.add(currentJar.getPath());

			final ProcessBuilder builder = new ProcessBuilder(command);
			builder.start();
			System.exit(0);
		} catch(Exception e) {}
	}

}
