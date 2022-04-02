package uz.pdp.cinemaroomrestfullservice.entity.administrationPack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.cinemaroomrestfullservice.entity.template.AbsEntity;
import uz.pdp.cinemaroomrestfullservice.entity.ticketPack.Ticket;
import uz.pdp.cinemaroomrestfullservice.entity.userPack.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "transaction_history")
public class TransactionHistory extends AbsEntity {

//    @ManyToOne
//    private User user;

    @ManyToMany
    @JoinTable(name = "transaction_histories_tickets",
    joinColumns = @JoinColumn(name = "transaction_id"),
    inverseJoinColumns = @JoinColumn(name = "ticket_id"))
    private List<Ticket> ticketList;

    private Double amount;
    private boolean isRefunded;
    private String paymentIntentId;

    @ManyToOne
    private PayType payType;


}
