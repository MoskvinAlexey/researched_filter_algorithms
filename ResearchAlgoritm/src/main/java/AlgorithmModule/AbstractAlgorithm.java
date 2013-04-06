package AlgorithmModule;


import FilterRuleModule.Rule;
import FilterRuleModule.FilterRules;
import java.util.ArrayList;



public abstract class AbstractAlgorithm {

    private static ArrayList<ArrayList<Rule>> filterRules;       // сделать public и типа FilterRules, тогда в алгоритме будут вызовы filterRules.get

    public AbstractAlgorithm(){
        filterRules = loadFilterRules();      //FilterRules filterRules = new FilterRules("data/rules");
                                              //обернуть в прокси, чтобы считать количество обращений к правилам
    }

    public ArrayList<ArrayList<Rule>> loadFilterRules(){
        return FilterRules.getFilterRules();

    }

    protected abstract String applyAlgorithm(Object packet);

    protected abstract Object preparePacket(byte [] packet);

    /**
     *
     * @param ruleType - тип правил фильтрации: IP =2, ARP=1, MAC=0
     * @param ruleNumber
     * @return правило фильтрации из таблицы ruleType, с номером в таблице ruleNumber
     */


    //перенести всё в FilterRules

    protected static Rule getFilterRuleSingle(int ruleType, int ruleNumber){
        return filterRules.get(ruleType).get(ruleNumber);
    }

    protected static int sizeFilterRules(){
        return filterRules.size();
    }

    protected static int sizeFilterRuleOfOneType(int ruleType){
        return filterRules.get(ruleType).size();
    }

    protected static boolean filterRuleOfOneTypeIsEmpty(int ruleType){
        return filterRules.get(ruleType).isEmpty();
    }






}
