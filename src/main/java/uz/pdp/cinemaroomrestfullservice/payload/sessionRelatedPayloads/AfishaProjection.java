package uz.pdp.cinemaroomrestfullservice.payload.sessionRelatedPayloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Hall;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AfishaProjection {

    Long id;
    Long movieId;
    String movieTitle;
    String movieCoverImage;
    List<Hall> halls;

}
