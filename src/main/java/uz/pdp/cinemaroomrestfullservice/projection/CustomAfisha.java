package uz.pdp.cinemaroomrestfullservice.projection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Hall;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.Afisha;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.ReservedHall;

import java.util.List;

//@Projection(types = ReservedHall.class)
public interface CustomAfisha {

    Long getId();
    Long getMovieId();
    String getMovieTitle();
    String getMovieCoverImage();
    String getMovieReleaseDateId();
    String getMovieReleaseDate();

    @Value("#{@hallRepository.getHallsByAfishaId(target.id, target.movieReleaseDateId)}")
    List<HallProjection> getHalls();

}
