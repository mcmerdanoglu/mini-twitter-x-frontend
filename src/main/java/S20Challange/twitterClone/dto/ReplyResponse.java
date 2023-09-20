package S20Challange.twitterClone.dto;

import S20Challange.twitterClone.entity.Reply;
import S20Challange.twitterClone.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ReplyResponse {
    private int id;
    private String username;
    private String content;
    private int likes;
    private int retweets;
}
