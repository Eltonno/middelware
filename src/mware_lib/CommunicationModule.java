package mware_lib;

public class CommunicationModule {
    int port;
    INameService nameService;
    boolean debug;
    ObjektBroker ob;
    Listener ls;

    public CommunicationModule(int port, INameService nameService, boolean debug, ObjectBroker ob) {
    this.port = port;
    this.nameService = nameService;
    this.debug = debug;
    this.ob = ob;
     ls = new RequestHandler(socket, ns).start();
    }

    public Object invoke(String host, int port, String method, Object... args) {
    Sender s = new Sender(host, port, method, args);
    return s.invoke();
        //TODO: remoteCall befehl an das CommunicationModule von der Adresse der Objektreferenz

    }

    public void remoteCall(String objectName, String methodName, Object... arg, String sendtohost, int sendtoport){
        Object asdf = ob.localCall(objectName, methodName, args);
        Sender se = new Sender(sendtohost, sendtoport, methodName, arg);
        se.sendResult(asdf);

    }

    public void shutdown() {
    }


    //TODO: EIN RECEIVER IST BENÖTIGT, DER WIE BEIM NAMESERVICE LAUSCHT
}
