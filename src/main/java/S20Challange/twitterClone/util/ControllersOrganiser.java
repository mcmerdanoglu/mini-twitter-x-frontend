package S20Challange.twitterClone.util;

import S20Challange.twitterClone.dto.ReplyResponse;
import S20Challange.twitterClone.dto.TweetResponse;
import S20Challange.twitterClone.dto.UserResponse;
import S20Challange.twitterClone.entity.Reply;
import S20Challange.twitterClone.entity.Tweet;
import S20Challange.twitterClone.entity.User;
import S20Challange.twitterClone.service.TweetService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ControllersOrganiser {
    private TweetService tweetService;
    private TweetResponse tweetResponse;
    private ReplyResponse replyResponse;
    private UserResponse userResponse;

    public ControllersOrganiser(TweetService tweetService, TweetResponse tweetResponse, ReplyResponse replyResponse, UserResponse userResponse) {
        this.tweetService = tweetService;
        this.tweetResponse = tweetResponse;
        this.replyResponse = replyResponse;
        this.userResponse = userResponse;
    }

    public void organiseTweet(TweetResponse tweetResponse, Tweet tweet){
        tweetResponse.setId(tweet.getId());
        tweetResponse.setContent(tweet.getContent());
        tweetResponse.setLikes(tweet.getLikes());
        tweetResponse.setRetweets(tweet.getRetweets());
    }

    public void organiseReply(ReplyResponse replyResponse, Reply reply){
        replyResponse.setId(reply.getId());
        replyResponse.setContent(reply.getContent());
        replyResponse.setUsername(reply.getUser().getUsername());
        replyResponse.setLikes(reply.getLikes());
        replyResponse.setRetweets(reply.getRetweets());
    }

    public void organiseUser(UserResponse userResponse, Tweet tweet){
        User user = tweet.getUser();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
    }

    public void organiseOnlyUser(UserResponse userResponse, User user){
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
    }

    public void organiseLikeAndRetweet(TweetResponse tweetResponse, Tweet tweet){
        tweetResponse.setId(tweet.getId());
        tweetResponse.setUsername(tweet.getUser().getUsername());
        tweetResponse.setContent(tweet.getContent());
        tweetResponse.setLikes(tweet.getLikes());
        tweetResponse.setRetweets(tweet.getRetweets());
    }
}