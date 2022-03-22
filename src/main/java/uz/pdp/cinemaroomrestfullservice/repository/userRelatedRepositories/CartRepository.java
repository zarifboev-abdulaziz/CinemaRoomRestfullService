package uz.pdp.cinemaroomrestfullservice.repository.userRelatedRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.cinemaroomrestfullservice.entity.userPack.Cart;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long user_id);
}
