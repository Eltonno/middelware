package mware_lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class INameService extends NameService {
    private String host;
    private int port;
    private boolean debug;
    Socket socket;

    INameService(String host, int port, boolean debug) throws UnknownHostException, IOException {
        this.host = host;
        this.port = port;
        this.debug = debug;
		java.net.Socket socket = new java.net.Socket("192.168.0.103",25000); // verbindet sich mit Server
    }

    @Override
    public void rebind(Object servant, String name) {
    	try {
			schreibeNachricht(socket, "{rebind," + servant.toString() + "," + name + "}");
			String antwort = leseNachricht(socket);
		 	System.out.println(antwort);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Override
    public Object resolve(String name) {
    	try {
			schreibeNachricht(socket, "{rebind, ," + name + "}");
			String antwort = leseNachricht(socket);
		 	System.out.println(antwort);
		 	return antwort;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Error";
    }
    
    void schreibeNachricht(java.net.Socket socket, String nachricht) throws IOException {
	 PrintWriter printWriter =
	    new PrintWriter(
	 	new OutputStreamWriter(
		    socket.getOutputStream()));
	printWriter.print(nachricht);
	printWriter.flush();
   }
    
   String leseNachricht(java.net.Socket socket) throws IOException {
	BufferedReader bufferedReader =
	    new BufferedReader(
		new InputStreamReader(
	  	    socket.getInputStream()));
	char[] buffer = new char[200];
	int anzahlZeichen = bufferedReader.read(buffer, 0, 200); // blockiert bis Nachricht empfangen
	String nachricht = new String(buffer, 0, anzahlZeichen);
	return nachricht;
   }
}
