package bottenskrapet;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchUri {

    private static final String BASE_URI =
            "https://api-extern.systembolaget.se/sb-api-ecommerce/v1/productsearch/search";

    private final Map<String, String> queries = new HashMap<>(Map.of(
            "page", "1",
            "size", "30"
    ));

    public SearchUri(Map<String, String> queries) {
        this.queries.putAll(queries);
    }

    public void setPage(int page) {
        this.queries.put("page", Integer.toString(page));
    }

    private String stringifyQueries() {
        return this.queries.keySet().stream()
                .map(key -> key + "=" + this.queries.get(key))
                .collect(Collectors.joining("&"));
    }

    public URI toUri() {
        String uriString = BASE_URI + "?" + stringifyQueries();

        try {
            return new URI(uriString);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}