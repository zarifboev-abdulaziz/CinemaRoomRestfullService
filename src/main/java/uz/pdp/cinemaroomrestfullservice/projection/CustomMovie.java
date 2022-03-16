package uz.pdp.cinemaroomrestfullservice.projection;


import org.springframework.data.rest.core.config.Projection;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Movie;

import java.util.ArrayList;
import java.util.List;

@Projection(types = Movie.class)
public interface CustomMovie {
    Long getId();
    String getTitle();
    String getDescription();
    Integer getDurationMinutes();
    String getTrailerVideoUrl();
    Double getMinPrice();
    Double getDistributorSharePercentage();


}
