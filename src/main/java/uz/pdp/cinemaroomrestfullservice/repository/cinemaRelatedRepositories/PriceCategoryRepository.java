package uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.PriceCategory;

@Repository
public interface PriceCategoryRepository extends JpaRepository<PriceCategory, Long> {
}
