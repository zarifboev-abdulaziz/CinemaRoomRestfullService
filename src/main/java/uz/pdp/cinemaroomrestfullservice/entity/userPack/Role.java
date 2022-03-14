package uz.pdp.cinemaroomrestfullservice.entity.userPack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.cinemaroomrestfullservice.entity.template.AbsEntity;
import uz.pdp.cinemaroomrestfullservice.entity.userPack.Permission;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "roles")
public class Role extends AbsEntity {
    private String name;

    @ManyToMany
    private List<Permission> permissionList = new ArrayList<>();
}
