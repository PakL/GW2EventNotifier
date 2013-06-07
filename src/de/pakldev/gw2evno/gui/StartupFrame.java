package de.pakldev.gw2evno.gui;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;
import de.pakldev.gw2evno.Configuration;
import de.pakldev.gw2evno.GW2EvNoMain;
import de.pakldev.gw2evno.Language;
import de.pakldev.gw2evno.gw2api.MapNames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartupFrame extends JFrame {

	private final GW2EvNoMain main;
	private EventManager eventManger;
	private Provider provider = null;

	private SpringLayout layout = new SpringLayout();

	private JProgressBar progressBar = new JProgressBar();

	private JComboBox worldBox;
	private JComboBox mapBox;

	public StartupFrame(GW2EvNoMain main) {
		this.main = main;

		this.setTitle("GW2 Event Notifier");
		this.setSize(400, 220);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container contentPane = this.getContentPane();
		contentPane.setLayout(layout);

		this.setVisible(true);
	}

	public void setProgressValue(int n) {
		progressBar.setValue(n);
	}
	public void setProgressMax(int n) {
		progressBar.setMaximum(n);
	}


	public int getWorldIndex() {
		return worldBox.getSelectedIndex();
	}

	public int getMapIndex() {
		return mapBox.getSelectedIndex();
	}

	public void setMapIndex(int i) {
		if( mapBox != null ) {
			mapBox.setSelectedIndex(i);
			if( eventManger != null ) {
				eventManger.actionPerformed(new ActionEvent(mapBox,-1,"mapChanged"));
			}
		}
	}

	public void resetToLoading() {
		if( eventManger != null ) {
			eventManger.stop();
		}
		if( provider != null ) {
			provider.reset();
			provider.stop();
		}

		final Container contentPane = this.getContentPane();
		contentPane.removeAll();
		contentPane.add(progressBar);
		layout.putConstraint(SpringLayout.NORTH, progressBar, -30, SpringLayout.SOUTH, progressBar);
		layout.putConstraint(SpringLayout.WEST, progressBar, 5, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.EAST, progressBar, -5, SpringLayout.EAST, contentPane);
		layout.putConstraint(SpringLayout.SOUTH, progressBar, -5, SpringLayout.SOUTH, contentPane);
		progressBar.setMaximum(3);
		progressBar.setValue(0);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				contentPane.validate();
				contentPane.repaint();
			}
		});
	}

	public void doneLoading() {
		this.showLoadedGUI();
	}

	private void showLoadedGUI() {
		final Container contentPane = this.getContentPane();

		JLabel lblLangBox = new JLabel(Language.language()+":");
		layout.putConstraint(SpringLayout.NORTH, lblLangBox, 5, SpringLayout.NORTH, contentPane);
		layout.putConstraint(SpringLayout.WEST, lblLangBox, 5, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.EAST, lblLangBox, 70, SpringLayout.WEST, lblLangBox);
		contentPane.add(lblLangBox);

		final JComboBox langBox = new JComboBox(new String[]{ "English", "Deutsch", "Français", "Español" });
		layout.putConstraint(SpringLayout.NORTH, langBox, 0, SpringLayout.NORTH, lblLangBox);
		layout.putConstraint(SpringLayout.WEST, langBox, 5, SpringLayout.EAST, lblLangBox);
		layout.putConstraint(SpringLayout.EAST, langBox, -5, SpringLayout.EAST, contentPane);
		contentPane.add(langBox);
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_EN) ) langBox.setSelectedIndex(0);
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_DE) ) langBox.setSelectedIndex(1);
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_FR) ) langBox.setSelectedIndex(2);
		if( Configuration.language.equalsIgnoreCase(MapNames.LANG_ES) ) langBox.setSelectedIndex(3);
		langBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if( langBox.getSelectedIndex() == 0 ) Configuration.language = MapNames.LANG_EN;
				if( langBox.getSelectedIndex() == 1 ) Configuration.language = MapNames.LANG_DE;
				if( langBox.getSelectedIndex() == 2 ) Configuration.language = MapNames.LANG_FR;
				if( langBox.getSelectedIndex() == 3 ) Configuration.language = MapNames.LANG_ES;
				Configuration.saveConfig();
				main.loadLanguage(Configuration.language);
			}
		});

		layout.putConstraint(SpringLayout.SOUTH, lblLangBox, 0, SpringLayout.SOUTH, langBox);

		JLabel lblWorldBox = new JLabel(Language.world()+":");
		layout.putConstraint(SpringLayout.NORTH, lblWorldBox, 5, SpringLayout.SOUTH, langBox);
		layout.putConstraint(SpringLayout.WEST, lblWorldBox, 5, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.EAST, lblWorldBox, 70, SpringLayout.WEST, lblWorldBox);
		contentPane.add(lblWorldBox);

		worldBox = new JComboBox(main.worlds.getWorlds().values().toArray(new String[0]));
		layout.putConstraint(SpringLayout.NORTH, worldBox, 0, SpringLayout.NORTH, lblWorldBox);
		layout.putConstraint(SpringLayout.WEST, worldBox, 5, SpringLayout.EAST, lblWorldBox);
		layout.putConstraint(SpringLayout.EAST, worldBox, -5, SpringLayout.EAST, contentPane);
		contentPane.add(worldBox); worldBox.setActionCommand("worldChanged");
		if( worldBox.getItemCount() > Configuration.worldIndex ) {
			worldBox.setSelectedIndex(Configuration.worldIndex);
		}

		layout.putConstraint(SpringLayout.SOUTH, lblWorldBox, 0, SpringLayout.SOUTH, worldBox);


		JLabel lblMapBox = new JLabel(Language.map()+":");
		layout.putConstraint(SpringLayout.NORTH, lblMapBox, 5, SpringLayout.SOUTH, worldBox);
		layout.putConstraint(SpringLayout.WEST, lblMapBox, 5, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.EAST, lblMapBox, 70, SpringLayout.WEST, lblMapBox);
		contentPane.add(lblMapBox);

		mapBox = new JComboBox(main.maps.getMaps().values().toArray(new String[0]));
		layout.putConstraint(SpringLayout.NORTH, mapBox, 0, SpringLayout.NORTH, lblMapBox);
		layout.putConstraint(SpringLayout.WEST, mapBox, 5, SpringLayout.EAST, lblMapBox);
		layout.putConstraint(SpringLayout.EAST, mapBox, -5, SpringLayout.EAST, contentPane);
		contentPane.add(mapBox); mapBox.setActionCommand("mapChanged");
		if( mapBox.getItemCount() > Configuration.mapIndex ) {
			mapBox.setSelectedIndex(Configuration.mapIndex);
		}

		layout.putConstraint(SpringLayout.SOUTH, lblMapBox, 0, SpringLayout.SOUTH, mapBox);

		JLabel helpMessage = new JLabel("<html>"+Language.helpMessage()+"</html>");
		layout.putConstraint(SpringLayout.NORTH, helpMessage, 5, SpringLayout.SOUTH, mapBox);
		layout.putConstraint(SpringLayout.WEST, helpMessage, 5, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.EAST, helpMessage, -5, SpringLayout.EAST, contentPane);
		layout.putConstraint(SpringLayout.SOUTH, helpMessage, -5, SpringLayout.NORTH, progressBar);
		contentPane.add(helpMessage);

		eventManger = new EventManager(main, this);
		worldBox.addActionListener(eventManger);
		mapBox.addActionListener(eventManger);
		eventManger.start();

		provider = Provider.getCurrentProvider(false);
		final StartupFrame sf = this;
		provider.register(KeyStroke.getKeyStroke("control BACK_SPACE"), new HotKeyListener() {
			@Override
			public void onHotKey(HotKey hotKey) {
				new SearchMap(main, sf);
			}
		});

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				contentPane.validate();
				contentPane.repaint();
			}
		});

	}

}
