package uz.pdp.cinemaroomrestfullservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminStats {
    private Integer numberOfUsers;

    private Integer numberOfTicketsSoldToday;
    private Integer numberOfTicketsRefundToday;

    private Integer numberOfTicketsSoldThisMonth;
    private Integer numberOfTicketsRefundThisMonth;

    private Double totalIncomeToday;
    private Double totalRefundAmountToday;

    private Double totalIncomeThisMonth;
    private Double totalRefundAmountThisMonth;



}
