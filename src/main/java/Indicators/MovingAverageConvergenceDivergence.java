package Indicators;

import GeneralClasses.Ema;

import java.util.ArrayList;
import java.util.List;

public class MovingAverageConvergenceDivergence extends Ema {
    private  List<Double> MACD;
    private List<Double> signal_line;


    public MovingAverageConvergenceDivergence(double cash,double assets){
        super(cash,assets);
    }

    public double signals(List<Double> prices) {
        List<Double> EMA_26 = ExponentialMovingAverage(prices, 24);
        List<Double> EMA_12 = ExponentialMovingAverage(prices,12);
        EMA_12=EMA_12.subList(EMA_12.size() - EMA_26.size(),EMA_12.size());
        MACD = new ArrayList<>();
        for (int i=0;i<EMA_12.size();i++){
            MACD.add(EMA_12.get(i)-EMA_26.get(i));
        }
        signal_line = ExponentialMovingAverage(MACD,9);
        List<Integer> signals = new ArrayList<>();
        MACD=MACD.subList(MACD.size()-signal_line.size(),MACD.size());
        signals.add(0);
        for (int i = 1; i < MACD.size(); i++) {
            if ((MACD.get(i) > signal_line.get(i)) & (MACD.get(i - 1) <= signal_line.get(i - 1))) {
                signals.add(1);
            } else if ((MACD.get(i) < signal_line.get(i) & (MACD.get(i - 1) >= signal_line.get(i - 1)))) {
                signals.add(-1);
            } else signals.add(0);
        }
        return portfolio.Calculateprofit(prices,signals);
    }
    public List <List <Double>> getdataforchart(){
        List <List <Double>> list = new ArrayList<>();
        list.add(MACD);
        list.add(signal_line);
        return list;
    }

    public List<Double> getPortfoliovalue() {
        return portfolio.getPortfoliovalue();
    }
}
