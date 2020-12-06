package hu.dreamteam.lux_rest.service;

import hu.dreamteam.lux_rest.entity.Comment;
import hu.dreamteam.lux_rest.repository.CommentRepo;
import hu.dreamteam.lux_rest.repository.PostRepo;
import hu.dreamteam.lux_rest.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {

    private CommentRepo commentRepo;
    private UserRepo userRepo;
    private PostRepo postRepo;

    @Autowired
    private void setRepo(CommentRepo commentRepo, UserRepo userRepo, PostRepo postRepo){
        this.commentRepo = commentRepo;
        this.userRepo = userRepo;
        this.postRepo = postRepo;
    }

    public Comment saveComment(Comment comment, String user, Long postId) throws Exception{
        comment.setUser(userRepo.findByUsername(user));
        comment.setDate(new Date());
        comment.setPost(postRepo.findById(postId).get());
        return commentRepo.save(comment);
    }

    public List<Comment> getComments(Long postId){
        return commentRepo.findByPost(postRepo.findById(postId).get());
    }


}
