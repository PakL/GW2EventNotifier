package de.pakldev.gw2evno.gui;


import com.sun.awt.AWTUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

public class MessageDialog extends JDialog implements Runnable {

	private final DialogManager dm;

	private boolean doNotAutoHide = false;
	private Thread autoHide = null;

	public MessageDialog(final DialogManager dm, String message, Image icon, final String wikiUrl, boolean interesting) {
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
					if( e.getButton() == MouseEvent.BUTTON3 ) {
						Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
						if( desktop != null && desktop.isSupported(Desktop.Action.BROWSE) ) {
							try {
								desktop.browse(new URI(wikiUrl));
							} catch(Exception ex) {}
						}
					} else {
						MessageDialog fr = (MessageDialog) e.getSource();
						dm.closeDialog(fr);
					}
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

		if( interesting ) {
			this.setBackground(Color.RED);
			this.getContentPane().setBackground(Color.RED);
		} else {
			this.setBackground(Color.WHITE);
			this.getContentPane().setBackground(Color.WHITE);
		}

		SpringLayout layout = new SpringLayout();
		this.getContentPane().setLayout(layout);

		JLabel iconlbl = new JLabel(new ImageIcon(icon));
		layout.putConstraint(SpringLayout.NORTH, iconlbl, 18, SpringLayout.NORTH, getContentPane());
		layout.putConstraint(SpringLayout.WEST, iconlbl, 10, SpringLayout.WEST, getContentPane());
		layout.putConstraint(SpringLayout.EAST, iconlbl, 64, SpringLayout.WEST, iconlbl);
		layout.putConstraint(SpringLayout.SOUTH, iconlbl, 64, SpringLayout.NORTH, iconlbl);
		this.getContentPane().add(iconlbl);

		JLabel msg = new JLabel(message, JLabel.CENTER);
		layout.putConstraint(SpringLayout.NORTH, msg, 0, SpringLayout.NORTH, getContentPane());
		layout.putConstraint(SpringLayout.EAST, msg, -10, SpringLayout.EAST, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, msg, -0, SpringLayout.SOUTH, getContentPane());
		layout.putConstraint(SpringLayout.WEST, msg, 10, SpringLayout.EAST, iconlbl);
		this.getContentPane().add(msg);

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
