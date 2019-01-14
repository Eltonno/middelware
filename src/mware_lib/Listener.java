package mware_lib;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.*;


public class Listener extends Thread {

    String host;
    int port;
    CommunicationModule com;
    ServerSocket ss;


    Listener(CommunicationModule com, ServerSocket ss, String host, int port){
        this.host = host;
        this.port = port;
        this.com = com;
        this.ss = ss;
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            socket = ss.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Listener läuft auf Port " + port);
        while(true){
            BufferedReader bufferedReader =
                    null;
            try {
                bufferedReader = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            String nachricht = null;
            try {
                nachricht = bufferedReader.readLine();
                System.out.println("Listener hat <<" + nachricht + ">> empfangen.");

            } catch (IOException e1) {
                e1.printStackTrace();
            }
            String[] empfangen = nachricht.split(";");
            if (empfangen.length == 6) {
                if (empfangen[0] == "remotecall") {
                    String[] arglist = empfangen[5].split(":");
                    Object[] params = new Object[arglist.length];
                    for (int i = 0; i < arglist.length; i++) {
                        if (arglist[i].matches("0-9")) {
                            params[i] = Integer.parseInt(arglist[i]);
                        } else if (arglist[i].matches("(0|([1-9][0-9]*))(\\.[0-9]+)")) {
                            params[i] = Double.parseDouble(arglist[i]);
                        } else {
                            params[i] = arglist[i];
                        }
                    }
                    try {
                        com.remoteCall(empfangen[1], Integer.parseInt(empfangen[2]), empfangen[3], empfangen[4], params);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
                else if (empfangen.length == 5){
                    if (empfangen[0] == "remotecall") {
                        try {
                            com.remoteCall(empfangen[1], Integer.parseInt(empfangen[2]), empfangen[3], empfangen[4]);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            else{
                //TODO: Exception werfen
                System.out.println(nachricht + " empfangen. Falsches Format!");
            }
            //WOHL FERTIG:RemoteCall abfangen und so splitten, dass man es bei CommunicationModule ausführen kann.

        }
    }
}
