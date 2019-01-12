package mware_lib;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Listener extends Thread {

    String host;
    int port;
    CommunicationModule com;

    Listener(CommunicationModule com, String host, int port){
        this.host = host;
        this.port = port;
        this.com = com;
    }

    @Override
    public void run() {
        serversocket = new ServerSocket(port);
        System.out.println("Listener läuft");
        while(true){
            try{
                socket = serversocket.accept();
                BufferedReader bufferedReader =
                        new BufferedReader(
                                new InputStreamReader(
                                        socket.getInputStream()));
                String nachricht = bufferedReader.readLine();
                //TODO:RemoteCall abfangen und so splitten, dass man es bei CommunicationModule ausführen kann.
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            // new thread for a client

        }
    }
}
