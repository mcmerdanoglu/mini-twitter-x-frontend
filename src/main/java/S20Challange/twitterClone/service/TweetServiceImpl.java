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
    @Transactional
    public void softDelete(int id) {
        Optional<Tweet> tweetOptional = tweetRepository.findById(id);
        if (tweetOptional.isPresent()) {
            Tweet tweet = tweetOptional.get();
            tweet.setUser(null);
            tweetRepository.save(tweet); // Set the user to null
            tweetRepository.delete(tweet); // Delete the tweet from the database
        }
    }

    @Override
    @Transactional
    public void delete(Tweet tweet) {
        tweetRepository.delete(tweet);
    }

    @Override
    public Tweet likeTweet(int id) {
        Tweet tweet = tweetRepository.findById(id).orElse(null);
        if (tweet != null) {
            tweet.incrementLikes();
            tweetRepository.save(tweet);
        }
        return tweet;
    }

    @Override
    public Tweet unlikeTweet(int id) {
        Tweet tweet = tweetRepository.findById(id).orElse(null);
        if (tweet != null) {
            tweet.decrementLikes();
            tweetRepository.save(tweet);
        }
        return tweet;
    }

    @Override
    public Tweet retweet(int id) {
        Tweet tweet = tweetRepository.findById(id).orElse(null);
        if (tweet != null) {
            tweet.incrementRetweets();
            tweetRepository.save(tweet);
        }
        return tweet;
    }

    @Override
    public Tweet undoRetweet(int id) {
        Tweet tweet = tweetRepository.findById(id).orElse(null);
        if (tweet != null) {
            tweet.decrementRetweets();
            tweetRepository.save(tweet);
        }
        return tweet;
    }
}
