package bottenskrapet;

import org.json.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedList;
import java.util.List;

public class SearchScraper extends Thread {
    private final ScraperMonitor monitor;
    private final SearchUri searchUri;

    public SearchScraper(ScraperMonitor monitor, SearchUri searchUri) {
        this.monitor = monitor;
        this.searchUri = searchUri;
    }

    private static HttpRequest createGetRequest(URI uri) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .header("Ocp-Apim-Subscription-Key", "cfc702aed3094c86b92d6d4ff7a54c84")
                .GET()
                .build();
    }

    private static String sendGetRequest(HttpRequest request) {
        try (var client = HttpClient.newHttpClient()) {
            return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static Product productFromJson(JSONObject obj) {
        int number = obj.getInt("productNumber");
        String name = obj.getString("productNameBold");
        double volume = obj.getDouble("volume");
        double price = obj.getDouble("price");
        double percentage = obj.getDouble("alcoholPercentage");

        return new Product(number, name, volume, price, percentage);
    }

    private static List<Product> getProductsFromResponse(String response) {
        List<Product> result = new LinkedList<>();
        var productArray = new JSONObject(response).getJSONArray("products");

        for (var i = 0; i < productArray.length(); i++) {
            var obj = productArray.getJSONObject(i);
            var product = productFromJson(obj);
            result.add(product);
        }

        return result;
    }

    private static int getNextPageFromResponse(String response) {
        var metadata = new JSONObject(response).getJSONObject("metadata");

        return metadata.getInt("nextPage");
    }

    @Override
    public void run() {
        try {
            while (true) {
                HttpRequest request = createGetRequest(this.searchUri.toUri());
                String response = sendGetRequest(request);

                List<Product> products = getProductsFromResponse(response);
                this.monitor.addProducts(products);

                int nextPage = getNextPageFromResponse(response);
                System.out.println(nextPage);
                if (nextPage == -1) {
                    break;
                }

                this.searchUri.setPage(nextPage);
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}