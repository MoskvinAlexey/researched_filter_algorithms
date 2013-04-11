package AlgorithmModule;



import FilterRuleModule.FilterRules;




public abstract class AbstractAlgorithm {


    public FilterRules filterRules;



    public void setFilterRules(FilterRules filterRules){
        this.filterRules = filterRules;
    }

    protected String apply(Object packet) {
        String retVal = applyAlgorithm(packet);
        //TODO обнулить совет и получить из него значение
        return retVal;
    }

    protected abstract String applyAlgorithm(Object packet);

    protected abstract Object preparePacket(byte [] packet);


}
