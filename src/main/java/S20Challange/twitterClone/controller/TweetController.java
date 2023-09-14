package S20Challange.twitterClone.controller;

import S20Challange.twitterClone.entity.Tweet;
import S20Challange.twitterClone.service.TweetService;

import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@RestController
@RequestMapping("/")
@Validated
public class TweetController {

    private TweetService tweetService;

    @Autowired
    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @GetMapping("/")
    public List<Tweet> getAllTweets(){
        return tweetService.findAll();
    }

    @GetMapping("/{id}")
    public Tweet getTweetById(@Positive @PathVariable int id){
        return tweetService.find(id);
    }

    @PostMapping("/")
    public Tweet addTweet(@RequestBody Tweet tweet){
        return tweetService.save(tweet);
    }

    @PutMapping("/{id}")
    public Tweet updateTweet(@RequestBody Tweet tweet, @PathVariable int id) {
        Tweet tweetToUpdate = getTweetById(id);
        if (tweetToUpdate != null) {
            tweet.setId(id);
            return tweetService.save(tweet);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public Tweet deleteTweet(@PathVariable int id){
        Tweet tweetToDelete = getTweetById(id);
        tweetService.delete(tweetToDelete);
        return tweetToDelete;
    }
}

