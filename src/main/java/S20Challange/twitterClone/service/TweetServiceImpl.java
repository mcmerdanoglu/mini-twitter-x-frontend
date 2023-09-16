package S20Challange.twitterClone.service;

import S20Challange.twitterClone.dao.TweetRepository;
import S20Challange.twitterClone.dao.UserRepository;
import S20Challange.twitterClone.entity.Tweet;
import S20Challange.twitterClone.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TweetServiceImpl implements TweetService{

    private TweetRepository tweetRepository;
   // private UserRepository userRepository;//User bilgisini tutabilmek için eklendi!

    @Autowired
    public TweetServiceImpl(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    @Override
    public List<Tweet> findAll() {
        return tweetRepository.findAll();
    }

    @Override
    public Tweet find(int id) {
        Optional<Tweet> tweet = tweetRepository.findById(id);
        if(tweet.isPresent()){
            return tweet.get();
        }
        //TODO throw exception
        return null;
    }

    @Override
    public Tweet save(Tweet tweet) {
        return tweetRepository.save(tweet);
    }

    @Override
    //@Transactional
    public void delete(Tweet tweet) {
        tweetRepository.delete(tweet);
    }
}
