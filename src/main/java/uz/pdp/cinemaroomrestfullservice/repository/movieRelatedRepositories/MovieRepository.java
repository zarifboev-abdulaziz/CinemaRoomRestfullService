package uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Hall;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Movie;
import uz.pdp.cinemaroomrestfullservice.projection.CustomMovie;

import java.util.List;


@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query(value = "select * from delete_movie_attachments(:movieId)", nativeQuery = true)
    boolean deleteAllFilesOfMovie(Long movieId);

    @Query(value = "select id,\n" +
            "       title,\n" +
            "       description,\n" +
            "       duration_minutes as durationMinutes,\n" +
            "       trailer_video_url as trailerVideoUrl,\n" +
            "       min_price as minPrice,\n" +
            "       distributor_share_percentage as distributorSharePercentage\n" +
            "from movies ", nativeQuery = true)
    Page<CustomMovie>  getAllMoviesWithPage(Pageable pageable);




}
