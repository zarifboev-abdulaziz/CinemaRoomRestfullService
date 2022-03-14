package uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Director;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Long> {
}
