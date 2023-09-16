package S20Challange.twitterClone.service;

import S20Challange.twitterClone.entity.Reply;
import S20Challange.twitterClone.entity.Tweet;

import java.util.List;

public interface ReplyService {

    List<Reply> findAll();

    Reply find(int id);

    Reply save(Reply reply);

    void delete(Reply reply);

    Reply likeReply(int id);

    Reply unlikeReply(int id);

    Reply retweetReply(int id);

    Reply undoRetweetReply(int id);
}
