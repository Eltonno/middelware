package mware_lib;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method.*;
import java.util.Arrays;


public class ObjectBroker {
    private boolean debug;
    private String host;
    private int port;
    private INameService nameService;
    private static ObjectBroker singleton = null;
    static CommunicationModule com;

    private ObjectBroker(String host, int port, boolean debug) throws IOException {
        //con = new ComHandler(host, port, debug);
        if (debug) {
            System.out.println("ObjectBroker gestartet mit host: " + host + " und port: " + port);
        }
        this.host = host;
        this.port = port;
        this.debug = debug;
        //this.debug = true;
        nameService = new INameService(host, port, this.debug, this);
        //Random rand = new Random(65535);
        //int listenerPort = rand.nextInt() + 1;
        //WOHL GELÖST: Abfangen ob Port schon besetzt ist. Vielleicht diesen Teil als Methode aussourcen.
        com = new CommunicationModule(host, nameService, this.debug, this);

    }

    public static ObjectBroker init(String serviceHost, int port, boolean debug) throws IOException {
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
        return com.invoke(name, host, port, methodName, args);
    }

    public String localCall(String name, String methodName, Object... args) {
        Object resolved = nameService.resolveLocally(name);
        if (debug) {
            System.out.println("OB>>> resolved Methods:" + Arrays.toString(resolved.getClass().getDeclaredMethods()));
        }
        Class[] argtypes = new Class[args.length];
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].getClass() == Integer.class) {
                    System.out.println(i + ":" + args[i] + "int");
                    argtypes[i] = int.class;
                } else if (args[i].getClass() == Double.class) {
                    System.out.println(i + ":" + args[i] + "double");
                    argtypes[i] = double.class;
                } else {
                    System.out.println(i + ":" + args[i] + "String");
                    argtypes[i] = String.class;
                }
            }
            if (debug) {
                System.out.println("OB>>> args:" + Arrays.toString(args) + "; argtypes: " + Arrays.toString(argtypes));
            }
        }
            try {
                return resolved.getClass().getDeclaredMethod(methodName, argtypes).invoke(resolved, args).toString();
            } catch (IllegalAccessException e) {
                return e.toString();
            } catch (InvocationTargetException e) {
                return e.getCause().toString();
            } catch (NoSuchMethodException e) {
                return e.toString();
            }catch (RuntimeException e){
            return e.toString();
            }
            //WOHL FERTIG: Mittels invoke die Methode ausführen
            //WOHL FERTIG: Das Ergebniss der Methode zurückgeben.



        //: EXCEPTION WERFEN
    }
}