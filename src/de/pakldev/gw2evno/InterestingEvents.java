package de.pakldev.gw2evno;

import java.io.*;

public class InterestingEvents {

	public static String[] eventIds = new String[]{
		"568A30CF-8512-462F-9D67-647D69BEFAED",
		"03BF176A-D59F-49CA-A311-39FC6F533F2F",
		"0464CB9E-1848-4AAA-BA31-4779A959DD71"
	};

	public static void loadInterestingEvents() {
		File evidFile = new File("gw2InterestingEvents.cfg");
		String evid = "";
		if( !evidFile.exists() ) {
			for(String e : InterestingEvents.eventIds) {
				if( !e.isEmpty() )
					evid += e + "\n";
			}

			try {
				OutputStream os = new FileOutputStream(evidFile);
				os.write(evid.getBytes());
				os.flush();
				os.close();
			} catch(Exception e) {}
		}
		try {
			InputStream is = new FileInputStream(evidFile);
			int r = -1;
			while((r = is.read()) >= 0) {
				int a = is.available();
				byte[] b = new byte[a+1];
				b[0] = (byte)r;
				is.read(b, 1, a);
				evid += new String(b);
			}
			is.close();
		} catch (Exception e) {}
		String[] eventIds = evid.split("\n");
	}

}
