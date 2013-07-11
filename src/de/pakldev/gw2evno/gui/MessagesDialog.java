package de.pakldev.gw2evno.gui;

import de.pakldev.gw2evno.Configuration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class MessagesDialog extends JDialog implements ActionListener, Runnable {

	private SpringLayout layout = new SpringLayout();
	private JButton closeButton = new JButton("X");
	private JButton nextMessage = new JButton(">");
	private JLabel messageCount = new JLabel("0", JLabel.CENTER);
	private JButton prevMessage = new JButton("<");

	private JPanel posIndicator = new JPanel();

	private JLabel iconlbl = new JLabel();
	private JLabel msg = new JLabel("", JLabel.CENTER);

	private Map<Integer, String> messages = new HashMap<Integer, String>();
	private Map<Integer, Image> icons = new HashMap<Integer, Image>();
	private Map<Integer, Boolean> interesting = new HashMap<Integer, Boolean>();
	private int currentIndex = -1;

	public static final Color BUTTON_COLOR = new Color(255, 255, 255);
	public static final Color BUTTON_OVER = new Color(230, 230, 230);

	public static final Color GW2_RED = new Color(170, 5, 5);
	public static final Color GW2_RED_DARK = new Color(145, 5, 5);

	private Thread indexSwitch = null;

	public MessagesDialog() {

		this.setUndecorated(true);
		this.setTitle("GW2 Event Notification");
		this.setSize(300, 125);
		this.setAlwaysOnTop(true);

		this.setBackground(Color.WHITE);

		if( Configuration.notificationx < 0 || Configuration.notificationy < 0 ) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Configuration.notificationx = (screenSize.width - this.getWidth());
			Configuration.notificationy = (screenSize.height - this.getHeight());
			Configuration.saveConfig();
		}
		this.setLocation(Configuration.notificationx, Configuration.notificationy);

		Container contentPane = this.getContentPane();
		contentPane.setLayout(layout);
		contentPane.setBackground(Color.WHITE);
		final MessagesDialog ft = this;
		contentPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if( indexSwitch != null ) {
					indexSwitch.interrupt();
					indexSwitch = null;
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				indexSwitch = new Thread(ft);
				indexSwitch.start();
			}
		});
		contentPane.addMouseMotionListener(new MouseMotionAdapter() {
			private int offsetX = 0;
			private int offsetY = 0;
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				ft.setLocation(x-offsetX, y-offsetY);
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				offsetX = e.getX();
				offsetY = e.getY();
			}
		});

		posIndicator.setBackground(GW2_RED);

		closeButton.setBorder(BorderFactory.createLineBorder(GW2_RED_DARK, 1));
		closeButton.setContentAreaFilled(false);
		closeButton.setOpaque(true);
		closeButton.setBackground(GW2_RED);
		closeButton.setForeground(Color.WHITE);
		closeButton.setMargin(new Insets(0, 0, 0, 0));
		closeButton.setFocusPainted(false);
		closeButton.addActionListener(this);
		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				closeButton.setBackground(GW2_RED_DARK);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				closeButton.setBackground(GW2_RED);
			}
		});

		nextMessage.setBorder(BorderFactory.createLineBorder(BUTTON_OVER, 1));
		nextMessage.setContentAreaFilled(false);
		nextMessage.setOpaque(true);
		nextMessage.setBackground(BUTTON_COLOR);
		nextMessage.setMargin(new Insets(0, 0, 0, 0));
		nextMessage.setFocusPainted(false);
		nextMessage.addActionListener(this);
		nextMessage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				nextMessage.setBackground(BUTTON_OVER);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				nextMessage.setBackground(BUTTON_COLOR);
			}
		});

		messageCount.setFont(messageCount.getFont().deriveFont(Font.BOLD));
		messageCount.setBackground(BUTTON_OVER);
		messageCount.setOpaque(true);

		prevMessage.setBorder(BorderFactory.createLineBorder(BUTTON_OVER, 1));
		prevMessage.setContentAreaFilled(false);
		prevMessage.setOpaque(true);
		prevMessage.setBackground(BUTTON_COLOR);
		prevMessage.setMargin(new Insets(0, 0, 0, 0));
		prevMessage.setFocusPainted(false);
		prevMessage.addActionListener(this);
		prevMessage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				prevMessage.setBackground(BUTTON_OVER);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				prevMessage.setBackground(BUTTON_COLOR);
			}
		});

		layout.putConstraint(SpringLayout.NORTH, posIndicator, 0, SpringLayout.NORTH, contentPane);
		layout.putConstraint(SpringLayout.EAST, posIndicator, 0, SpringLayout.EAST, contentPane);
		layout.putConstraint(SpringLayout.WEST, posIndicator, 0, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.SOUTH, posIndicator, 5, SpringLayout.NORTH, posIndicator);

		layout.putConstraint(SpringLayout.SOUTH, closeButton, 1, SpringLayout.SOUTH, contentPane);
		layout.putConstraint(SpringLayout.EAST, closeButton, -10, SpringLayout.EAST, contentPane);
		layout.putConstraint(SpringLayout.WEST, closeButton, -30, SpringLayout.EAST, closeButton);
		layout.putConstraint(SpringLayout.NORTH, closeButton, -30, SpringLayout.SOUTH, closeButton);

		layout.putConstraint(SpringLayout.NORTH, nextMessage, 0, SpringLayout.NORTH, closeButton);
		layout.putConstraint(SpringLayout.EAST, nextMessage, 0, SpringLayout.WEST, closeButton);
		layout.putConstraint(SpringLayout.WEST, nextMessage, -30, SpringLayout.EAST, nextMessage);
		layout.putConstraint(SpringLayout.SOUTH, nextMessage, 0, SpringLayout.SOUTH, closeButton);

		layout.putConstraint(SpringLayout.NORTH, messageCount, 0, SpringLayout.NORTH, closeButton);
		layout.putConstraint(SpringLayout.EAST, messageCount, 0, SpringLayout.WEST, nextMessage);
		layout.putConstraint(SpringLayout.WEST, messageCount, -30, SpringLayout.EAST, messageCount);
		layout.putConstraint(SpringLayout.SOUTH, messageCount, 0, SpringLayout.SOUTH, closeButton);

		layout.putConstraint(SpringLayout.NORTH, prevMessage, 0, SpringLayout.NORTH, closeButton);
		layout.putConstraint(SpringLayout.EAST, prevMessage, 0, SpringLayout.WEST, messageCount);
		layout.putConstraint(SpringLayout.WEST, prevMessage, -30, SpringLayout.EAST, prevMessage);
		layout.putConstraint(SpringLayout.SOUTH, prevMessage, 0, SpringLayout.SOUTH, closeButton);


		layout.putConstraint(SpringLayout.SOUTH, iconlbl, -5, SpringLayout.SOUTH, contentPane);
		layout.putConstraint(SpringLayout.WEST, iconlbl, 10, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.EAST, iconlbl, 25, SpringLayout.WEST, iconlbl);
		layout.putConstraint(SpringLayout.NORTH, iconlbl, -25, SpringLayout.SOUTH, iconlbl);

		layout.putConstraint(SpringLayout.SOUTH, msg, 0, SpringLayout.NORTH, iconlbl);
		layout.putConstraint(SpringLayout.EAST, msg, -10, SpringLayout.EAST, contentPane);
		layout.putConstraint(SpringLayout.NORTH, msg, 5, SpringLayout.NORTH, contentPane);
		layout.putConstraint(SpringLayout.WEST, msg, 10, SpringLayout.WEST, contentPane);

		contentPane.add(posIndicator);
		contentPane.add(closeButton);
		contentPane.add(nextMessage);
		contentPane.add(messageCount);
		contentPane.add(prevMessage);
		contentPane.add(iconlbl);
		contentPane.add(msg);

		if( messages.size() > 0 ) {
			currentIndex = 0;
			int key = -1;
			int i = 0;
			for(int k : messages.keySet()) {
				if( i < currentIndex ) { i++; continue; }
				key = k; break;
			}

			msg.setText("<html><center>"+messages.get(key)+"</center></html>");
			iconlbl.setIcon(new ImageIcon(icons.get(key).getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
			messageCount.setText("" + messages.size());

			setIndicator((messages.size()-1));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == closeButton ) {
			close();
			return;
		}
		if( e.getSource() == prevMessage ) {
			currentIndex--;
			if( currentIndex < 0 ) currentIndex = 0;
		} else if( e.getSource() == nextMessage ) {
			currentIndex++;
			if( currentIndex >= messages.size() ) currentIndex = (messages.size()-1);
		}

		showMessage(currentIndex);
	}

	@Override
	public void run() {
		try {
			while(currentIndex <= messages.size()) {
				Thread.sleep(5000);
				currentIndex++;
				if( currentIndex >= messages.size() ) {
					break;
				} else {
					showMessage(currentIndex);
				}
			}
			close();
		} catch (InterruptedException e) {}
	}

	public void addMessage(String eventId, String message, Image icon, boolean interesting) {
		if( !messages.containsValue(message) ) {
			int newIndex = messages.size();
			this.messages.put(newIndex, message);
			this.icons.put(newIndex, icon);
			this.interesting.put(newIndex, interesting);
			if( newIndex == 0 ) {
				currentIndex = 0;
				showMessage(currentIndex);
				indexSwitch = new Thread(this);
				indexSwitch.start();
				this.setVisible(true);
			}
		}
		setIndicator((messages.size()-1)-currentIndex);
		messageCount.setText("" + messages.size());
	}

	public void showMessage(int index) {
		int key = -1;
		int i = 0;
		for(int k : messages.keySet()) {
			if( i < index ) { i++; continue; }
			key = k; break;
		}

		String message = messages.get(key);
		if( interesting.get(key) ) {
			this.setBackground(GW2_RED);
			this.getContentPane().setBackground(GW2_RED);
			posIndicator.setBackground(Color.WHITE);
			message = "<font color=\"#ffffff\">"+message+"</font>";
		} else {
			this.setBackground(Color.WHITE);
			this.getContentPane().setBackground(Color.WHITE);
			posIndicator.setBackground(GW2_RED);
		}
		msg.setText("<html><center>"+message+"</center></html>");
		iconlbl.setIcon(new ImageIcon(icons.get(key).getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
		setIndicator( (messages.size()-1)-index );
	}

	public void setIndicator(int index) {
		int max = messages.size();
		int width = this.getWidth();

		int w = (width/max);
		int x = width-(w*(index+1));

		Container contentPane = this.getContentPane();

		layout.putConstraint(SpringLayout.WEST, posIndicator, x, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.EAST, posIndicator, w, SpringLayout.WEST, posIndicator);
	}

	public void close() {
		int x = this.getX();
		int y = this.getY();
		if( x != Configuration.notificationx || y != Configuration.notificationy ) {
			Configuration.notificationx = x;
			Configuration.notificationy = y;
			Configuration.saveConfig();
		}

		this.setVisible(false);
		messages.clear();
		icons.clear();
		interesting.clear();
		currentIndex = -1;
	}
}
