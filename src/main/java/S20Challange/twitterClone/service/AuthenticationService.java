package S20Challange.twitterClone.service;

import S20Challange.twitterClone.dao.UserRepository;
import S20Challange.twitterClone.dto.LoginResponse;
import S20Challange.twitterClone.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private TokenService tokenService;

    @Autowired
    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public User register(String email, String password, String username) {
        Optional<User> foundUserEmail = userRepository.findUserByEmail(email);//Aynı email ile başka user ekleyince hata mesajı vermesi için eklendi
        Optional<User> foundUserUsername = userRepository.findUserByUsername(username);//Aynı username ile başka user ekleyince hata mesajı vermesi için eklendi
        if (foundUserEmail.isPresent() || foundUserUsername.isPresent()) {
            //TODO Throw Exception
            return null;
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setUsername(username);
        return userRepository.save(user);
    }

    public LoginResponse login(String email, String password) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
            String token = tokenService.generateJwtToken(auth);
            return new LoginResponse(token);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new LoginResponse("");
        }
     }
}
