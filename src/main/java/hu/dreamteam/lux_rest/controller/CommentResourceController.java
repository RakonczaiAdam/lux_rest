package hu.dreamteam.lux_rest.controller;

import hu.dreamteam.lux_rest.entity.Comment;
import hu.dreamteam.lux_rest.entity.User;
import hu.dreamteam.lux_rest.service.CommentService;
import hu.dreamteam.lux_rest.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comment")
@CrossOrigin(origins = {"http://localhost:3000"})
public class CommentResourceController {

    private CommentService commentService;
    private Map<String, SseEmitter> sses = new HashMap<>();
    private FriendshipService friendshipService;
    
    @Autowired
    private void setCommentService(CommentService commentService){
        this.commentService = commentService;
    }

    @Autowired
    private void setFriendshipService(FriendshipService friendshipService){
        this.friendshipService = friendshipService;
    }

    @RequestMapping(value = "/subscribe", consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribe(HttpServletRequest request){
        SseEmitter sseEmitter = new SseEmitter();
        try{
            sseEmitter.send(SseEmitter.event().name("INIT"));
        }catch (Exception e){
            e.printStackTrace();
        }
        sseEmitter.onCompletion(() -> sses.remove(sseEmitter));
        sses.put(request.getUserPrincipal().getName(), sseEmitter);
        return sseEmitter;
    }

    @GetMapping(value = "/get")
    public List<Comment> get(@RequestParam(name = "post_id") Long postId){
        return commentService.getComments(postId);
    }

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> save(@RequestParam(name = "post_id") Long postId,
                                       @RequestBody Comment comment,
                                       HttpServletRequest request){
        try{
            Comment insertedCommented = commentService.saveComment(comment, request.getUserPrincipal().getName(), postId);
            List<User> friends = friendshipService.getFriends(request.getUserPrincipal().getName());
            List<String> friendName = new ArrayList<>();
            friends.forEach((i) -> {
                friendName.add(i.getUsername());
            });
            sses.forEach((key, value) ->{
                try {
                    if(friendName.contains(key))
                        value.send(SseEmitter.event().name(request.getUserPrincipal().getName()).data(insertedCommented));
                }catch (IOException ioe){
                    ioe.printStackTrace();
                }
            });
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
