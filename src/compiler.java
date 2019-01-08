import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class compiler {
    public static void main(String args[]){
        String file = args[0];
        BufferedReader reader;
        String fileSeparator = System.getProperty("file.separator");
        String wClass = "";
        String module = "";
        String newLine = "";
        Path path = null;
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
                        path = Paths.get(module+fileSeparator+"_"+wClass+"ImplBase.java");
                        String text = "package "+module+";\n\n" +
                                "public abstract class _"+parts[1]+"ImplBase {\n\n";
                        bytes = text.getBytes();
                        Files.write(path,bytes,StandardOpenOption.CREATE_NEW,StandardOpenOption.WRITE);
                        break;
                    case "\t};" :
                        text = "\t\tpublic static _"+wClass+"ImplBase narrowCast(Object rawObjectRef){\n" +
                                "return new _"+wClass+"Handler(rawObjectRef);\n}";
                        bytes = text.getBytes();
                        Files.write(path,bytes,StandardOpenOption.APPEND,StandardOpenOption.WRITE);
                        break;
                    case "};":
                        text = "}";
                        bytes = text.getBytes();
                        Files.write(path,bytes,StandardOpenOption.APPEND,StandardOpenOption.WRITE);
                        break;
                    default:
                        newLine = "";
                        for (String a:parts) {
                                newLine = newLine +" "+a.replace("\t","");
                        }
                        text = "\tpublic abstract "+newLine.replace("string","String")+"\n";
                        bytes = text.getBytes();
                        Files.write(path,bytes,StandardOpenOption.APPEND,StandardOpenOption.WRITE);
                        break;
                }
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
