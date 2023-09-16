package S20Challange.twitterClone.controller;

import S20Challange.twitterClone.dto.LoginRequest;
import S20Challange.twitterClone.dto.LoginResponse;
import S20Challange.twitterClone.dto.RegistrationUser;
import S20Challange.twitterClone.entity.User;
import S20Challange.twitterClone.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    public AuthenticationService authenticationService;

    @Autowired
    public UserController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public User register(@RequestBody RegistrationUser registrationUser) {
        return authenticationService.register(registrationUser.getEmail(), registrationUser.getPassword(), registrationUser.getUsername());
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }
}
