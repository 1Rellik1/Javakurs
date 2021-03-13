import java.io.IOException;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String companies_url = HTTP.Get("https://iss.moex.com/iss/engines/stock/markets/shares/boardgroups/57/" +
                "securities.jsonp?iss.meta=off&security_collection=3&sort_column=SHORTNAME&sort_order=asc&lang=ru" +
                "&_=1615584079650");
        HashMap<String, String> companies = HTTP.JparseCompanies(companies_url);
        String company= "Yandex clA";
        MovingAverageStrategy movingAverageStrategy= new MovingAverageStrategy();
        movingAverageStrategy.signals(HTTP.JparseArray(HTTP.Get(HTTP.MakeUrl(companies.get(company)))));

    }
    }
