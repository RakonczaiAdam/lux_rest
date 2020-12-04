package hu.dreamteam.lux_rest.controller;

import hu.dreamteam.lux_rest.entity.Comment;
import hu.dreamteam.lux_rest.service.CommentService;
import hu.dreamteam.lux_rest.service.PostService;
import hu.dreamteam.lux_rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@RestController
@RequestMapping("/comment")
public class CommentResourceController {

    private CommentService commentService;

    @Autowired
    private void setCommentService(CommentService commentService){
        this.commentService = commentService;
    }

    @GetMapping("/get")
    public Collection<Comment> get(@RequestParam(name = "post_id") Long postId){
        return commentService.getComments(postId);
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestParam(name = "post_id") Long postId,
                                       @RequestBody Comment comment,
                                       HttpServletRequest request){
        try{
            commentService.saveComment(comment, request.getUserPrincipal().getName(), postId);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
