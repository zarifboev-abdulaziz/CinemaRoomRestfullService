package uz.pdp.cinemaroomrestfullservice.payload.authPayloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.cinemaroomrestfullservice.entity.enums.Gender;
import uz.pdp.cinemaroomrestfullservice.entity.userPack.Permission;
import uz.pdp.cinemaroomrestfullservice.entity.userPack.Role;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    private String fullName;
    @Email
    private String email;
    @NotNull
    private String password;
    private LocalDate dateOfBirth;
    private Gender gender;

}
