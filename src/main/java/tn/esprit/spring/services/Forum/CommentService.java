package tn.esprit.spring.services.Forum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Comment;
import tn.esprit.spring.entities.User;
import tn.esprit.spring.interfaces.CommentInterface;
import tn.esprit.spring.repositories.CommentRepository;
import tn.esprit.spring.repositories.PostRepository;
import tn.esprit.spring.repositories.UserRepository;
import tn.esprit.spring.services.User.EmailService;

import java.util.*;

@Service
@Slf4j
public class CommentService implements CommentInterface {

    @Autowired
    CommentRepository commentRepo;
    @Autowired
    PostRepository postRepo;
    @Autowired
    EmailService emailService;
    @Autowired
    UserRepository userRepository;


    @Override
    public Comment addComment(Comment c)
    {
        c.setContent(ConvertirListString(c.getContent()));
        return commentRepo.save(c);
    }


    public Comment addReplay(Comment c,int id)
    {
        Comment replay = commentRepo.findById(id).orElseThrow(()->new NoSuchElementException("Comment not found with UIDPER:" + id));
        c.setIdPer(replay);
        c.setContent(ConvertirListString(c.getContent()));
        return commentRepo.save(c);
    }

    @Override
    public Comment updateComment(Comment c)
    {
        return commentRepo.save(c);
    }

    @Override
    public void deleteComment(int idc) {
        commentRepo.deleteById(idc);
    }

    @Override
    public List<Comment> retrieveAllComment() {
        return commentRepo.findAll();
    }

    @Override
    public Comment retrieveComment(int idc) {

        return commentRepo.findById(idc).get();
    }



   // @Scheduled(fixedRate = 1000)
    public void findCommentBybadWords(){

        int count = 0;
        for (User u :
                userRepository.findAll()) {
            for (Comment r :
                    retrieveAllComment()) {
                if(Objects.equals(u.getUserName(), r.getUser().getUserName())){

                    if (r.getContent().contains("****")){
                        count++;
                    }
                }
            }
            if (count >= 3){
                u.setScore(0);
                //Email
                String to = u.getEmail();
                String subject = "User Banned";
                String text = "Your are banned because you used too many bad words";
                emailService.sendEmail(to, subject, text);
                count=0;

                userRepository.save(u);
            }
        }
    }





    public String ConvertirListString(String s){

        String resultat = "";
        List<String> lstring = new ArrayList<>();
        List<String> badword = new ArrayList<>(Arrays.asList("pute", "nick", "fuck","shit","duck","dick","pussy","ass"));
        String[] strArr = s.split("\\s+");//Splitting using whitespace
        Collections.addAll(lstring, strArr);

        for (String str : lstring ){
            for (String b :
                    badword) {
                System.out.println("boolean"+str.equals(b));
                if (str.equals(b)) {
                    System.out.println("iftru"+str);
                    str = "*****";
                    System.out.println("res"+str);
                }
            }
            resultat=resultat+" "+str ;
        }
        return resultat;
    }
}
