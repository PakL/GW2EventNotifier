package de.pakldev.gw2evno.gui;

import de.pakldev.gw2evno.Configuration;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SFAppsettings extends JPanel implements ActionListener, KeyListener {

	private final StartupFrame sf;
	private SpringLayout layout = new SpringLayout();

	private JLabel labelWebport = new JLabel("Web interface port:");
	private JLabel labelWebsocketport = new JLabel("Web socket port:");
	private JLabel labelHotkey = new JLabel("Map changing hotkey:");
	private JTextField txtWebport = new JTextField(""+Configuration.webPort);
	private JTextField txtWebsocketport = new JTextField(""+Configuration.webSocketPort);
	private JTextField txtHotkeyKey = new JTextField((!KeyEvent.getKeyModifiersText(Configuration.hotkeyMod).isEmpty() ? KeyEvent.getKeyModifiersText(Configuration.hotkeyMod)+" + " : "") + KeyEvent.getKeyText(Configuration.hotkeyKey));

	public SFAppsettings(StartupFrame startupFrame) {
		this.sf = startupFrame;
		this.setLayout(layout);
		this.setBorder(BorderFactory.createTitledBorder(" Application settings: "));

		layout.putConstraint(SpringLayout.NORTH, labelWebport, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, labelWebport, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.SOUTH, labelWebport, 0, SpringLayout.SOUTH, txtWebport);
		layout.putConstraint(SpringLayout.EAST, labelWebport, 130, SpringLayout.WEST, labelWebport);
		this.add(labelWebport);

		txtWebport.setEnabled(false); txtWebport.addActionListener(this);
		layout.putConstraint(SpringLayout.NORTH, txtWebport, 0, SpringLayout.NORTH, labelWebport);
		layout.putConstraint(SpringLayout.EAST, txtWebport, -5, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.WEST, txtWebport, 5, SpringLayout.EAST, labelWebport);
		this.add(txtWebport);

		layout.putConstraint(SpringLayout.NORTH, labelWebsocketport, 5, SpringLayout.SOUTH, labelWebport);
		layout.putConstraint(SpringLayout.WEST, labelWebsocketport, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.SOUTH, labelWebsocketport, 0, SpringLayout.SOUTH, txtWebsocketport);
		layout.putConstraint(SpringLayout.EAST, labelWebsocketport, 130, SpringLayout.WEST, labelWebsocketport);
		this.add(labelWebsocketport);

		txtWebsocketport.setEnabled(false); txtWebsocketport.addActionListener(this);
		layout.putConstraint(SpringLayout.NORTH, txtWebsocketport, 0, SpringLayout.NORTH, labelWebsocketport);
		layout.putConstraint(SpringLayout.EAST, txtWebsocketport, -5, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.WEST, txtWebsocketport, 5, SpringLayout.EAST, labelWebsocketport);
		this.add(txtWebsocketport);

		layout.putConstraint(SpringLayout.NORTH, labelHotkey, 5, SpringLayout.SOUTH, labelWebsocketport);
		layout.putConstraint(SpringLayout.WEST, labelHotkey, 5, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.SOUTH, labelHotkey, 0, SpringLayout.SOUTH, txtHotkeyKey);
		layout.putConstraint(SpringLayout.EAST, labelHotkey, 130, SpringLayout.WEST, labelHotkey);
		this.add(labelHotkey);

		txtHotkeyKey.setEnabled(false); txtHotkeyKey.addKeyListener(this);
		layout.putConstraint(SpringLayout.NORTH, txtHotkeyKey, 0, SpringLayout.NORTH, labelHotkey);
		layout.putConstraint(SpringLayout.EAST, txtHotkeyKey, -5, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.WEST, txtHotkeyKey, 5, SpringLayout.EAST, labelHotkey);
		this.add(txtHotkeyKey);


		layout.putConstraint(SpringLayout.SOUTH, this, 5, SpringLayout.SOUTH, labelHotkey);
	}

	public void setWebportEnabled(boolean enabled) {
		txtWebport.setEnabled(enabled);
		txtWebsocketport.setEnabled(enabled);
	}
	public void setHotkeyEnabled(boolean enabled) {
		txtHotkeyKey.setEnabled(enabled);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == txtWebport ) {
			String p = txtWebport.getText();
			p = p.replaceAll("[^0-9]", "");
			try {
				int port = Integer.parseInt(p); txtWebport.setText(""+port);
				if( port >= 1024 && port <= 65535 ) {
					if( sf.setNewWebPort(port) ) {
						Configuration.webPort = port;
						Configuration.saveConfig();
					}
				} else {
					JOptionPane.showMessageDialog(sf, "This is not a valid port number.\nPort must be between 1024 and 65535.", "Webinterface", JOptionPane.ERROR_MESSAGE);
				}
			} catch(NumberFormatException ex) {
				txtWebport.setText(""+Configuration.webPort);
				JOptionPane.showMessageDialog(sf, "This is not a valid port number.\nPort must be between 1024 and 65535.", "Webinterface", JOptionPane.ERROR_MESSAGE);
			}
		} else if( e.getSource() == txtWebsocketport ) {
			String p = txtWebsocketport.getText();
			p = p.replaceAll("[^0-9]", "");
			try {
				int port = Integer.parseInt(p); txtWebsocketport.setText(""+port);
				if( port >= 1024 && port <= 65535 ) {
					if( sf.setNewWebSocketPort(port) ) {
						Configuration.webSocketPort = port;
						Configuration.saveConfig();
					}
				} else {
					JOptionPane.showMessageDialog(sf, "This is not a valid port number.\nPort must be between 1024 and 65535.", "Webinterface", JOptionPane.ERROR_MESSAGE);
				}
			} catch(NumberFormatException ex) {
				txtWebsocketport.setText(""+Configuration.webSocketPort);
				JOptionPane.showMessageDialog(sf, "This is not a valid port number.\nPort must be between 1024 and 65535.", "Webinterface", JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if( e.getSource() instanceof  JTextField ) {
			e.consume();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if( e.getSource() == txtHotkeyKey ) {
			String modifier = KeyEvent.getKeyModifiersText(e.getModifiers());

			if( e.getKeyCode() != KeyEvent.VK_ALT && e.getKeyCode() != KeyEvent.VK_CONTROL && e.getKeyCode() != KeyEvent.VK_SHIFT ) {
				int keycode = e.getKeyCode();
				if( keycode != 0 ) {
					String key = KeyEvent.getKeyText(keycode);
					txtHotkeyKey.setText((!modifier.isEmpty() ? modifier+" + " : "") + key);
					Configuration.hotkeyMod = e.getModifiers();
					Configuration.hotkeyKey = keycode;
					Configuration.saveConfig();
					e.consume();
					sf.setNewHotKey(e.getModifiers(), keycode);
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
