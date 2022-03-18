package uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Seat;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    void deleteByRowId(Long row_id);

    List<Seat> findByRowId(Long row_id);

    Optional<Seat> findByIdAndRowId(Long id, Long row_id);

    void deleteByIdAndRowId(Long id, Long row_id);

    boolean existsByNumberAndRowId(Integer number, Long row_id);

    boolean existsByNumberAndRowIdAndIdNot(Integer number, Long row_id, Long id);

}
