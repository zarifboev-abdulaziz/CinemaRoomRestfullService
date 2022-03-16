package uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.Afisha;

import java.util.List;

@Repository
public interface AfishaRepository extends JpaRepository<Afisha, Long> {

    @Query(value = "with cte as (\n" +
            "    select ms.id            as id,\n" +
            "           ms.movie_id      as movieId,\n" +
            "           m.title          as movieTitle,\n" +
            "           m.cover_image_id as movieCoverImage,\n" +
            "           json_agg(h.*)    as halllist\n" +
            "    from movie_sessions ms\n" +
            "             join movies m on m.id = ms.movie_id\n" +
            "             join session_halls sh on ms.id = sh.movie_session_id\n" +
            "             join halls h on h.id = sh.hall_id\n" +
            "    group by ms.id, ms.movie_id, m.title, m.cover_image_id\n" +
            ")\n" +
            "\n" +
            "select cast(json_build_object('id', cte.id, 'movieId', cte.movieId, 'movieTitle', cte.movieTitle, 'movieCoverImage', cte.movieCoverImage, 'halls', cte.halllist) as text)\n" +
            "from cte;", nativeQuery = true)
    List<String> getAllAfisha();

    //    Long getId();
//    Long getMovieId();
//    String getMovieTitle();
//    String getMovieCoverImage();
//    List<Hall> halls();


}
