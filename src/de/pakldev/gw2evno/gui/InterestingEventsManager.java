package de.pakldev.gw2evno.gui;

import de.pakldev.gw2evno.GW2EvNoMain;
import de.pakldev.gw2evno.InterestingEvents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InterestingEventsManager extends JDialog {

	private final GW2EvNoMain main;
	private SpringLayout layout = new SpringLayout();

	private JTextField txtSearch = new JTextField();

	private JList<EventEntry> listActive = new JList<EventEntry>();
	private JList<EventEntry> listInactive = new JList<EventEntry>();
	private FilteredListModel filteredListModel = new FilteredListModel();

	public InterestingEventsManager(GW2EvNoMain main) {
		this.main = main;
		this.setModal(true);
		this.setTitle("Interesting Events");

		Container contentPane = this.getContentPane();
		contentPane.setLayout(layout);

		JScrollPane scrollActive = new JScrollPane(listActive);

		layout.putConstraint(SpringLayout.NORTH, scrollActive, 5, SpringLayout.SOUTH, txtSearch);
		layout.putConstraint(SpringLayout.WEST, scrollActive, 5, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.EAST, scrollActive, -5, SpringLayout.HORIZONTAL_CENTER, contentPane);
		layout.putConstraint(SpringLayout.SOUTH, scrollActive, -5, SpringLayout.SOUTH, contentPane);

		JScrollPane scrollInactive = new JScrollPane(listInactive);

		layout.putConstraint(SpringLayout.NORTH, txtSearch, 5, SpringLayout.NORTH, contentPane);
		layout.putConstraint(SpringLayout.WEST, txtSearch, 0, SpringLayout.WEST, scrollInactive);
		layout.putConstraint(SpringLayout.EAST, txtSearch, 0, SpringLayout.EAST, scrollInactive);
		layout.putConstraint(SpringLayout.SOUTH, txtSearch, 20, SpringLayout.NORTH, txtSearch);

		layout.putConstraint(SpringLayout.NORTH, scrollInactive, 5, SpringLayout.SOUTH, txtSearch);
		layout.putConstraint(SpringLayout.WEST, scrollInactive, 5, SpringLayout.HORIZONTAL_CENTER, contentPane);
		layout.putConstraint(SpringLayout.EAST, scrollInactive, -5, SpringLayout.EAST, contentPane);
		layout.putConstraint(SpringLayout.SOUTH, scrollInactive, -5, SpringLayout.SOUTH, contentPane);

		contentPane.add(txtSearch);
		contentPane.add(scrollActive);
		contentPane.add(scrollInactive);

		txtSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if( filteredListModel != null )
					filteredListModel.setFilter(txtSearch.getText().toLowerCase());
			}
		});


		listInactive.setModel(filteredListModel);

		listInactive.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if( e.getClickCount() == 2 ) {
					EventEntry ee = listInactive.getSelectedValue();
					addToActive(ee);
				}
			}
		});
		listActive.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if( e.getClickCount() == 2 ) {
					EventEntry ee = listActive.getSelectedValue();
					removeFromActive(ee);
				}
			}
		});
	}

	@Override
	public void setVisible(boolean visible) {
		this.setLocation(main.sf.getX(), main.sf.getY());
		this.setSize(main.sf.getWidth(), main.sf.getHeight());
		updateLists();
		super.setVisible(visible);
	}

	private void addToActive(EventEntry entry) {
		String[] newEntrys = new String[InterestingEvents.eventIds.length+1];
		int i = 0;
		for(String e : InterestingEvents.eventIds) {
			newEntrys[i] = e;
			i++;
		}
		newEntrys[i] = entry.getId();

		InterestingEvents.eventIds = newEntrys;
		InterestingEvents.saveInterestingEvents();
		filteredListModel.remove(entry);
		updateLists();
	}
	private void removeFromActive(EventEntry entry) {
		String[] newEntrys = new String[InterestingEvents.eventIds.length-1];
		int i = 0;
		for(String e : InterestingEvents.eventIds) {
			if( !entry.getId().equalsIgnoreCase(e) ) {
				newEntrys[i] = e;
				i++;
			}
		}

		InterestingEvents.eventIds = newEntrys;
		InterestingEvents.saveInterestingEvents();
		filteredListModel.add(entry);
		updateLists();
	}

	private void updateLists() {
		EventEntry[] active = new EventEntry[InterestingEvents.eventIds.length];
		for(int i=0;i<active.length;i++) {
			active[i] = new EventEntry(InterestingEvents.eventIds[i], main.events.getName(InterestingEvents.eventIds[i]));
		}
		listActive.setListData(active);

		EventEntry[] inactive = new EventEntry[(main.events.getEvents().size()-active.length)];
		int i = 0;
		for(String id : main.events.getEvents().keySet()) {
			if( !InterestingEventsManager.inArray(active, id) ) {
				inactive[i] = new EventEntry(id, main.events.getName(id));
				i++;
			}
		}

		filteredListModel.setListData(inactive);
	}

	public static boolean inArray(EventEntry[] array, String needle) {
		for(EventEntry e : array) {
			if( e.getId().equalsIgnoreCase(needle) ) {
				return true;
			}
		}

		return false;
	}

	class EventEntry implements Comparable<EventEntry> {

		private final String id;
		private final String name;
		public EventEntry(String id, String name) {
			this.id = id;
			this.name = name;
		}

		public String getId() {
			return id;
		}

		public String toString() {
			return name;
		}

		@Override
		public int compareTo(EventEntry o) {
			return this.toString().compareToIgnoreCase(o.toString());
		}
	}

}
