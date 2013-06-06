package de.pakldev.gw2evno.gw2api;


import de.pakldev.gw2evno.GW2EvNoMain;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.swing.*;
import java.util.Map;
import java.util.TreeMap;

public class WorldNames {

	private TreeMap<String, String> worldNames = new TreeMap<String, String>();

	public WorldNames() {
		try {
			String worldNamesStr = GW2EvNoMain.loadURL("https://api.guildwars2.com/v1/world_names.json?lang=de");
			JSONArray worldNames_ = (JSONArray) JSONValue.parse(worldNamesStr);
			for(Object obj : worldNames_) {
				JSONObject o = (JSONObject)obj;
				if( o.containsKey("id") && o.containsKey("name") ) {
					String oid = (String) o.get("id");
					String oname = (String) o.get("name");

					worldNames.put(oid, oname);
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
		}
	}

	public Map<String, String> getWorlds() {
		return worldNames;
	}

	public String getWorldIdAt(int index) {
		int i = 0;
		for(String key : worldNames.keySet()) {
			if( i == index ) return key;
			i++;
		}
		return "";
	}

}
