package de.pakldev.gw2evno.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import de.pakldev.gw2evno.GW2EvNoMain;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange ex) throws IOException {
		String path = ex.getRequestURI().getPath();

		Map<String, String> q = new HashMap<String, String>();
		String query = ex.getRequestURI().getQuery();
		if( query != null && !query.isEmpty() ) {
			String[] s = query.split("&");
			for(String i : s) {
				String[] m = i.split("=", 2);
				if( m.length == 2 ) {
					q.put(m[0], m[1]);
				}
			}
		}

		String fileName = path.substring(path.lastIndexOf("/")+1);
		if( fileName.isEmpty() ) {
			path += "index.html";
		}

		path = path.replace("/icons/", "../");

		System.out.println("[Web] Requested file: "+ path);

		if( path.startsWith("/") ) {
			path = path.substring(1);
		}

		InputStream loadIs = GW2EvNoMain.class.getResourceAsStream("res/webtemplate/"+path);
		if( path.startsWith( "../") ) {
			path = path.substring(3);
			loadIs = GW2EvNoMain.class.getResourceAsStream("res/"+path);
		}

		if( loadIs == null ) {
			System.out.println("[Web] File not found: "+path);
			ex.sendResponseHeaders(404, -1);
			ex.close();
		} else {
			boolean queryable = false;
			boolean chuncked = true;
			ArrayList<String> contentType = new ArrayList<String>();
			if( path.endsWith(".html") || path.endsWith(".htm") ) {
				contentType.add("text/html; charset=utf-8");
				queryable = true;
				chuncked = false;
			}
			if( path.endsWith(".js") || path.endsWith(".css") ) {
				chuncked = false;
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

			OutputStream os = null;
			if( chuncked ) {
				ex.sendResponseHeaders(200, 0);
				os = ex.getResponseBody();
			}

			String readFirst = "";
			if( (queryable && q.size() > 0) || !chuncked ) {
				BufferedReader br = new BufferedReader(new InputStreamReader(loadIs));
				String line = "";
				while( (line = br.readLine()) != null ) {
					readFirst += (readFirst.isEmpty()?"":"\n") + line;
				}
				br.close();
			} else {
				int r = -1;
				while((r = loadIs.read()) >= 0) {
					int a = loadIs.available();
					byte[] b = new byte[a+1];
					b[0] = (byte)r;
					loadIs.read(b, 1, a);
					os.write(b);
				}
			}
			loadIs.close();

			if( queryable && !readFirst.isEmpty() ) {
				for(String key : q.keySet()) {
					readFirst = readFirst.replaceAll("\\{"+key+"}", q.get(key) );
				}
			}

			if( !chuncked ) {
				ex.sendResponseHeaders(200, readFirst.getBytes().length);
				os = ex.getResponseBody();
			}

			if( !chuncked || (queryable && !readFirst.isEmpty()) ) {
				os.write(readFirst.getBytes());
			}

			os.flush();
			os.close();
		}
		ex.close();
	}
}
