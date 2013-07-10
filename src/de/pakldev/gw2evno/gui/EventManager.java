package de.pakldev.gw2evno.gui;

import de.pakldev.gw2evno.Configuration;
import de.pakldev.gw2evno.GW2EvNoMain;
import de.pakldev.gw2evno.InterestingEvents;
import de.pakldev.gw2evno.Language;
import de.pakldev.gw2evno.gw2api.EventNames;
import de.pakldev.gw2evno.gw2api.Events;
import de.pakldev.gw2evno.web.WebSocketServer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager implements Runnable, ActionListener, ChangeListener {

	private final GW2EvNoMain main;
	private final StartupFrame sf;
	private final DialogManager dm;

	private int world = 0;
	private int map = 0;
	private int oldtimeout = 15;
	private int newtimeout = 15;
	private boolean interestingOnly = false;

	private Map<String, Integer> lastEventState = null;
	private Map<String, Integer> interestingState = null;

	private Thread thisThread = null;
	private boolean running = false;
	private long lastUpdate = System.currentTimeMillis();

	public EventManager(GW2EvNoMain main, StartupFrame sf) {
		this.main = main;
		this.sf = sf;
		dm = new DialogManager();

		world = sf.getWorldIndex();
		map = sf.getMapIndex();
		oldtimeout = Configuration.timeout;
		newtimeout = Configuration.timeout;
		interestingOnly = Configuration.interestingOnly;
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

					WebSocketServer ws = main.web.getWebSocket();
					if( ws != null ) { ws.broadcastSettings();ws.broadcastEvents(); }
					System.out.println("[GUI] World changed to " + main.worlds.getWorld(main.worlds.getWorldIdAt(world)));
				}
			} else if( e.getActionCommand().equalsIgnoreCase("mapChanged") ) {
				if( map != cmbBox.getSelectedIndex() ) {
					int newMap = cmbBox.getSelectedIndex();
					lastEventState = Events.getEvents(main.worlds.getWorldIdAt(world), main.maps.getMapIdAt(newMap));
					map = newMap;
					Configuration.mapIndex = map;
					Configuration.saveConfig();
					lastUpdate = System.currentTimeMillis();

					WebSocketServer ws = main.web.getWebSocket();
					if( ws != null ) { ws.broadcastSettings();ws.broadcastEvents(); }
					System.out.println("[GUI] Map changed to " + main.maps.getMap(main.maps.getMapIdAt(map)));
				}
			}
		}
	}

	public void start() {
		if( !running ) {
			running = true;
			lastUpdate = System.currentTimeMillis();
			thisThread = new Thread(this);
			thisThread.start();
			sf.setApplicationState(SFMenu.APPLICATION_STATE_RUNNING);
		}
	}

	public void stop() {
		if( running ) {
			running = false;
			thisThread.interrupt();
			dm.clear();
			sf.setApplicationState(SFMenu.APPLICATION_STATE_PAUSED);
		}
	}

	@Override
	public void run() {
		if( oldtimeout < 10 ) { oldtimeout = 10; newtimeout = 10; }
		int timeout = (oldtimeout * 1000);

		int oldSec = 0;

		refreshStatus(0, timeout);

		System.out.println("[System] Checking for new states");
		this.checkForNewStates();

		while(running) {
			int timeFinished = (int) (System.currentTimeMillis()-lastUpdate);
			refreshStatus(timeFinished, timeout);
			if( timeFinished >= timeout && running ) {
				refreshStatus(-1, 0);
				System.out.println("[System] Checking for new states");
				this.checkForNewStates();
				lastUpdate = System.currentTimeMillis();
				if( oldtimeout != newtimeout ) {
					if( newtimeout < 10 ) newtimeout = 10;
					Configuration.timeout = newtimeout;
					Configuration.saveConfig();
					timeout = (newtimeout * 1000);
					oldtimeout = newtimeout;
					timeFinished = 0;
					refreshStatus(timeFinished, timeout);
				}
			}

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				running = false;
				break;
			}
		}
		sf.setStatusAndProgress("Refreshing paused or stopped", 1, 1);
	}

	private int oldSec = 0;
	private void refreshStatus(int timeFinished, int timeout) {
		if( timeFinished < 0 || timeout == 0 ) {
			sf.setStatusAndProgress("Refreshing status", -1);
		} else {
			int newSec = (int)Math.ceil(((float) timeout - (float) timeFinished) /1000f  );
			if( newSec != oldSec ) {
				oldSec = newSec;
				sf.setStatusAndProgress(oldSec + " seconds until refresh", timeFinished, timeout);
			} else {
				sf.setMaximumProgress(timeout);
				sf.setProgress(timeFinished);
			}
		}
	}

	private void checkForNewStates() {
		List<String> shown = new ArrayList<String>();
		for(String eventId : InterestingEvents.eventIds) {
			int newState = Events.getEvent(main.worlds.getWorldIdAt(world), eventId);
			int oldState = -1;
			if( interestingState == null ) {
				oldState = newState;
				interestingState = new HashMap<String, Integer>();
				interestingState.put(eventId, oldState);
			}
			if( interestingState.containsKey(eventId) ) {
				oldState = interestingState.get(eventId);
			}

			if( oldState != newState ) {
				String eventName = main.events.getName(eventId);
				if( EventNames.getEventType(eventId) == Events.TYPE_SKILL ) continue;

				if( newState == Events.STATE_WARMUP ) {
					dm.newDialog(eventId, "<html><center><font color=\"#FFFFFF\"><b>" + eventName + "</b><br />" + Language.warmup() + "</font></center></html>", EventNames.getIcon(eventId), true);
				} else if( newState == Events.STATE_PREPARATION ) {
					dm.newDialog(eventId, "<html><center><font color=\"#FFFFFF\"><b>" + eventName + "</b><br />" + Language.preparation() + "</font></center></html>", EventNames.getIcon(eventId), true);
				} else if( newState == Events.STATE_ACTIVE ) {
					dm.newDialog(eventId, "<html><center><font color=\"#FFFFFF\"><b>" + eventName + "</b><br />" + Language.active() + "</font></center></html>", EventNames.getIcon(eventId), true);
				} else if( newState == Events.STATE_INACTIVE ) {
					dm.newDialog(eventId, "<html><center><font color=\"#FFFFFF\"><b>" + eventName + "</b><br />" + Language.inactive() + "</font></center></html>", EventNames.getIcon(eventId), true);
				}
				shown.add(eventId);

				interestingState.put(eventId, newState);
			}

		}


		Map<String, Integer> newStates = Events.getEvents(main.worlds.getWorldIdAt(world), main.maps.getMapIdAt(map));
		if( !interestingOnly ) {
			if( lastEventState == null ) {
				lastEventState = newStates;
			}
			for(String eventId : newStates.keySet()) {
				if( lastEventState.containsKey(eventId) ) {
					int oldState = lastEventState.get(eventId);
					int newState = newStates.get(eventId);
					if( oldState != newState ) {
						String eventName = main.events.getName(eventId);
						if( eventName.startsWith(Language.skillChallenge()) ) continue;

						if( !shown.contains(eventId) ) {
							if( newState == Events.STATE_WARMUP ) {
								dm.newDialog(eventId, "<html><center><b>" + eventName + "</b><br />" + Language.warmup() + "</center></html>", EventNames.getIcon(eventId), false);
							} else if( newState == Events.STATE_PREPARATION ) {
								dm.newDialog(eventId, "<html><center><b>" + eventName + "</b><br />" + Language.preparation() + "</center></html>", EventNames.getIcon(eventId), false);
							} else if( newState == Events.STATE_ACTIVE ) {
								dm.newDialog(eventId, "<html><center><b>" + eventName + "</b><br />" + Language.active() + "</center></html>", EventNames.getIcon(eventId), false);
							} else if( newState == Events.STATE_INACTIVE ) {
								dm.newDialog(eventId, "<html><center><b>" + eventName + "</b><br />" + Language.inactive() + "</center></html>", EventNames.getIcon(eventId), false);
							}
						}
					}
				}
			}
		}
		lastEventState = newStates;

		WebSocketServer ws = main.web.getWebSocket();
		if( ws != null ) {
			ws.broadcastEvents();
			ws.broadcastInterestingevents();
		}

		sf.setMapEvents(lastEventState.size());
		sf.setInterestingEvents(interestingState.size());
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if( e.getSource() instanceof JSpinner ) {
			JSpinner spinner = (JSpinner) e.getSource();
			newtimeout = (Integer) spinner.getValue();
		} else if( e.getSource() instanceof JCheckBox ) {
			JCheckBox check = (JCheckBox) e.getSource();
			WebSocketServer ws = main.web.getWebSocket();
			if( check.isSelected() && !interestingOnly ) {
				interestingOnly = true;
				Configuration.interestingOnly = true;
				Configuration.saveConfig();

				if( ws != null ) ws.broadcastSettings();
			} else if( !check.isSelected() && interestingOnly ) {
				interestingOnly = false;
				Configuration.interestingOnly = false;
				Configuration.saveConfig();

				if( ws != null ) ws.broadcastSettings();
			}

		}
	}

	public Map<String, Integer> getMapStates() {
		return this.lastEventState;
	}

	public Map<String, Integer> getInterestingStates() {
		return this.interestingState;
	}
}
