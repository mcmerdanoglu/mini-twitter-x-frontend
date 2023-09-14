package S20Challange.twitterClone.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="reply", schema = "twitterclone2")
public class Reply {
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

    @ManyToOne (cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.PERSIST,CascadeType.REFRESH})
    //Çok olan reply tek olan tweet olduğu için reply classına ManyToOne yazdık!!!
    @JoinColumn(name="tweet_id") //foreign key
    private Tweet tweet;

    @ManyToOne (cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.PERSIST,CascadeType.REFRESH})
    //Çok olan reply tek olan user olduğu için reply classına ManyToOne yazdık!!!
    @JoinColumn(name="user_id") //foreign key
    private User user;
}
