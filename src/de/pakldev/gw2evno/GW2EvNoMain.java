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
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

public class GW2EvNoMain {

	public final StartupFrame sf;

	public WorldNames worlds;
	public MapNames maps;
	public EventNames events;

	public GW2EvNoMain() {
		System.out.println("[System] Start up GUI");
		sf = new StartupFrame(this);

		System.out.println("[System] Loading event names to guess the related icon");
		EventNames.loadEventIcons();

		System.out.println("[System] Starting web interface");
		new WebInterface(this);

		this.loadLanguage(Configuration.language);
	}

	public void loadLanguage(final String language) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("[System] Loading language files for '" + language + "'");
				sf.resetToLoading();

				worlds = new WorldNames(language);
				sf.setProgressValue(1);
				System.out.println("[System] World names loaded");
				maps = new MapNames(language);
				sf.setProgressValue(2);
				System.out.println("[System] Map names loaded");
				events = new EventNames(language);
				sf.setProgressValue(3);
				System.out.println("[System] Event names loaded");
				sf.doneLoading();
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

		new GW2EvNoMain();
	}

	public static String loadURL(String urlStr) throws MalformedURLException {
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
			System.exit(0);
		}

		return result;
	}

}
