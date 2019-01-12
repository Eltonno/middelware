package mware_lib;

public class CommunicationModule {
    int port;
    INameService nameService;
    boolean debug;
    ObjectBroker ob;
    String host;

    public CommunicationModule(String host, int port, INameService nameService, boolean debug, ObjectBroker ob) {
    this.port = port;
    this.nameService = nameService;
    this.debug = debug;
    this.ob = ob;
    this.host = host;
     new Listener(this, host, port).start();
    }

    public Object invoke(String name, String host, int port, String method, Object... args) {
    Sender s = new Sender(this, host, port, method, args);
    return s.invoke();
        //TODO: remoteCall befehl an das CommunicationModule von der Adresse der Objektreferenz

    }

    public void remoteCall(String sendtohost, int sendtoport, String objectName, String methodName, Object... arg){
        Object asdf = ob.localCall(objectName, methodName, arg);
        Sender se = new Sender(this, sendtohost, sendtoport, methodName, arg);
        se.sendResult(asdf);

    }

    public void shutdown() {
    }


    //TODO: EIN RECEIVER IST BENÖTIGT, DER WIE BEIM NAMESERVICE LAUSCHT
}
