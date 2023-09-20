package S20Challange.twitterClone.dto;

import S20Challange.twitterClone.entity.Reply;
import S20Challange.twitterClone.entity.User;
import S20Challange.twitterClone.exceptions.MessageException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class TweetResponse {
    private int id;
    private String username;
    private String content;
    private int likes;
    private int retweets;
    private List<ReplyResponse> replyList;
}
