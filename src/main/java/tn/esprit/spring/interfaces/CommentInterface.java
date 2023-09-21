package tn.esprit.spring.interfaces;

import tn.esprit.spring.entities.Comment;

import java.util.List;

public interface CommentInterface {

    public Comment addComment (Comment c);
    public Comment updateComment (Comment c);
    public void deleteComment (int idc);
    public List<Comment> retrieveAllComment();
    public Comment retrieveComment(int idc);
}
