package S20Challange.twitterClone.service;

import S20Challange.twitterClone.entity.Reply;
import S20Challange.twitterClone.entity.Tweet;

import java.util.List;

public interface ReplyService {

    List<Reply> findAll();

    Reply find(int id);

    Tweet save(Tweet tweet);

    void delete(Tweet tweet);
}
