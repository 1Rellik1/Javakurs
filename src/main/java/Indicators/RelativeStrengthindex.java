package Indicators;

import GeneralClasses.Ema;
import GeneralClasses.Portfolio;

import java.util.ArrayList;
import java.util.List;

public class RelativeStrengthindex extends Ema {

    private List <Double> rs;

    public RelativeStrengthindex(double cash,double assets) {
        portfolio = new Portfolio(cash,assets);
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
        rs= new ArrayList<>();
        for (int i=period-1;i<prices.size();i++) {
            rs.add(RSI(prices.subList(i - period + 1, i)));
            if (RSI(prices.subList(i - period + 1, i)) >= 80) signals.add(-1);
            else if (RSI(prices.subList(i - period + 1, i)) <= 20) signals.add(1);
            else signals.add(0);
        }
        prices=prices.subList(prices.size()-rs.size(),prices.size());
        return  portfolio.Calculateprofit(prices,signals);
    }

    public List<Double> getdataforchart() {
        return rs;
    }

    public List<Double> getPortfoliovalue() {
        return this.portfolio.getPortfoliovalue();
    }
}
