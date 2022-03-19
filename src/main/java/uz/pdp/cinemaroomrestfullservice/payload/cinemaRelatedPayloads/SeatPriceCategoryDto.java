package uz.pdp.cinemaroomrestfullservice.payload.cinemaRelatedPayloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatPriceCategoryDto {
   private Long priceCategoryId;
   private Long rowId;
   private Integer startSeatNumber;
   private Integer finishSeatNumber;

}


