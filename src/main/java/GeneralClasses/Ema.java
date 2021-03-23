package GeneralClasses;

import java.util.ArrayList;
import java.util.List;

public class Ema extends GeneralFunctionsForIndicators {
    public Ema(double cash, double assets) {
        super(cash, assets);
    }

    protected List<Double> ExponentialMovingAverage(List<Double> prices, int period){
        List <Double> ema = new ArrayList<>();
        for (int i=period-1;i<prices.size();i++){
            ema.add(EMA_value(prices.subList(i - period + 1, i)));
        }
        return ema;
    }

    protected double EMA_value(List<Double> prices_kusok)
    {
        var period = prices_kusok.size();
        var k = 2 / (1 + period);
        var ema_value = prices_kusok.get(0);

        for (int i = 1; i < period; i++)
        {
            ema_value = prices_kusok.get(i) * k + ema_value * (1 - k);
        }

        return ema_value;

    }
}
