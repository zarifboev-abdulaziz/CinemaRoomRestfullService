package uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.cinemaroomrestfullservice.entity.cinemaPack.Hall;
import uz.pdp.cinemaroomrestfullservice.entity.template.AbsEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "reserved_halls")
@OnDelete(action = OnDeleteAction.CASCADE)
public class ReservedHall extends AbsEntity {
    @ManyToOne(cascade = CascadeType.MERGE)
    private Afisha afisha;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Hall hall;

    @ManyToOne(cascade = CascadeType.MERGE)
    private SessionDate startDate;

    @ManyToOne(cascade = CascadeType.MERGE)
    private SessionTime startTime;

    @ManyToOne(cascade = CascadeType.MERGE)
    private SessionTime endTime;


}
