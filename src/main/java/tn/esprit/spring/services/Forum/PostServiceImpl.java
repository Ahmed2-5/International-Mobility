package tn.esprit.spring.services.Forum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.spring.entities.Post;
import tn.esprit.spring.interfaces.PostInterface;
import tn.esprit.spring.repositories.PostRepository;
import tn.esprit.spring.repositories.UserRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class PostServiceImpl implements PostInterface {

     @Autowired
    PostRepository postRepo;
     @Autowired
    UserRepository userRepository;


    @Override
    public Post addPost(Post p) {

        p.setRate(0f);
        p.setNbrRate(0);
        return postRepo.save(p);
    }

    @Override
    public Post updatePost(Post p) {
        return postRepo.save(p);
    }

    @Override
    public void deletePost(int idp) {
        postRepo.deleteById(idp);
    }

    @Override
    public List<Post> retrieveAllPost() {
        List<Post> postList = postRepo.findAll();
        return postList;

    }

    @Override
    public Post retrievePost(int idp) {
        return postRepo.findById(idp).get();
    }

    @Override
    public Post uploadImage(MultipartFile file, int id) throws IOException {
        Post p = postRepo.findById(id).orElse(null);
        p.setImageUrl(file.getBytes());
        return postRepo.save(p);
    }

    @Override
    public Post rate(int id, int note) {

        Post p =  postRepo.findById(id).orElse(null);
        p.setRate((p.getRate()*p.getNbrRate()+note)/ (p.getRate()+1));
        p.setNbrRate(p.getNbrRate()+1);
        return postRepo.save(p);
    }


    public Map<String,String> StaticticByAdress(){

        Map<String, String> map = new HashMap<>();
        float count =0;
        float nbruser =  userRepository.findAll().size();

        // StringBuilder adresse = new StringBuilder();

        for (String s :
                userRepository.adresseUser()) {
            for (Post p:
                 postRepo.findAll()) {

                if(Objects.equals(s,p.getUser().getAdresse())){
                    count = count+1 ;
                }
            }
            System.out.println("ccc"+count);
            float resultat =count/nbruser ;
            map.put(s, resultat*100+"%");
            count =0;
        }

/*
        for (Post p :
                retrieveAllPost()) {
            for (String s :
                    userRepository.adresseUser()) {
                if (p.getUser().getAdresse().equals(s)){
                    count=count+1;
                    System.out.println("count"+count);
                    adresse.append(s);
                }
            }
            map.put(adresse.toString(), (count / userRepository.findAll().size())*100 + "%");
           // adresse.setLength(0);
            System.out.println("mappp"+map);
        }
            */
  return map;


    }


}
