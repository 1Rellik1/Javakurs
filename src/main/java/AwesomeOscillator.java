import java.util.ArrayList;
import java.util.List;

public class AwesomeOscillator extends Indicators{
    private List<Double> ma;
    private List<Double> ma1;
    private Portfolio portfolio;
    public AwesomeOscillator() {
        portfolio=new Portfolio(10000000,0);
    }

    public double signals(List<Double>prices) {
        ma = MovingAverage(prices, 60);
        ma1 = MovingAverage(prices, 30);
        ma1 = ma1.subList(ma1.size() - ma.size(), ma1.size());
        List<Integer> signals = new ArrayList<>();
        signals.add(0);
        for (int i = 1; i < ma1.size(); i++) {
            if ((ma1.get(i) > ma.get(i)) & (ma1.get(i - 1) <= ma.get(i - 1))) {
                signals.add(1);
            } else if ((ma1.get(i) < ma.get(i) & (ma1.get(i - 1) >= ma.get(i - 1)))) {
                signals.add(-1);
            } else signals.add(0);
        }
        return portfolio.Calculateprofit(prices,signals);
    }
}
