package S20Challange.twitterClone.dao;

import S20Challange.twitterClone.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TweetRepository extends JpaRepository<Tweet, Integer> {
}
