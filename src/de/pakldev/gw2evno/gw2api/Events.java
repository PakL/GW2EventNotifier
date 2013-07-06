package de.pakldev.gw2evno.gw2api;

import de.pakldev.gw2evno.GW2EvNoMain;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Events {

	public final static int STATE_SUCCESS = 1;
	public final static int STATE_FAIL = 2;
	public final static int STATE_ACTIVE = 3;
	public final static int STATE_WARMUP = 4;
	public final static int STATE_PREPARATION = 5;
	public final static int STATE_INACTIVE = 6;

	public final static int TYPE_DYNAMIC = 10;
	public final static int TYPE_GROUP = 11;
	public final static int TYPE_SKILL = 12;

	public static Image ICON_ATTACK;
	public static Image ICON_CAPTURE;
	public static Image ICON_COLLECT;
	public static Image ICON_FIST;
	public static Image ICON_KILL;
	public static Image ICON_OBJECT;
	public static Image ICON_PROTECT;
	public static Image ICON_SKILL;
	public static Image ICON_STAR;

	public static void loadImages() throws IOException {
		Events.ICON_ATTACK = ImageIO.read(GW2EvNoMain.class.getResourceAsStream("res/attack_icon.png"));
		Events.ICON_CAPTURE = ImageIO.read(GW2EvNoMain.class.getResourceAsStream("res/capture_icon.png"));
		Events.ICON_COLLECT = ImageIO.read(GW2EvNoMain.class.getResourceAsStream("res/collect_icon.png"));
		Events.ICON_FIST = ImageIO.read(GW2EvNoMain.class.getResourceAsStream("res/fist_icon.png"));
		Events.ICON_KILL = ImageIO.read(GW2EvNoMain.class.getResourceAsStream("res/kill_icon.png"));
		Events.ICON_OBJECT = ImageIO.read(GW2EvNoMain.class.getResourceAsStream("res/object_icon.png"));
		Events.ICON_PROTECT = ImageIO.read(GW2EvNoMain.class.getResourceAsStream("res/protect_icon.png"));
		Events.ICON_SKILL = ImageIO.read(GW2EvNoMain.class.getResourceAsStream("res/skill_icon.png"));
		Events.ICON_STAR = ImageIO.read(GW2EvNoMain.class.getResourceAsStream("res/star_icon.png"));
	}

	public static Map<String, Integer> getEvents(String worldId, String mapId) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		if( !worldId.isEmpty() && !mapId.isEmpty() ) {
			try {
				String eventsStr = GW2EvNoMain.loadURL("https://api.guildwars2.com/v1/events.json?world_id="+worldId+"&map_id="+mapId);
				JSONObject eventsObj = (JSONObject) JSONValue.parse(eventsStr);
				if( eventsObj != null ) {
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
								else if( stateStr.equalsIgnoreCase("Inactive") ) state = Events.STATE_INACTIVE;

								result.put(eventId, state);
							}
						}
					}
				}
			} catch (Exception e) {}
		}
		return result;
	}

	public static int getEvent(String worldId, String eventId) {
		int result = STATE_FAIL;
		if( !worldId.isEmpty() && !eventId.isEmpty() ) {
			try {
				String eventsStr = GW2EvNoMain.loadURL("https://api.guildwars2.com/v1/events.json?world_id="+worldId+"&event_id="+eventId);
				JSONObject eventsObj = (JSONObject) JSONValue.parse(eventsStr);
				if( eventsObj != null  ) {
					if( eventsObj.containsKey("events") ) {
						JSONArray events = (JSONArray) eventsObj.get("events");
						for(Object obj : events) {
							JSONObject o = (JSONObject) obj;
							if( o.containsKey("event_id") && o.containsKey("state") ) {
								if( ((String) o.get("event_id")).equalsIgnoreCase(eventId) ) {
									String stateStr = (String)o.get("state");
									int state = Events.STATE_FAIL;
									if( stateStr.equalsIgnoreCase("Success") ) state = Events.STATE_SUCCESS;
									else if( stateStr.equalsIgnoreCase("Fail") ) state = Events.STATE_FAIL;
									else if( stateStr.equalsIgnoreCase("Active") ) state = Events.STATE_ACTIVE;
									else if( stateStr.equalsIgnoreCase("Warmup") ) state = Events.STATE_WARMUP;
									else if( stateStr.equalsIgnoreCase("Preparation") ) state = Events.STATE_PREPARATION;

									result = state;
									break;
								}
							}
						}
					}
				}
			} catch (Exception e) {}
		}
		return result;
	}

}
