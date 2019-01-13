package mware_lib;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;

public class CommunicationModule {
    int port;
    INameService nameService;
    boolean debug;
    ObjectBroker ob;
    String host;

    public CommunicationModule(String host, INameService nameService, boolean debug, ObjectBroker ob) throws IOException {
    this.nameService = nameService;
    this.debug = debug;
    this.ob = ob;
    this.host = host;
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        port = ss.getLocalPort();
        //EIN RECEIVER IST BENÖTIGT, DER WIE BEIM NAMESERVICE LAUSCHT
        new Listener(this, ss.accept(), host, port).start();
    }

    public Object invoke(String name, String host, int port, String method, Object... args) throws IOException {
    Sender s = new Sender(this, host, port, method, args);
    return s.invoke();
        //TODO: remoteCall befehl an das CommunicationModule von der Adresse der Objektreferenz

    }

    public void remoteCall(String sendtohost, int sendtoport, String objectName, String methodName, Object... arg) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Object asdf = ob.localCall(objectName, methodName, arg);
        Sender se = new Sender(this, sendtohost, sendtoport, methodName, arg);
        se.sendResult(asdf);

    }


    public void shutdown() {
    }


}
