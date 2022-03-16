package uz.pdp.cinemaroomrestfullservice.payload.sessionRelatedPayloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Hall;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.Afisha;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.SessionDate;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.SessionTime;

import javax.persistence.ManyToOne;
import java.sql.Date;
import java.sql.Time;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class ReservedHallDto {
    private Long id;
    private Long afishaId;
    private Long hallId;

    private Date startDate;
    private Date endDate;

    private Time startTime;
    private Time endTime;


}
