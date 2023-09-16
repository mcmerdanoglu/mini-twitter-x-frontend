package S20Challange.twitterClone.controller;

import S20Challange.twitterClone.dao.TweetRepository;
import S20Challange.twitterClone.dao.UserRepository;
import S20Challange.twitterClone.dto.ReplyResponse;
import S20Challange.twitterClone.dto.TweetResponse;
import S20Challange.twitterClone.dto.UserResponse;
import S20Challange.twitterClone.entity.Reply;
import S20Challange.twitterClone.entity.Tweet;
import S20Challange.twitterClone.entity.User;
import S20Challange.twitterClone.service.ReplyService;
import S20Challange.twitterClone.service.TweetService;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/tweet/reply")
@Validated
public class ReplyController {

    private ReplyService replyService;
    private TweetService tweetService;
    private UserRepository userRepository;
    private TweetRepository tweetRepository;


    @Autowired
    public ReplyController(ReplyService replyService, TweetService tweetService, UserRepository userRepository, TweetRepository tweetRepository) {
        this.replyService = replyService;
        this.tweetService = tweetService;
        this.userRepository = userRepository;
        this.tweetRepository = tweetRepository;
    }

    @PostMapping("/{id}")
    public ReplyResponse addReply(@RequestBody Reply reply, @Positive @PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            String subClaim = jwt.getClaim("sub").toString();

            try {
                Optional<User> userOptional = userRepository.findUserByUsername(subClaim);

                if (userOptional.isPresent()) {
                    User authenticatedUser = userOptional.get();
                    reply.setUser(authenticatedUser);

                    Tweet tweet = tweetService.find(id);

                    if (tweet != null) {
                        // Associate the reply with the tweet
                        reply.setUser(authenticatedUser);
                        reply.setTweet(tweet);

                    Reply savedReply = replyService.save(reply);

                    ReplyResponse replyResponse = new ReplyResponse();
                    replyResponse.setId(savedReply.getId());
                    replyResponse.setUsername(savedReply.getUser().getUsername());
                    replyResponse.setContent(savedReply.getContent());
                    replyResponse.setLikes(savedReply.getLikes());
                    replyResponse.setRetweets(savedReply.getRetweets());

                    UserResponse userResponse = new UserResponse();
                    userResponse.setId(authenticatedUser.getId());
                    userResponse.setUsername(authenticatedUser.getUsername());
                    userResponse.setEmail(authenticatedUser.getEmail());

                    replyResponse.setUsername(authenticatedUser.getUsername());

                    return replyResponse;
                } else {
                    return null;
                }
                } else {
                    return null;
                }
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            return null;
        }
    }



    /*---------------------------LIKE & UNLIKE and RETWEET & UNDORETWEET METHODS-------------------------------------------*/

    @PostMapping("/like/{id}")
    public ResponseEntity<ReplyResponse> likeReply(@Positive @PathVariable int id) {
        Reply likedReply = replyService.likeReply(id);
        if (likedReply != null) {
            // Create and return a response with updated likes count
            ReplyResponse replyResponse = new ReplyResponse();
            replyResponse.setId(likedReply.getId());
            replyResponse.setUsername(likedReply.getUser().getUsername());
            replyResponse.setContent(likedReply.getContent());
            replyResponse.setLikes(likedReply.getLikes());
            // ... set other fields ...
            return new ResponseEntity<>(replyResponse, HttpStatus.OK);
        } else {
            // Handle the case where the tweet is not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/like/{id}")
    public ResponseEntity<ReplyResponse> unlikeReply(@Positive @PathVariable int id) {
        Reply unlikedReply = replyService.unlikeReply(id);
        if (unlikedReply != null) {
            // Create and return a response with updated likes count
            ReplyResponse replyResponse = new ReplyResponse();
            replyResponse.setId(unlikedReply.getId());
            replyResponse.setUsername(unlikedReply.getUser().getUsername());
            replyResponse.setContent(unlikedReply.getContent());
            replyResponse.setLikes(unlikedReply.getLikes());
            // ... set other fields ...
            return new ResponseEntity<>(replyResponse, HttpStatus.OK);
        } else {
            // Handle the case where the tweet is not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/retweet/{id}")
    public ResponseEntity<ReplyResponse> retweetReply(@Positive @PathVariable int id) {
        Reply retweetedReply = replyService.retweetReply(id);
        if (retweetedReply != null) {
            // Create and return a response with updated likes count
            ReplyResponse replyResponse = new ReplyResponse();
            replyResponse.setId(retweetedReply.getId());
            replyResponse.setUsername(retweetedReply.getUser().getUsername());
            replyResponse.setContent(retweetedReply.getContent());
            replyResponse.setLikes(retweetedReply.getLikes());
            replyResponse.setRetweets(retweetedReply.getRetweets());
            // ... set other fields ...
            return new ResponseEntity<>(replyResponse, HttpStatus.OK);
        } else {
            // Handle the case where the tweet is not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/retweet/{id}")
    public ResponseEntity<ReplyResponse> undoRetweetReply(@Positive @PathVariable int id) {
        Reply undoRetweetedReply = replyService.undoRetweetReply(id);
        if (undoRetweetedReply != null) {
            // Create and return a response with updated likes count
            ReplyResponse replyResponse = new ReplyResponse();
            replyResponse.setId(undoRetweetedReply.getId());
            replyResponse.setUsername(undoRetweetedReply.getUser().getUsername());
            replyResponse.setContent(undoRetweetedReply.getContent());
            replyResponse.setLikes(undoRetweetedReply.getLikes());
            replyResponse.setRetweets(undoRetweetedReply.getRetweets());
            // ... set other fields ...
            return new ResponseEntity<>(replyResponse, HttpStatus.OK);
        } else {
            // Handle the case where the tweet is not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
