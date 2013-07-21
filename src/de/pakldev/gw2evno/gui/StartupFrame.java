package de.pakldev.gw2evno.gui;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;
import de.pakldev.gw2evno.Configuration;
import de.pakldev.gw2evno.GW2EvNoMain;
import de.pakldev.gw2evno.web.WebInterface;

import javax.swing.*;
import java.awt.*;

public class StartupFrame extends JFrame {

	private final GW2EvNoMain main;
	private EventManager eventManger;
	private Provider provider = null;

	private SpringLayout layout = new SpringLayout();

	private SFMenu menuBar;
	private SFStatusBar statusBar = new SFStatusBar(this);
	private SFReqsettings reqSettings = new SFReqsettings(this);
	private SFEnvironmentStatus envStatus = new SFEnvironmentStatus(this);
	private SFAppsettings appSettings = new SFAppsettings(this);

	private boolean pauseAfterReady = false;

	public StartupFrame(GW2EvNoMain main, boolean startuppaused) {
		this.main = main;
		pauseAfterReady = startuppaused;
		menuBar = new SFMenu(this);

		this.setTitle("GW2 Event Notifier");
		this.setSize(400, 340);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container contentPane = this.getContentPane();
		contentPane.setLayout(layout);

		this.setJMenuBar(menuBar);

		layout.putConstraint(SpringLayout.SOUTH, statusBar, 0, SpringLayout.SOUTH, contentPane);
		layout.putConstraint(SpringLayout.WEST, statusBar, 0, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.EAST, statusBar, 0, SpringLayout.EAST, contentPane);
		layout.putConstraint(SpringLayout.NORTH, statusBar, -25, SpringLayout.SOUTH, statusBar);
		contentPane.add(statusBar);

		JPanel scrollPanel = new JPanel();
		SpringLayout scrollLayout = new SpringLayout();
		scrollPanel.setLayout(scrollLayout);

		scrollLayout.putConstraint(SpringLayout.NORTH, reqSettings, 5, SpringLayout.NORTH, scrollPanel);
		scrollLayout.putConstraint(SpringLayout.WEST, reqSettings, 5, SpringLayout.WEST, scrollPanel);
		scrollLayout.putConstraint(SpringLayout.EAST, reqSettings, -5, SpringLayout.EAST, scrollPanel);
		scrollPanel.add(reqSettings);

		scrollLayout.putConstraint(SpringLayout.NORTH, envStatus, 5, SpringLayout.SOUTH, reqSettings);
		scrollLayout.putConstraint(SpringLayout.WEST, envStatus, 5, SpringLayout.WEST, scrollPanel);
		scrollLayout.putConstraint(SpringLayout.EAST, envStatus, -5, SpringLayout.EAST, scrollPanel);
		scrollPanel.add(envStatus);

		scrollLayout.putConstraint(SpringLayout.NORTH, appSettings, 5, SpringLayout.SOUTH, envStatus);
		scrollLayout.putConstraint(SpringLayout.WEST, appSettings, 5, SpringLayout.WEST, scrollPanel);
		scrollLayout.putConstraint(SpringLayout.EAST, appSettings, -5, SpringLayout.EAST, scrollPanel);
		scrollPanel.add(appSettings);

		scrollLayout.putConstraint(SpringLayout.SOUTH, scrollPanel, 5, SpringLayout.SOUTH, appSettings);
		JScrollPane scroll = new JScrollPane(scrollPanel);
		scroll.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		layout.putConstraint(SpringLayout.NORTH, scroll, 0, SpringLayout.NORTH, contentPane);
		layout.putConstraint(SpringLayout.WEST, scroll, 0, SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.EAST, scroll, 0, SpringLayout.EAST, contentPane);
		layout.putConstraint(SpringLayout.SOUTH, scroll, 0, SpringLayout.NORTH, statusBar);
		contentPane.add(scroll);

		this.setVisible(true);
	}

	public GW2EvNoMain getMain() {
		return main;
	}

	public void loadLanguage(String lang) {
		main.loadLanguage(lang);
	}
	public void setWebinterfaceEnabled(boolean enabled) {
		menuBar.webinterfaceEnabled(enabled);
	}

	public void setStatusAndProgress(String label, int value) {
		statusBar.setStatusAndProgress(label, value);
	}
	public void setStatusAndProgress(String label, int value, int max) {
		statusBar.setStatusAndProgress(label, value, max);
	}
	public void setStatus(String label) {
		statusBar.setStatus(label);
	}
	public void setProgress(int value) {
		statusBar.setProgress(value);
	}
	public void setMaximumProgress(int max) {
		statusBar.setMaximumProgress(max);
	}

	public int getWorldIndex() {
		return reqSettings.getWorldIndex();
	}
	public int getMapIndex() {
		return reqSettings.getMapIndex();
	}
	public boolean isInterestingOnly() {
		return reqSettings.isInterestingOnly();
	}
	public void setWorldIndex(int i) {
		reqSettings.setWorldIndex(i);
	}
	public void setMapIndex(int i) {
		reqSettings.setMapIndex(i);
	}
	public void setInterestinOnly(boolean io){
		reqSettings.setInterestinOnly(io);
	}

	public void setApplicationState(int state) {
		menuBar.setApplicationState(state);
		envStatus.setApplicationState(state);
	}
	public int getApplicationState() {
		return menuBar.getApplicationState();
	}
	public void setWebinterfaceState(int state) {
		envStatus.setWebinterfaceState(state);
		menuBar.setWebinterfaceState(state);
	}

	public void startEventManager() {
		if(eventManger != null) {
			eventManger.start();
		}
	}
	public void stopEventManager() {
		if(eventManger != null) {
			eventManger.stop();
		}
	}

	public void setMapEvents(int events) {
		envStatus.setMapEvents(events);
	}
	public void setInterestingEvents(int events) {
		envStatus.setInterestingEvents(events);
	}

	public void setNewHotKey(int modifier, int keycode) {
		if( provider != null ) {
			provider.reset();
			final StartupFrame sf = this;
			provider.register(KeyStroke.getKeyStroke(keycode, modifier), new HotKeyListener() {
				@Override
				public void onHotKey(HotKey hotKey) {
					new SearchMap(main, sf);
				}
			});
		}
	}
	public void setWebportEnabled(boolean enabled) {
		appSettings.setWebportEnabled(enabled);
	}
	public boolean setNewWebPort(int port) {
		appSettings.setWebportEnabled(false);
		boolean r = true;
		if( main.web.getState() == WebInterface.WEB_STATE_STARTED ) {
			main.web.stop();
			r = main.web.start(port, Configuration.webSocketPort);
		}
		appSettings.setWebportEnabled(true);
		return r;
	}
	public boolean setNewWebSocketPort(int port) {
		appSettings.setWebportEnabled(false);
		boolean r = true;
		if( main.web.getState() == WebInterface.WEB_STATE_STARTED ) {
			main.web.stop();
			r = main.web.start(Configuration.webPort, port);
		}
		appSettings.setWebportEnabled(true);
		return r;
	}

	public EventManager getEventManger() {
		return eventManger;
	}

	public void loading() {
		System.out.println("[GUI] Setting to loading mode...");
		if( eventManger != null ) {
			eventManger.stop();
		}
		if( provider != null ) {
			appSettings.setHotkeyEnabled(false);
			provider.reset();
		}

		menuBar.applicationEnabled(false);
		menuBar.languageEnabled(false);
		statusBar.setStatusAndProgress("Preparing...", -1, 3);
		reqSettings.setDisabledAll(true);
	}

	public void ready() {
		System.out.println("[GUI] Setting to ready mode...");

		reqSettings.fillWorld(main.worlds.getWorlds().values().toArray(new String[0]));
		reqSettings.fillMap(main.maps.getMaps().values().toArray(new String[0]));

		reqSettings.setWorldIndex(Configuration.worldIndex);
		reqSettings.setMapIndex(Configuration.mapIndex);
		reqSettings.setTimeout(Configuration.timeout);
		reqSettings.setInterestinOnly(Configuration.interestingOnly);

		eventManger = new EventManager(main, this);
		reqSettings.registerEventManager(eventManger);
		if( !pauseAfterReady ) {
			eventManger.start();
			System.out.println("[System] EventManager started");
		}

		reqSettings.setDisabledAll(false);
		menuBar.applicationEnabled(true);
		menuBar.languageEnabled(true);

		if( provider == null ) provider = Provider.getCurrentProvider(false);
		final StartupFrame sf = this;
		provider.register(KeyStroke.getKeyStroke(Configuration.hotkeyKey, Configuration.hotkeyMod), new HotKeyListener() {
			@Override
			public void onHotKey(HotKey hotKey) {
				new SearchMap(main, sf);
			}
		});
		appSettings.setHotkeyEnabled(true);
	}

}
