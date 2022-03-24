package uz.pdp.cinemaroomrestfullservice.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import uz.pdp.cinemaroomrestfullservice.entity.userPack.User;
import uz.pdp.cinemaroomrestfullservice.exceptions.ForbiddenException;

@Component
@Aspect
public class CheckPermissionExecutor {
    @Before(value = "@annotation(checkPermission)")
    public void checkUserPermissionMyMethod(CheckPermission checkPermission){
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean anyMatch = principal.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(checkPermission.value()));
        if (!anyMatch){
            throw new ForbiddenException(checkPermission.value(), "Access Denied");
        }
    }
}
