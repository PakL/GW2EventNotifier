package de.pakldev.gw2evno.gui;

import de.pakldev.gw2evno.Configuration;
import de.pakldev.gw2evno.GW2EvNoMain;
import de.pakldev.gw2evno.gw2api.WorldNames;
import de.pakldev.gw2evno.web.WebInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SFMenu extends JMenuBar implements ActionListener {

	private final StartupFrame sf;

	private JMenu menuApplication = new JMenu("Application");
	private JRadioButtonMenuItem itemPaused = new JRadioButtonMenuItem("Paused");
	private JRadioButtonMenuItem itemRunning = new JRadioButtonMenuItem("Running");
	private JMenuItem itemReset = new JMenuItem("Reset settings");
	private JMenuItem itemClose = new JMenuItem("Close");

	private JMenu menuLanguage = new JMenu("Language");
	private JRadioButtonMenuItem itemLangEN = new JRadioButtonMenuItem("English");
	private JRadioButtonMenuItem itemLangDE = new JRadioButtonMenuItem("Deutsch");
	private JRadioButtonMenuItem itemLangFR = new JRadioButtonMenuItem("Français");
	private JRadioButtonMenuItem itemLangES = new JRadioButtonMenuItem("Español");

	private JMenu menuWebinterface = new JMenu("Web interface");
	private JRadioButtonMenuItem itemStopped = new JRadioButtonMenuItem("Stopped");
	private JRadioButtonMenuItem itemStarted = new JRadioButtonMenuItem("Started");
	private JMenuItem itemOpen = new JMenuItem("Open");

	private JMenu menuHelp = new JMenu("Help");
	private JMenuItem itemHelp = new JMenuItem("Help");
	private JMenuItem itemChangelog = new JMenuItem("Changelog");
	private JMenuItem itemReport = new JMenuItem("Report a bug");


	public SFMenu(StartupFrame startupFrame) {
		this.sf = startupFrame;

		ButtonGroup stateGroup = new ButtonGroup();
		itemPaused.setSelected(true);
		itemPaused.setEnabled(false);
		itemPaused.addActionListener(this);
		stateGroup.add(itemPaused);
		itemRunning.setEnabled(false);
		itemRunning.addActionListener(this);
		stateGroup.add(itemRunning);
		menuApplication.add(itemPaused);
		menuApplication.add(itemRunning);

		menuApplication.addSeparator();

		itemReset.addActionListener(this);
		menuApplication.add(itemReset);
		itemClose.addActionListener(this);
		menuApplication.add(itemClose);

		menuApplication.setMnemonic(KeyEvent.VK_A);
		this.add(menuApplication);

		ButtonGroup langGroup = new ButtonGroup();
		if( Configuration.language.equalsIgnoreCase("en") ) itemLangEN.setSelected(true);
		itemLangEN.addActionListener(this);
		langGroup.add(itemLangEN);
		menuLanguage.add(itemLangEN);
		if( Configuration.language.equalsIgnoreCase("de") ) itemLangDE.setSelected(true);
		itemLangDE.addActionListener(this);
		langGroup.add(itemLangDE);
		menuLanguage.add(itemLangDE);
		if( Configuration.language.equalsIgnoreCase("fr") ) itemLangFR.setSelected(true);
		itemLangFR.addActionListener(this);
		langGroup.add(itemLangFR);
		menuLanguage.add(itemLangFR);
		if( Configuration.language.equalsIgnoreCase("es") ) itemLangES.setSelected(true);
		itemLangES.addActionListener(this);
		langGroup.add(itemLangES);
		menuLanguage.add(itemLangES);

		menuLanguage.setEnabled(false);
		menuLanguage.setMnemonic(KeyEvent.VK_L);
		this.add(menuLanguage);

		ButtonGroup webGroup = new ButtonGroup();
		itemStopped.setSelected(true);
		itemStopped.addActionListener(this);
		webGroup.add(itemStopped);
		menuWebinterface.add(itemStopped);
		itemStarted.addActionListener(this);
		webGroup.add(itemStarted);
		menuWebinterface.add(itemStarted);
		menuWebinterface.addSeparator();
		itemOpen.addActionListener(this);
		menuWebinterface.add(itemOpen);

		menuWebinterface.setMnemonic(KeyEvent.VK_W);
		menuWebinterface.setEnabled(false);
		this.add(menuWebinterface);

		itemHelp.addActionListener(this);
		menuHelp.add(itemHelp);

		itemChangelog.addActionListener(this);
		menuHelp.add(itemChangelog);

		itemReport.addActionListener(this);
		menuHelp.add(itemReport);

		menuHelp.setMnemonic(KeyEvent.VK_H);
		this.add(menuHelp);

		this.add(Box.createRigidArea(new Dimension(0, 25)));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == itemReset ) {
			int r = JOptionPane.showConfirmDialog(sf, "This resets all configuration settings.\nThe application is going to restart itself afterwards.\nShould I continue?", "Settings reseting", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if( r == 0 ) {
				Configuration.resetConfig();
				GW2EvNoMain.restartApplication();
			}
		} else if( e.getSource() == itemClose ) {
			System.exit(0);
		} else if( e.getSource() == itemHelp ) {
			GW2EvNoMain.openUrl("https://github.com/PakL/GW2EventNotifier/wiki");
		} else if( e.getSource() == itemChangelog ) {
			GW2EvNoMain.openUrl("https://github.com/PakL/GW2EventNotifier/wiki/Changelog");
		} else if( e.getSource() == itemReport ) {
			GW2EvNoMain.openUrl("https://github.com/PakL/GW2EventNotifier/issues");
		} else if( e.getSource() == itemOpen ) {
			try {
				String localhostname = InetAddress.getLocalHost().getCanonicalHostName();
				GW2EvNoMain.openUrl("http://"+localhostname+":"+Configuration.webPort+"/");
			} catch (UnknownHostException e1) {}
		} else if( e.getSource() == itemLangEN || e.getSource() == itemLangDE || e.getSource() == itemLangFR || e.getSource() == itemLangES ) {
			if( itemLangEN.isSelected() ) {
				sf.loadLanguage(WorldNames.LANG_EN);
			} else if( itemLangDE.isSelected() ) {
				sf.loadLanguage(WorldNames.LANG_DE);
			} else if( itemLangFR.isSelected() ) {
				sf.loadLanguage(WorldNames.LANG_FR);
			} else if( itemLangES.isSelected() ) {
				sf.loadLanguage(WorldNames.LANG_ES);
			}
		} else if( e.getSource() == itemPaused || e.getSource() == itemRunning ) {
			if( itemPaused.isSelected() ) {
				sf.stopEventManager();
			} else if( itemRunning.isSelected() ) {
				sf.startEventManager();
			}
		} else if( e.getSource() == itemStopped || e.getSource() == itemStarted ) {
			if( itemStopped.isSelected() ) {
				sf.getMain().web.stop();
			} else if( itemStarted.isSelected() ) {
				sf.getMain().web.start(Configuration.webPort, Configuration.webSocketPort);
			}
		}
	}

	public static final int APPLICATION_STATE_PAUSED = 11;
	public static final int APPLICATION_STATE_RUNNING = 12;
	public void setApplicationState(int state) {
		if(state == APPLICATION_STATE_PAUSED) {
			itemPaused.setSelected(true);
			itemRunning.setSelected(false);
		} else if(state == APPLICATION_STATE_RUNNING) {
			itemPaused.setSelected(false);
			itemRunning.setSelected(true);
		}
	}
	public void setWebinterfaceState(int state) {
		if(state == WebInterface.WEB_STATE_STOPPED) {
			itemStopped.setSelected(true);
			itemStarted.setSelected(false);
		} else if(state == WebInterface.WEB_STATE_STARTED) {
			itemStopped.setSelected(false);
			itemStarted.setSelected(true);
		}
	}

	public void applicationEnabled(boolean enabled) {
		itemPaused.setEnabled(enabled);
		itemRunning.setEnabled(enabled);
	}

	public void languageEnabled(boolean enabled) {
		menuLanguage.setEnabled(enabled);
	}

	public void webinterfaceEnabled(boolean enabled) {
		menuWebinterface.setEnabled(enabled);
	}

}
