package mware_lib;

public class INameService extends NameService {
    private String host;
    private int port;
    private boolean debug;

    INameService(String host, int port, boolean debug) {
        this.host = host;
        this.port = port;
        this.debug = debug;
    }

    @Override
    public void rebind(Object servant, String name) {

    }

    @Override
    public Object resolve(String name) {
        return null;
    }
}
