package uz.pdp.cinemaroomrestfullservice.entity.userPack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import uz.pdp.cinemaroomrestfullservice.entity.enums.RoleName;
import uz.pdp.cinemaroomrestfullservice.entity.template.AbsEntity;
import uz.pdp.cinemaroomrestfullservice.entity.userPack.Permission;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "roles")
public class Role extends AbsEntity {
    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany
    private Set<Permission> permissions = new HashSet<>();


    public Role(String name) {
        this.name = name;
    }
}
