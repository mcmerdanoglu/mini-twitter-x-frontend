package S20Challange.twitterClone.controller;

import S20Challange.twitterClone.dto.LoginRequest;
import S20Challange.twitterClone.dto.LoginResponse;
import S20Challange.twitterClone.dto.RegistrationUser;
import S20Challange.twitterClone.entity.User;
import S20Challange.twitterClone.exceptions.GlobalExceptionHandler;
import S20Challange.twitterClone.exceptions.MessageException;
import S20Challange.twitterClone.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    public AuthenticationService authenticationService;

    @Autowired
    public UserController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public User register(@Valid @RequestBody RegistrationUser registrationUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors, e.g., return a custom error response
            throw new MessageException("Credentials are not valid", HttpStatus.BAD_REQUEST);
        }
        return authenticationService.register(registrationUser.getEmail(), registrationUser.getPassword(), registrationUser.getUsername());
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors, e.g., return a custom error response
            throw new MessageException("Credentials are not valid", HttpStatus.BAD_REQUEST);
        }
        return authenticationService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }
}
