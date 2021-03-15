import java.util.ArrayList;
import java.util.List;

public class MoneyFlowIndex extends Indicators{
    private Portfolio portfolio;

    public MoneyFlowIndex() {
        this.portfolio = new Portfolio(100000,0);
    }
    private double MFI (List<Double> prices_part,List<Long> volumes_part){
        List <Double> up = new ArrayList<>();
        List <Double>  down = new ArrayList<>();
        for (int i=1; i<prices_part.size();i++) {
            var buff = prices_part.get(i) - prices_part.get(i - 1);
            if (buff > 0) {
                up.add(prices_part.get(i)*volumes_part.get(i));
            } else if (buff < 0) {
                down.add(prices_part.get(i)*volumes_part.get(i));
            }
        }
        var x=EMA_value(up);
        var y =EMA_value(down);

        var RS = (EMA_value(up)/EMA_value(down));
        return 100-(100/(1+RS));
    }
    public double signals(List<Double> prices, List<Long> volumes){
        List <Integer> signals = new ArrayList<>();
        var period = 60;
        for (int i=period-1;i<prices.size();i++) {
            if (MFI(prices.subList(i - period + 1, i),volumes.subList(i - period + 1, i)) >= 80) signals.add(-1);
            else if (MFI(prices.subList(i - period + 1, i),volumes.subList(i - period + 1, i)) <= 20) signals.add(1);
            else signals.add(0);
        }
        System.out.println(signals);
        return  portfolio.Calculateprofit(prices,signals);
    }
}
