package S20Challange.twitterClone.dto;

import S20Challange.twitterClone.entity.Reply;
import S20Challange.twitterClone.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyResponse {
    private int id;
    private String username;
    private String content;
    private int likes;
    private int retweets;
}
