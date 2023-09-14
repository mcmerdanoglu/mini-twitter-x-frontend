package S20Challange.twitterClone.service;

import S20Challange.twitterClone.dao.UserRepository;
import S20Challange.twitterClone.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String email, String password, String username){
        Optional<User> foundUserEmail = userRepository.findUserByEmail(email);//Aynı email ile başka user ekleyince hata mesajı vermesi için eklendi
        Optional<User> foundUserUsername = userRepository.findUserByUsername(username);//Aynı username ile başka user ekleyince hata mesajı vermesi için eklendi
        if(foundUserEmail.isPresent() || foundUserUsername.isPresent()){
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
}
