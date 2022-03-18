package uz.pdp.cinemaroomrestfullservice.repository.cinemaRelatedRepositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Hall;
import uz.pdp.cinemaroomrestfullservice.projection.HallProjection;
import uz.pdp.cinemaroomrestfullservice.projection.HallProjectionDto;

import java.util.List;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {

    @Query(value = "select h.id, h.name,\n" +
            "       ms.movie_announcement_id as movieAnnouncementId,\n" +
            "       ms.start_date_id as startDateId\n" +
            "from movie_sessions ms\n" +
            "         join halls h on h.id = ms.hall_id\n" +
            "where ms.movie_announcement_id =:movieAnnouncement and ms.start_date_id =:startDate\n" +
            "group by h.id, h.name, ms.movie_announcement_id, ms.start_date_id", nativeQuery = true)
    List<HallProjection> getHallsByMovieAnnouncementId(Long movieAnnouncement, Long startDate);


    @Query(value = "with cte as (\n" +
            "select count(s.*) as count, h2.id as hallid from seats s\n" +
            "join hall_rows r on r.id = s.row_id\n" +
            "right join halls h2 on h2.id = r.hall_id\n" +
            "group by h2.id)\n" +
            "select h. id, h.name,\n" +
            "       (select count(hr.*) from hall_rows hr where hr.hall_id = h.id) as numberOfRows,\n" +
            "       cte.count as numberOfSeats\n" +
            "from halls h\n" +
            "join cte on cte.hallid = h.id \n" +
            "order by h.created_at", nativeQuery = true)
    Page<HallProjectionDto> getHallWithStats(Pageable pageable);




}
