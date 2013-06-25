package de.pakldev.gw2evno.gui;

import de.pakldev.gw2evno.Configuration;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class SFReqsettings extends JPanel {

	private final StartupFrame sf;
	private SpringLayout layout = new SpringLayout();

	private JLabel labelWorld = new JLabel("World:");
	private JLabel labelMap = new JLabel("Map:");
	private JLabel labelTimeout = new JLabel("Refresh timeout:");
	private JLabel labelInteresting = new JLabel("Interesting events only:");
	private JComboBox cmbWorld = new JComboBox(new String[]{ "Please wait..." });
	private JComboBox cmbMap = new JComboBox(new String[]{ "Please wait..." });
	private JSpinner spnTimeout = new JSpinner(new SpinnerNumberModel(Configuration.timeout, 10, 3600, 5));
	private JCheckBox cbxInteresting = new JCheckBox();

	public SFReqsettings(StartupFrame startupFrame) {
		this.sf = startupFrame;
		this.setLayout(layout);
		this.setBorder(BorderFactory.createTitledBorder(" Request settings: "));

		layout.putConstraint(SpringLayout.NORTH, labelWorld, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, labelWorld, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.SOUTH, labelWorld, 0, SpringLayout.SOUTH, cmbWorld);
		layout.putConstraint(SpringLayout.EAST, labelWorld, 130, SpringLayout.WEST, labelWorld);
		this.add(labelWorld);

		cmbWorld.setEnabled(false); cmbWorld.setActionCommand("worldChanged");
		layout.putConstraint(SpringLayout.NORTH, cmbWorld, 0, SpringLayout.NORTH, labelWorld);
		layout.putConstraint(SpringLayout.EAST, cmbWorld, -5, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.WEST, cmbWorld, 5, SpringLayout.EAST, labelWorld);
		this.add(cmbWorld);

		layout.putConstraint(SpringLayout.NORTH, labelMap, 5, SpringLayout.SOUTH, labelWorld);
		layout.putConstraint(SpringLayout.WEST, labelMap, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.SOUTH, labelMap, 0, SpringLayout.SOUTH, cmbMap);
		layout.putConstraint(SpringLayout.EAST, labelMap, 130, SpringLayout.WEST, labelMap);
		this.add(labelMap);

		cmbMap.setEnabled(false); cmbMap.setActionCommand("mapChanged");
		layout.putConstraint(SpringLayout.NORTH, cmbMap, 0, SpringLayout.NORTH, labelMap);
		layout.putConstraint(SpringLayout.EAST, cmbMap, -5, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.WEST, cmbMap, 5, SpringLayout.EAST, labelMap);
		this.add(cmbMap);

		layout.putConstraint(SpringLayout.NORTH, labelTimeout, 5, SpringLayout.SOUTH, labelMap);
		layout.putConstraint(SpringLayout.WEST, labelTimeout, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.SOUTH, labelTimeout, 0, SpringLayout.SOUTH, spnTimeout);
		layout.putConstraint(SpringLayout.EAST, labelTimeout, 130, SpringLayout.WEST, labelTimeout);
		this.add(labelTimeout);

		spnTimeout.setEnabled(false);spnTimeout.setValue(Configuration.timeout);
		spnTimeout.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				SpinnerNumberModel nModel = (SpinnerNumberModel) spnTimeout.getModel();
				int newvalue = ((Integer)spnTimeout.getValue()) + ((((Integer) nModel.getStepSize()) * e.getWheelRotation()) * -1);
				if (newvalue < 10) newvalue = 10; else if (newvalue > 3600) newvalue = 3600;
				spnTimeout.setValue(newvalue);
			}
		});
		layout.putConstraint(SpringLayout.NORTH, spnTimeout, 0, SpringLayout.NORTH, labelTimeout);
		layout.putConstraint(SpringLayout.EAST, spnTimeout, -5, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.WEST, spnTimeout, 5, SpringLayout.EAST, labelTimeout);
		this.add(spnTimeout);

		layout.putConstraint(SpringLayout.NORTH, labelInteresting, 5, SpringLayout.SOUTH, labelTimeout);
		layout.putConstraint(SpringLayout.WEST, labelInteresting, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.SOUTH, labelInteresting, 0, SpringLayout.SOUTH, cbxInteresting);
		layout.putConstraint(SpringLayout.EAST, labelInteresting, 130, SpringLayout.WEST, labelInteresting);
		this.add(labelInteresting);

		cbxInteresting.setEnabled(false);cbxInteresting.setSelected(Configuration.interestingOnly);
		layout.putConstraint(SpringLayout.NORTH, cbxInteresting, 0, SpringLayout.NORTH, labelInteresting);
		layout.putConstraint(SpringLayout.WEST, cbxInteresting, 5, SpringLayout.EAST, labelInteresting);
		this.add(cbxInteresting);

		layout.putConstraint(SpringLayout.SOUTH, this, 5, SpringLayout.SOUTH, labelInteresting);

	}

	public void registerEventManager(EventManager em) {
		for( ActionListener al : cmbWorld.getActionListeners() )
			cmbWorld.removeActionListener(al);
		for( ActionListener al : cmbMap.getActionListeners() )
			cmbMap.removeActionListener(al);
		for( ChangeListener cl : spnTimeout.getChangeListeners() )
			spnTimeout.removeChangeListener(cl);
		for( ChangeListener cl : cbxInteresting.getChangeListeners() )
			cbxInteresting.removeChangeListener(cl);

		cmbWorld.addActionListener(em);
		cmbMap.addActionListener(em);
		spnTimeout.addChangeListener(em);
		cbxInteresting.addChangeListener(em);
	}

	public void setDisabledAll(boolean disabled) {
		cmbWorld.setEnabled(!disabled);
		cmbMap.setEnabled(!disabled);
		spnTimeout.setEnabled(!disabled);
		cbxInteresting.setEnabled(!disabled);
	}

	public int getWorldIndex() {
		return cmbWorld.getSelectedIndex();
	}

	public int getMapIndex() {
		return cmbMap.getSelectedIndex();
	}

	public int getTimeout() {
		return (Integer)spnTimeout.getValue();
	}

	public boolean isInterestingOnly() {
		return cbxInteresting.isSelected();
	}

	public void fillWorld(String[] worlds) {
		cmbWorld.setModel(new DefaultComboBoxModel(worlds));
	}

	public void setWorldIndex(int i) {
		if( cmbWorld.getItemCount() > i ) {
			cmbWorld.setSelectedIndex(i);
			for( ActionListener al : cmbWorld.getActionListeners() ) {
				al.actionPerformed(new ActionEvent(cmbWorld, -1, cmbWorld.getActionCommand()));
			}
		}
	}

	public void fillMap(String[] maps) {
		cmbMap.setModel(new DefaultComboBoxModel(maps));
	}

	public void setMapIndex(int i) {
		if( cmbMap.getItemCount() > i ) {
			cmbMap.setSelectedIndex(i);
			for( ActionListener al : cmbMap.getActionListeners() ) {
				al.actionPerformed(new ActionEvent(cmbMap, -1, cmbMap.getActionCommand()));
			}
		}
	}

	public void setTimeout(int to) {
		spnTimeout.setValue(to);
		for(ChangeListener cl : spnTimeout.getChangeListeners()) {
			cl.stateChanged(new ChangeEvent(spnTimeout));
		}
	}

	public void setInterestinOnly(boolean io){
		cbxInteresting.setSelected(io);
		for(ChangeListener cl : cbxInteresting.getChangeListeners()) {
			cl.stateChanged(new ChangeEvent(cbxInteresting));
		}
	}
}
