import java.util.ArrayList;
import java.util.List;

public class MovingAverageStrategy extends Indicators {
    private List<Double> ma;
    private Portfolio portfolio;
    public MovingAverageStrategy(double cash,double assets) {
        this.portfolio=new Portfolio(cash,assets);
    }

    public double signals(List<Double>prices) {
        ma = MovingAverage(prices, 60);
        prices = prices.subList(prices.size() - ma.size(), prices.size());
        List<Integer> signals = new ArrayList<>();
        signals.add(0);
        for (int i = 1; i < prices.size(); i++) {
            if ((prices.get(i) > ma.get(i)) & (prices.get(i - 1) <= ma.get(i - 1))) {
                signals.add(1);
            } else if ((prices.get(i) < ma.get(i) & (prices.get(i - 1) >= ma.get(i - 1)))) {
                signals.add(-1);
            } else signals.add(0);
        }
        return portfolio.Calculateprofit(prices,signals);
    }

    public List<Double> getMa(List<Double>prices) {
        return MovingAverage(prices, 60);
    }
    public List<Double> getPortfoliovalue() {
        return portfolio.getPortfoliovalue();
    }
}
