package de.pakldev.gw2evno.gw2api;


import de.pakldev.gw2evno.GW2EvNoMain;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.swing.*;
import java.util.Map;
import java.util.TreeMap;

public class MapNames {

	private TreeMap<String, String> mapNames = new TreeMap<String, String>();

	public MapNames() {
		try {
			String mapNamesStr = GW2EvNoMain.loadURL("https://api.guildwars2.com/v1/map_names.json?lang=de");
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

	public String getMapIdAt(int index) {
		int i = 0;
		for(String key : mapNames.keySet()) {
			if( i == index ) return key;
			i++;
		}
		return "";
	}

}
