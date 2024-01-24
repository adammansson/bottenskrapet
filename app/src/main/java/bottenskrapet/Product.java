package bottenskrapet;

public record Product(int number, String name, double volume, double price, double percentage) implements Comparable<Product> {
    public double calculateApk() {
        return (volume * percentage) / price;
    }
    @Override
    public int compareTo(Product that) {
        if (this.equals(that)) {
            return 0;
        }

        return (int) (that.calculateApk() - this.calculateApk());
    }
}