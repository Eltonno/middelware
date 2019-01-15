package mware_lib;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.util.Arrays;


public class Listener extends Thread {

    String host;
    int port;
    CommunicationModule com;
    ServerSocket ss;


    Listener(CommunicationModule com, ServerSocket ss, String host, int port) {
        this.host = host;
        this.port = port;
        this.com = com;
        this.ss = ss;
    }

    @Override
    public void run() {
        System.out.println("Listener läuft auf Port " + port);
        Socket socket = null;

        while (true) {
           // System.out.println("Listener>> In While True");
            try {
                socket = ss.accept();
            //    System.out.println("Listener>> Verbindung accepted");
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader bufferedReader =
                    null;
            try {
                bufferedReader =
                        new BufferedReader(
                                new InputStreamReader(
                                        socket.getInputStream()));
                char[] buffer = new char[250];
               // System.out.println("Listener>>Nun sollte er lesen");
                int anzahlZeichen = bufferedReader.read(buffer, 0, 250); // blockiert bis Nachricht empfangen
                String nachricht = new String(buffer, 0, anzahlZeichen);
                //System.out.println("Listener hat <<" + nachricht + ">> empfangen.");

                String[] empfangen = nachricht.split(";");
                if (empfangen.length == 6) {
                    //System.out.println("Listener>>Empfangen.length = 6. empfangen[0] = " + empfangen[0]);
                    if (empfangen[0].matches("remotecall")) {
                       // System.out.println("Listener>>remotecall erkannt");
                        String[] arglist = empfangen[5].split(":");
                        Object[] params = new Object[arglist.length];
                        for (int i = 0; i < arglist.length; i++) {
                            if (arglist[i].matches("-?\\d+")) {
                                params[i] = Integer.parseInt(arglist[i]);
                            } else if (arglist[i].matches("(-?(\\d)+(\\.)?(\\d)*)")) {
                                params[i] = Double.parseDouble(arglist[i]);
                            } else {
                                params[i] = arglist[i];
                            }
                        }
                        try {
                            // System.out.println("Listener>>com.remoteCall aufrufen");
                            String zuruck = com.remoteCall(empfangen[1], Integer.parseInt(empfangen[2]), empfangen[3], empfangen[4], params);
                            //System.out.println("Listener>>com.remoteCall Ergebnis war " + zuruck);
                            PrintWriter printWriter =
                                    new PrintWriter(
                                            new OutputStreamWriter(
                                                    socket.getOutputStream()));
                            //printWriter.print("ping");
                            //printWriter.flush();
                            printWriter.print(zuruck);
                            printWriter.flush();/*
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (StringIndexOutOfBoundsException e){
                            System.out.println(e);

                        }*/
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                } else if (empfangen.length == 5) {
                   // System.out.println("Listener>>Empfangen.length = 5");
                    if (empfangen[0].matches("remotecall")) {
                     //   System.out.println("Listener>>remotecall erkannt");
                        try {
                        //    System.out.println("Listener>>com.remoteCall aufrufen");
                            com.remoteCall(empfangen[1], Integer.parseInt(empfangen[2]), empfangen[3], empfangen[4]);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }catch (StringIndexOutOfBoundsException e){
                            System.out.println(e);
                        }
                    }
                } else {
                    //TODO: Exception werfen
                    System.out.println(nachricht + " empfangen. Falsches Format!");
                }
                //WOHL FERTIG:RemoteCall abfangen und so splitten, dass man es bei CommunicationModule ausführen kann.

            } catch (IOException e) {
                e.printStackTrace();
            }catch (StringIndexOutOfBoundsException e){
                System.out.println(e);
                }
        }
    }
}
