package mware_lib;

import java.io.IOException;

public class ObjectBroker{
    private boolean debug;
    private String host;
    private int port;
    private NameService nameService;
    private static ObjectBroker singleton = null;
    CommunicationModule com;

    private ObjectBroker(String host, int port, boolean debug) throws IOException {
        //con = new ComHandler(host, port, debug);
        this.host = host;
        this.port = port;
        this.debug = debug;
        nameService = new INameService(host, port, debug);
        com = new CommunicationModule(port, nameService, debug);
    }

    public static ObjectBroker init(String serviceHost, int port, boolean debug) throws IOException {
        if (singleton==null)
            return singleton = new ObjectBroker(serviceHost, port, debug);
        return singleton;
    }

    // Liefert den Namensdienst (Stellvetreterobjekt).
    public NameService getNameService() {
        return this.nameService;
    }

    // Beendet die Benutzung der Middleware in dieser Anwendung.
    public void shutDown() throws IOException {
        if (debug)
            System.out.println("Object Brooker heruntergefahren");
        com.shutdown();
    }
}

/*
public void shutdown() throws IOException {
		is.close();
		os.close();
		socketOut.close();
	}
	*/