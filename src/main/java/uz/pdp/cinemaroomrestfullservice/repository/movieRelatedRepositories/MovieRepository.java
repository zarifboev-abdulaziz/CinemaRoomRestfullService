package uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query(value = "select * from delete_movie_attachments(:movieId)", nativeQuery = true)
    boolean deleteAllFilesOfMovie(Long movieId);


}
