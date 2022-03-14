package uz.pdp.cinemaroomrestfullservice.entity.cinemaPack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.cinemaroomrestfullservice.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "seats")
public class Seat extends AbsEntity {
    private Integer number;

    @ManyToOne
    private Row row;

    @ManyToOne
    private PriceCategory priceCategory;


}
