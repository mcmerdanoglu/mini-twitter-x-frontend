package S20Challange.twitterClone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name="tweet", schema = "twitterclone2")
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="content")
    private String content;

    @Column(name="likes")
    private int likes;

    @Column(name="retweets")
    private int retweets;

    @JsonIgnore
    @OneToMany(mappedBy = "tweet", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    //Reply classında tweet olarak işaretlendin manasında.(tweet_id in reply foreign key)
    private List<Reply> replyList;

    @JsonIgnore
    @ManyToOne (cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.PERSIST,CascadeType.REFRESH}, fetch = FetchType.EAGER)
    //Çok olan tweet tek olan user olduğu için tweet classına ManyToOne yazdık!!!
    @JoinColumn(name="user_id") //foreign key
    private User user;
}
