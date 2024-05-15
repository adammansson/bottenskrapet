package bottenskrapet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    /*
    private static Product productFromJson(JSONObject obj) {
        int number = obj.getInt("productNumber");
        String boldName = obj.getString("productNameBold");

        String thinName = "";
        if (!obj.isNull("productNameThin")) {
            thinName = obj.getString("productNameThin");
        }

        double volume = obj.getDouble("volume");
        double price = obj.getDouble("price");
        double percentage = obj.getDouble("alcoholPercentage");
        JSONArray images = obj.getJSONArray("images");

        String firstImageUrl = "";
        if (!images.isEmpty()) {
            JSONObject firstImage = images.getJSONObject(0);
            firstImageUrl = firstImage.getString("imageUrl");
        }

        return new Product(number, boldName, thinName, volume, price, percentage, firstImageUrl);
    }
    */

    private static List<Product> getProductsFromResponse(String response) {
        List<Product> result = new LinkedList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            System.out.println(response);
            JsonNode node = mapper.readTree(response);
            JsonNode array = node.;
            System.out.println(array);
            List<Product> products = mapper.readValue(array.toString(), new TypeReference<List<Product>>(){});
            System.out.println(products);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        /*
        for (var i = 0; i < productArray.length(); i++) {
            var obj = productArray.getJSONObject(i);
            var product = productFromJson(obj);
            result.add(product);
        }
        */

        return result;
    }

    /*
    private static int getNextPageFromResponse(String response) {
        var metadata = new JSONObject(response).getJSONObject("metadata");

        return metadata.getInt("nextPage");
    }
     */

    @Override
    public void run() {
        try {
            while (true) {
                HttpRequest request = createGetRequest(this.searchUri.toUri());
                String response = sendGetRequest(request);

                List<Product> products = getProductsFromResponse(response);
                this.monitor.addProducts(products);

                /*
                int nextPage = getNextPageFromResponse(response);
                */
                int nextPage = -1;
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