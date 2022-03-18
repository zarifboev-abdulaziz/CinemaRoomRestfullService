package uz.pdp.cinemaroomrestfullservice.projection;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public interface HallProjection {
    Long getId();
    String getName();

//    movieAnnouncementId
//    startDateId

    @Value("#{@sessionTimeRepository.getTimesByMovieAnnouncementIdAndHall(target.id, target.movieAnnouncementId, target.startDateId)}")
    List<String> getTimes();


}
