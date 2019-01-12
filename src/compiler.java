import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

// TODO: narrowCast in dem Maße implementieren das es auch funktioniert
// TODO: ausserdem nochmal alles durchgehen und nachsehen ob das Ergebnis mit seinem Code Funktioniert
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
                        String textImpl = "package "+module+";\n\n" +
                                "public abstract class _"+parts[1]+"ImplBase {\n\n";
                        bytes = textImpl.getBytes();
                        Files.write(pathImpl,bytes,StandardOpenOption.CREATE_NEW,StandardOpenOption.WRITE);
                        pathHandler = Paths.get(module+fileSeparator+"_"+wClass+"ImplBase.java");
                        String textHandler = "package "+module+";\n\n" +
                                "public  class _"+parts[1]+"Handler {\n\n";
                        bytes = textHandler.getBytes();
                        Files.write(pathHandler,bytes,StandardOpenOption.CREATE_NEW,StandardOpenOption.WRITE);
                        break;
                    case "\t};" :
                        textImpl = "\t\tpublic static _"+wClass+"ImplBase narrowCast(Object rawObjectRef){\n" +
                                "return new _"+wClass+"Handler(rawObjectRef);\n}";
                        bytes = textImpl.getBytes();
                        Files.write(pathImpl,bytes,StandardOpenOption.APPEND,StandardOpenOption.WRITE);
                        textHandler = "\t\tpublic static _"+wClass+"Handler narrowCast(Object rawObjectRef){\n" +
                        "return new _"+wClass+"Handler(\n" +
                        "private String name = rawObjectRef.toString().split(\",\")[0];\n" +
                        "private String host = rawObjectRef.toString().split(\",\")[1];\n" +
                        "private int port = Integer.parseInt(rawObjectRef.toString().split(\",\")[2]);\n";
                        bytes = textHandler.getBytes();
                        Files.write(pathHandler,bytes,StandardOpenOption.APPEND,StandardOpenOption.WRITE);
                        break;
                    case "};":
                        textImpl = "}";
                        bytes = textImpl.getBytes();
                        Files.write(pathImpl,bytes,StandardOpenOption.APPEND,StandardOpenOption.WRITE);
                        break;
                    default:
                        newLine = "";
                        for (String a:parts) {
                                newLine = newLine +" "+a.replace("\t","");
                        }
                        textImpl = "\tpublic abstract "+newLine.replace("string","String")+"\n";
                        bytes = textImpl.getBytes();
                        Files.write(pathImpl,bytes,StandardOpenOption.APPEND,StandardOpenOption.WRITE);
                        textHandler = "\tpublic "+newLine.replace("string","String")+" throws RuntimeException\n"+
                        "Object result = RemoteDelegator.invokeMethod(name, host, port, \"_BankImplBase\", \"balanceInquiry\");\n"+
                        "if (result instanceof RuntimeException) throw (RuntimeException) result;\n"+
                        "return String.valueOf(result);\n";
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
