package uz.pdp.cinemaroomrestfullservice.projection.AvailableSeatsProjection;

import org.springframework.beans.factory.annotation.Value;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Seat;

import java.util.List;

public interface RowsProjection {

    Long getRowId();
    Integer getRowNumber();

    @Value("#{@seatRepository.getAvailableSeatsByRowIdAndByMovieAnnouncementId(target.rowId, target.movieSessionId)}")
    List<SeatsProjection> getSeats();

}
