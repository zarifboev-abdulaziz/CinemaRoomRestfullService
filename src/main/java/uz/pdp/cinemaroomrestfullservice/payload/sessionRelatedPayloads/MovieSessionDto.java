package uz.pdp.cinemaroomrestfullservice.payload.sessionRelatedPayloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Movie;

import javax.persistence.ManyToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieSessionDto {
    @ManyToOne
    private Long movieId;
    private boolean active;

}
