package S20Challange.twitterClone.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user", schema = "twitterclone2")
public class UserFirst {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    //@JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL/*, fetch = FetchType.EAGER*/)
    //Tweet classında user olarak işaretlendin manasında.(user_id in tweet foreign key)
    private List<Tweet> tweetList;

    //@JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL/*, fetch = FetchType.EAGER*/)
    //Reply classında user olarak işaretlendin manasında.(user_id in reply foreign key)
    private List<Reply> replyList;

/*
    //  AccountControllerdaki addAccountByCustomerId methodu için ekstra yazılan add methodu!
    public void add(Account account){
        if(accountList == null){
            accountList = new ArrayList<>();
        }
        accountList.add(account);
    }*/
}
