package uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Hall;
import uz.pdp.cinemaroomrestfullservice.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "reserved_halls")
@OnDelete(action = OnDeleteAction.CASCADE)
public class ReservedHall extends AbsEntity {
    @ManyToOne
    private Afisha afisha;

    @ManyToOne
    private Hall hall;

    @ManyToOne
    private SessionDate startDate;

    @ManyToOne
    private SessionTime startTime;

    @ManyToOne
    private SessionTime endTime;


}
