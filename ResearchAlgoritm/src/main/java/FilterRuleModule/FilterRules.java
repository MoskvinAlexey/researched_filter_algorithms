package FilterRuleModule;


import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterRules {

    public File fileWithFilterRules;
    public static ArrayList<Byte> filterRules;
    public static Pattern pattern;
    private static Matcher matcher;

    public FilterRules(String fileName){
        pattern = Pattern.compile("ip:(\\d+):(drop|accept|pass):\\w+,\\w+:\\w*:\\w*:\\w*:\\w*:(\\w*.\\w*.\\w*.\\w*):(\\w*):(\\w*.\\w*.\\w*.\\w*):(\\w*)");
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
        matcher = pattern.matcher(thisLine);
        boolean temp = matcher.matches();



    }
}
