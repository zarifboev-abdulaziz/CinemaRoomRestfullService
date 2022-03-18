package uz.pdp.cinemaroomrestfullservice.projection;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;

//@Projection(types = ReservedHall.class)
public interface CustomMovieAnnouncement {

    Long getId();      //Movie Announcement id
    Long getMovieId();
    String getMovieTitle();
    String getMovieCoverImage();
    String getMovieReleaseDateId();
    String getMovieReleaseDate();

    @Value("#{@hallRepository.getHallsByMovieAnnouncementId(target.id, target.movieReleaseDateId)}")
    List<HallProjection> getHalls();

}
