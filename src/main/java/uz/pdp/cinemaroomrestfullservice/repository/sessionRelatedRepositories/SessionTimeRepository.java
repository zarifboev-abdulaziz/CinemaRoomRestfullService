package uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.SessionTime;

import java.lang.management.OperatingSystemMXBean;
import java.sql.Time;
import java.util.Optional;

@Repository
public interface SessionTimeRepository extends JpaRepository<SessionTime, Long> {

    Optional<SessionTime> findByTime(Time time);
}
