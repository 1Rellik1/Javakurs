package GeneralClasses;

public class GeneralFunctionsForIndicators {

    protected Portfolio portfolio;

    public GeneralFunctionsForIndicators(double cash,double assets){
        this.portfolio=new Portfolio(cash,assets);
    }
}
