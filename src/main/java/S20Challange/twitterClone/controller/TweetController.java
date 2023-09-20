package S20Challange.twitterClone.controller;

import S20Challange.twitterClone.dao.UserRepository;
import S20Challange.twitterClone.dto.ReplyResponse;
import S20Challange.twitterClone.dto.TweetResponse;
import S20Challange.twitterClone.dto.UserResponse;
import S20Challange.twitterClone.entity.Tweet;
import S20Challange.twitterClone.entity.User;
import S20Challange.twitterClone.exceptions.GlobalExceptionHandler;
import S20Challange.twitterClone.exceptions.MessageException;
import S20Challange.twitterClone.service.TweetService;

import S20Challange.twitterClone.service.UserService;
import S20Challange.twitterClone.util.ControllersOrganiser;
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
    private ControllersOrganiser controllersOrganiser;
    private UserRepository userRepository;//Aslında burada olmaması lazım ama çağrılan method için mecbur kalındı

    @Autowired
    public TweetController(TweetService tweetService, ControllersOrganiser controllersOrganiser, UserRepository userRepository) {
        this.tweetService = tweetService;
        this.controllersOrganiser = controllersOrganiser;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public List<TweetResponse> getAllTweets() {
        List<Tweet> tweets = tweetService.findAll();
        List<TweetResponse> tweetResponses = new ArrayList<>();

        for (Tweet tweet : tweets) {
            TweetResponse tweetResponse = new TweetResponse();
            //original setters
            controllersOrganiser.organiseTweet(tweetResponse, tweet);

            List<ReplyResponse> replyResponses = tweet.getReplyList()
                    .stream()
                    .map(reply -> {
                        ReplyResponse replyResponse = new ReplyResponse();
                        //original setters
                        controllersOrganiser.organiseReply(replyResponse, reply);
                        return replyResponse;
                    })
                    .collect(Collectors.toList());

            tweetResponse.setReplyList(replyResponses);

            UserResponse userResponse = new UserResponse();
            //original setters
            controllersOrganiser.organiseUser(userResponse, tweet);
            tweetResponse.setUsername(userResponse.getUsername());
            tweetResponses.add(tweetResponse);
        }
        return tweetResponses;
    }

    @GetMapping("/{id}")
    public TweetResponse getTweetById(@Positive @PathVariable int id) {
        Tweet tweet = tweetService.find(id);

        if (tweet == null) {
            throw new MessageException("Tweet with given id " + id + " does not exist", HttpStatus.NOT_FOUND);
        }

        TweetResponse tweetResponse = new TweetResponse();
        //original setters
        controllersOrganiser.organiseTweet(tweetResponse, tweet);

        List<ReplyResponse> replyResponses = tweet.getReplyList()
                .stream()
                .map(reply -> {
                    ReplyResponse replyResponse = new ReplyResponse();
                    //original setters
                    controllersOrganiser.organiseReply(replyResponse, reply);
                    return replyResponse;
                })
                .collect(Collectors.toList());

        tweetResponse.setReplyList(replyResponses);

        UserResponse userResponse = new UserResponse();
        //original setters
        controllersOrganiser.organiseUser(userResponse, tweet);
        tweetResponse.setUsername(userResponse.getUsername());

        return tweetResponse;
        //return new MessageException("Your request done succesfully.",HttpStatus.OK);
    }

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

                    if (tweet.getContent() == null || tweet.getContent().trim().isEmpty()) {
                        throw new MessageException("Tweet must have non-empty content", HttpStatus.BAD_REQUEST);
                    }

                    tweet.setUser(authenticatedUser);

                    Tweet savedTweet = tweetService.save(tweet);

                    TweetResponse tweetResponse = new TweetResponse();
                    //original setters
                    controllersOrganiser.organiseTweet(tweetResponse, savedTweet);

                    UserResponse userResponse = new UserResponse();
                    //original setters
                    controllersOrganiser.organiseUser(userResponse, tweet);
                    tweetResponse.setUsername(userResponse.getUsername());

                    System.out.println("Your tweet has been posted succesfully" + " " + HttpStatus.CREATED);
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

    @PutMapping("/{id}")
    public TweetResponse updateTweet(@RequestBody Tweet updatedTweet, @Positive @PathVariable int id) {
        TweetResponse tweetToUpdateResponse = getTweetById(id);

        if (tweetToUpdateResponse == null) {
            throw new MessageException("Tweet with given id " + id + " does not exist", HttpStatus.NOT_FOUND);
        } else if (tweetToUpdateResponse != null) {
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

    @DeleteMapping("/{id}")
    public TweetResponse deleteTweet(@Positive @PathVariable int id) {
        Tweet tweetToDelete = tweetService.find(id);

        if (tweetToDelete == null) {
            throw new MessageException("Tweet with given id " + id + " does not exist", HttpStatus.NOT_FOUND);
        } else if (tweetToDelete != null) {
            TweetResponse tweetResponse = new TweetResponse();
            //original setters
            controllersOrganiser.organiseTweet(tweetResponse, tweetToDelete);

            List<ReplyResponse> replyResponses = tweetToDelete.getReplyList()
                    .stream()
                    .map(reply -> {
                        ReplyResponse replyResponse = new ReplyResponse();
                        //original setters
                        controllersOrganiser.organiseReply(replyResponse, reply);
                        return replyResponse;
                    })
                    .collect(Collectors.toList());

            tweetResponse.setReplyList(replyResponses);

            UserResponse userResponse = new UserResponse();
            User user = tweetToDelete.getUser();

            if (user != null) {
                //original setters
                controllersOrganiser.organiseOnlyUser(userResponse, user);
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
    public TweetResponse likeTweet(@Positive @PathVariable int id) {
        Tweet likedTweet = tweetService.likeTweet(id);

        if (likedTweet != null) {
            TweetResponse tweetResponse = new TweetResponse();
            //original setters
            controllersOrganiser.organiseLikeAndRetweet(tweetResponse, likedTweet);

            List<ReplyResponse> replyResponses = likedTweet.getReplyList()
                    .stream()
                    .map(reply -> {
                        ReplyResponse replyResponse = new ReplyResponse();
                        //original setters
                        controllersOrganiser.organiseReply(replyResponse, reply);
                        return replyResponse;
                    })
                    .collect(Collectors.toList());

            tweetResponse.setReplyList(replyResponses);

            return tweetResponse;
        } else {
            throw new MessageException("Tweet with given id " + id + " does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/like/{id}")
    public TweetResponse unlikeTweet(@Positive @PathVariable int id) {
        Tweet unlikedTweet = tweetService.unlikeTweet(id);
        if (unlikedTweet != null) {
            // Create and return a response with updated likes count
            TweetResponse tweetResponse = new TweetResponse();
            //original setters
            controllersOrganiser.organiseLikeAndRetweet(tweetResponse, unlikedTweet);

            List<ReplyResponse> replyResponses = unlikedTweet.getReplyList()
                    .stream()
                    .map(reply -> {
                        ReplyResponse replyResponse = new ReplyResponse();
                        //original setters
                        controllersOrganiser.organiseReply(replyResponse, reply);
                        return replyResponse;
                    })
                    .collect(Collectors.toList());

            tweetResponse.setReplyList(replyResponses);
            return tweetResponse;
        } else {
            throw new MessageException("Tweet with given id " + id + " does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/retweet/{id}")
    public TweetResponse retweet(@Positive @PathVariable int id) {
        Tweet retweetedTweet = tweetService.retweet(id);
        if (retweetedTweet != null) {
            TweetResponse tweetResponse = new TweetResponse();
            //original setters
            controllersOrganiser.organiseLikeAndRetweet(tweetResponse, retweetedTweet);

            List<ReplyResponse> replyResponses = retweetedTweet.getReplyList()
                    .stream()
                    .map(reply -> {
                        ReplyResponse replyResponse = new ReplyResponse();
                        //original setters
                        controllersOrganiser.organiseReply(replyResponse, reply);
                        return replyResponse;
                    })
                    .collect(Collectors.toList());

            tweetResponse.setReplyList(replyResponses);

            return tweetResponse;
        } else {
            throw new MessageException("Tweet with given id " + id + " does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/retweet/{id}")
    public TweetResponse undoRetweet(@Positive @PathVariable int id) {
        Tweet undoRetweetedTweet = tweetService.undoRetweet(id);
        if (undoRetweetedTweet != null) {
            TweetResponse tweetResponse = new TweetResponse();
            //original setters
            controllersOrganiser.organiseLikeAndRetweet(tweetResponse, undoRetweetedTweet);

            List<ReplyResponse> replyResponses = undoRetweetedTweet.getReplyList()
                    .stream()
                    .map(reply -> {
                        ReplyResponse replyResponse = new ReplyResponse();
                        //original setters
                        controllersOrganiser.organiseReply(replyResponse, reply);
                        return replyResponse;
                    })
                    .collect(Collectors.toList());

            tweetResponse.setReplyList(replyResponses);

            return tweetResponse;
        } else {
            throw new MessageException("Tweet with given id " + id + " does not exist", HttpStatus.NOT_FOUND);
        }
    }
}