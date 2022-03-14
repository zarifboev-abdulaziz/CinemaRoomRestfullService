package uz.pdp.cinemaroomrestfullservice.repository.movieRelatedRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Distributor;

@Repository
public interface DistributorRepository extends JpaRepository<Distributor, Long> {
}
