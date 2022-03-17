package uz.pdp.cinemaroomrestfullservice.payload.cinemaRelatedPayloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RowSeat {
    private Integer rowNumber;
    private Integer numberOfSeats;


}
