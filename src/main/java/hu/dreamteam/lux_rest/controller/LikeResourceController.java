package hu.dreamteam.lux_rest.controller;

import hu.dreamteam.lux_rest.entity.Like;
import hu.dreamteam.lux_rest.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@RestController
@RequestMapping("/like")
@CrossOrigin(origins = {"http://localhost:3000", "https://lux-client.herokuapp.com"})
public class LikeResourceController {

    private LikeService likeService;

    @Autowired
    private void setLikeService(LikeService likeService){
        this.likeService = likeService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestParam(name = "post_id") Long postId,
                                       HttpServletRequest request){
        try{
            likeService.saveLike(request.getUserPrincipal().getName(), postId);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get")
    public Collection<Like> get(@RequestParam(name = "post_id") Long postId){
        return likeService.getLikes(postId);
    }

}
