package uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.SessionDate;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Repository
public interface SessionDateRepository extends JpaRepository<SessionDate, Long> {

    Optional<SessionDate> findByDate(LocalDate date);

}
