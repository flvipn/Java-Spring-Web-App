package important.controller;

import important.model.Product;
import important.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.ok(service.getProducts());
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product prod) {
        Product savedProduct = service.addProduct(prod);
        return ResponseEntity.status(201).body(savedProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @Valid @RequestBody Product prod) {
        return ResponseEntity.ok(service.updateProduct(id, prod));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Product>> filterProductsByBrand(@PathVariable String brand) {
        List<Product> filteredProducts = service.getProductsByBrand(brand);
        return ResponseEntity.ok(filteredProducts);
    }

    @GetMapping("/{id}")
    public String getProductById(@PathVariable int id, Model model) {
        Product product = service.getProductById(id);
        model.addAttribute("product", product);
        return "product";
    }


}
