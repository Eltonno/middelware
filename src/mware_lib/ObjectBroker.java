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
    static CommunicationModule com;

    private ObjectBroker(String host, int port, boolean debug) throws IOException {
        //con = new ComHandler(host, port, debug);
        System.out.println("ObjectBroker gestartet mit host: " + host + " und port: " + port);
        this.host = host;
        this.port = port;
        this.debug = debug;
        nameService = new INameService(host, port, debug, this);
        //Random rand = new Random(65535);
        //int listenerPort = rand.nextInt() + 1;
        //WOHL GELÖST: Abfangen ob Port schon besetzt ist. Vielleicht diesen Teil als Methode aussourcen.
        System.out.println("ob_co"+Integer.toString(port));
        com = new CommunicationModule(host, nameService, debug, this);

    }

    public static ObjectBroker init(String serviceHost, int port, boolean debug) throws IOException {
        System.out.println("ob_in"+Integer.toString(port));
        if (singleton == null)
            return singleton = new ObjectBroker(serviceHost, port, debug);
        return singleton;
    }

    public static CommunicationModule getCom(){
        return com;
    }

    // Liefert den Namensdienst (Stellvetreterobjekt).
    public NameService getNameService() {
        return this.nameService;
    }

    // Beendet die Benutzung der Middleware in dieser Anwendung.
    public void shutDown() throws IOException {
        if (debug)
            System.out.println("Object Brooker heruntergefahren");
        System.exit(0);
        //SnameService.shutdown();
    }

    public Object remoteCall(String name, String host, int port, String methodName, Object... args) throws IOException {
        System.out.println("ob_rc"+Integer.toString(port));
        return com.invoke(name, host, port, methodName, args);
    }

    public Object localCall(String name, String methodName, Object... args) {
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
            try {
                return resolved.getClass().getDeclaredMethod(methodName, argtypes).invoke(resolved, args);
            } catch (IllegalAccessException e) {
                return e;
            } catch (InvocationTargetException e) {
                return e;
            } catch (NoSuchMethodException e) {
                return e;
            }
            //WOHL FERTIG: Mittels invoke die Methode ausführen
            //WOHL FERTIG: Das Ergebniss der Methode zurückgeben.


        }
        //TODO: EXCEPTION WERFEN
        return "error";
    }
}