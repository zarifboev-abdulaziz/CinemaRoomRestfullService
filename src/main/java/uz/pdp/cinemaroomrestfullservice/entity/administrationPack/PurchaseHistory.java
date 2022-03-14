package uz.pdp.cinemaroomrestfullservice.entity.administrationPack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.cinemaroomrestfullservice.entity.template.AbsEntity;
import uz.pdp.cinemaroomrestfullservice.entity.ticketPack.Ticket;
import uz.pdp.cinemaroomrestfullservice.entity.userPack.User;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "purchase_history")
public class PurchaseHistory extends AbsEntity {

    @ManyToOne
    private User user;

    @OneToOne
    private Ticket ticket;

    private Date date;

    @ManyToOne
    private PayType payType;


}
