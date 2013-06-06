package de.pakldev.gw2evno.gw2api;

import de.pakldev.gw2evno.GW2EvNoMain;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class EventNames {

	public static final String LANG_EN = "en";
	public static final String LANG_DE = "de";
	public static final String LANG_FR = "fr";
	public static final String LANG_ES = "es";

	private Map<String, String> eventNames = new HashMap<String, String>();

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

	public String getName(String id) {
		if( eventNames.containsKey(id) ) {
			return eventNames.get(id);
		}
		return id;
	}

}
