package S20Challange.twitterClone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    public void incrementLikes() {
        this.likes++;
    }

    public void decrementLikes() {
        if (this.likes > 0) {
            this.likes--;
        }
    }

    public void incrementRetweets() {
        this.retweets++;
    }

    public void decrementRetweets() {
        if (this.retweets > 0) {
            this.retweets--;
        }
    }

    @JsonIgnore
    @ManyToOne (cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.PERSIST,CascadeType.REFRESH}, fetch = FetchType.EAGER)
    //Çok olan reply tek olan tweet olduğu için reply classına ManyToOne yazdık!!!
    @JoinColumn(name="tweet_id") //foreign key
    private Tweet tweet;

    @JsonIgnore
    @ManyToOne (cascade = {CascadeType.DETACH,CascadeType.MERGE, CascadeType.PERSIST,CascadeType.REFRESH}, fetch = FetchType.EAGER)
    //Çok olan reply tek olan user olduğu için reply classına ManyToOne yazdık!!!
    @JoinColumn(name="user_id") //foreign key
    private User user;
}
