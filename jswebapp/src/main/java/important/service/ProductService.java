package important.service;

import important.model.Product;
import important.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepo repo;

    @Autowired
    public ProductService(ProductRepo repo) {
        this.repo = repo;
    }

    public List<Product> getProducts() {
        return repo.findAll();
    }

    public Product getProductById(int prodId) {
        return repo.findById(prodId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + prodId));
    }

    public Product addProduct(Product prod) {
        if (repo.findByName(prod.getName()).isPresent()) {
            throw new RuntimeException("Product with name '" + prod.getName() + "' already exists.");
        }
        if (prod.getThumbnail() == null || prod.getThumbnail().isEmpty()) {
            prod.setThumbnail("/images/default.jpg"); // Imagine default
        }
        return repo.save(prod);
    }

    public Product updateProduct(int id, Product updatedProd) {
        Product existingProduct = getProductById(id);

        existingProduct.setCategory(updatedProd.getCategory());
        existingProduct.setBrand(updatedProd.getBrand());
        existingProduct.setName(updatedProd.getName());
        existingProduct.setDescription(updatedProd.getDescription());
        existingProduct.setPrice(updatedProd.getPrice());
        existingProduct.setAvailable(updatedProd.isAvailable());
        existingProduct.setQuantity(updatedProd.getQuantity());

        return repo.save(existingProduct);
    }

    public void deleteProduct(int prodId) {
        if (!repo.existsById(prodId)) {
            throw new RuntimeException("Cannot delete: Product with id " + prodId + " does not exist.");
        }
        repo.deleteById(prodId);
    }

    public List<Product> getProductsByBrand(String brand) {
        return repo.findByBrand(brand);
    }

}
