import java.util.ArrayList;
import java.util.List;

public class RelativeStrengthindex extends Indicators{
    private Portfolio portfolio;

    public RelativeStrengthindex() {
        this.portfolio = new Portfolio(100000,0);
    }
    private double RSI (List<Double> prices_part){
        List <Double> up = new ArrayList<>();
        List <Double>  down = new ArrayList<>();
        for (int i=1; i<prices_part.size();i++) {
            var buff = prices_part.get(i) - prices_part.get(i - 1);
            if (buff > 0) {
                up.add(buff);
            } else if (buff < 0) {
                down.add(-buff);
            }
        }
        var x=EMA_value(up);
        var y =EMA_value(down);
        var RS = (EMA_value(up)/EMA_value(down));
        return 100-(100/(1+RS));
    }
    public double signals(List<Double> prices){
        List <Integer> signals = new ArrayList<>();
        var period = 60;
        for (int i=period-1;i<prices.size();i++) {
            if (RSI(prices.subList(i - period + 1, i)) >= 80) signals.add(-1);
            else if (RSI(prices.subList(i - period + 1, i)) <= 20) signals.add(1);
            else signals.add(0);
        }

        return  portfolio.Calculateprofit(prices,signals);
    }
}
