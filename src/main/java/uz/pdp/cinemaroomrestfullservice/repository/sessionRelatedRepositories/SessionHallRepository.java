package uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.SessionHall;

@Repository
public interface SessionHallRepository extends JpaRepository<SessionHall, Long> {
}
