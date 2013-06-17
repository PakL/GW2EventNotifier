package de.pakldev.gw2evno.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import de.pakldev.gw2evno.GW2EvNoMain;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class FileHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange ex) throws IOException {
		String path = ex.getRequestURI().getPath();

		String fileName = path.substring(path.lastIndexOf("/")+1);
		if( fileName.isEmpty() ) {
			path += "index.html";
		}

		path = path.replace("/icons/", "../");

		System.out.println("[Web] Requested file: "+ path);

		InputStream loadIs = GW2EvNoMain.class.getResourceAsStream("res/webtemplate/"+path);

		if( loadIs == null ) {
			System.out.println("[Web] File not found: "+path);
			ex.sendResponseHeaders(404, -1);
			ex.close();
		} else {
			ArrayList<String> contentType = new ArrayList<String>();
			if( path.endsWith(".html") || path.endsWith(".htm") ) {
				contentType.add("text/html; charset=utf-8");
			}
			if( path.endsWith(".png") ) {
				contentType.add("image/png");
			}
			if( path.endsWith(".jpg") || path.endsWith(".jpeg") ) {
				contentType.add("image/jpeg");
			}

			if( contentType.size() > 0 ) {
				ex.getResponseHeaders().put("Content-Type", contentType);
			}

			ex.sendResponseHeaders(200, 0);
			OutputStream os = ex.getResponseBody();
			int r = -1;
			while((r = loadIs.read()) >= 0) {
				int a = loadIs.available();
				byte[] b = new byte[a+1];
				b[0] = (byte)r;
				loadIs.read(b, 1, a);
				os.write(b);
			}

			loadIs.close();
			os.flush();
			os.close();
		}
		ex.close();
	}
}
