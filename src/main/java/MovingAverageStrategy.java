import java.util.ArrayList;
import java.util.List;

public class MovingAverageStrategy extends Indicators {
    List<Double> ma;
    Portfolio portfolio;
    public MovingAverageStrategy() {
        portfolio=new Portfolio(100000,0);
    }

    public void signals(List<Double>prices) {
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
        portfolio.Calculateprofit(prices,signals);
    }
}
