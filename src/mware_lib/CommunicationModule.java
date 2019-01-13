package mware_lib;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

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
        //TODO: Habe das socket.accept in die Listener Class umgepackt, weil er sich da sonst dran aufgehangen hatteS
        //EIN RECEIVER IST BENÖTIGT, DER WIE BEIM NAMESERVICE LAUSCHT
        // Socket sock = null;
        //try {
        //    sock = ss.accept();
        //}catch (Exception e){
        //    System.out.println(e);
        //}
        //System.out.println(sock);
        Listener listen = new Listener(this, ss, host, port);
        listen.start();
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
