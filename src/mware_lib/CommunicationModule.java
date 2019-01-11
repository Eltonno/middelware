package mware_lib;

public class CommunicationModule {
    int port;
    INameService nameService;
    boolean debug;
    ObjektBroker ob;

    public CommunicationModule(int port, INameService nameService, boolean debug, ObjectBroker ob) {
    this.port = port;
    this.nameService = nameService;
    this.debug = debug;
    this.ob = ob;
    }

    public Object invoke(ObjectReference ref, String method, Object... args) {

        //TODO: remoteCall befehl an ObjektBroker von der Adresse der Objektreferenz

    }

    public Object remoteCall(String objectName, String methodName, Object... arg){
        return ob.localCall(objectName, methodName, Object... args);
    }

    public void shutdown() {
    }

    //TODO: EIN RECEIVER IST BENÖTIGT, DER WIE BEIM NAMESERVICE LAUSCHT
}
