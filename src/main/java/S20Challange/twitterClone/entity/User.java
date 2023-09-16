package S20Challange.twitterClone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user", schema = "twitterclone2")
public class User implements UserDetails { //Önce @Data yorumlaştırılır öyle implement edilir yoksa hata verir. Implement sonrası @Datayı tekrar aç!
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;


    @Column(name = "username")
    private String username;


    @Getter
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    //Tweet classında user olarak işaretlendin manasında.(user_id in tweet foreign key)
    private List<Tweet> tweetList;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    //Reply classında user olarak işaretlendin manasında.(user_id in reply foreign key)
    private List<Reply> replyList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
       return username;
        //return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

/*
    //  AccountControllerdaki addAccountByCustomerId methodu için ekstra yazılan add methodu!
    public void add(Account account){
        if(accountList == null){
            accountList = new ArrayList<>();
        }
        accountList.add(account);
    }*/
}
