package uz.pdp.cinemaroomrestfullservice.entity.movieSessionPack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.cinemaroomrestfullservice.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.sql.Date;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "session_dates")
@OnDelete(action = OnDeleteAction.CASCADE)
public class SessionDate extends AbsEntity {
    @Column(columnDefinition = "date")
    private Date date;


}
