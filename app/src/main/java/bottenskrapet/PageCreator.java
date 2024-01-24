package bottenskrapet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class PageCreator {
    private final List<Product> products;

    public PageCreator(List<Product> products) {
        this.products = products;
    }

    private Document readDocumentFromFile() {
        try {
            File input = new File("docs/template.html");
            return Jsoup.parse(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Element productToHtml(Product product) {
        Element productDiv = new Element("div");
        productDiv.addClass("product");

        productDiv.append("<div class=\"boldName\">" + product.boldName() + "</div>");
        productDiv.append("<div class=\"thinName\">" + product.thinName() + "</div>");
        productDiv.append("<div class=\"price\">" + product.price() + "kr</div>");
        productDiv.append("<div class=\"info\">" + product.volume() + "ml at " + product.percentage() + "%</div>");
        productDiv.append("<div class=\"apk\">APK: " + String.format("%.2f", product.calculateApk()) + "</div>");
        productDiv.append("<img src=" + product.imageUrl() + ">");

        return productDiv;
    }

    private void writeDocumentToFile(Document doc) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("docs/index.html"));
            writer.write(doc.html());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void create() {
        Document doc = readDocumentFromFile();
        Element mainContent = doc.select("#main-content").first();
        for (var product : this.products) {
            mainContent.appendChild(productToHtml(product));
        }
        writeDocumentToFile(doc);
    }
}