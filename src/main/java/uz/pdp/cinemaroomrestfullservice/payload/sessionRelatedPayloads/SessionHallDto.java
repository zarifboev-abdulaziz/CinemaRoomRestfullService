package uz.pdp.cinemaroomrestfullservice.payload.sessionRelatedPayloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Hall;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.MovieSession;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionHallDto {
    private Long id;
    private Long movieSessionId;
    private Long hallId;

}
