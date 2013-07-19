package de.pakldev.gw2evno.gui;


import javax.swing.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class FilteredListModel extends AbstractListModel {

	private final Set<InterestingEventsManager.EventEntry> source = new TreeSet<InterestingEventsManager.EventEntry>();
	private final ArrayList<Integer> indices = new ArrayList<Integer>();
	private boolean filtering = false;
	private String lastFilter = "";

	public void setFilter(String f) {
		indices.clear();
		if( f.isEmpty() ) filtering = false;
		else {
			filtering = true;
			int i = 0;
			for(InterestingEventsManager.EventEntry ee : source){
				if( ee.toString().toLowerCase().contains(f) ) {
					indices.add(i);
				}
				i++;
			}
		}
		lastFilter = f;
		fireContentsChanged(this, 0, getSize() - 1);
	}

	public void add(InterestingEventsManager.EventEntry entry) {
		source.add(entry);
		setFilter(lastFilter);
	}

	public void remove(InterestingEventsManager.EventEntry entry) {
		source.remove(entry);
		setFilter(lastFilter);
	}

	public void setListData(InterestingEventsManager.EventEntry[] data) {
		source.clear();
		indices.clear();
		filtering = false;
		for(InterestingEventsManager.EventEntry e : data) {
			source.add(e);
		}
		setFilter(lastFilter);
		fireContentsChanged(this, 0, getSize() - 1);
	}

	public int getSize() {
		return (filtering) ? indices.size() : source.size();
	}

	public Object getElementAt(int index) {
		return (filtering) ? source.toArray(new InterestingEventsManager.EventEntry[0])[indices.get(index)] : source.toArray(new InterestingEventsManager.EventEntry[0])[index];
	}
}