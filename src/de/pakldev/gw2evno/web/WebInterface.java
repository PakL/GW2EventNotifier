package de.pakldev.gw2evno.web;

import com.sun.net.httpserver.HttpServer;
import de.pakldev.gw2evno.GW2EvNoMain;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebInterface {

	public static final int WEB_STATE_STOPPED = 21;
	public static final int WEB_STATE_STARTED = 22;

	private final GW2EvNoMain main;
	private HttpServer server = null;
	private int port = 8086;

	public WebInterface(GW2EvNoMain main) {
		this.main = main;
	}

	public void start() {
		try {
			server = HttpServer.create();
			server.bind(new InetSocketAddress(port), 0);
			server.createContext("/", new FileHandler());
			server.createContext("/data/", new DataHandler(main));
			server.start();
			main.sf.setWebinterfaceState(WEB_STATE_STARTED);
			System.out.println("[Web] Now listening on port 8086");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		if( server != null ) {
			server.stop(0);
		}
		main.sf.setWebinterfaceState(WEB_STATE_STOPPED);
	}

	public int getPort() {
		return port;
	}

}
