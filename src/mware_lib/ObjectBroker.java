package mware_lib;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method.*;


public class ObjectBroker {
    private boolean debug;
    private String host;
    private int port;
    private INameService nameService;
    private static ObjectBroker singleton = null;
    CommunicationModule com;

    private ObjectBroker(String host, int port, boolean debug) throws IOException {
        //con = new ComHandler(host, port, debug);
        this.host = host;
        this.port = port;
        this.debug = debug;
        nameService = new INameService(host, port, debug);
        //Random rand = new Random(65535);
        //int listenerPort = rand.nextInt() + 1;
        //WOHL GELÖST: Abfangen ob Port schon besetzt ist. Vielleicht diesen Teil als Methode aussourcen.
        com = new CommunicationModule(host, nameService, debug, this);
    }

    public static ObjectBroker init(String serviceHost, int port, boolean debug) throws IOException {
        if (singleton == null)
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
        //SnameService.shutdown();
    }

    public Object remoteCall(String name, String host, int port, String methodName, Object... args) throws IOException {
        return com.invoke(name, host, port, methodName, args);
    }

    public Object localCall(String name, String methodName, Object... args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object resolved = nameService.resolveLocally(name);
        Class[] argtypes = new Class[args.length];
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].getClass() == Integer.class) {
                    argtypes[i] = Integer.class;
                } else if (args[i].getClass() == Double.class) {
                    argtypes[i] = Double.class;
                } else {
                    argtypes[i] = String.class;
                }
            }
            return resolved.getClass().getDeclaredMethod(methodName, argtypes).invoke(resolved, args);
            //WOHL FERTIG: Mittels invoke die Methode ausführen
            //WOHL FERTIG: Das Ergebniss der Methode zurückgeben.


        }
        //TODO: EXCEPTION WERFEN
        return "error";
    }
}