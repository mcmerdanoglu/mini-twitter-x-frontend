package S20Challange.twitterClone.service;

import S20Challange.twitterClone.entity.Tweet;

import java.util.List;

public interface TweetService {

    List<Tweet> findAll();

    Tweet find(int id);

    Tweet save(Tweet tweet);

    void delete(Tweet tweet);

    void softDelete(int id);//tweet delete için tweet tablosundaki useri önce nulla çekmek için yazıldı ama işe yaramamadı
}
