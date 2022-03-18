package uz.pdp.cinemaroomrestfullservice.projection.AvailableSeatsProjection;

import org.springframework.beans.factory.annotation.Value;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface AvailableSeatsDto {
    Long getMovieSessionId();

    @Value("#{@rowRepository.getAvailableRowsByMovieAnnouncementId(target.movieSessionId)}")
    List<RowsProjection> getRows();


}
