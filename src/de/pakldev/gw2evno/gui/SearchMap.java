package de.pakldev.gw2evno.gui;

import de.pakldev.gw2evno.GW2EvNoMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SearchMap extends JDialog implements ActionListener, KeyListener {

	private final GW2EvNoMain main;
	private final StartupFrame sf;

	private final JTextField txtSearch;
	private final JLabel lblResult;

	private String resultId = "";

	public SearchMap(GW2EvNoMain main, StartupFrame sf) {
		this.main = main;
		this.sf = sf;

		this.setAlwaysOnTop(true);
		this.setResizable(false);
		this.setSize(300, 150);
		this.setLocationRelativeTo(null);

		SpringLayout layout = new SpringLayout();
		Container contentPane = this.getContentPane();
		contentPane.setLayout(layout);

		txtSearch = new JTextField(""); txtSearch.setFont(txtSearch.getFont().deriveFont(20f));
		layout.putConstraint(SpringLayout.NORTH, txtSearch, 10, SpringLayout.NORTH, contentPane);
		layout.putConstraint(SpringLayout.WEST, txtSearch, 10, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.EAST, txtSearch, -10, SpringLayout.EAST, contentPane);
		layout.putConstraint(SpringLayout.SOUTH, txtSearch, 30, SpringLayout.NORTH, txtSearch);
		txtSearch.addKeyListener(this);
		txtSearch.addActionListener(this);

		lblResult = new JLabel(""); lblResult.setFont(lblResult.getFont().deriveFont(20f));
		layout.putConstraint(SpringLayout.SOUTH, lblResult, -10, SpringLayout.SOUTH, contentPane);
		layout.putConstraint(SpringLayout.WEST, lblResult, 10, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.EAST, lblResult, -10, SpringLayout.EAST, contentPane);
		layout.putConstraint(SpringLayout.SOUTH, lblResult, 10, SpringLayout.SOUTH, txtSearch);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				setVisible(false);
				dispose();
			}
		});
		this.setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		this.setVisible(false);
		if( !resultId.isEmpty() ) {
			sf.setMapIndex(main.maps.getIndexByMapId(resultId));
		}
		dispose();
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		if( e.getKeyCode() == KeyEvent.VK_ESCAPE ) {
			this.setVisible(false);
			dispose();
			return;
		}
		if( txtSearch.getText().isEmpty() ) {
			resultId = "";
			lblResult.setText("");
		} else {
			resultId = main.maps.searchMap(txtSearch.getText());
			lblResult.setText(main.maps.getMap(resultId));
		}
	}
}
