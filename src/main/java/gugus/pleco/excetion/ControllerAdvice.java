package gugus.pleco.excetion;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "gugus.pleco.controller")
public class ControllerAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto dupulicate(UserDupulicatedException e){
        return new ErrorDto(e.getMessage(),false);
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto emailNotFound(UsernameNotFoundException e){
        return new ErrorDto(e.getMessage(),false);
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto badPassword(BadCredentialsException e){
        return new ErrorDto(e.getMessage(),false);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto notTime(TimeDissatisfactionException e){
        return new ErrorDto(e.getMessage(),false);
    }

}
