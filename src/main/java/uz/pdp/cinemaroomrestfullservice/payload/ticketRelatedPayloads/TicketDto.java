package uz.pdp.cinemaroomrestfullservice.payload.ticketRelatedPayloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketDto {
    private Long id;
    private String MovieName;
    private LocalDate MovieDate;
    private LocalTime MovieTime;
    public String hallName;
    private Integer rowNumber;
    private Integer seatNumber;
    private double price;


}
