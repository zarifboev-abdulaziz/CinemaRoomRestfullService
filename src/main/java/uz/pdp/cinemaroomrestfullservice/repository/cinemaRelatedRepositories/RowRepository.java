package uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Row;

import java.util.List;
import java.util.Optional;

@Repository
public interface RowRepository extends JpaRepository<Row, Long> {

    Page<Row> findAllByHallId(Long hall_id, Pageable pageable);

    Optional<Row> findByIdAndHallId(Long id, Long hall_id);

    Optional<Row> deleteByIdAndHallId(Long id, Long hall_id);

    boolean existsByNumberAndHallId(Integer number, Long hall_id);

    boolean existsByNumberAndHallIdAndIdNot(Integer number, Long hall_id, Long id);

}
