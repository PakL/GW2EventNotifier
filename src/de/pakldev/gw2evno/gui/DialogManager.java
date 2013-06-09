package de.pakldev.gw2evno.gui;

import com.sun.awt.AWTUtilities;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class DialogManager {

	private Map<Integer, MessageDialog> dialogs = new HashMap<Integer, MessageDialog>();

	public void newDialog(String message, Image icon, String wikiUrl, boolean interesting) {
		MessageDialog md = new MessageDialog(this, message, icon, wikiUrl, interesting);
		int i = 1;
		while( dialogs.containsKey(i) ) {
			i++;
		}
		dialogs.put(i, md);
		md.setLocation(md.getX(), md.getY()-(100*(i-1)));
		System.out.println("[Dialog] New" + (interesting ? " interesting" : "") + " dialog created");
	}

	public void closeDialog(MessageDialog dialog) {
		System.out.println("[Dialog] Dialog fading out");
		new Thread(new ClosingAnimation(dialog)).start();
	}

	public void removeDialog(MessageDialog dialog) {
		Map<Integer, MessageDialog> dialogs_ = new HashMap<Integer, MessageDialog>();
		for(int i : dialogs.keySet()) {
			if( !dialogs.get(i).equals(dialog) ) {
				dialogs_.put(i, dialogs.get(i));
			}
		}
		dialogs = dialogs_;
		dialog.setVisible(false);
		dialog.dispose();
		System.out.println("[Dialog] Dialog removed");
	}

	class ClosingAnimation implements Runnable {

		private MessageDialog dialog;
		public ClosingAnimation(MessageDialog dialog) {
			this.dialog = dialog;
		}

		@Override
		public void run() {
			long started = System.currentTimeMillis();
			while( (System.currentTimeMillis()-started) < 500 ) {
				float currop = AWTUtilities.getWindowOpacity(dialog);
				float newop = (1f-((float)(System.currentTimeMillis()-started) / 500));
				if( newop < currop )
					AWTUtilities.setWindowOpacity(dialog, newop);

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					break;
				}
			}

			removeDialog(dialog);
		}

	}

}
