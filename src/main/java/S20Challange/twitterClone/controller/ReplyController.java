package S20Challange.twitterClone.controller;

import S20Challange.twitterClone.dao.TweetRepository;
import S20Challange.twitterClone.dao.UserRepository;
import S20Challange.twitterClone.dto.ReplyResponse;
import S20Challange.twitterClone.dto.TweetResponse;
import S20Challange.twitterClone.dto.UserResponse;
import S20Challange.twitterClone.entity.Reply;
import S20Challange.twitterClone.entity.Tweet;
import S20Challange.twitterClone.entity.User;
import S20Challange.twitterClone.exceptions.MessageException;
import S20Challange.twitterClone.service.ReplyService;
import S20Challange.twitterClone.service.TweetService;
import S20Challange.twitterClone.util.ControllersOrganiser;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tweet/reply")
@Validated
public class ReplyController {

    private ReplyService replyService;
    private TweetService tweetService;
    private UserRepository userRepository;
    private ControllersOrganiser controllersOrganiser;

    @Autowired
    public ReplyController(ReplyService replyService, TweetService tweetService,
                           UserRepository userRepository, ControllersOrganiser controllersOrganiser) {
        this.replyService = replyService;
        this.tweetService = tweetService;
        this.userRepository = userRepository;
        this.controllersOrganiser = controllersOrganiser;
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

                    if (reply.getContent() == null || reply.getContent().trim().isEmpty()) {
                        throw new MessageException("Reply must have non-empty content", HttpStatus.BAD_REQUEST);
                    }

                    reply.setUser(authenticatedUser);

                    Tweet tweet = tweetService.find(id);

                    if (tweet != null) {
                        reply.setUser(authenticatedUser);
                        reply.setTweet(tweet);

                        Reply savedReply = replyService.save(reply);

                        ReplyResponse replyResponse = new ReplyResponse();
                        //original setters
                        controllersOrganiser.organiseReply(replyResponse, savedReply);

                        UserResponse userResponse = new UserResponse();
                        //original setters
                        controllersOrganiser.organiseOnlyUser(userResponse,authenticatedUser);

                        replyResponse.setUsername(authenticatedUser.getUsername());
                        return replyResponse;
                    } else {
                        throw new MessageException("Tweet with given id " + id + " does not exist", HttpStatus.NOT_FOUND);
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

    @DeleteMapping("/{id}")
    public ReplyResponse deleteReply(@Positive @PathVariable int id) {
        Reply replyToDelete = replyService.find(id);

        if (replyToDelete != null) {
            ReplyResponse replyResponse = new ReplyResponse();
            //original setters
            controllersOrganiser.organiseReply(replyResponse, replyToDelete);

            UserResponse userResponse = new UserResponse();
            User user = replyToDelete.getUser();
            if (user != null) {
                //original setters
                controllersOrganiser.organiseOnlyUser(userResponse,user);
            }

            replyResponse.setUsername(user.getUsername());

            replyService.softDelete(id);
            replyService.delete(replyToDelete);

            return replyResponse;
        }
        throw new MessageException("Tweet with given id " + id + " does not exist", HttpStatus.NOT_FOUND);
    }

    /*---------------------------LIKE & UNLIKE and RETWEET & UNDORETWEET METHODS-------------------------------------------*/

    @PostMapping("/like/{id}")
    public ReplyResponse likeReply(@Positive @PathVariable int id) {
        Reply likedReply = replyService.likeReply(id);
        if (likedReply != null) {
            ReplyResponse replyResponse = new ReplyResponse();
            //original setters
            controllersOrganiser.organiseReply(replyResponse, likedReply);
            return replyResponse;
        } else {
            throw new MessageException("Tweet with given id " + id + " does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/like/{id}")
    public ReplyResponse unlikeReply(@Positive @PathVariable int id) {
        Reply unlikedReply = replyService.unlikeReply(id);
        if (unlikedReply != null) {
            ReplyResponse replyResponse = new ReplyResponse();
            //original setters
            controllersOrganiser.organiseReply(replyResponse, unlikedReply);
            return replyResponse;
        } else {
            throw new MessageException("Tweet with given id " + id + " does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/retweet/{id}")
    public ReplyResponse retweetReply(@Positive @PathVariable int id) {
        Reply retweetedReply = replyService.retweetReply(id);
        if (retweetedReply != null) {
            ReplyResponse replyResponse = new ReplyResponse();
            //original setters
            controllersOrganiser.organiseReply(replyResponse, retweetedReply);
            return replyResponse;
        } else {
            throw new MessageException("Tweet with given id " + id + " does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/retweet/{id}")
    public ReplyResponse undoRetweetReply(@Positive @PathVariable int id) {
        Reply undoRetweetedReply = replyService.undoRetweetReply(id);
        if (undoRetweetedReply != null) {
            ReplyResponse replyResponse = new ReplyResponse();
            //original setters
            controllersOrganiser.organiseReply(replyResponse, undoRetweetedReply);
            return replyResponse;
        } else {
            throw new MessageException("Tweet with given id " + id + " does not exist", HttpStatus.NOT_FOUND);
        }
    }
}
