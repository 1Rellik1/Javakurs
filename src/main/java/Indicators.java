import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Indicators {
    private double Avarage (List<Double> part){
        double summ=0;
        for (int i=0;i<part.size();i++){
            summ=summ+part.get(i);
        }
        return (summ/((double)part.size()));
    }
    protected List <Double> MovingAverage(List<Double> prices, int period){
        List <Double> ma = new ArrayList<>();
        for (int i=period-1;i<prices.size();i++){
            ma.add(Avarage(prices.subList(i-period+1,i)));
        }
        return ma;
    }
}
