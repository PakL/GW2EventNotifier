package de.pakldev.gw2evno;


import de.pakldev.gw2evno.gui.StartupFrame;
import de.pakldev.gw2evno.gw2api.EventNames;
import de.pakldev.gw2evno.gw2api.Events;
import de.pakldev.gw2evno.gw2api.MapNames;
import de.pakldev.gw2evno.gw2api.WorldNames;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

public class GW2EvNoMain {

	private final StartupFrame sf;

	public WorldNames worlds;
	public MapNames maps;
	public EventNames events;

	public GW2EvNoMain() {
		System.out.println("Start up GUI");
		sf = new StartupFrame(this);
		this.loadLanguage(Configuration.language);
	}

	public void loadLanguage(String language) {
		System.out.println("Loading language files for '" + language + "'");
		sf.resetToLoading();

		worlds = new WorldNames(language);
		sf.setProgressValue(1);
		maps = new MapNames(language);
		sf.setProgressValue(2);
		events = new EventNames(language);
		sf.setProgressValue(3);
		sf.doneLoading();
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Forcing default file encoding to utf-8");
		System.setProperty("file.encoding","UTF-8");
		Field charset = Charset.class.getDeclaredField("defaultCharset");
		charset.setAccessible(true);
		charset.set(null,null);

		System.out.println("Creating keystore from ssl certificate");
		GW2EvNoMain.importSSLCert();
		System.out.println("Loading images");
		Events.loadImages();
		System.out.println("Loading event names to guess the related icon");
		EventNames.guessEventIcons();

		try {
			System.out.println("Setting system look and feel");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception ex) {}

		System.out.println("Loading configuration");
		Configuration.loadConfig();
		System.out.println("Loading InterestingEvents");
		InterestingEvents.loadInterestingEvents();

		new GW2EvNoMain();
	}

	public static void importSSLCert() throws Exception {
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(null, null);

		BufferedInputStream bis = new BufferedInputStream(GW2EvNoMain.class.getResourceAsStream("guildwars2.com.cer"));
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		Certificate cert = null;

		if (bis.available() > 0) {
			cert = cf.generateCertificate( bis );
			ks.setCertificateEntry( "SGCert", cert );
		}

		ks.setCertificateEntry("SGCert", cert);
		ks.store(new FileOutputStream("guildwars2.com.keystore"), "secret".toCharArray());
		System.setProperty("javax.net.ssl.trustStore", "guildwars2.com.keystore");
	}

	public static String loadURL(String urlStr) throws MalformedURLException {
		URL url = new URL(urlStr);
		String result = "";
		try {
			InputStream is = url.openStream();
			int r = -1;
			while((r = is.read()) >= 0) {
				int a = is.available();
				byte[] b = new byte[a+1];
				b[0] = (byte)r;
				is.read(b, 1, a);
				result += new String(b);
			}
			is.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.toString(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		return result;
	}

}
