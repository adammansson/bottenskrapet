package bottenskrapet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PageCreator {
    private final List<Product> products;

    public PageCreator(List<Product> products) {
        this.products = products;
    }

    private Element productToHtml(Product product) {
       Element productDiv = new Element("div");

       productDiv.append("<h2>" + product.name() + "</h2>");
       productDiv.append("<h2>" + product.percentage() + "</h2>");

       return productDiv;
    }

    public void create() {
        try {
            File input = new File("docs/template.html");
            Document doc = Jsoup.parse(input);
            Element body = doc.select("body").first();
            System.out.println(doc);
            System.out.println(body);

            for (var product : this.products) {
                body.appendChild(productToHtml(product));
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter("docs/index.html"));
            writer.write(doc.html());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}