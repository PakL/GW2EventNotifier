package de.pakldev.gw2evno.gui;


import com.sun.awt.AWTUtilities;
import de.pakldev.gw2evno.GW2EvNoMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MessageDialog extends JDialog implements Runnable {

	private final DialogManager dm;

	private boolean doNotAutoHide = false;
	private Thread autoHide = null;

	private String eventid = "";

	public MessageDialog(final DialogManager dm, final String eventid, String message, Image icon, boolean interesting) {
		this.dm = dm;
		this.eventid = eventid;

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

					if( e.getButton() == MouseEvent.BUTTON3 ) {
						try {
							String localhostname = InetAddress.getLocalHost().getHostName();
							GW2EvNoMain.openUrl("http://"+localhostname+":8086/map.html?eventid="+eventid);
						} catch (UnknownHostException e1) {}
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
		layout.putConstraint(SpringLayout.NORTH, iconlbl, 34, SpringLayout.NORTH, getContentPane());
		layout.putConstraint(SpringLayout.WEST, iconlbl, 10, SpringLayout.WEST, getContentPane());
		layout.putConstraint(SpringLayout.EAST, iconlbl, 32, SpringLayout.WEST, iconlbl);
		layout.putConstraint(SpringLayout.SOUTH, iconlbl, 32, SpringLayout.NORTH, iconlbl);
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
