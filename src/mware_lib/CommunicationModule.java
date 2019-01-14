package mware_lib;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class CommunicationModule {
    static int port;
    INameService nameService;
    boolean debug;
    ObjectBroker ob;
    static String host;

    public  CommunicationModule(String host, INameService nameService, boolean debug, ObjectBroker ob) throws IOException {
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
        System.out.println("CommunicationMoudle gestartet mit host: " + host + " und port: " + port);
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

    String getHost(){
        return host;
    }

    int getPort(){
        return port;
    }

    public static Object invoke(String name, String tohost, int toport, String method, Object... args) throws IOException {
        System.out.println("invoke beim CommunicationModule aufgerufen.");
        Sender s = new Sender(host, port, name, tohost, toport, method, args);
        return s.invoke();
        //WOHL FERTIG: remoteCall befehl an das CommunicationModule von der Adresse der Objektreferenz

    }

    public void remoteCall(String sendtohost, int sendtoport, String objectName, String methodName, Object... arg) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        System.out.println("CommunicationModule in remoteCall.");
        System.out.println("CommunicationModule sendet ObjectBroker localCall mit " + objectName + "," + methodName + "," + Arrays.toString(arg));

        Object asdf = ob.localCall(objectName, methodName, arg);
        Sender se = new Sender(host, port, "dummy", sendtohost, sendtoport, methodName, arg);
        se.sendResult(asdf);

    }


    public void shutdown() {
    }


}
