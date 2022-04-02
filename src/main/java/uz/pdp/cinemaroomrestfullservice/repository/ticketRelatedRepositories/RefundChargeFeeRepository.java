package uz.pdp.cinemaroomrestfullservice.repository.ticketRelatedRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.cinemaroomrestfullservice.entity.ticketPack.RefundChargeFee;

import java.sql.Timestamp;

public interface RefundChargeFeeRepository extends JpaRepository<RefundChargeFee, Long> {


    @Query(value = "select cast((sd.date + st.time) as timestamp) as movieSessionTime\n" +
            "from movie_sessions ms\n" +
            "join tickets t on ms.id = t.movie_session_id\n" +
            "join session_dates sd on ms.start_date_id = sd.id\n" +
            "join session_times st on ms.start_time_id = st.id\n" +
            "where t.id =:ticketId", nativeQuery = true)
    Timestamp getMovieSessionTime(Long ticketId);

    @Query(value = "select *\n" +
            "from get_percentage_by_interval(:intervalTime)", nativeQuery = true)
    Double getPercentageByInterval(Long intervalTime);

//    create function get_percentage_by_interval(i_left_minute double precision) returns double precision
//    language plpgsql
//    as
//            $$
//    DECLARE
//    table_record RECORD;
//    BEGIN
//
//
//        for table_record in
//    select rcf.interval_minutes as interval, rcf.percentage as percent from refund_charge_fee rcf order by rcf.interval_minutes asc
//    loop
//            if i_left_minute < table_record.interval then return table_record.percent; end if;
//    end loop;
//
//    return 0;
//
//    end;
//    $$;

}
