package de.pakldev.gw2evno;


import de.pakldev.gw2evno.gui.StartupFrame;
import de.pakldev.gw2evno.gw2api.EventNames;
import de.pakldev.gw2evno.gw2api.MapNames;
import de.pakldev.gw2evno.gw2api.WorldNames;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

public class GW2EvNoMain {

	public WorldNames worlds;
	public MapNames maps;
	public EventNames events;

	public GW2EvNoMain() {
		StartupFrame sf = new StartupFrame(this);

		worlds = new WorldNames();
		sf.setProgressValue(1);
		maps = new MapNames();
		sf.setProgressValue(2);
		events = new EventNames();
		sf.setProgressValue(3);
		sf.doneLoading();
	}

	public static void main(String[] args) throws Exception {
		GW2EvNoMain.importSSLCert();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception ex) {}

		Configuration.loadConfig();
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
