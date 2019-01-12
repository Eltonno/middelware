package mware_lib;

public class Sender {

    String host;
    int port;
    CommunicationModule com;
    String nachricht;

    Sender(CommunicationModule com, String host, int port, String method, Object... args){
        this.host = host;
        this.port = port;
        this.com = com;
        nachricht = method; //TODO: die args irgendwie in den String einbauen
    }

    public Object invoke() {
        serversocket = new ServerSocket(port);
        System.out.println("Sender läuft");
        while(true){
            try{
                PrintWriter printWriter =
                        new PrintWriter(
                                new OutputStreamWriter(
                                        socket.getOutputStream()));
                printWriter.print(nachricht);
                printWriter.flush();
                socket = serversocket.accept();
                BufferedReader bufferedReader =
                        new BufferedReader(
                                new InputStreamReader(
                                        socket.getInputStream()));
                String result = bufferedReader.readLine();
                return result;
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            // new thread for a client

        }
    }


}