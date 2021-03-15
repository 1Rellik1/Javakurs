import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws Exception {
        String r = HTTP.Get("https://iss.moex.com/cs/engines/stock/markets/shares/boardgroups/57/securities/SBER.hs?s1.type=candles&interval=60&candles=500");
        MoneyFlowIndex movingAverageConvergenceDivergence = new MoneyFlowIndex();
        movingAverageConvergenceDivergence.signals(HTTP.JparseArray(r),HTTP.GetVolumes(r));
//        VisualInteface visualInteface=new VisualInteface();
//        visualInteface.thisStart();
    }
    }
