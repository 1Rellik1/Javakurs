
import org.json.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class HTTP {
    public static String Get (String url) throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(
                URI.create(url))
                .header("accept", "application/json")
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return (response.body());
    }
    public static List<Double> JparseArray(String input){
        var Jobj = new JSONObject(input);
        var array = Jobj.getJSONArray("candles").getJSONObject(0).getJSONArray("data");
        List <Double> prices = new ArrayList<>();
        for (int i=0; i< array.length();i++){
            var subarray = array.getJSONArray(i);
            var open = subarray.getDouble(1);
            var high = subarray.getDouble(2);;
            var low= subarray.getDouble(3);
            var close = subarray.getDouble(4);

            double price =(open+close+high+low)/4.0;
            prices.add(price);
        }
        return prices;
    }
}
