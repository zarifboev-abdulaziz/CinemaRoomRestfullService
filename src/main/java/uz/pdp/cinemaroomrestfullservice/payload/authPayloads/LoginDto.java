package uz.pdp.cinemaroomrestfullservice.payload.authPayloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @Email
    private String email;
    @NotNull
    private String password;
}
