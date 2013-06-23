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

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataHandler implements HttpHandler {

	private GW2EvNoMain main;

	public DataHandler(GW2EvNoMain main) {
		this.main = main;
	}

	@Override
	public void handle(HttpExchange ex) throws IOException {
		String path = ex.getRequestURI().getPath();

		Map<String, String> q = new HashMap<String, String>();
		String query = ex.getRequestURI().getQuery();
		if( query != null && !query.isEmpty() ) {
			String[] s = query.split("&");
			for(String i : s) {
				String[] m = i.split("=", 2);
				if( m.length == 2 ) {
					q.put(m[0], m[1]);
				}
			}
		}

		//System.out.println("[Web] Requesting data: " + path);

		Object res = new JSONArray();

		if( path.equalsIgnoreCase("/data/worlds/") || path.equalsIgnoreCase("/data/worlds") ) {
			res = new JSONObject();
			if( main.worlds != null ) {
				for(String id : main.worlds.getWorlds().keySet()) {
					((JSONObject)res).put(id, main.worlds.getWorld(id));
				}
			}
		} else if( path.equalsIgnoreCase("/data/maps/") || path.equalsIgnoreCase("/data/maps") ) {
			res = new JSONObject();
			if( main.maps != null ) {
				for(String id : main.maps.getMaps().keySet()) {
					((JSONObject)res).put(id, main.maps.getMap(id));
				}
			}
		} else if( path.equalsIgnoreCase("/data/events/") || path.equalsIgnoreCase("/data/events") ) {
			res = new JSONObject();
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


					((JSONObject)res).put(id, details);
				}
			}
		} else if( path.equalsIgnoreCase("/data/mapevents") || path.equalsIgnoreCase("/data/mapevents/") ) {
			EventManager evma = main.sf.getEventManger();
			res = new JSONArray();
			if( evma != null ) {
				Map<String, Integer> states = evma.getMapStates();
				if( states != null ) {
					for(String id : states.keySet()) {
						int state = states.get(id);
						JSONObject obj = new JSONObject();
						obj.put("id", id);
						obj.put("state", state);
						obj.put("langstate", Language.state(state));
						((JSONArray)res).add(obj);
					}
				}
			}
		} else if( path.equalsIgnoreCase("/data/interestingevents") || path.equalsIgnoreCase("/data/interestingevents/") ) {
			EventManager evma = main.sf.getEventManger();
			res = new JSONArray();
			if( evma != null ) {
				Map<String, Integer> states = evma.getInterestingStates();
				if( states != null ) {
					for(String id : states.keySet()) {
						int state = states.get(id);
						JSONObject obj = new JSONObject();
						obj.put("id", id);
						obj.put("state", state);
						obj.put("langstate", Language.state(state));
						((JSONArray)res).add(obj);
					}
				}
			}
		} else if( path.equalsIgnoreCase("/data/settings") || path.equalsIgnoreCase("/data/settings/") ) {
			res = new JSONObject();
			((JSONObject)res).put("world", main.worlds.getWorldIdAt(main.sf.getWorldIndex()));
			((JSONObject)res).put("map", main.maps.getMapIdAt(main.sf.getMapIndex()));
			((JSONObject)res).put("interestingonly", main.sf.isInterestingOnly());
		} else if( path.equalsIgnoreCase("/data/setworld") || path.equalsIgnoreCase("/data/setworld/") ) {
			if( q.containsKey("world") ) {
				main.sf.setWorldIndex(main.worlds.getIndexByWorldId(q.get("world")));
				((JSONArray)res).add("done");
			}
		} else if( path.equalsIgnoreCase("/data/setmap") || path.equalsIgnoreCase("/data/setmap/") ) {
			if( q.containsKey("map") ) {
				main.sf.setMapIndex(main.maps.getIndexByMapId(q.get("map")));
				((JSONArray)res).add("done");
			}
		} else if( path.equalsIgnoreCase("/data/setinterestingonly") || path.equalsIgnoreCase("/data/setinterestingonly/") ) {
			if( q.containsKey("interestingonly") ) {
				boolean io = false;
				if( q.get("interestingonly").equalsIgnoreCase("true") || q.get("interestingonly").equalsIgnoreCase("1") ) {
					io = true;
				}
				main.sf.setInterestinOnly(io);
				((JSONArray)res).add("done");
			}
		} else {
			System.out.println("[Web] Unknown data request: " + path);
			ex.sendResponseHeaders(404, -1);
			return;
		}


		String result = res.toString();

		ArrayList<String> contentType = new ArrayList<String>();
		contentType.add("text/plain; charset=utf-8");
		ex.getResponseHeaders().put("Content-Type", contentType);
		ex.sendResponseHeaders(200, result.getBytes().length);

		OutputStream os = ex.getResponseBody();
		os.write(result.getBytes());
		os.flush();
		os.close();
		ex.close();
	}

}
