package uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Seat;
import uz.pdp.cinemaroomrestfullservice.projection.AvailableSeatsProjection.AvailableSeatsDto;
import uz.pdp.cinemaroomrestfullservice.projection.AvailableSeatsProjection.SeatsProjection;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    void deleteByRowId(Long row_id);

    List<Seat> findByRowId(Long row_id);

    Optional<Seat> findByIdAndRowId(Long id, Long row_id);

    Optional<Seat> findByNumberAndRowId(Integer number, Long row_id);

    void deleteByIdAndRowId(Long id, Long row_id);

    boolean existsByNumberAndRowId(Integer number, Long row_id);

    boolean existsByNumberAndRowIdAndIdNot(Integer number, Long row_id, Long id);

    @Query(value = "select ms.id as movieSessionId\n" +
            "from movie_sessions ms\n" +
            "where ms.id =:movieSession", nativeQuery = true)
    AvailableSeatsDto getAvailableSeatsForMovieSession(Long movieSession);

    @Query(value = "select s.id seatId, s.number seatNumber\n" +
            "from seats s\n" +
            "join hall_rows hr on hr.id = s.row_id\n" +
            "join halls h on h.id = hr.hall_id\n" +
            "join movie_sessions ms on h.id = ms.hall_id\n" +
            "where ms.id =:movieSessionId \n" +
            "AND hr.id =:rowId \n" +
            "AND s.id not in (\n" +
            "    select t.seat_id\n" +
            "    from tickets t\n" +
            "    where t.movie_session_id =:movieSessionId \n" +
            "    OR t.status != 'REFUNDED'\n" +
            "    )", nativeQuery = true)
    List<SeatsProjection> getAvailableSeatsByRowIdAndByMovieAnnouncementId(Long rowId, Long movieSessionId);

}
