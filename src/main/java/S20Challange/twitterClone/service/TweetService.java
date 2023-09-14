package S20Challange.twitterClone.service;

import S20Challange.twitterClone.entity.Tweet;

import java.util.List;

public interface TweetService {

    List<Tweet> findAll();

    Tweet find(int id);

    Tweet save(Tweet tweet);

    void delete(Tweet tweet);
}
