import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

// TODO: Die fertigen Handler Klassen zuende nacharbeiten, Klassen müssten so mit unserem Programm zusammenarbeiten
public class compiler {
    public static void main(String args[]){
        String file = args[0];
        BufferedReader reader;
        String fileSeparator = System.getProperty("file.separator");
        String wClass = "";
        String module = "";
        String newLine;
        String functions = "";
        String abstracts;
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
                                "\n" +
                                "import mware_lib.CommunicationModule;\n" +
                                "import java.io.IOException;\n\n"+
                                "public abstract class _"+parts[1]+"ImplBase {\n";
                        bytes = textImpl.getBytes();
                        Files.write(pathImpl,bytes,StandardOpenOption.CREATE_NEW,StandardOpenOption.WRITE);
                        break;
                    case "\t};" :
                        textImpl = "\t\tpublic static _"+wClass+"ImplBase narrowCast(Object rawObjectRef) throws IOException{\n" +
                                "\t    return new _"+wClass+"ImplBase() {\n" +
                                "\n" +
                                "            String ref = (String) rawObjectRef;\n\n" +
                                functions +
                                "};\n}\n}";
                        bytes = textImpl.getBytes();
                        Files.write(pathImpl,bytes,StandardOpenOption.APPEND,StandardOpenOption.WRITE);
                        functions = "";
                        /*textHandler = "}";
                        bytes = textHandler.getBytes();
                        Files.write(pathHandler,bytes,StandardOpenOption.APPEND,StandardOpenOption.WRITE);*/
                        break;
                    case "};":
                        /*textImpl = "}";
                        bytes = textImpl.getBytes();
                        Files.write(pathImpl,bytes,StandardOpenOption.APPEND,StandardOpenOption.WRITE);*/
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
                        textImpl = "\tpublic abstract "+newLine.replace("string","String").replace(";", "")+" throws Exception;\n\n";
                        functions = functions +
                            "\t\t\t@Override\n" +
                                "\t\t\tpublic "+newLine.replace("string","String").replace(";","")+" throws Exception {\n" +
                                "\t\t\t\tref = ref.replace(\"\\\"\", \"\");\n" +
                                "\t\t\tString name = ref.split(\",\")[0];\n" +
                                "\t\t\tString host = ref.split(\",\")[1];\n" +
                                "\t\t\tint port = Integer.parseInt(ref.split(\",\")[2]);\n" +
                                "                Object result = null;\n" +
                                "                result = CommunicationModule.invoke(name, host, port, \"deposit\", "+parts[2].replace(")","").replace(";","")+");\n" +
                                "                if (result instanceof Exception) {\n" +
                                "                   throw new RuntimeException(result.toString());\n" +
                                "               }\n" +
                                "\t\t\t\treturn "+value+";\n" +
                                "\n" +
                                "\t\t\t}\n\n";
                        bytes = textImpl.getBytes();
                        Files.write(pathImpl,bytes,StandardOpenOption.APPEND,StandardOpenOption.WRITE);
                        break;
                }
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
