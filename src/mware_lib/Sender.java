package mware_lib;
import java.io.*;
//import java.net.ServerSocket;
import java.net.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class Sender {

    String host;
    int port;
    String nachricht;

    Sender(String comhost, int comport, String name, String host, int port, String method, Object... args){
        this.host = host;
        this.port = port;
        nachricht = "remotecall;" + comhost +";" + Integer.toString(comport) + ";" + name + ";" + method;
        if (args.length>0){
            nachricht = nachricht + ";";
            for (int i = 0; i<args.length; i++){
                if (args[i].getClass() == Integer.class){
                    nachricht = nachricht + ":" + Integer.toString((Integer) args[i]);
                }else if (args[i].getClass() == Double.class){
                    nachricht = nachricht + Double.toString((Double) args[i]);
                }else{
                    nachricht = nachricht + args[i];
                }
            }
        }//WOHL FERTIG: die args irgendwie in den String einbauen
    }

    public void sendResult(Object result) throws IOException {
        Socket socket = new Socket(host, port);
        System.out.println("Sender läuft");
        try {
            PrintWriter printWriter =
                    new PrintWriter(
                            new OutputStreamWriter(
                                    socket.getOutputStream()));
            printWriter.print(result);
            printWriter.flush();
            //WOHL FERTIG: result an den die Addresse schicken
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


        public Object invoke() throws IOException {
        Socket socket = new Socket(host, port);
        System.out.println("Sender läuft");
        try {
            PrintWriter printWriter =
                    new PrintWriter(
                            new OutputStreamWriter(
                                    socket.getOutputStream()));
            printWriter.print(nachricht);
            printWriter.flush();
            //socket.accept();
            BufferedReader bufferedReader =
                    new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()));
            String result = bufferedReader.readLine();
            socket.close();
            String[] empfangen = result.split(";");
            if (empfangen.length == 2) {
                if (empfangen[0] == "result") {
                        if ( empfangen[1].matches("0-9")){
                            return Integer.parseInt(empfangen[1]);
                        }else if (empfangen[1].matches("(0|([1-9][0-9]*))(\\.[0-9]+)")){
                            return Double.parseDouble(empfangen[1]);
                        }else{
                            return empfangen[1];
                        }
                }
            }else{
                //TODO:Exception senden
                return "error";
            }
        } catch (IOException e) {
            System.out.println("I/O error: " + e);
            return e;
        }
        //TODO:Exception senden
        return "error";

    }


}
