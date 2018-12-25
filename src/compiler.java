import java.io.*;

public class compiler {
    public static void main(String args[]){
        System.out.println(System.getProperty("user.home"));
        String file = args[0];
        BufferedReader reader;
        //String fileSeparator = System.getProperty("file.separator");
        String wClass = "";
        String module = "";
        String newLine = "";
        int counter = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                String[] parts = line.split(" ");
                switch (parts[0]) {
                    case "module":
                        System.out.println(parts[1]);
                        module = parts[1];
                        break;
                    case "\tclass":
                        wClass = parts[1];
                        try(FileWriter fw = new FileWriter(wClass+"ImplBase.java", true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        PrintWriter out = new PrintWriter(bw))
                        {
                            out.println("package "+module+";");
                            out.println("public abstract class _"+parts[1]+"ImplBase {");
                        } catch (IOException e) {
                        //exception handling left as an exercise for the reader
                        }
                        break;
                    case "\t};" :
                        try(FileWriter fw = new FileWriter(wClass+"ImplBase.java", true);
                                   BufferedWriter bw = new BufferedWriter(fw);
                                   PrintWriter out = new PrintWriter(bw))
                        {
                                out.println("public static _"+wClass+"ImplBase narrowCast(Object rawObjectRef){\n" +
                                        "return new _"+wClass+"Handler(rawObjectRef);\n}");

                        } catch (IOException e) {

                        }
                        break;
                    case "};":
                        try(FileWriter fw = new FileWriter(wClass+"ImplBase.java", true);
                                  BufferedWriter bw = new BufferedWriter(fw);
                                  PrintWriter out = new PrintWriter(bw))
                        {
                        if (counter == 0){
                            out.println("}");
                        }else{
                            out.println("}");
                        }
                        } catch (IOException e) {

                        }
                        break;
                    case "\t\tdouble" :
                        newLine = "";
                        try(FileWriter fw = new FileWriter(wClass+"ImplBase.java", true);
                            BufferedWriter bw = new BufferedWriter(fw);
                            PrintWriter out = new PrintWriter(bw))
                        {
                            for (String a:parts) {
                                newLine = newLine +" "+a.replace("\t","");
                            }

                            out.println("\tpublic abstract "+newLine.replace("string","String"));
                        } catch (IOException e) {
                            //exception handling left as an exercise for the reader
                        }
                        break;
                    case "\t\tint":
                        try(FileWriter fw = new FileWriter(wClass+"ImplBase.java", true);
                            BufferedWriter bw = new BufferedWriter(fw);
                            PrintWriter out = new PrintWriter(bw))
                        {
                            for (String a:parts) {
                                newLine = newLine +" "+a.replace("\t","");
                            }

                            out.println("\tpublic abstract "+newLine);
                        } catch (IOException e) {
                            //exception handling left as an exercise for the reader
                        }
                        break;
                    case "\t\tstring":
                        newLine = "";
                        try(FileWriter fw = new FileWriter(wClass+"ImplBase.java", true);
                            BufferedWriter bw = new BufferedWriter(fw);
                            PrintWriter out = new PrintWriter(bw))
                        {
                            for (String a:parts) {
                                newLine = newLine +" "+a.replace("\t","");
                            }

                            out.println("\tpublic abstract "+newLine.replace("string","String"));
                        } catch (IOException e) {
                            //exception handling left as an exercise for the reader
                        }
                        break;
                    default:
                        break;
                }
                System.out.println(line);
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
