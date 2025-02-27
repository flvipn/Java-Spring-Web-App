package important.service;

import important.model.Product;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final List<Product> products = new ArrayList<>(List.of(
            new Product(1, "Jordan 1 UNC Toe", 149.99),
            new Product(2, "Jordan 3 Black Cement", 219.99),
            new Product(3, "Jordan 5 Black Metallic", 209.99),
            new Product(4, "Jordan 4 Bred", 224.99)
    ));

    public List<Product> getProducts() {
        return products;
    }

    public Product getProductById(int prodId) {
        return products.stream()
                .filter(p -> p.getProdId() == prodId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + prodId));
    }

    public void addProduct(Product prod) {
        products.add(prod);
    }

    public void updateProduct(Product prod) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProdId() == prod.getProdId()) {
                products.set(i, prod);
                return;
            }
        }
        throw new RuntimeException("Product not found with id: " + prod.getProdId());
    }

    public void deleteProduct(int prodId) {
        boolean removed = products.removeIf(p -> p.getProdId() == prodId);
        if (!removed) {
            throw new RuntimeException("Product not found with id: " + prodId);
        }
    }
}
