package uz.pdp.cinemaroomrestfullservice.payload.sessionRelatedPayloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class ReservedHallDto {
    private Long id;
    private Long afishaId;
    private Long hallId;

    private LocalDate startDate;
    private LocalDate endDate;

    private LocalTime startTime;
    private LocalTime endTime;


}
