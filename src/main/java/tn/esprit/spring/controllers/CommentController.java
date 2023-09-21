package tn.esprit.spring.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Comment;

import tn.esprit.spring.services.Forum.CommentService;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService ;


    @PostMapping("/add-comment")
    public ResponseEntity<?> addPost(@RequestBody Comment c ){
        return new ResponseEntity<Comment>( commentService.addComment(c), HttpStatus.CREATED);
    }


    @GetMapping("/retreive-comment")
    public ResponseEntity<?> AfficheComment(){
        return new ResponseEntity<List<Comment>>( commentService.retrieveAllComment(), HttpStatus.CREATED);
    }

    @PostMapping("/replyToComment/{id}")
    public Comment addReplay(@RequestBody Comment c,@PathVariable int id)
    {
        return commentService.addReplay(c,id);
    }
}
