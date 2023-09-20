package S20Challange.twitterClone.dao;

import S20Challange.twitterClone.entity.Tweet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TweetRepository extends JpaRepository<Tweet, Integer> {

    /*
    @Transactional
    @Modifying
    @Query("UPDATE Tweet t SET t.user = null WHERE t.id = ?1 AND t.user IS NOT NULL")
    int softDeleteTweet(int id);
     */
}
