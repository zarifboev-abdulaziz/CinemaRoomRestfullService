package uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.SessionTime;
import uz.pdp.cinemaroomrestfullservice.projection.HallProjection;

import java.lang.management.OperatingSystemMXBean;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionTimeRepository extends JpaRepository<SessionTime, Long> {

    Optional<SessionTime> findByTime(LocalTime time);



    @Query(value = "select cast(st.time as text) as times \n" +
            "from session_times st \n" +
            "join movie_sessions ms on st.id = ms.start_time_id \n" +
            "where  ms.movie_announcement_id=:movieAnnouncementId \n" +
            "AND ms.start_date_id=:startDateId \n" +
            "AND ms.hall_id =:hallId ", nativeQuery = true)
    List<String> getTimesByMovieAnnouncementIdAndHall(Long hallId, Long movieAnnouncementId, Long startDateId);
}
