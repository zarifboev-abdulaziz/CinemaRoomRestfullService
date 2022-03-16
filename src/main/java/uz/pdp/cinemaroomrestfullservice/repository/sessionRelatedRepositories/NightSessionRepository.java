package uz.pdp.cinemaroomrestfullservice.repository.sessionRelatedRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.NightSessionAddFee;

@Repository
public interface NightSessionRepository extends JpaRepository<NightSessionAddFee, Long> {
}
