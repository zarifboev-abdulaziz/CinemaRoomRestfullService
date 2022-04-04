package uz.pdp.cinemaroomrestfullservice.repository.userRelatedRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.cinemaroomrestfullservice.entity.userPack.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmailAndEmailCode(String email, String emailCode);

    Optional<User> findByEmail(String email);

    @Query(value = "select count(*) from users", nativeQuery = true)
    Integer getCountOfUsers();
}
