package bottenskrapet;

public record Product(int productNumber,
                      String productNameBold,
                      String productNameThin,
                      double volume,
                      double price,
                      double alcoholPercentage,
                      String imageUrl) implements Comparable<Product> {

    public double calculateApk() {
        return (volume * alcoholPercentage) / price;
    }

    @Override
    public int compareTo(Product that) {
        if (this.equals(that)) {
            return 0;
        }

        return (int) (that.calculateApk() - this.calculateApk());
    }
}