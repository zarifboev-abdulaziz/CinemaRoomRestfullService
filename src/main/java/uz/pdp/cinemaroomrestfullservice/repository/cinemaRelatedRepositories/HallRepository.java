package uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Hall;
import uz.pdp.cinemaroomrestfullservice.projection.HallProjection;

import java.util.List;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {

    @Query(value = "    select h.id, h.name \n" +
            "    from reserved_halls rh\n" +
            "    join halls h on h.id = rh.hall_id\n" +
            "    where rh.afisha_id =:afishaId and rh.start_date_id =:startDateId\n" +
            "    group by h.id, h.name", nativeQuery = true)
    List<HallProjection> getHallsByAfishaId(Long afishaId, Long startDateId);




}
