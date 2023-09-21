package tn.esprit.spring.services.Forum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Comment;
import tn.esprit.spring.entities.RatingForum;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.interfaces.RatingfORUMInterface;
import tn.esprit.spring.repositories.RatingForumRepository;
import tn.esprit.spring.repositories.UserRepository;

import java.util.NoSuchElementException;

@Service
@Slf4j
public class RatingForumService implements RatingfORUMInterface {
     @Autowired
     private CommentService commentService;
     @Autowired
     private RatingForumRepository ratingRepository;
     @Autowired
     private UserRepository userRepository ;


    @Override
    public Comment addLike(String iduser, int idComment) {

        Comment c = commentService.retrieveComment(idComment);
        User u = userRepository.findById(iduser).orElseThrow(() -> new NoSuchElementException("User not found with UIDPER:" + iduser));
        System.out.println("ssss"+  u.toString());
        if (ratingRepository.findByUserAndComment(u,c) == null){
            RatingForum r = new RatingForum();
            r.setComment(c);
            r.setUser(u);
            r.setType(1);
            ratingRepository.save(r);
            return c ;
        }
        else{

            ratingRepository.delete(ratingRepository.findByUserAndComment(u,c));
            RatingForum r = new RatingForum();
            r.setComment(c);
            r.setUser(u);
            r.setType(1);
            ratingRepository.save(r);
            return c ;
        }

    }

    @Override
    public Comment addDesLike(String iduser, int idComment) {
        Comment c = commentService.retrieveComment(idComment);
        User u = userRepository.findById(iduser).orElseThrow(() -> new NoSuchElementException("EnqSections not found with UIDPER:" + iduser));

        if (ratingRepository.findByUserAndComment(u,c) == null){
            RatingForum r = new RatingForum();
            r.setComment(c);
            r.setUser(u);
            r.setType(2);
            ratingRepository.save(r);
            return c ;
        }
        else{

            ratingRepository.delete(ratingRepository.findByUserAndComment(u,c));
            RatingForum r = new RatingForum();
            r.setComment(c);
            r.setUser(u);
            r.setType(2);
            ratingRepository.save(r);
            return c ;
        }

    }
}
