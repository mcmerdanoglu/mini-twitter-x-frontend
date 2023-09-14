package S20Challange.twitterClone.dao;

import S20Challange.twitterClone.entity.User;
import S20Challange.twitterClone.entity.UserFirst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM user u WHERE u.email = :email")
    Optional<User> findUserByEmail(String email);
}

