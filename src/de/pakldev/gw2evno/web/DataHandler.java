package de.pakldev.gw2evno.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import de.pakldev.gw2evno.GW2EvNoMain;
import de.pakldev.gw2evno.Language;
import de.pakldev.gw2evno.gui.EventManager;
import de.pakldev.gw2evno.gw2api.EventNames;
import de.pakldev.gw2evno.gw2api.Events;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;

public class DataHandler implements HttpHandler {

	private GW2EvNoMain main;

	public DataHandler(GW2EvNoMain main) {
		this.main = main;
	}

	@Override
	public void handle(HttpExchange ex) throws IOException {
		String path = ex.getRequestURI().getPath();

		System.out.println("[Web] Requesting data: " + path);

		if( path.equalsIgnoreCase("/data/maps/") || path.equalsIgnoreCase("/data/maps") ) {
			JSONArray arr = new JSONArray();
			if( main.maps != null ) {
				for(String id : main.maps.getMaps().keySet()) {
					JSONObject obj = new JSONObject();
					obj.put(id, main.maps.getMap(id));
					arr.add(obj);
				}
			}
			String result = arr.toString();

			ArrayList<String> contentType = new ArrayList<String>();
			contentType.add("text/plain; charset=utf-8");
			ex.getResponseHeaders().put("Content-Type", contentType);
			ex.sendResponseHeaders(200, result.getBytes().length);

			OutputStream os = ex.getResponseBody();
			os.write(result.getBytes());
			os.flush();
			os.close();
		} else if( path.equalsIgnoreCase("/data/events/") || path.equalsIgnoreCase("/data/events") ) {
			JSONObject obj = new JSONObject();
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


					obj.put(id, details);
				}
			}
			String result = obj.toString();

			ArrayList<String> contentType = new ArrayList<String>();
			contentType.add("text/plain; charset=utf-8");
			ex.getResponseHeaders().put("Content-Type", contentType);
			ex.sendResponseHeaders(200, result.getBytes().length);

			OutputStream os = ex.getResponseBody();
			os.write(result.getBytes());
			os.flush();
			os.close();
		} else if( path.equalsIgnoreCase("/data/mapevents") || path.equalsIgnoreCase("/data/mapevents/") ) {
			EventManager evma = main.sf.getEventManger();
			JSONArray arr = new JSONArray();
			if( evma != null ) {
				Map<String, Integer> states = evma.getMapStates();
				if( states != null ) {
					for(String id : states.keySet()) {
						int state = states.get(id);
						JSONObject obj = new JSONObject();
						obj.put("id", id);
						obj.put("state", state);
						obj.put("langstate", Language.state(state));
						arr.add(obj);
					}
				}
			}

			String result = arr.toString();

			ArrayList<String> contentType = new ArrayList<String>();
			contentType.add("text/plain; charset=utf-8");
			ex.getResponseHeaders().put("Content-Type", contentType);
			ex.sendResponseHeaders(200, result.getBytes().length);

			OutputStream os = ex.getResponseBody();
			os.write(result.getBytes());
			os.flush();
			os.close();
		} else {
			System.out.println("[Web] Unknown data request: " + path);
			ex.sendResponseHeaders(404, -1);
		}
		//ex.close();
	}

}
