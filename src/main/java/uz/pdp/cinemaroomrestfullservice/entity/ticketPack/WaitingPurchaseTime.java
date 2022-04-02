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
@Entity(name = "waiting_transaction_time")
public class WaitingPurchaseTime extends AbsEntity {
    private int minute;

}
