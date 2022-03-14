package uz.pdp.cinemaroomrestfullservice.entity.userPack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.cinemaroomrestfullservice.entity.enums.Gender;
import uz.pdp.cinemaroomrestfullservice.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User extends AbsEntity {
    private String fullName;
    private String username;
    private String password;
    private Date dateOfBirth;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @ManyToMany
    private List<Role> roleList = new ArrayList<>();

    @ManyToMany
    private List<Permission> permissionList = new ArrayList<>();



}
