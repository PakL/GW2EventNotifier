package de.pakldev.gw2evno.web;

import com.sun.net.httpserver.HttpServer;
import de.pakldev.gw2evno.Configuration;
import de.pakldev.gw2evno.GW2EvNoMain;
import org.java_websocket.drafts.Draft_17;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;

public class WebInterface {

	public static final int WEB_STATE_STOPPED = 21;
	public static final int WEB_STATE_STARTED = 22;

	private final GW2EvNoMain main;

	private WebSocketServer webSocket = null;
	private HttpServer server = null;
	private int port = Configuration.webPort;
	private int socketport = Configuration.webSocketPort;
	private int state = WEB_STATE_STOPPED;

	public WebInterface(GW2EvNoMain main) {
		this.main = main;
	}

	public boolean start(int port, int socketport) {
		this.port = port;
		this.socketport = socketport;
		try {
			server = HttpServer.create();
			server.bind(new InetSocketAddress(this.port), 0);
			server.createContext("/", new FileHandler());
			server.createContext("/data/", new DataHandler(main));
			server.start();
			state = WEB_STATE_STARTED;
			main.sf.setWebinterfaceState(WEB_STATE_STARTED);
			System.out.println("[Web] Now listening on port "+this.port);
			webSocket = new WebSocketServer(main, this.socketport, new Draft_17());
			System.out.println("[Web][Socket] Now listening on port "+this.socketport);
			return true;
		} catch (IOException e) {
			if( e.getMessage().startsWith("Address already in use") ) {
				JOptionPane.showMessageDialog(null, "The port "+this.port+" is already in use by another program.\nPlease select another port and try again!", "Webinterface", JOptionPane.ERROR_MESSAGE);
			}
		}
		return false;
	}

	public void broadcast(String message) {
		if( webSocket != null ) {
			webSocket.broadcastMessage(message);
		}
	}

	public WebSocketServer getWebSocket() {
		return webSocket;
	}

	public void stop() {
		if( server != null ) {
			server.stop(0);
			System.out.println("[Web] Server stopped.");
		}
		if( webSocket != null ) {
			try {
				webSocket.stop();
				System.out.println("[Web][Socket] Server stopped.");
			} catch(Exception ex) {}
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
