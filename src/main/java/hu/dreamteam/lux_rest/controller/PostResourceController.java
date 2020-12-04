package hu.dreamteam.lux_rest.controller;

import hu.dreamteam.lux_rest.entity.Post;
import hu.dreamteam.lux_rest.service.PostService;
import hu.dreamteam.lux_rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@RestController
@RequestMapping("/post")
public class PostResourceController {

    private PostService postService;

    @Autowired
    private void setPostService(PostService postService){
        this.postService = postService;
    }

    @GetMapping("/get")
    public Collection<Post> get(HttpServletRequest request){
        return postService.getPosts(request.getUserPrincipal().getName());
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Post post, HttpServletRequest request){
        try {
            postService.savePost(post,request.getUserPrincipal().getName());
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
