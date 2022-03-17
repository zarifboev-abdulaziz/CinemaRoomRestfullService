package uz.pdp.cinemaroomrestfullservice.payload.cinemaRelatedPayloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HallDto {

    private String hallName;
    private double vipAdditionalFeePercentage;
    private Integer numberOfRows;
    private Integer fixedNumberOfSeatsForEachRow;
    private List<RowDto> rowDtos;

}
