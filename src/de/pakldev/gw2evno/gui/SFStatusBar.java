package de.pakldev.gw2evno.gui;

import javax.swing.*;

public class SFStatusBar extends JPanel {

	private final StartupFrame sf;
	private SpringLayout layout = new SpringLayout();

	private JLabel label = new JLabel("Preparing...");
	private JProgressBar progress = new JProgressBar();

	public SFStatusBar(StartupFrame startupFrame) {
		this.sf = startupFrame;
		this.setLayout(layout);

		layout.putConstraint(SpringLayout.NORTH, label, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, label, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.SOUTH, label, 0, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, label, 0, SpringLayout.HORIZONTAL_CENTER, this);
		this.add(label);

		progress.setIndeterminate(true);
		layout.putConstraint(SpringLayout.NORTH, progress, 2, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, progress, 5, SpringLayout.EAST, label);
		layout.putConstraint(SpringLayout.EAST, progress, -5, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, progress, -2, SpringLayout.SOUTH, this);
		this.add(progress);

		this.setBorder(BorderFactory.createLoweredSoftBevelBorder());
	}

	public void setStatusAndProgress(String label, int value) {
		this.setStatus(label);
		this.setProgress(value);
	}

	public void setStatusAndProgress(String label, int value, int max) {
		this.setStatusAndProgress(label, value);
		this.setMaximumProgress(max);
	}

	public void setStatus(String label) {
		this.label.setText(label);
	}

	public void setProgress(int value) {
		if( value < progress.getMinimum() ) {
			progress.setIndeterminate(true);
		} else {
			progress.setIndeterminate(false);
			progress.setValue(value);
		}
	}

	public void setMaximumProgress(int max) {
		progress.setMaximum(max);
	}

}
