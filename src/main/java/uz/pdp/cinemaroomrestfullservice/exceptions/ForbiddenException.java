package uz.pdp.cinemaroomrestfullservice.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
@AllArgsConstructor
public class ForbiddenException extends RuntimeException{
    private String type;
    private String message;
}
