package uz.pdp.cinemaroomrestfullservice.entity.moviePack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.cinemaroomrestfullservice.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "distributors")
public class Distributor extends AbsEntity {
    private String name;

    @Column(columnDefinition = "text")
    private String description;


}
