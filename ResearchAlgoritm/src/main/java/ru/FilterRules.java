package ru;


import java.io.File;
import java.util.ArrayList;

public class FilterRules {

    public File fileWithFilterRules;
    public static ArrayList<Byte> filterRules;

    public FilterRules(String fileName){
        fileWithFilterRules = new File(fileName);
        loadFilterRulesFromFile(fileWithFilterRules);

    }

    public static ArrayList<Byte> getFilterRules(){
        return filterRules;
    }

    public static void loadFilterRulesFromFile(File file){
       //parse file with filter rules
    }
}
