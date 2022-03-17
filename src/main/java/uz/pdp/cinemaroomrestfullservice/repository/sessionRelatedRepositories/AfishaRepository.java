package uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.Afisha;
import uz.pdp.cinemaroomrestfullservice.projection.CustomAfisha;

import java.util.List;

@Repository
public interface AfishaRepository extends JpaRepository<Afisha, Long> {

    @Query(value = "select a.id,\n" +
            "       m.id movieId,\n" +
            "       m.title movieTitle,\n" +
            "       m.cover_image_id movieCoverImage,\n" +
            "       rh.start_date_id movieReleaseDateId, \n" +
            "       sd.date movieReleaseDate \n" +
            "from afishas a\n" +
            "join movies m on m.id = a.movie_id\n" +
            "join reserved_halls rh on a.id = rh.afisha_id\n" +
            "join session_dates sd on sd.id = rh.start_date_id\n" +
            "where sd.date >= cast(now() as date) \n" +
            "group by a.id, m.id, m.title, m.cover_image_id, rh.start_date_id, sd.date\n" +
            "order by rh.start_date_id ", nativeQuery = true)
    Page<CustomAfisha> getAllAfishaDto(Pageable pageable);


    //    Long getId();
//    Long getMovieId();
//    String getMovieTitle();
//    String getMovieCoverImage();
//    List<Hall> halls();


}
