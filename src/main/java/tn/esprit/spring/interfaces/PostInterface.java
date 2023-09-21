package tn.esprit.spring.interfaces;

import org.springframework.web.multipart.MultipartFile;
import tn.esprit.spring.entities.Post;

import java.io.IOException;
import java.util.List;

public interface PostInterface {
     Post addPost (Post p);
     Post updatePost (Post p);void deletePost (int idp);
     List<Post> retrieveAllPost();
     Post retrievePost(int idp);

     Post uploadImage(MultipartFile file ,int id) throws IOException;

     Post rate (int id ,int note);
}
