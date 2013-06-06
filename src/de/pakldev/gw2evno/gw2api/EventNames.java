package de.pakldev.gw2evno.gw2api;

import de.pakldev.gw2evno.GW2EvNoMain;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class EventNames {

	public static final String LANG_EN = "en";
	public static final String LANG_DE = "de";
	public static final String LANG_FR = "fr";
	public static final String LANG_ES = "es";

	private Map<String, String> eventNames = new HashMap<String, String>();
	private static Map<String, Image> eventIcons = new HashMap<String, Image>();

	public EventNames(String language) {
		try {
			String eventNamesStr = GW2EvNoMain.loadURL("https://api.guildwars2.com/v1/event_names.json?lang="+language);
			JSONArray eventNames_ = (JSONArray)JSONValue.parse(eventNamesStr);
			for(Object obj : eventNames_) {
				JSONObject o = (JSONObject)obj;
				if( o.containsKey("id") && o.containsKey("name") ) {
					String oid = (String) o.get("id");
					String oname = (String) o.get("name");

					eventNames.put(oid, oname);
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void guessEventIcons() {
		try {
			String eventNamesStr = GW2EvNoMain.loadURL("https://api.guildwars2.com/v1/event_names.json?lang=en");
			JSONArray eventNames_ = (JSONArray)JSONValue.parse(eventNamesStr);
			for(Object obj : eventNames_) {
				JSONObject o = (JSONObject)obj;
				if( o.containsKey("id") && o.containsKey("name") ) {
					String oid = (String) o.get("id");
					String oname = (String) o.get("name");

					oname = oname.toLowerCase();

					if(
						oname.startsWith("stop") ||
						oname.startsWith("defeat") ||
						oname.startsWith("clear") ||
						oname.startsWith("draw out") ||
						oname.startsWith("beat") ||
						oname.startsWith("attack")
					) {
						eventIcons.put(oid, Events.ICON_ATTACK);
					} else if(
						oname.startsWith("capture") ||
						oname.startsWith("seize control") ||
						oname.startsWith("hold") ||
						oname.startsWith("fend off") ||
						oname.startsWith("subdue and capture")
					) {
						eventIcons.put(oid, Events.ICON_CAPTURE);
					} else if(
						oname.startsWith("collect") ||
						oname.startsWith("gather") ||
						oname.startsWith("cull") ||
						oname.startsWith("catch") ||
						oname.startsWith("retake") ||
						oname.startsWith("regain")
					) {
						eventIcons.put(oid, Events.ICON_COLLECT);
					} else if(
						oname.startsWith("kill") ||
						oname.startsWith("slay") ||
						oname.startsWith("hunt")
					) {
						eventIcons.put(oid, Events.ICON_KILL);
					} else if(
						oname.startsWith("rescue") ||
						oname.startsWith("defend") ||
						oname.startsWith("escort") ||
						oname.startsWith("help") ||
						oname.startsWith("patrol") ||
						oname.startsWith("support") ||
						oname.startsWith("join") ||
						oname.startsWith("save")
					) {
						eventIcons.put(oid, Events.ICON_PROTECT);
					} else {
						eventIcons.put(oid, Events.ICON_OBJECT);
					}
				}
			}
		} catch (Exception e) {}
	}

	public static Image getIcon(String id) {
		if( eventIcons.containsKey(id) ) {
			return eventIcons.get(id);
		}
		return Events.ICON_OBJECT;
	}

	public String getName(String id) {
		if( eventNames.containsKey(id) ) {
			return eventNames.get(id);
		}
		return id;
	}

}
