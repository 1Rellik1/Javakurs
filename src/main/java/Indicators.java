import java.util.ArrayList;
import java.util.List;

public class Indicators {

    private double Average(List<Double> part){
        double summ=0;
        for (int i=0;i<part.size();i++){
            summ=summ+part.get(i);
        }
        return (summ/((double)part.size()));
    }

    protected List <Double> MovingAverage(List<Double> prices, int period){
        List <Double> ma = new ArrayList<>();
        for (int i=period-1;i<prices.size();i++){
            ma.add(Average(prices.subList(i-period+1,i)));
        }
        return ma;
    }

    protected List <Double> ExponentialMovingAverage(List<Double> prices, int period){
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
