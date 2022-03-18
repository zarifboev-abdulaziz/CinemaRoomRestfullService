package uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.MovieAnnouncement;
import uz.pdp.cinemaroomrestfullservice.projection.CustomMovieAnnouncement;

@Repository
public interface MovieAnnouncementRepository extends JpaRepository<MovieAnnouncement, Long> {

    @Query(value = "select ma.id,\n" +
            "                   m.id movieId,\n" +
            "                   m.title movieTitle,\n" +
            "                   m.cover_image_id movieCoverImage,\n" +
            "                   ms.start_date_id movieReleaseDateId,\n" +
            "                   sd.date movieReleaseDate\n" +
            "            from movie_announcements ma\n" +
            "            join movies m on m.id = ma.movie_id\n" +
            "            join movie_sessions ms on ma.id = ms.movie_announcement_id\n" +
            "            join session_dates sd on sd.id = ms.start_date_id\n" +
            "            where sd.date >= cast(now() as date)\n" +
            "            group by ma.id, m.id, m.title, m.cover_image_id, ms.start_date_id, sd.date\n" +
            "            order by ms.start_date_id ", nativeQuery = true)
    Page<CustomMovieAnnouncement> getAllMovieAnnouncementsDto(Pageable pageable);


}
