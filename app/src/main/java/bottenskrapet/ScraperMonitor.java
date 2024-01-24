package bottenskrapet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScraperMonitor {
    private final HashSet<Product> products = new HashSet<>();

    public synchronized void addProduct(Product product) {
        this.products.add(product);
    }

    public synchronized void addProducts(List<Product> products) {
        this.products.addAll(products);
    }

    public synchronized Set<Product> getProducts() {
        return new HashSet<>(products);
    }
}