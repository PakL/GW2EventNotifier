package de.pakldev.gw2evno;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class InterestingEvents {

	private static Preferences preference;

	public static String[] eventIds = new String[]{
		"568A30CF-8512-462F-9D67-647D69BEFAED",
		"03BF176A-D59F-49CA-A311-39FC6F533F2F",
		"0464CB9E-1848-4AAA-BA31-4779A959DD71"
	};

	public static void loadInterestingEvents() {
		try {
			InterestingEvents.preference = Preferences.userRoot().node("/de/pakldev/gw2evno/InterestingEvents");

			String eventsJSON = InterestingEvents.preference.get("eventids", "[\"568A30CF-8512-462F-9D67-647D69BEFAED\",\"03BF176A-D59F-49CA-A311-39FC6F533F2F\",\"0464CB9E-1848-4AAA-BA31-4779A959DD71\"]");
			JSONArray events = (JSONArray) JSONValue.parse(eventsJSON);
			List<String> e = new ArrayList<String>();
			for(Object o : events.toArray() ) {
				String s = (String)o;
				e.add(s);
			}
			InterestingEvents.eventIds = e.toArray(new String[0]);

			System.out.println("[Config] Interesting events loaded with no error.");
		} catch(Exception ex) {
			System.err.println("[Config] Error loading interesting events: "+ex.getMessage());
		}
	}

	public static void saveInterestingEvents() {
		try {
			JSONArray events = new JSONArray();
			for(String id : eventIds)
				events.add(id);

			InterestingEvents.preference.put("eventids", events.toString());

			System.out.println("[Config] Interesting events saved with no error.");
		} catch(Exception ex) {
			System.err.println("[Config] Error saving interesting events: "+ex.getMessage());
		}
	}

}
