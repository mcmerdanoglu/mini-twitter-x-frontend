package S20Challange.twitterClone.service;

import S20Challange.twitterClone.dao.ReplyRepository;
import S20Challange.twitterClone.entity.Reply;
import S20Challange.twitterClone.entity.Tweet;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReplyServiceImpl implements ReplyService{

    private ReplyRepository replyRepository;

    @Autowired
    public ReplyServiceImpl(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    @Override
    public List<Reply> findAll() {
        return replyRepository.findAll();
    }

    @Override
    public Reply find(int id) {
        Optional<Reply> reply = replyRepository.findById(id);
        if(reply.isPresent()){
            return reply.get();
        }
        //TODO throw exception
        return null;
    }

    @Override
    public Reply save(Reply reply) {
        return replyRepository.save(reply);
    }

    @Override
    @Transactional
    public void delete(Reply reply) {
        replyRepository.delete(reply);
    }

    @Override
    public Reply likeReply(int id) {
        Reply reply = replyRepository.findById(id).orElse(null);
        if (reply != null) {
            reply.incrementLikes();
            replyRepository.save(reply);
        }
        return reply;
    }

    @Override
    public Reply unlikeReply(int id) {
        Reply reply = replyRepository.findById(id).orElse(null);
        if (reply != null) {
            reply.decrementLikes();
            replyRepository.save(reply);
        }
        return reply;
    }

    @Override
    public Reply retweetReply(int id) {
        Reply reply = replyRepository.findById(id).orElse(null);
        if (reply != null) {
            reply.incrementRetweets();
            replyRepository.save(reply);
        }
        return reply;
    }

    @Override
    public Reply undoRetweetReply(int id) {
        Reply reply = replyRepository.findById(id).orElse(null);
        if (reply != null) {
            reply.decrementRetweets();
            replyRepository.save(reply);
        }
        return reply;
    }
}
