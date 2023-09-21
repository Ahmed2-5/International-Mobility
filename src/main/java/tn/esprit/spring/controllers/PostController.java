package tn.esprit.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.spring.entities.Post;
import tn.esprit.spring.services.Forum.PostServiceImpl;


import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Post")
public class PostController {


    @Autowired
    private PostServiceImpl postService ;


    @PostMapping("/add-post")
    public ResponseEntity<?> addPost(@RequestBody Post p ){


        return new ResponseEntity<Post>( postService.addPost(p), HttpStatus.CREATED);
    }


    @GetMapping("/affiher-post")
    public ResponseEntity<?> AfficherPost(){
        return new ResponseEntity<List<Post>>( postService.retrieveAllPost(), HttpStatus.FOUND);
    }

    @PostMapping("/file/{id}")
    @ResponseBody
    @Transactional
    public Object  uploadImg (@RequestParam("image") @Nullable MultipartFile image , @PathVariable("id") int id) throws IOException {
        assert image != null;
        return new ResponseEntity<Post>( postService.uploadImage(image,id), HttpStatus.FOUND);

    }

    @PostMapping("/rate/{id}/{note}")
    @ResponseBody
    public ResponseEntity<?>  rate (@PathVariable("note") int note , @PathVariable("id") int id) throws IOException {

        return new ResponseEntity<Post>( postService.rate(id,note), HttpStatus.FOUND);

    }

    @GetMapping("/stat")
    public Map<String,String> statByAdresse() {
        return postService.StaticticByAdress();
    }



}


