package mware_lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class INameService extends NameService {
    private String host;
    private int port;
    private boolean debug;
    HashMap objects = new HashMap();
    Socket socket;

    INameService(String host, int port, boolean debug) throws UnknownHostException, IOException {
        this.host = host;
        this.port = port;
        this.debug = debug;
		socket = new Socket(host,port); // verbindet sich mit Server
    }
// TODO: Neuer Thread für jedes Rebind um Fehlern vorzubeugen
    @Override
    public void rebind(Object servant, String name) {
    	System.out.println(servant.getClass());
    	try {
			schreibeNachricht(socket, "{rebind;" + name + ";" + host + ";" + port + "}");
			Object antwort = leseNachricht(socket);
		 	System.out.println(antwort);
		 	objects.put(name, servant);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
// TODO: Neuer Thread für jedes resolve, um Blockaden durch Fehlern vorzubeugen
// TODO: Fehlerbehandlung implementieren, falls Object nicht im Nameservice registriert ist
    @Override
	//
    public Object resolve(String name) {
    	try {
			schreibeNachricht(socket, "{resolve;" + name + "; ; }");
			String antwort = leseNachricht(socket);
		 	System.out.println(antwort);
		 //	if(antwort != "null"){
		 //	String[] values = antwort.split(",");
		//	if (values[1] == host && (values[2] == Integer.toString(port))){
	//			return resolveLocal(name);
//			}
		//}
		 	return antwort;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Error";
    }

    public Object resolveLocally(String name){
    	return objects.getOrDefault(name, null);
    	//TODO: Fehlerbehandlung vielleicht? Was wenn nicht vorhanden?
		//TODO :|| Wenn nicht vorhanden null zurück geben ist aus meiner sicht das logischte
	}
//TODO: warum gibt es die funktion theoretisch doppelt?
    public Object resolveLocal(String name){
    	return objects.get(name);
	}

	@Override
	public void shutdown() {
		try {
			schreibeNachricht(socket, "{shutdown;good;bye}");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void schreibeNachricht(Socket socket, String nachricht) throws IOException {
    	PrintWriter printWriter =
	    new PrintWriter(
	 	new OutputStreamWriter(
		    socket.getOutputStream()));
	printWriter.print(nachricht);
	printWriter.flush();
   }
    
   String leseNachricht(Socket socket) throws IOException {
	BufferedReader bufferedReader =
	    new BufferedReader(
		new InputStreamReader(
	  	    socket.getInputStream()));
	char[] buffer = new char[200];
	int anzahlZeichen = bufferedReader.read(buffer, 0, 200); // blockiert bis Nachricht empfangen
	String nachricht = new String(buffer, 0, anzahlZeichen);
	if (nachricht == "null" || nachricht == null)
		return null;
	return nachricht;
   }
}
