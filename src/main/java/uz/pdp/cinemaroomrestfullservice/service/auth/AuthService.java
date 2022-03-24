package uz.pdp.cinemaroomrestfullservice.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.cinemaroomrestfullservice.entity.enums.Gender;
import uz.pdp.cinemaroomrestfullservice.entity.enums.RoleName;
import uz.pdp.cinemaroomrestfullservice.entity.userPack.User;
import uz.pdp.cinemaroomrestfullservice.payload.ApiResponse;
import uz.pdp.cinemaroomrestfullservice.payload.authPayloads.LoginDto;
import uz.pdp.cinemaroomrestfullservice.payload.authPayloads.RegisterDto;
import uz.pdp.cinemaroomrestfullservice.repository.userRelatedRepositories.RoleRepository;
import uz.pdp.cinemaroomrestfullservice.repository.userRelatedRepositories.UserRepository;
import uz.pdp.cinemaroomrestfullservice.security.CurrentUser;
import uz.pdp.cinemaroomrestfullservice.security.JwtProvider;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Lazy
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;

    public ApiResponse registerUser(RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail()))
            return new ApiResponse("This email is already exists", false);

        User user = new User();
        user.setFullName(registerDto.getFullName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setDateOfBirth(registerDto.getDateOfBirth());
        user.setGender(registerDto.getGender());
        user.setRoles(Collections.singleton(roleRepository.findByName(RoleName.ROLE_USER.name())));
        user.setEmailCode(UUID.randomUUID().toString());

        Boolean aBoolean = sendEmail(user.getEmail(), user.getEmailCode());
        if(!aBoolean){
            user.setEnabled(true);
            userRepository.save(user);
            return new ApiResponse("Email not send to user but account activated.", false);
        }

        user.setEnabled(true);
        userRepository.save(user);

        return new ApiResponse("Successfully Registered. Please confirm your email in order to activate!", true);
    }

    private Boolean sendEmail(String sendingEmail, String emailCode) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("testpdp@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Confirmation");
            mailMessage.setText("<a href='http://localhost:8080/api/auth/verifyEmail?emailCode=" + emailCode + "&email=" + sendingEmail + "'>Confirm</a>");
            javaMailSender.send(mailMessage);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    public ApiResponse verifyEmail(String emailCode, String email) {
        Optional<User> optionalUser = userRepository.findByEmailAndEmailCode(email, emailCode);
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            userRepository.save(user);
            return new ApiResponse("Account is confirmed", true);
        }
        return new ApiResponse("Account already confirmed", false);
    }

    public ApiResponse login(LoginDto loginDto) {

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getEmail(), loginDto.getPassword()
            ));

            User principal = (User) authentication.getPrincipal();
            String generatedToken = jwtProvider.generateToken(principal.getEmail(), principal.getRoles());

            return new ApiResponse("Token", true, generatedToken);
        }catch (BadCredentialsException e){
            return new ApiResponse("Email or password not found", false);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

