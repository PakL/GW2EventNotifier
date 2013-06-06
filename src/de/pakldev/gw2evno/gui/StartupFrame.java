package de.pakldev.gw2evno.gui;

import de.pakldev.gw2evno.GW2EvNoMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class StartupFrame extends JFrame {

	private final GW2EvNoMain main;
	private final EventManager eventManger;

	private SpringLayout layout = new SpringLayout();

	private JProgressBar progressBar = new JProgressBar();

	private JComboBox worldBox;
	private JComboBox mapBox;

	public StartupFrame(GW2EvNoMain main) {
		this.main = main;
		this.eventManger = new EventManager(main, this);

		this.setTitle("GW2 Event Notifier");
		this.setSize(400, 200);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container contentPane = this.getContentPane();
		contentPane.setLayout(layout);

		layout.putConstraint(SpringLayout.NORTH, progressBar, -30, SpringLayout.SOUTH, progressBar);
		layout.putConstraint(SpringLayout.WEST, progressBar, 5, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.EAST, progressBar, -5, SpringLayout.EAST, contentPane);
		layout.putConstraint(SpringLayout.SOUTH, progressBar, -5, SpringLayout.SOUTH, contentPane);
		contentPane.add(progressBar);
		progressBar.setMaximum(3);
		progressBar.setValue(0);

		this.setVisible(true);
	}

	public void setProgressValue(int n) {
		progressBar.setValue(n);
	}
	public void setProgressMax(int n) {
		progressBar.setMaximum(n);
	}

	public void doneLoading() {
		this.showLoadedGUI();
	}

	private void showLoadedGUI() {
		final Container contentPane = this.getContentPane();

		JLabel lblWorldBox = new JLabel("Welt:");
		layout.putConstraint(SpringLayout.NORTH, lblWorldBox, 5, SpringLayout.NORTH, contentPane);
		layout.putConstraint(SpringLayout.WEST, lblWorldBox, 5, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.EAST, lblWorldBox, 50, SpringLayout.WEST, lblWorldBox);
		contentPane.add(lblWorldBox);

		worldBox = new JComboBox(main.worlds.getWorlds().values().toArray(new String[0]));
		layout.putConstraint(SpringLayout.NORTH, worldBox, 0, SpringLayout.NORTH, lblWorldBox);
		layout.putConstraint(SpringLayout.WEST, worldBox, 5, SpringLayout.EAST, lblWorldBox);
		layout.putConstraint(SpringLayout.EAST, worldBox, -5, SpringLayout.EAST, contentPane);
		contentPane.add(worldBox); worldBox.addActionListener(eventManger);worldBox.setActionCommand("worldChanged");

		layout.putConstraint(SpringLayout.SOUTH, lblWorldBox, 0, SpringLayout.SOUTH, worldBox);

		JLabel lblMapBox = new JLabel("Karte:");
		layout.putConstraint(SpringLayout.NORTH, lblMapBox, 5, SpringLayout.SOUTH, worldBox);
		layout.putConstraint(SpringLayout.WEST, lblMapBox, 5, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.EAST, lblMapBox, 50, SpringLayout.WEST, lblMapBox);
		contentPane.add(lblMapBox);

		mapBox = new JComboBox(main.maps.getMaps().values().toArray(new String[0]));
		layout.putConstraint(SpringLayout.NORTH, mapBox, 0, SpringLayout.NORTH, lblMapBox);
		layout.putConstraint(SpringLayout.WEST, mapBox, 5, SpringLayout.EAST, lblMapBox);
		layout.putConstraint(SpringLayout.EAST, mapBox, -5, SpringLayout.EAST, contentPane);
		contentPane.add(mapBox); mapBox.addActionListener(eventManger);mapBox.setActionCommand("mapChanged");

		layout.putConstraint(SpringLayout.SOUTH, lblMapBox, 0, SpringLayout.SOUTH, mapBox);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				contentPane.validate();
				contentPane.repaint();

				eventManger.start();
			}
		});

	}

}
