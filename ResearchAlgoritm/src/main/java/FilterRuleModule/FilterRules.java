package FilterRuleModule;


import java.io.*;
import java.util.ArrayList;


public class FilterRules {

    public File fileWithFilterRules;
    public static ArrayList<Byte> filterRules;

    public FilterRules(String fileName){
        fileWithFilterRules = new File(fileName);
        if(!fileWithFilterRules.exists()) {
            System.out.println("File " + fileName + " is not exist!");
            System.exit(1);           //add cath Exception
        }

        try {
            loadFilterRulesFromFile(fileWithFilterRules);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<Byte> getFilterRules(){
        return filterRules;
    }

    public static void loadFilterRulesFromFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()));
        String thisLine;
        while((thisLine = br.readLine())!=null){
            parse(thisLine);
        }
        br.close();
    }

    private static void parse(String thisLine) {
          String[] temp = thisLine.split(":");
          System.out.println(temp.length);



    }
}
