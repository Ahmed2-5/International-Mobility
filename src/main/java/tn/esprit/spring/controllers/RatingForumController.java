package tn.esprit.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.spring.entities.Comment;
import tn.esprit.spring.services.Forum.RatingForumService;


@RestController
@RequestMapping("/RatingForum")
public class RatingForumController {

    @Autowired
    RatingForumService ratingService;

    @PostMapping("/Like/{iduser}/{idComment}")
    public Comment LikeComeent (@PathVariable String iduser,@PathVariable int idComment)
    {
        return ratingService.addLike(iduser,idComment);
    }

    @PostMapping("/DisLike/{iduser}/{idComment}")
    public Comment DisLikeComeent (@PathVariable String iduser,@PathVariable int idComment)
    {
        return ratingService.addDesLike(iduser,idComment);
    }

}
