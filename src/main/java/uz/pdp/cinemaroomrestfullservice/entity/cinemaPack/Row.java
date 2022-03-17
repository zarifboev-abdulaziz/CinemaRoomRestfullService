package uz.pdp.cinemaroomrestfullservice.entity.cinemaPack;

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
@Entity(name = "hall_rows")
@OnDelete(action = OnDeleteAction.CASCADE)
public class Row extends AbsEntity {
    private Integer number;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Hall hall;
}
