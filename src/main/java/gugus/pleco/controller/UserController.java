package gugus.pleco.controller;

import gugus.pleco.aop.aspect.annotation.Log;
import gugus.pleco.controller.userdto.DuplicateDto;
import gugus.pleco.controller.userdto.SignUpDto;
import gugus.pleco.controller.userdto.UserDto;
import gugus.pleco.domain.User;
import gugus.pleco.excetion.UserDupulicatedException;
import gugus.pleco.jwt.JwtTokenProvider;
import gugus.pleco.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    @Log
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public SignUpDto signup(@RequestBody UserDto userDto)throws UserDupulicatedException {
        User user = userService.join(userDto);
        return new SignUpDto(user.getId(), true);
    }

    @Log
    @GetMapping("/duplicate")
    @ResponseStatus(HttpStatus.OK)
    public DuplicateDto checkEmail(@RequestParam String email) throws UserDupulicatedException  {
        return new DuplicateDto(userService.checkEmail(email));
    }
    @Log
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> login(@RequestBody UserDto userDto, HttpServletResponse response) throws UsernameNotFoundException, BadCredentialsException, Throwable {
        Map<String, String> map = jwtTokenProvider.createToken(userDto.getEmail(), Arrays.asList("ROLE_USER"));
        Long id = userService.login(userDto,map.get("refresh"));
        response.setHeader("X-AUTH-TOKEN",map.get("access"));
        return new ResponseEntity<String>("ok",HttpStatus.OK);
    }

}
