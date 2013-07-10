package de.pakldev.gw2evno.web;

import de.pakldev.gw2evno.GW2EvNoMain;
import de.pakldev.gw2evno.Language;
import de.pakldev.gw2evno.gw2api.EventNames;
import de.pakldev.gw2evno.gw2api.Events;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ClientHandshake;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WebSocketServer extends org.java_websocket.server.WebSocketServer {

	private final GW2EvNoMain main;
	private List<WebSocket> connections = new ArrayList<WebSocket>();

	public WebSocketServer(GW2EvNoMain main, int port, Draft d) throws UnknownHostException {
		super(new InetSocketAddress(port), Collections.singletonList(d));
		this.main = main;
		this.start();
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		connections.add(conn);
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		connections.remove(conn);
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		try {
			JSONObject json = (JSONObject) JSONValue.parse(message);
			String request = (String)json.get("request");
			JSONObject res = new JSONObject();
			if( request.equalsIgnoreCase("worlds") ) {
				sendWorlds(conn);
			} else if( request.equalsIgnoreCase("maps") ) {
				sendMaps(conn);
			} else if( request.equalsIgnoreCase("eventnames") ) {
				sendEventnames(conn);
			} else if( request.equalsIgnoreCase("settings") ) {
				sendSettings(conn);
			} else if( request.equalsIgnoreCase("interestingevents") ) {
				sendInterestingevents(conn);
			} else if( request.equalsIgnoreCase("events") ) {
				sendEvents(conn);
			}
			conn.send(res.toString());
		} catch(Exception e) {}
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		if( conn != null ) {
			conn.close(0);
			connections.remove(conn);
		}
	}

	public void broadcastMessage(String message) {
		for(WebSocket conn : connections) {
			if( conn.isOpen() && !conn.isClosing() ) {
				conn.send(message);
			}
		}
	}

	public void broadcastWorlds() { sendWorlds(null); };
	public void sendWorlds(WebSocket conn) {
		JSONObject res = new JSONObject();
		res.put("type", "worlds");
		JSONObject worlds = new JSONObject();
		if( main.worlds != null ) {
			for(String id : main.worlds.getWorlds().keySet()) {
				worlds.put(id, main.worlds.getWorld(id));
			}
		}
		res.put("worlds", worlds);
		if( conn == null ) broadcastMessage(res.toString());
		else conn.send(res.toString());
	}

	public void broadcastMaps() { sendMaps(null); };
	public void sendMaps(WebSocket conn) {
		JSONObject res = new JSONObject();
		res.put("type", "maps");
		JSONObject maps = new JSONObject();
		if( main.maps != null ) {
			for(String id : main.maps.getMaps().keySet()) {
				maps.put(id, main.maps.getMap(id));
			}
		}
		res.put("maps", maps);
		if( conn == null ) broadcastMessage(res.toString());
		else conn.send(res.toString());
	}

	public void broadcastEventnames() { sendEventnames(null); };
	public void sendEventnames(WebSocket conn) {
		JSONObject res = new JSONObject();
		res.put("type", "eventnames");
		JSONObject events = new JSONObject();
		if( main.events != null ) {
			for(String id : main.events.getEvents().keySet()) {
				JSONObject details = new JSONObject();
				details.put("name", main.events.getName(id));
				String icon = "";
				if( EventNames.getIcon(id) == Events.ICON_ATTACK ) {
					icon = "attack";
				} else if( EventNames.getIcon(id) == Events.ICON_SKILL ) {
					icon = "skill";
				} else if( EventNames.getIcon(id) == Events.ICON_CAPTURE ) {
					icon = "capture";
				} else if( EventNames.getIcon(id) == Events.ICON_COLLECT ) {
					icon = "collect";
				} else if( EventNames.getIcon(id) == Events.ICON_FIST ) {
					icon = "fist";
				} else if( EventNames.getIcon(id) == Events.ICON_KILL ) {
					icon = "kill";
				} else if( EventNames.getIcon(id) == Events.ICON_OBJECT ) {
					icon = "object";
				} else if( EventNames.getIcon(id) == Events.ICON_PROTECT ) {
					icon = "protect";
				} else {
					icon = "star";
				}
				details.put("icon", icon);


				events.put(id, details);
			}
		}
		res.put("events", events);
		if( conn == null ) broadcastMessage(res.toString());
		else conn.send(res.toString());
	}

	public void broadcastSettings() { sendSettings(null); };
	public void sendSettings(WebSocket conn) {
		JSONObject res = new JSONObject();
		res.put("type", "settings");
		JSONObject settings = new JSONObject();
		settings.put("world", main.worlds.getWorldIdAt(main.sf.getWorldIndex()));
		settings.put("map", main.maps.getMapIdAt(main.sf.getMapIndex()));
		settings.put("interestingonly", main.sf.isInterestingOnly());
		res.put("settings", settings);
		if( conn == null ) broadcastMessage(res.toString());
		else conn.send(res.toString());
	}

	public void broadcastInterestingevents() { sendInterestingevents(null); };
	public void sendInterestingevents(WebSocket conn) {
		JSONObject res = new JSONObject();
		res.put("type", "interestingevents");
		JSONArray events = new JSONArray();
		Map<String, Integer> interestingState = main.sf.getEventManger().getInterestingStates();
		if( interestingState != null ) {
			for(String id : interestingState.keySet()) {
				int state = interestingState.get(id);
				JSONObject obj = new JSONObject();
				obj.put("id", id);
				obj.put("state", state);
				obj.put("langstate", Language.state(state));
				events.add(obj);
			}
		}
		res.put("events", events);
		if( conn == null ) broadcastMessage(res.toString());
		else conn.send(res.toString());
	}

	public void broadcastEvents() { sendEvents(null); };
	public void sendEvents(WebSocket conn) {
		JSONObject res = new JSONObject();
		res.put("type", "events");
		JSONArray events = new JSONArray();
		Map<String, Integer> lastEventState = main.sf.getEventManger().getMapStates();
		if( lastEventState != null ) {
			for(String id : lastEventState.keySet()) {
				int state = lastEventState.get(id);
				JSONObject obj = new JSONObject();
				obj.put("id", id);
				obj.put("state", state);
				obj.put("langstate", Language.state(state));
				events.add(obj);
			}
		}
		res.put("events", events);
		if( conn == null ) broadcastMessage(res.toString());
		else conn.send(res.toString());
	}

}
