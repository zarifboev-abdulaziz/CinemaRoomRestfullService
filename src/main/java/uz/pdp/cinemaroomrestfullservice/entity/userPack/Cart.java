package uz.pdp.cinemaroomrestfullservice.entity.userPack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.cinemaroomrestfullservice.entity.template.AbsEntity;
import uz.pdp.cinemaroomrestfullservice.entity.userPack.User;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "carts")
public class Cart extends AbsEntity {
    @OneToOne
    private User user;


}
