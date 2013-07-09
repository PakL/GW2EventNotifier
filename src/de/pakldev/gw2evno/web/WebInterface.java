package de.pakldev.gw2evno.web;

import com.sun.net.httpserver.HttpServer;
import de.pakldev.gw2evno.Configuration;
import de.pakldev.gw2evno.GW2EvNoMain;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;

public class WebInterface {

	public static final int WEB_STATE_STOPPED = 21;
	public static final int WEB_STATE_STARTED = 22;

	private final GW2EvNoMain main;
	private HttpServer server = null;
	private int port = Configuration.webPort;
	private int state = WEB_STATE_STOPPED;

	public WebInterface(GW2EvNoMain main) {
		this.main = main;
	}

	public boolean start(int port) {
		this.port = port;
		try {
			server = HttpServer.create();
			server.bind(new InetSocketAddress(this.port), 0);
			server.createContext("/", new FileHandler());
			server.createContext("/data/", new DataHandler(main));
			server.start();
			state = WEB_STATE_STARTED;
			main.sf.setWebinterfaceState(WEB_STATE_STARTED);
			System.out.println("[Web] Now listening on port "+this.port);
			return true;
		} catch (IOException e) {
			if( e.getMessage().startsWith("Address already in use") ) {
				JOptionPane.showMessageDialog(null, "The port "+this.port+" is already in use by another program.\nPlease select another port and try again!", "Webinterface", JOptionPane.ERROR_MESSAGE);
			}
		}
		return false;
	}

	public void stop() {
		if( server != null ) {
			server.stop(0);
		}
		state = WEB_STATE_STOPPED;
		main.sf.setWebinterfaceState(WEB_STATE_STOPPED);
	}

	public int getPort() {
		return port;
	}

	public int getState() {
		return state;
	}

}
