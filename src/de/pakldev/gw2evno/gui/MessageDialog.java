package de.pakldev.gw2evno.gui;


import com.sun.awt.AWTUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MessageDialog extends JDialog implements Runnable {

	private final DialogManager dm;

	private boolean doNotAutoHide = false;
	private Thread autoHide = null;

	public MessageDialog(final DialogManager dm, String message) {
		this.dm = dm;

		this.setUndecorated(true);
		this.setSize(300, 100);
		this.setAlwaysOnTop(true);
		AWTUtilities.setWindowOpacity(this, 0.8f);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(screenSize.width-300, screenSize.height-100);

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if( e.getSource() instanceof MessageDialog ) {
					MessageDialog fr = (MessageDialog) e.getSource();
					dm.closeDialog(fr);
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				if( e.getSource() instanceof MessageDialog ) {
					MessageDialog fr = (MessageDialog) e.getSource();
					fr.stopAutoHide();
					float currop = AWTUtilities.getWindowOpacity(fr);
					if( currop >= 0.8f )
						AWTUtilities.setWindowOpacity(fr, 1.0f);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if( e.getSource() instanceof MessageDialog ) {
					MessageDialog fr = (MessageDialog) e.getSource();
					fr.startAutoHide();
					float currop = AWTUtilities.getWindowOpacity(fr);
					if( currop >= 0.8f )
						AWTUtilities.setWindowOpacity(fr, 0.8f);
				}
			}
		});
		this.setBackground(Color.BLACK);
		this.getContentPane().setBackground(Color.WHITE);

		SpringLayout layout = new SpringLayout();
		this.getContentPane().setLayout(layout);
		JLabel msg = new JLabel(message, JLabel.CENTER);
		layout.putConstraint(SpringLayout.NORTH, msg, 20, SpringLayout.NORTH, getContentPane());
		layout.putConstraint(SpringLayout.EAST, msg, -20, SpringLayout.EAST, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, msg, -20, SpringLayout.SOUTH, getContentPane());
		layout.putConstraint(SpringLayout.WEST, msg, 20, SpringLayout.WEST, getContentPane());

		this.getContentPane().add(msg, BorderLayout.CENTER);

		this.setVisible(true);
		autoHide = new Thread(this);
		autoHide.start();
	}

	public void stopAutoHide() {
		doNotAutoHide = true;
		autoHide.interrupt();
	}

	public void startAutoHide() {
		doNotAutoHide = false;
		autoHide = new Thread(this);
		autoHide.start();
	}

	@Override
	public void run() {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
		} finally {
			if( !doNotAutoHide){
				dm.closeDialog(this);
			}
		}
	}
}
