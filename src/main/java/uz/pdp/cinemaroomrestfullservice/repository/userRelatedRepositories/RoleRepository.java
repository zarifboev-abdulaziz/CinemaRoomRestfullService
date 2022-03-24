package uz.pdp.cinemaroomrestfullservice.repository.userRelatedRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.cinemaroomrestfullservice.entity.enums.RoleName;
import uz.pdp.cinemaroomrestfullservice.entity.userPack.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(RoleName roleName);
    Role findByName(String name);
}
