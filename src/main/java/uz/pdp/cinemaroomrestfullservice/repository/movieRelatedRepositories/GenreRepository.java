package uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
}
