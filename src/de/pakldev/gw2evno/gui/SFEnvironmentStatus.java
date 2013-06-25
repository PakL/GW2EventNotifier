package de.pakldev.gw2evno.gui;

import de.pakldev.gw2evno.web.WebInterface;

import javax.swing.*;

public class SFEnvironmentStatus extends JPanel {

	private final StartupFrame sf;
	private SpringLayout layout = new SpringLayout();

	private JLabel labelRequest = new JLabel("Refreshing:");
	private JLabel labelMapEvents = new JLabel("Events on map:");
	private JLabel labelInterestingEvents = new JLabel("Interesting events:");
	private JLabel labelWebinterface = new JLabel("Web interface:");

	private JLabel lblRequest = new JLabel("Paused");
	private JLabel lblMapEvents = new JLabel("n/s");
	private JLabel lblInterestingEvents = new JLabel("n/s");
	private JLabel lblWebinterface = new JLabel("Stopped");

	public SFEnvironmentStatus(StartupFrame startupFrame) {
		this.sf = startupFrame;
		this.setLayout(layout);
		this.setBorder(BorderFactory.createTitledBorder(" Status: "));

		layout.putConstraint(SpringLayout.NORTH, labelRequest, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, labelRequest, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, labelRequest, 130, SpringLayout.WEST, labelRequest);
		this.add(labelRequest);

		layout.putConstraint(SpringLayout.NORTH, lblRequest, 0, SpringLayout.NORTH, labelRequest);
		layout.putConstraint(SpringLayout.EAST, lblRequest, -5, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.WEST, lblRequest, 5, SpringLayout.EAST, labelRequest);
		this.add(lblRequest);

		layout.putConstraint(SpringLayout.NORTH, labelMapEvents, 5, SpringLayout.SOUTH, labelRequest);
		layout.putConstraint(SpringLayout.WEST, labelMapEvents, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, labelMapEvents, 130, SpringLayout.WEST, labelMapEvents);
		this.add(labelMapEvents);

		layout.putConstraint(SpringLayout.NORTH, lblMapEvents, 0, SpringLayout.NORTH, labelMapEvents);
		layout.putConstraint(SpringLayout.EAST, lblMapEvents, -5, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.WEST, lblMapEvents, 5, SpringLayout.EAST, labelMapEvents);
		this.add(lblMapEvents);

		layout.putConstraint(SpringLayout.NORTH, labelInterestingEvents, 5, SpringLayout.SOUTH, labelMapEvents);
		layout.putConstraint(SpringLayout.WEST, labelInterestingEvents, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, labelInterestingEvents, 130, SpringLayout.WEST, labelInterestingEvents);
		this.add(labelInterestingEvents);

		layout.putConstraint(SpringLayout.NORTH, lblInterestingEvents, 0, SpringLayout.NORTH, labelInterestingEvents);
		layout.putConstraint(SpringLayout.EAST, lblInterestingEvents, -5, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.WEST, lblInterestingEvents, 5, SpringLayout.EAST, labelInterestingEvents);
		this.add(lblInterestingEvents);

		layout.putConstraint(SpringLayout.NORTH, labelWebinterface, 5, SpringLayout.SOUTH, labelInterestingEvents);
		layout.putConstraint(SpringLayout.WEST, labelWebinterface, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, labelWebinterface, 130, SpringLayout.WEST, labelWebinterface);
		this.add(labelWebinterface);

		layout.putConstraint(SpringLayout.NORTH, lblWebinterface, 0, SpringLayout.NORTH, labelWebinterface);
		layout.putConstraint(SpringLayout.EAST, lblWebinterface, -5, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.WEST, lblWebinterface, 5, SpringLayout.EAST, labelWebinterface);
		this.add(lblWebinterface);

		layout.putConstraint(SpringLayout.SOUTH, this, 5, SpringLayout.SOUTH, labelWebinterface);
	}

	public void setApplicationState(int state) {
		if( state == SFMenu.APPLICATION_STATE_RUNNING ) {
			lblRequest.setText("Running");
		} else {
			lblRequest.setText("Paused");
		}
	}

	public void setMapEvents(int events) {
		lblMapEvents.setText(""+events+"");
	}

	public void setInterestingEvents(int events) {
		lblInterestingEvents.setText(""+events+"");
	}

	public void setWebinterfaceState(int state) {
		if( state == WebInterface.WEB_STATE_STARTED ) {
			lblWebinterface.setText("Started (Listening on port "+sf.getMain().web.getPort()+")");
		} else {
			lblWebinterface.setText("Stopped");
		}
	}

}
