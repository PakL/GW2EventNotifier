package de.pakldev.gw2evno.web;

import com.sun.net.httpserver.HttpServer;
import de.pakldev.gw2evno.GW2EvNoMain;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebInterface {

	private HttpServer server;

	public WebInterface(GW2EvNoMain main) {
		try {
			server = HttpServer.create();
			server.bind(new InetSocketAddress(8086), 0);
			server.createContext("/", new FileHandler());
			server.createContext("/data/", new DataHandler(main));
			server.start();
			System.out.println("[Web] Now listening on port 8086");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		if( server != null ) {
			server.stop(0);
		}
	}

}
