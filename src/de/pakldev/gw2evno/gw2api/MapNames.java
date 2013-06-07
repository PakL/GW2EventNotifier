package de.pakldev.gw2evno.gw2api;


import de.pakldev.gw2evno.Configuration;
import de.pakldev.gw2evno.GW2EvNoMain;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.swing.*;
import java.util.Map;
import java.util.TreeMap;

public class MapNames {

	public static final String LANG_EN = "en";
	public static final String LANG_DE = "de";
	public static final String LANG_FR = "fr";
	public static final String LANG_ES = "es";

	private TreeMap<String, String> mapNames = new TreeMap<String, String>();

	public MapNames(String language) {
		try {
			String mapNamesStr = GW2EvNoMain.loadURL("https://api.guildwars2.com/v1/map_names.json?lang="+language);
			JSONArray mapNames_ = (JSONArray) JSONValue.parse(mapNamesStr);
			for(Object obj : mapNames_) {
				JSONObject o = (JSONObject)obj;
				if( o.containsKey("id") && o.containsKey("name") ) {
					String oid = (String) o.get("id");
					String oname = (String) o.get("name");

					mapNames.put(oid, oname);
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
		}
	}

	public Map<String, String> getMaps() {
		return mapNames;
	}

	public String getMap(String id) {
		for(String key : mapNames.keySet()) {
			if( key.equalsIgnoreCase(id) )
				return mapNames.get(key);
		}
		return "";
	}

	public String searchMap(String search) {
		search = search.toLowerCase();
		for(String key : mapNames.keySet()) {
			String name = mapNames.get(key).toLowerCase();
			if( name.startsWith(search) ) return key;
			String[] splits = name.split(" |\\-");
			for(String split : splits) {
				if( split.startsWith(search) ) return key;
			}
			if( name.contains(search) ) return key;

			if( name.replaceAll("\\-", " ").startsWith(search) ) return key;
			if( name.replaceAll("\\-", " ").contains(search			) ) return key;

			if( name.replaceAll(" ", "").startsWith(search) ) return key;
			if( name.replaceAll(" ", "").contains(search) ) return key;

			if( name.replaceAll("\\-| ", "").startsWith(search) ) return key;
			if( name.replaceAll("\\-| ", "").contains(search) ) return key;
		}

		return "";
	}

	public String getMapIdAt(int index) {
		int i = 0;
		for(String key : mapNames.keySet()) {
			if( i == index ) return key;
			i++;
		}
		return "";
	}

	public int getIndexByMapId(String id) {
		int i = 0;
		for(String key : mapNames.keySet()) {
			if( key.equalsIgnoreCase(id) ) return i;
			i++;
		}

		return Configuration.mapIndex;
	}

}
