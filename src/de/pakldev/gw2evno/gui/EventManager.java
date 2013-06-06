package de.pakldev.gw2evno.gui;

import de.pakldev.gw2evno.Configuration;
import de.pakldev.gw2evno.GW2EvNoMain;
import de.pakldev.gw2evno.Language;
import de.pakldev.gw2evno.gw2api.EventNames;
import de.pakldev.gw2evno.gw2api.Events;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class EventManager implements Runnable, ActionListener {

	private final GW2EvNoMain main;
	private final StartupFrame sf;
	private final DialogManager dm;

	private int world = 0;
	private int map = 0;

	private Map<String, Integer> lastEventState = null;

	private Thread thisThread = null;
	private boolean running = true;
	private long lastUpdate = System.currentTimeMillis();

	public EventManager(GW2EvNoMain main, StartupFrame sf) {
		this.main = main;
		this.sf = sf;
		dm = new DialogManager();

		world = sf.getWorldIndex();
		map = sf.getMapIndex();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() instanceof JComboBox ) {
			JComboBox cmbBox = (JComboBox) e.getSource();
			if( e.getActionCommand().equalsIgnoreCase("worldChanged") ) {
				if( world != cmbBox.getSelectedIndex() ) {
					int newWorld = cmbBox.getSelectedIndex();
					lastEventState = Events.getEvents(main.worlds.getWorldIdAt(newWorld), main.maps.getMapIdAt(map));
					world = newWorld;
					Configuration.worldIndex = world;
					Configuration.saveConfig();
					lastUpdate = System.currentTimeMillis();
				}
			} else if( e.getActionCommand().equalsIgnoreCase("mapChanged") ) {
				if( map != cmbBox.getSelectedIndex() ) {
					int newMap = cmbBox.getSelectedIndex();
					lastEventState = Events.getEvents(main.worlds.getWorldIdAt(world), main.maps.getMapIdAt(newMap));
					map = newMap;
					Configuration.mapIndex = map;
					Configuration.saveConfig();
					lastUpdate = System.currentTimeMillis();
				}
			}
		}
	}

	public void start() {
		thisThread = new Thread(this);
		thisThread.start();
	}

	public void stop() {
		running = false;
		thisThread.interrupt();
	}

	@Override
	public void run() {
		sf.setProgressMax(15000);
		sf.setProgressValue(0);

		while(running) {
			int timeFinished = (int) (System.currentTimeMillis()-lastUpdate);
			sf.setProgressValue(timeFinished);
			if( timeFinished >= 15000 ) {
				this.checkForNewStates();
				lastUpdate = System.currentTimeMillis();
			}

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				running = false;
				break;
			}
		}
	}

	private void checkForNewStates() {
		Map<String, Integer> newStates = Events.getEvents(main.worlds.getWorldIdAt(world), main.maps.getMapIdAt(map));
		if( lastEventState == null ) {
			lastEventState = newStates;
			return;
		}
		for(String eventId : newStates.keySet()) {
			if( lastEventState.containsKey(eventId) ) {
				int oldState = lastEventState.get(eventId);
				int newState = newStates.get(eventId);
				if( oldState != newState ) {
					String eventName = main.events.getName(eventId);
					if( eventName.startsWith(Language.skillChallenge()) ) continue;

					if( newState == Events.STATE_WARMUP ) {
						dm.newDialog("<html><center><b>" + eventName + "</b><br />" + Language.warmup() + "</center></html>", EventNames.getIcon(eventId));
					} else if( newState == Events.STATE_PREPARATION ) {
						dm.newDialog("<html><center><b>" + eventName + "</b><br />" + Language.preparation() + "</center></html>", EventNames.getIcon(eventId));
					} else if( newState == Events.STATE_ACTIVE ) {
						dm.newDialog("<html><center><b>" + eventName + "</b><br />" + Language.active() + "</center></html>", EventNames.getIcon(eventId));
					}
				}
			}
		}

		lastEventState = newStates;
	}

}
