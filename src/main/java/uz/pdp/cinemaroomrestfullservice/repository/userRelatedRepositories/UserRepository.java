package uz.pdp.cinemaroomrestfullservice.repository.userRelatedRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.cinemaroomrestfullservice.entity.userPack.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
