package uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.cinemaroomrestfullservice.entity.template.AbsEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "night_session_add_fee")
@OnDelete(action = OnDeleteAction.CASCADE)
public class NightSessionAddFee extends AbsEntity {
    private double percentage;

    @OneToOne(cascade = CascadeType.ALL)
    private SessionDate date;
}
