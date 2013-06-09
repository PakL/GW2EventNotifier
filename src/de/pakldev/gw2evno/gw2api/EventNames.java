package de.pakldev.gw2evno.gw2api;

import de.pakldev.gw2evno.GW2EvNoMain;
import de.pakldev.gw2evno.Language;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class EventNames {

	public static final String LANG_EN = "en";
	public static final String LANG_DE = "de";
	public static final String LANG_FR = "fr";
	public static final String LANG_ES = "es";

	private Map<String, String> eventNames = new HashMap<String, String>();

	private static Map<String, Image> eventIcons = new HashMap<String, Image>();
	private static Map<String, Integer> eventTypes = new HashMap<String, Integer>();

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

	public static void loadEventIcons() {
		try {
			InputStream is = GW2EvNoMain.class.getResourceAsStream("res/eventTypes.json");
			String eventNamesStr = "";
			int r = -1;
			while( (r = is.read()) >= 0 ) {
				int a = is.available();
				byte[] b = new byte[a+1];
				b[0] = (byte) r;
				is.read(b, 1, a);
				eventNamesStr += new String(b);
			}
			is.close();

			JSONArray eventNames_ = (JSONArray)JSONValue.parse(eventNamesStr);
			for(Object obj : eventNames_) {
				JSONObject o = (JSONObject)obj;
				if( o.containsKey("id") && o.containsKey("icon") && o.containsKey("type") ) {
					String oid = (String) o.get("id");
					String icon = (String) o.get("icon");
					String type = (String) o.get("type");

					if( icon.equalsIgnoreCase("boss") ) eventIcons.put(oid, Events.ICON_KILL);
					else if( icon.equalsIgnoreCase("cog") ) eventIcons.put(oid, Events.ICON_OBJECT);
					else if( icon.equalsIgnoreCase("collect") ) eventIcons.put(oid, Events.ICON_COLLECT);
					else if( icon.equalsIgnoreCase("fist") ) eventIcons.put(oid, Events.ICON_FIST);
					else if( icon.equalsIgnoreCase("flag") ) eventIcons.put(oid, Events.ICON_CAPTURE);
					else if( icon.equalsIgnoreCase("shield") ) eventIcons.put(oid, Events.ICON_PROTECT);
					else if( icon.equalsIgnoreCase("star") ) eventIcons.put(oid, Events.ICON_STAR);
					else if( icon.equalsIgnoreCase("swords") ) eventIcons.put(oid, Events.ICON_ATTACK);
					else if( icon.equalsIgnoreCase("wrench") ) eventIcons.put(oid, Events.ICON_OBJECT);
					else if( icon.equalsIgnoreCase("skill") ) eventIcons.put(oid, Events.ICON_SKILL);
					else eventIcons.put(oid, Events.ICON_STAR);

					if( type.equalsIgnoreCase("dynamic") ) eventTypes.put(oid, Events.TYPE_DYNAMIC);
					else if( type.equalsIgnoreCase("group") ) eventTypes.put(oid, Events.TYPE_GROUP);
					else if( type.equalsIgnoreCase("skill") ) eventTypes.put(oid, Events.TYPE_SKILL);
					else eventTypes.put(oid, Events.TYPE_DYNAMIC);

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
			if( eventTypes.containsKey(id) ) {
				if( eventTypes.get(id) == Events.TYPE_GROUP ) {
					return "[" + Language.group() + "] "+ eventNames.get(id);
				}
			}
			return eventNames.get(id);
		}
		return id;
	}

}
