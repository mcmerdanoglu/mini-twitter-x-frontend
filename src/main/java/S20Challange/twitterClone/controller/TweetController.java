package S20Challange.twitterClone.controller;

import S20Challange.twitterClone.dao.UserRepository;
import S20Challange.twitterClone.dto.ReplyResponse;
import S20Challange.twitterClone.dto.TweetResponse;
import S20Challange.twitterClone.dto.UserResponse;
import S20Challange.twitterClone.entity.Tweet;
import S20Challange.twitterClone.entity.User;
import S20Challange.twitterClone.service.TweetService;

import S20Challange.twitterClone.service.UserService;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tweet")
@Validated
public class TweetController {

    private TweetService tweetService;
    private UserRepository userRepository;//Aslında burada olmaması lazım ama çağrılan method için mecbur kalındı

    @Autowired
    public TweetController(TweetService tweetService, UserRepository userRepository) {
        this.tweetService = tweetService;
        this.userRepository = userRepository;
    }

    /*
        @GetMapping("/")
        public List<Tweet> getAllTweets() {
            return tweetService.findAll();
        }
    */

    @GetMapping("/")
    public List<TweetResponse> getAllTweets() {
        List<Tweet> tweets = tweetService.findAll();
        List<TweetResponse> tweetResponses = new ArrayList<>();

        for (Tweet tweet : tweets) {
            TweetResponse tweetResponse = new TweetResponse();
            tweetResponse.setId(tweet.getId());
            tweetResponse.setContent(tweet.getContent());
            tweetResponse.setLikes(tweet.getLikes());
            tweetResponse.setRetweets(tweet.getRetweets());
            //tweetResponse.setReplyList(tweet.getReplyList());

            List<ReplyResponse> replyResponses = tweet.getReplyList()
                    .stream()
                    .map(reply -> {
                        ReplyResponse replyResponse = new ReplyResponse();
                        replyResponse.setId(reply.getId());
                        replyResponse.setContent(reply.getContent());
                        replyResponse.setUsername(reply.getUser().getUsername());
                        replyResponse.setLikes(reply.getLikes());
                        replyResponse.setRetweets(reply.getRetweets());
                        return replyResponse;
                    })
                    .collect(Collectors.toList());

            tweetResponse.setReplyList(replyResponses);

            UserResponse userResponse = new UserResponse();
            User user = tweet.getUser();
            userResponse.setId(user.getId());
            userResponse.setUsername(user.getUsername());
            userResponse.setEmail(user.getEmail());

            tweetResponse.setUsername(userResponse.getUsername());

            tweetResponses.add(tweetResponse);
        }
        return tweetResponses;
    }

    /*
        @GetMapping("/{id}")
        public Tweet getTweetById(@Positive @PathVariable int id) {
            return tweetService.find(id);
        }
    */
    @GetMapping("/{id}")
    public TweetResponse getTweetById(@Positive @PathVariable int id) {
        Tweet tweet = tweetService.find(id);
        TweetResponse tweetResponse = new TweetResponse();
        tweetResponse.setId(tweet.getId());
        tweetResponse.setContent(tweet.getContent());
        tweetResponse.setLikes(tweet.getLikes());
        tweetResponse.setRetweets(tweet.getRetweets());
        //tweetResponse.setReplyList(tweet.getReplyList());

        List<ReplyResponse> replyResponses = tweet.getReplyList()
                .stream()
                .map(reply -> {
                    ReplyResponse replyResponse = new ReplyResponse();
                    replyResponse.setId(reply.getId());
                    replyResponse.setContent(reply.getContent());
                    replyResponse.setUsername(reply.getUser().getUsername());
                    replyResponse.setLikes(reply.getLikes());
                    replyResponse.setRetweets(reply.getRetweets());
                    return replyResponse;
                })
                .collect(Collectors.toList());

        tweetResponse.setReplyList(replyResponses);

        UserResponse userResponse = new UserResponse();
        User user = tweet.getUser();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());

        tweetResponse.setUsername(userResponse.getUsername());

        return tweetResponse;
    }

    /*
        @PostMapping("/")
        public Tweet addTweet(@RequestBody Tweet tweet){
            return tweetService.save(tweet);
        }
    */
    @PostMapping("/")
    public TweetResponse addTweet(@RequestBody Tweet tweet) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            String subClaim = jwt.getClaim("sub").toString();

            try {
                Optional<User> userOptional = userRepository.findUserByUsername(subClaim);

                if (userOptional.isPresent()) {
                    User authenticatedUser = userOptional.get();
                    tweet.setUser(authenticatedUser);

                    Tweet savedTweet = tweetService.save(tweet);

                    TweetResponse tweetResponse = new TweetResponse();
                    tweetResponse.setId(savedTweet.getId());
                    tweetResponse.setContent(savedTweet.getContent());
                    tweetResponse.setLikes(savedTweet.getLikes());
                    tweetResponse.setRetweets(savedTweet.getRetweets());


                    UserResponse userResponse = new UserResponse();
                    userResponse.setId(authenticatedUser.getId());
                    userResponse.setUsername(authenticatedUser.getUsername());
                    userResponse.setEmail(authenticatedUser.getEmail());

                    tweetResponse.setUsername(userResponse.getUsername());

                    return tweetResponse;
                } else {
                    // Handle the case where the user is not found
                    // You can return an error response or take other appropriate actions
                    return null;
                }
            } catch (NumberFormatException e) {
                // Handle the case where "sub" claim is not a valid integer
                // You can return an error response or take other appropriate actions
                return null;
            }
        } else {
            return null;
        }
    }
/*
    @PutMapping("/{id}")
    public Tweet updateTweet(@RequestBody Tweet tweet, @PathVariable int id) {
        Tweet tweetToUpdate = getTweetById(id);
        if (tweetToUpdate != null) {
            tweet.setId(id);
            return tweetService.save(tweet);
        }
        return null;
    }
*/

    @PutMapping("/{id}")
    public TweetResponse updateTweet(@RequestBody Tweet updatedTweet,@Positive @PathVariable int id) {
        TweetResponse tweetToUpdateResponse = getTweetById(id);

        if (tweetToUpdateResponse != null) {
            Tweet tweetToUpdate = tweetService.find(id);

            if (tweetToUpdate != null) {

                tweetToUpdate.setContent(updatedTweet.getContent());
                tweetToUpdate.setLikes(updatedTweet.getLikes());
                tweetToUpdate.setRetweets(updatedTweet.getRetweets());

                tweetService.save(tweetToUpdate);

                tweetToUpdateResponse.setContent(tweetToUpdate.getContent());
                return tweetToUpdateResponse;
            }
        }
        return null;
    }
/*
    @DeleteMapping("/{id}")
    public Tweet deleteTweet(@Positive @PathVariable int id) {
        Tweet tweetToDelete = tweetService.find(id);
        tweetService.delete(tweetToDelete);
        return tweetToDelete;
    }
*/
/*
    @DeleteMapping("/{id}")
    public TweetResponse deleteTweet(@Positive @PathVariable int id) {
        Tweet tweetToDelete = tweetService.find(id);

        if (tweetToDelete != null) {

            tweetToDelete.setUser(null);

            TweetResponse tweetResponse = new TweetResponse();
            tweetResponse.setId(tweetToDelete.getId());
            tweetResponse.setContent(tweetToDelete.getContent());
            tweetResponse.setLikes(tweetToDelete.getLikes());
            tweetResponse.setRetweets(tweetToDelete.getRetweets());
            tweetResponse.setReplyList(tweetToDelete.getReplyList());

            UserResponse userResponse = new UserResponse();
            User user = tweetToDelete.getUser();

            if (user != null) {
                userResponse.setId(user.getId());
                userResponse.setUsername(user.getUsername());
                userResponse.setEmail(user.getEmail());
            }

            tweetResponse.setUserResponse(userResponse);

            tweetService.delete(tweetToDelete);

            return tweetResponse;
        }
        return null;
    }
*/
@DeleteMapping("/{id}")
public TweetResponse deleteTweet(@Positive @PathVariable int id) {
    Tweet tweetToDelete = tweetService.find(id);

    if (tweetToDelete != null) {
        // You can still create a response for the deleted Tweet before deleting it
        TweetResponse tweetResponse = new TweetResponse();
        tweetResponse.setId(tweetToDelete.getId());
        tweetResponse.setContent(tweetToDelete.getContent());
        tweetResponse.setLikes(tweetToDelete.getLikes());
        tweetResponse.setRetweets(tweetToDelete.getRetweets());
        //tweetResponse.setReplyList(tweetToDelete.getReplyList());

        List<ReplyResponse> replyResponses = tweetToDelete.getReplyList()
                .stream()
                .map(reply -> {
                    ReplyResponse replyResponse = new ReplyResponse();
                    replyResponse.setId(reply.getId());
                    replyResponse.setContent(reply.getContent());
                    replyResponse.setUsername(reply.getUser().getUsername());
                    return replyResponse;
                })
                .collect(Collectors.toList());

        tweetResponse.setReplyList(replyResponses);

        UserResponse userResponse = new UserResponse();
        User user = tweetToDelete.getUser();

        if (user != null) {
            userResponse.setId(user.getId());
            userResponse.setUsername(user.getUsername());
            userResponse.setEmail(user.getEmail());
        }

        tweetResponse.setUsername(userResponse.getUsername());

        tweetService.softDelete(id);
        tweetService.delete(tweetToDelete);

        return tweetResponse;
    }
    return null;
}

/*---------------------------LIKE & UNLIKE and RETWEET & UNDORETWEET METHODS-------------------------------------------*/

    @PostMapping("/like/{id}")
    public ResponseEntity<TweetResponse> likeTweet(@Positive @PathVariable int id) {
        Tweet likedTweet = tweetService.likeTweet(id);
        if (likedTweet != null) {
            // Create and return a response with updated likes count
            TweetResponse tweetResponse = new TweetResponse();
            tweetResponse.setId(likedTweet.getId());
            tweetResponse.setContent(likedTweet.getContent());
            tweetResponse.setLikes(likedTweet.getLikes());
            // ... set other fields ...
            return new ResponseEntity<>(tweetResponse, HttpStatus.OK);
        } else {
            // Handle the case where the tweet is not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/like/{id}")
    public ResponseEntity<TweetResponse> unlikeTweet(@Positive @PathVariable int id) {
        Tweet unlikedTweet = tweetService.unlikeTweet(id);
        if (unlikedTweet != null) {
            // Create and return a response with updated likes count
            TweetResponse tweetResponse = new TweetResponse();
            tweetResponse.setId(unlikedTweet.getId());
            tweetResponse.setContent(unlikedTweet.getContent());
            tweetResponse.setLikes(unlikedTweet.getLikes());
            // ... set other fields ...
            return new ResponseEntity<>(tweetResponse, HttpStatus.OK);
        } else {
            // Handle the case where the tweet is not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/retweet/{id}")
    public ResponseEntity<TweetResponse> retweet(@Positive @PathVariable int id) {
        Tweet retweetedTweet = tweetService.retweet(id);
        if (retweetedTweet != null) {
            // Create and return a response with updated likes count
            TweetResponse tweetResponse = new TweetResponse();
            tweetResponse.setId(retweetedTweet.getId());
            tweetResponse.setContent(retweetedTweet.getContent());
            tweetResponse.setLikes(retweetedTweet.getLikes());
            tweetResponse.setRetweets(retweetedTweet.getRetweets());
            // ... set other fields ...
            return new ResponseEntity<>(tweetResponse, HttpStatus.OK);
        } else {
            // Handle the case where the tweet is not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/retweet/{id}")
    public ResponseEntity<TweetResponse> undoRetweet(@Positive @PathVariable int id) {
        Tweet undoRetweetedTweet = tweetService.undoRetweet(id);
        if (undoRetweetedTweet != null) {
            // Create and return a response with updated likes count
            TweetResponse tweetResponse = new TweetResponse();
            tweetResponse.setId(undoRetweetedTweet.getId());
            tweetResponse.setContent(undoRetweetedTweet.getContent());
            tweetResponse.setLikes(undoRetweetedTweet.getLikes());
            tweetResponse.setRetweets(undoRetweetedTweet.getRetweets());
            // ... set other fields ...
            return new ResponseEntity<>(tweetResponse, HttpStatus.OK);
        } else {
            // Handle the case where the tweet is not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

