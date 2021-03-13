import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String r = HTTP.Get("https://iss.moex.com/cs/engines/stock/markets/shares/boardgroups/57/securities/SBER.hs?s1.type=candles&interval=60&candles=500");

        MovingAverageStrategy movingAverageStrategy= new MovingAverageStrategy();
        movingAverageStrategy.signals(HTTP.JparseArray(r));
    }
    }
