package de.pakldev.gw2evno.gui;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;
import de.pakldev.gw2evno.Configuration;
import de.pakldev.gw2evno.GW2EvNoMain;
import de.pakldev.gw2evno.Language;
import de.pakldev.gw2evno.gw2api.MapNames;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class StartupFrame extends JFrame {

	private final GW2EvNoMain main;
	private EventManager eventManger;
	private Provider provider = null;

	private SpringLayout layout = new SpringLayout();

	private JProgressBar progressBar = new JProgressBar();

	private JComboBox worldBox;
	private JComboBox mapBox;
	private JSpinner timeout;
	private JCheckBox interestinonly;

	public StartupFrame(GW2EvNoMain main) {
		this.main = main;

		this.setTitle("GW2 Event Notifier");
		this.setSize(400, 300);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container contentPane = this.getContentPane();
		contentPane.setLayout(layout);

		JLabel wait = new JLabel("<html><center>Please wait!<br />Bitte warten!<br />Attendez, s'il vous plaît!<br />Espere, por favor!</center></html>", JLabel.CENTER);
		layout.putConstraint(SpringLayout.NORTH, wait, 0, SpringLayout.NORTH, contentPane);
		layout.putConstraint(SpringLayout.EAST, wait, 0, SpringLayout.EAST, contentPane);
		layout.putConstraint(SpringLayout.SOUTH, wait, 0, SpringLayout.SOUTH, contentPane);
		layout.putConstraint(SpringLayout.WEST, wait, 0, SpringLayout.WEST, contentPane);
		contentPane.add(wait);

		this.setVisible(true);
	}

	public void setProgressValue(int n) {
		progressBar.setValue(n);
		progressBar.setIndeterminate(false);
	}
	public void setProgressMax(int n) {
		progressBar.setMaximum(n);
	}
	public void setProgressIndeterminate(boolean indeterminate){
		progressBar.setIndeterminate(indeterminate);
	}


	public int getWorldIndex() {
		return worldBox.getSelectedIndex();
	}

	public int getMapIndex() {
		return mapBox.getSelectedIndex();
	}

	public boolean isInterestingOnly() {
		return interestinonly.isSelected();
	}

	public void setInterestinOnly(boolean io){
		interestinonly.setSelected(io);
		if(eventManger != null) {
			eventManger.stateChanged(new ChangeEvent(interestinonly));
		}
	}

	public void setMapIndex(int i) {
		System.out.println("[GUI] New map index: "+i);
		if( mapBox != null ) {
			mapBox.setSelectedIndex(i);
			if( eventManger != null ) {
				eventManger.actionPerformed(new ActionEvent(mapBox,-1,"mapChanged"));
			}
		}
	}
	public void setWorldIndex(int i) {
		System.out.println("[GUI] New world index: "+i);
		if( worldBox != null ) {
			worldBox.setSelectedIndex(i);
			if( eventManger != null ) {
				eventManger.actionPerformed(new ActionEvent(worldBox,-1,"worldChanged"));
			}
		}
	}

	public EventManager getEventManger() {
		return eventManger;
	}

	public void resetToLoading() {
		System.out.println("[GUI] Setting GUI to loading only");
		if( eventManger != null ) {
			eventManger.stop();
		}
		if( provider != null ) {
			provider.reset();
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
				System.out.println("[GUI] Repainting...");
				contentPane.validate();
				contentPane.repaint();
			}
		});
	}

	public void doneLoading() {
		this.showLoadedGUI();
	}

	private void showLoadedGUI() {
		System.out.println("[GUI] Setting GUI to working interface");
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


		JLabel lblTimeout = new JLabel(Language.stateRefresh()+":");
		layout.putConstraint(SpringLayout.NORTH, lblTimeout, 5, SpringLayout.SOUTH, mapBox);
		layout.putConstraint(SpringLayout.WEST, lblTimeout, 5, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.EAST, lblTimeout, 200, SpringLayout.WEST, lblTimeout);
		contentPane.add(lblTimeout);
		JLabel lblSeconds = new JLabel(Language.seconds());
		layout.putConstraint(SpringLayout.NORTH, lblSeconds, 0, SpringLayout.NORTH, lblTimeout);
		layout.putConstraint(SpringLayout.EAST, lblSeconds, -5, SpringLayout.EAST, contentPane);
		layout.putConstraint(SpringLayout.WEST, lblSeconds, -60, SpringLayout.EAST, lblSeconds);
		contentPane.add(lblSeconds);

		timeout = new JSpinner(new SpinnerNumberModel(Configuration.timeout, 10, 3600, 5));
		layout.putConstraint(SpringLayout.NORTH, timeout, 0, SpringLayout.NORTH, lblTimeout);
		layout.putConstraint(SpringLayout.WEST, timeout, 5, SpringLayout.EAST, lblTimeout);
		layout.putConstraint(SpringLayout.EAST, timeout, -5, SpringLayout.WEST, lblSeconds);
		contentPane.add(timeout);
		timeout.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				SpinnerNumberModel nModel = (SpinnerNumberModel) timeout.getModel();
				int step = (Integer)nModel.getStepSize();
				int value = (Integer) timeout.getValue();
				int newvalue = value+((step*e.getWheelRotation())*-1);
				if( newvalue < 10 ) newvalue = 10;
				if( newvalue > 3600 ) newvalue = 3600;
				timeout.setValue(newvalue);
			}
		});

		layout.putConstraint(SpringLayout.SOUTH, lblTimeout, 0, SpringLayout.SOUTH, timeout);
		layout.putConstraint(SpringLayout.SOUTH, lblSeconds, 0, SpringLayout.SOUTH, timeout);


		JLabel lblInterestingOnly = new JLabel(Language.interestingOnly()+":");
		layout.putConstraint(SpringLayout.NORTH, lblInterestingOnly, 5, SpringLayout.SOUTH, timeout);
		layout.putConstraint(SpringLayout.WEST, lblInterestingOnly, 5, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.EAST, lblInterestingOnly, 200, SpringLayout.WEST, lblInterestingOnly);
		contentPane.add(lblInterestingOnly);

		interestinonly = new JCheckBox();
		layout.putConstraint(SpringLayout.NORTH, interestinonly, 0, SpringLayout.NORTH, lblInterestingOnly);
		layout.putConstraint(SpringLayout.WEST, interestinonly, 5, SpringLayout.EAST, lblInterestingOnly);
		layout.putConstraint(SpringLayout.EAST, interestinonly, -5, SpringLayout.EAST, contentPane);
		contentPane.add(interestinonly);
		if( Configuration.interestingOnly ) interestinonly.setSelected(true);

		layout.putConstraint(SpringLayout.SOUTH, lblInterestingOnly, 0, SpringLayout.SOUTH, interestinonly);


		JLabel helpMessage = new JLabel("<html>"+Language.helpMessage()+"</html>");
		layout.putConstraint(SpringLayout.NORTH, helpMessage, 5, SpringLayout.SOUTH, timeout);
		layout.putConstraint(SpringLayout.WEST, helpMessage, 5, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.EAST, helpMessage, -5, SpringLayout.EAST, contentPane);
		layout.putConstraint(SpringLayout.SOUTH, helpMessage, -5, SpringLayout.NORTH, progressBar);
		contentPane.add(helpMessage);


		eventManger = new EventManager(main, this);
		worldBox.addActionListener(eventManger);
		mapBox.addActionListener(eventManger);
		timeout.addChangeListener(eventManger);
		interestinonly.addChangeListener(eventManger);
		eventManger.start();
		System.out.println("[System] EventManager started");

		if( provider == null ) provider = Provider.getCurrentProvider(false);
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
				System.out.println("[GUI] Repainting...");
				contentPane.validate();
				contentPane.repaint();
			}
		});

	}

}
