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
    private String nshost;
    private int nsport;
    private boolean debug;
    ObjectBroker ob;
    HashMap objects = new HashMap();
    Socket socket;

    INameService(String host, int port, boolean debug, ObjectBroker ob) throws UnknownHostException, IOException {
        this.nshost = host;
		System.out.println("ns"+Integer.toString(port));
        this.nsport = port;
        this.debug = debug;
        this.ob = ob;
		socket = new Socket(host,port); // verbindet sich mit Server
		System.out.println("INameService gestartet mit host: " + host + " und port: " + port);
	}
// TODO: Neuer Thread für jedes Rebind um Fehlern vorzubeugen
    @Override
    public void rebind(Object servant, String name) {
    	System.out.println("rebind beim INameservice aufgerufen.");
    	try {
    		String host = ob.getCom().getHost();
    		int port = ob.getCom().getPort();
			System.out.println("INameservice sendet an Nameservice folgende Nachricht: " + "{rebind;" + name + ";" + host + ";" + Integer.toString(port) + "}");

			schreibeNachricht(socket, "{rebind;" + name + ";" + host + ";" + Integer.toString(port) + "}");
			Object antwort = leseNachricht(socket);
		 	System.out.println(antwort + " als Antwort vom Nameservice bekommen");
		 	objects.put(name, servant);
			System.out.println(servant + " unter " + name + " beim INameservice gespeichert.");

		} catch (IOException e) {
			e.printStackTrace();
		}
    }
// TODO: Neuer Thread für jedes resolve, um Blockaden durch Fehlern vorzubeugen
// TODO: Fehlerbehandlung implementieren, falls Object nicht im Nameservice registriert ist
    @Override
	//
    public Object resolve(String name) {
		System.out.println("resolve beim INameservice aufgerufen.");

		try {
			System.out.println("{resolve;" + name + "; ; } an den Nameservice gesendet");
			schreibeNachricht(socket, "{resolve;" + name + "; ; }");
			String antwort = leseNachricht(socket);
			//antwort.replace("\"", " ");
		 	System.out.println(antwort + " vom Nameservice als Antwort empfangen");
		 	if(antwort.matches("null")){
		 		return null;
			}
		 //	String[] values = antwort.split(",");
		//	if (values[1] == host && (values[2] == Integer.toString(port))){
	//			return resolveLocal(name);
//			}
		//}
		 	return antwort;
		} catch (IOException e) {
			return e;
		}
    }

    public Object resolveLocally(String name){
		System.out.println("resolveLocally im INameServer gibt " + objects.getOrDefault(name, null) + " zurück");

		return objects.getOrDefault(name, null);
    	//TODO: Fehlerbehandlung vielleicht? Was wenn nicht vorhanden?
		//TODO :|| Wenn nicht vorhanden null zurück geben ist aus meiner sicht das logischte
	}
//TODO: warum gibt es die funktion theoretisch doppelt?
	//TODO: War wohl aus Versehen.
   // public Object resolveLocal(String name){
   // 	return objects.get(name);
	//}

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
