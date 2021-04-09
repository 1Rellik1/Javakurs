
import org.json.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class HTTP {
    public static String MakeUrl(String ticker){
        String url = "https://iss.moex.com/cs/engines/stock/markets/shares/boardgroups/57/securities/" + ticker +
                ".hs?s1.type=candles&interval=60&candles=500";
        return url;
    }
    public static String Get(String url) throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(
                URI.create(url))
                .header("accept", "application/json")
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return (response.body());
    }

    public static List<Double> JparseArray(String input) {
        var Jobj = new JSONObject(input);
        var array = Jobj.getJSONArray("candles").getJSONObject(0).getJSONArray("data");
        List<Double> prices = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            var subarray = array.getJSONArray(i);
            var open = subarray.getDouble(1);
            var high = subarray.getDouble(2);
            var low = subarray.getDouble(3);
            var close = subarray.getDouble(4);
            double price = (open + close + high + low) / 4.0;
            prices.add(price);
        }
        return prices;
    }
    public static List<Long> GetVolumes(String input) {
        var Jobj = new JSONObject(input);
        var volumes_array = Jobj.getJSONArray("volumes").getJSONObject(0).getJSONArray("data");
        List<Long> volumes= new ArrayList<>();
        for (int i = 0; i < volumes_array.length(); i++) {
            var volume = volumes_array.getJSONArray(i).getLong(1);
            volumes.add(volume);
        }
        return volumes;
    }
    public static List<String> GetTime(String input) {
        var Jobj = new JSONObject(input);
        var array = Jobj.getJSONArray("candles").getJSONObject(0).getJSONArray("data");
        List<String> times= new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            var time = array.getJSONArray(i).getLong(0);
            Date date = new Date(time);
            SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String java_date = jdf.format(date);
            times.add(java_date);
        }
        return times;
    }

    public static HashMap JparseCompanies(String input) {
        var jsonObject = new JSONObject(input);
        var array = jsonObject.getJSONObject("securities").getJSONArray("data");
        HashMap<String, String> companies = new HashMap<>();
        for (int i=0; i< array.length();i++){
            var subarray = array.getJSONArray(i);
            companies.put(subarray.getString(2),subarray.getString(0));
        }
        return (companies);
    }
}
