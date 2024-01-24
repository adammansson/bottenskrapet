package bottenskrapet;

import java.util.*;

public class App {
    /*
    private static final String[] CATEGORIES = {
            "Vin",
            "Öl",
            "Sprit",
            "Cider%20%26%20blanddrycker",
    };

    private static final int[] STORES = {
            1255,
    };
    */

    void run() {
        ScraperMonitor monitor = new ScraperMonitor();

        SearchUri storeSearchUri = new SearchUri(Map.of(
                "isInStoreAssortmentSearch", "true",
                "storeId", "1255"
        ));

        var scraperThread = new SearchScraper(monitor, storeSearchUri);
        scraperThread.start();

        try {
            scraperThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<Product> products = new ArrayList<>(monitor.getProducts());
        products.sort(Comparator.naturalOrder());
        System.out.println(products.getFirst());
    }

    public static void main(String[] args) {
        new App().run();
    }
}
