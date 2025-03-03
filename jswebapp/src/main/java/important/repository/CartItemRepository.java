package important.repository;

import important.model.CartItem;
import important.model.User;
import important.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
    List<CartItem> findByUser(User user);
    void deleteByUser(User user);
    CartItem findByUserAndProduct(User user, Product product);
}
