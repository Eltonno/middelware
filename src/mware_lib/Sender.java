package mware_lib;
import java.io.*;
//import java.net.ServerSocket;
import java.net.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class Sender {

    String host;
    int port;
    String comhost;
    int comport;
    String nachricht;

    Sender(String comhost, int comport, String name, String host, int port, String method, Object... args){
        //System.out.println("Im Sender");
        this.host = host;
        this.port = port;
        this.comhost = comhost;
        this.comport = comport;
        nachricht = "remotecall;" + comhost +";" + Integer.toString(comport) + ";" + name + ";" + method;
        if (args.length>0){
            nachricht = nachricht + ";";
            for (int i = 0; i<args.length; i++){
                if (args[i].getClass() == Integer.class){
                    nachricht = nachricht +  Integer.toString((Integer)args[i]) + ":";
                }else if (args[i].getClass() == Double.class){
                    nachricht = nachricht +  Double.toString((Double) args[i]) + ":";
                }else{
                    nachricht = nachricht +  args[i] + ":";
                }
            }
        }//WOHL FERTIG: die args irgendwie in den String einbauen
    }

    public void sendResult(Object result) throws IOException {
       String nachricht;
            if (result.getClass() == Integer.class){
                nachricht = Integer.toString((Integer) result);
            }else if (result.getClass() == Double.class){
                nachricht = Double.toString((Double) result);
            }else{
                nachricht = result.toString();
            }
        Socket socket = new Socket(comhost, comport);
        //System.out.println("Sender gestartet mit Host: " + comhost + " und Port: " + comport );
        try {
            //System.out.println("Sender sendet: " + nachricht + " als Result");

            PrintWriter printWriter =
                    new PrintWriter(
                            new OutputStreamWriter(
                                    socket.getOutputStream()));
            printWriter.print(nachricht);
            printWriter.flush();
            //WOHL FERTIG: result an den die Addresse schicken
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


        public Object invoke() {
            //System.out.println("Invoke aufgerufen");

            Socket socket = null;
            try {
                socket = new Socket(host, port);
            } catch (IOException e) {
                return e;
            }
           // System.out.println("Sender gestartet mit Host: " + host + " und Port: " + port );
            //System.out.println("Sender sendet: " + nachricht);

            try {
            PrintWriter printWriter =
                    new PrintWriter(
                            new OutputStreamWriter(
                                    socket.getOutputStream()));
            //printWriter.print("ping");
            //printWriter.flush();
            printWriter.print(nachricht);
            printWriter.flush();
            //socket.accept();
                BufferedReader bufferedReader =
                        new BufferedReader(
                                new InputStreamReader(
                                        socket.getInputStream()));
                char[] buffer = new char[1000];
               // System.out.println("Sender>>Nun sollte er lesen");
                int anzahlZeichen = bufferedReader.read(buffer, 0, 1000); // blockiert bis Nachricht empfangen
                String result = new String(buffer, 0, anzahlZeichen);
            socket.close();
             //   System.out.println("Sender empfängt " + result);
                String empfangen = result;
                        if ( empfangen.matches("\\d+")){
                            return Integer.parseInt(empfangen);
                        }else if (empfangen.matches("\\d+\\.\\d+")){
                            return Double.parseDouble(empfangen);
                        }else if (empfangen.contains("Exception")){
                            return(new RuntimeException(empfangen));
                        }else{
                            return empfangen;
                        }
        }

         catch (IOException e) {
            System.out.println("I/O error: " + e);
            return e;
        } catch (Exception e) {
                return e;
            }
            //TODO:Exception senden

    }


}
