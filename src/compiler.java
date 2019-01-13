import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

// TODO: Die fertigen Handler Klassen zuende nacharbeiten, Klassen müssten so mit unserem Programm zusammenarbeiten
public class compiler {
    public static void main(String args[]){
        String file = args[0];
        BufferedReader reader;
        String fileSeparator = System.getProperty("file.separator");
        String wClass = "";
        String module = "";
        String newLine = "";
        Path pathImpl = null;
        Path pathHandler = null;
        byte[] bytes;
        int counter = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                String[] parts = line.split(" ");
                switch (parts[0]) {
                    case "module":
                        module = parts[1];
                        break;
                    case "\tclass":
                        wClass = parts[1];
                        if (!Files.exists(Paths.get(module)))
                            new File(module).mkdirs();
                        pathImpl = Paths.get(module+fileSeparator+"_"+wClass+"ImplBase.java");
                        Files.deleteIfExists(pathImpl);
                        String textImpl = "package "+module+";\n\n" +
                                "import mware_lib.CommunicationModule;\n"+
                                "public abstract class _"+parts[1]+"ImplBase {\n\n"+
                                "\t\tpublic static _"+wClass+"ImplBase narrowCast(Object rawObjectRef){\n" +
                                "return new _"+wClass+"Handler(rawObjectRef);\n}";
                        bytes = textImpl.getBytes();
                        Files.write(pathImpl,bytes,StandardOpenOption.CREATE_NEW,StandardOpenOption.WRITE);
                        pathHandler = Paths.get(module+fileSeparator+"_"+wClass+"Handler.java");
                        Files.deleteIfExists(pathHandler);
                        String textHandler = "package "+module+";\n\n" +
                                "import mware_lib.ObjectBroker;\n"+
                                "public  class _"+parts[1]+"Handler extends _"+parts[1]+"ImplBase {\n\n"+
                                "private static String name;\n"+
                                "private static String host;\n"+
                                "private static int port;\n"+
                                "ObjectBroker ob;\n\n"+
                                "_"+parts[1]+"Handler(Object rawObjectRef){\n"+
                                "name = rawObjectRef.toString().split(\",\")[0];\n" +
                                "host = rawObjectRef.toString().split(\",\")[1];\n" +
                                "port = Integer.parseInt(rawObjectRef.toString().split(\",\")[2]);\n" +
                                "ob = ObjectBroker.init(host, port, false);\n}\n"+
                                "\t\tpublic static _"+wClass+"Handler narrowCast(Object rawObjectRef){\n" +
                                "return new _"+wClass+"Handler(rawObjectRef);}\n";
                        bytes = textHandler.getBytes();
                        Files.write(pathHandler,bytes,StandardOpenOption.CREATE_NEW,StandardOpenOption.WRITE);
                        break;
                    case "\t};" :
                        textImpl = "}";
                        bytes = textImpl.getBytes();
                        Files.write(pathImpl,bytes,StandardOpenOption.APPEND,StandardOpenOption.WRITE);
                        textHandler = "}";
                        bytes = textHandler.getBytes();
                        Files.write(pathHandler,bytes,StandardOpenOption.APPEND,StandardOpenOption.WRITE);
                        break;
                    case "};":
                        break;
                    default:
                        newLine = "";
                        String value = parts[0].replaceAll("\t", "");
                        if (value.equals("double")) {
                            value = "(double) result";
                        } else if (value.equals("string")){
                            value = "String.valueOf(result)";
                        } else {
                            value = "(int) result";
                        }
                        for (String a:parts) {
                                newLine = newLine +" "+a.replace("\t","");
                        }
                        textImpl = "\tpublic abstract "+newLine.replace("string","String")+" throws Exception\n";
                        bytes = textImpl.getBytes();
                        Files.write(pathImpl,bytes,StandardOpenOption.APPEND,StandardOpenOption.WRITE);
                        newLine = newLine.replaceAll(";","");
                        textHandler = "\tpublic "+newLine.replace("string","String")+" throws Exception {\n"+
                        "Object result = this.ob.remoteCall(name, host, port,"+parts[1]+","+parts[3]+");\n"+
                        "if (result instanceof RuntimeException) throw (RuntimeException) result;\n"+
                        "return "+value+";\n}\n";
                        bytes = textHandler.getBytes();
                        Files.write(pathHandler,bytes,StandardOpenOption.APPEND,StandardOpenOption.WRITE);
                        break;
                }
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
