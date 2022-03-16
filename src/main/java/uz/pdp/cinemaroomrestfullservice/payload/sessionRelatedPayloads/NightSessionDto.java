package uz.pdp.cinemaroomrestfullservice.payload.sessionRelatedPayloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NightSessionDto {
    private double percentage;
    private Date date;
}
