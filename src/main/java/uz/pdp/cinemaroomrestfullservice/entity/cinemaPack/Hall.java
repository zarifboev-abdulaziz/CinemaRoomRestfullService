package uz.pdp.cinemaroomrestfullservice.entity.cinemaPack;

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
@Entity(name = "halls")
public class Hall extends AbsEntity {
    private String name;
    private double vipAdditionalFeePercentage = 0;
}
