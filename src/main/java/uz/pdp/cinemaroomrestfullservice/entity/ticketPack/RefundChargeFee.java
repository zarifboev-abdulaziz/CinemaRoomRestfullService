package uz.pdp.cinemaroomrestfullservice.entity.ticketPack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.cinemaroomrestfullservice.entity.template.AbsEntity;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "refund_charge_fee")
public class RefundChargeFee extends AbsEntity {
    private int intervalMinutes;
    private double percentage;


}
