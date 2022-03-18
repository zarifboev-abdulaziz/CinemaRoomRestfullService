package uz.pdp.cinemaroomrestfullservice.entity.ticketPack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Seat;
import uz.pdp.cinemaroomrestfullservice.entity.moviePack.Attachment;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.MovieAnnouncement;
import uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack.MovieSession;
import uz.pdp.cinemaroomrestfullservice.entity.template.AbsEntity;
import uz.pdp.cinemaroomrestfullservice.entity.userPack.Cart;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tickets")
public class Ticket extends AbsEntity {

    @ManyToOne
    private MovieSession movieSession;

    @ManyToOne
    private Seat seat;

    @OneToOne
    private Attachment qrCode;

    private double price;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @ManyToOne
    private Cart cart;





}
