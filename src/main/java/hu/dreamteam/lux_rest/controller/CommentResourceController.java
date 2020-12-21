package hu.dreamteam.lux_rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import hu.dreamteam.lux_rest.entity.Comment;
import hu.dreamteam.lux_rest.entity.User;
import hu.dreamteam.lux_rest.service.CommentService;
import hu.dreamteam.lux_rest.service.FriendshipService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comment")
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
    public SseEmitter subscribe(@RequestParam("username") String username){
        SseEmitter sseEmitter = new SseEmitter(100000l);
        /*try{
            sseEmitter.send(SseEmitter.event().name("INIT"));
        }catch (Exception e){
            e.printStackTrace();
        }*/
        //sseEmitter.onCompletion(() -> sses.remove(sseEmitter));
        sseEmitter.onTimeout(()->{
            sseEmitter.complete();
        });
        sses.put(username, sseEmitter);
        return sseEmitter;
    }

    @GetMapping(value = "/get")
    public List<Comment> get(@RequestParam(name = "post_id") Long postId){
        return commentService.getComments(postId);
    }

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Comment> save(@RequestParam(name = "post_id") Long postId,
                                       @RequestBody Comment comment,
                                       HttpServletRequest request){
        Comment returned = null;
        try{
            Comment insertedCommented = commentService.saveComment(comment, request.getUserPrincipal().getName(), postId);
            returned = insertedCommented;
            List<User> friends = friendshipService.getFriends(request.getUserPrincipal().getName());
            List<String> friendName = new ArrayList<>();
            friends.forEach((i) -> {
                friendName.add(i.getUsername());
            });
            ObjectMapper mapper = new ObjectMapper();
            String data = mapper.writeValueAsString(insertedCommented);

            sses.forEach((key, value) ->{
                try {
                    if(friendName.contains(key))
                        value.send(SseEmitter.event().name("comment").data(insertedCommented.getPost().getId()+"-"+data));
                }catch (IOException ioe){
                    sses.remove(key);
                    ioe.printStackTrace();
                }
            });
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(returned, HttpStatus.OK);
    }

}
