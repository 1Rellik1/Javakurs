import java.util.ArrayList;
import java.util.List;

public class Portfolio {
    private double cash;
    private double asset;
    private double profit;
    private List<Double> portfoliovalue;

    public Portfolio(double cash, double asset) {
        this.cash = cash;
        this.asset = asset;
        portfoliovalue=new ArrayList<>();
    }

    public double getCash() {
        return cash;
    }

    public double getAsset() {
        return asset;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public void setAsset(double asset) {
        this.asset = asset;
    }
    public double getProfit() {
        return profit;
    }

    public List<Double> getPortfoliovalue() {
        return portfoliovalue;
    }
    public double Calculateprofit(List<Double>prices,List<Integer>signals){
        for (int i=0; i< signals.size();i++){
            if (signals.get(i)==1){
                var amount_buy=Math.floor(cash/prices.get(i));
                cash-=amount_buy*prices.get(i);
                asset+=amount_buy;
            }
            if (signals.get(i)==-1){
                var amount_sell= asset;
                cash+=amount_sell*prices.get(i);
                asset-=amount_sell;
            }
            portfoliovalue.add(cash+asset*prices.get(i));
        }
        profit=portfoliovalue.get(portfoliovalue.size()-1)-portfoliovalue.get(0);
        return (profit);
    }


}
