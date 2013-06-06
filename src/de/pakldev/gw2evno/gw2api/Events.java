package de.pakldev.gw2evno.gw2api;

import de.pakldev.gw2evno.GW2EvNoMain;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.HashMap;
import java.util.Map;

public class Events {

	public final static int STATE_SUCCESS = 1;
	public final static int STATE_FAIL = 2;
	public final static int STATE_ACTIVE = 3;
	public final static int STATE_WARMUP = 4;
	public final static int STATE_PREPARATION = 5;

	public static Map<String, Integer> getEvents(String worldId, String mapId) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		if( !worldId.isEmpty() && !mapId.isEmpty() ) {
			try {
				String eventsStr = GW2EvNoMain.loadURL("https://api.guildwars2.com/v1/events.json?world_id="+worldId+"&map_id="+mapId);
				JSONObject eventsObj = (JSONObject) JSONValue.parse(eventsStr);
				if( eventsObj.containsKey("events") ) {
					JSONArray events = (JSONArray) eventsObj.get("events");
					for(Object obj : events) {
						JSONObject o = (JSONObject) obj;
						if( o.containsKey("event_id") && o.containsKey("state") ) {
							String eventId = (String) o.get("event_id");
							String stateStr = (String)o.get("state");
							int state = Events.STATE_FAIL;
							if( stateStr.equalsIgnoreCase("Success") ) state = Events.STATE_SUCCESS;
							else if( stateStr.equalsIgnoreCase("Fail") ) state = Events.STATE_FAIL;
							else if( stateStr.equalsIgnoreCase("Active") ) state = Events.STATE_ACTIVE;
							else if( stateStr.equalsIgnoreCase("Warmup") ) state = Events.STATE_WARMUP;
							else if( stateStr.equalsIgnoreCase("Preparation") ) state = Events.STATE_PREPARATION;

							result.put(eventId, state);
						}
					}
				}
			} catch (Exception e) {}
		}
		return result;
	}

}
