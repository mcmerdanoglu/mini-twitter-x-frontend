package S20Challange.twitterClone.dao;

import S20Challange.twitterClone.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {
}
