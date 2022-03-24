package uz.pdp.cinemaroomrestfullservice.entity.userPack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.pdp.cinemaroomrestfullservice.entity.enums.Gender;
import uz.pdp.cinemaroomrestfullservice.entity.template.AbsEntity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User extends AbsEntity implements UserDetails {
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private LocalDate dateOfBirth;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @ManyToMany
    private Set<Role> roles = new HashSet<>();

    @ManyToMany
    private Set<Permission> permissions = new HashSet<>();

    private boolean enabled = false;
    private String emailCode;


    public User(String fullName, String email, String password, LocalDate dateOfBirth, Gender gender, Set<Role> roles, boolean enabled) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.roles = roles;
        this.enabled = enabled;
    }

    //UserDetails methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<String> permissions = new HashSet<>();
        for (Role role : this.roles) {
            role.getPermissions().forEach(permission -> permissions.add(permission.getName()));
        }
        this.permissions.forEach(permission -> permissions.add(permission.getName()));

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        permissions.forEach(permission -> grantedAuthorities.add(new SimpleGrantedAuthority(permission)));
        return grantedAuthorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
