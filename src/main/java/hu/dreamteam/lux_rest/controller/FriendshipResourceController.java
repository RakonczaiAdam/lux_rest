package hu.dreamteam.lux_rest.controller;

import hu.dreamteam.lux_rest.entity.User;
import hu.dreamteam.lux_rest.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@RestController
@RequestMapping("/friendship")
public class FriendshipResourceController {

    private FriendshipService friendshipService;

    @Autowired
    private void setFriendshipService(FriendshipService friendshipService){
        this.friendshipService = friendshipService;
    }

    @PostMapping("/request")
    public ResponseEntity<String> request(@RequestParam(name = "receiver_id") Long receiverId,
                                          HttpServletRequest request){
        try {
            friendshipService.friendRequest(request.getUserPrincipal().getName(), receiverId);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/response")
    public ResponseEntity<String> response(@RequestParam(name = "sender_id") Long senderId,
                                           @RequestParam(name = "response_status") Character responseStatus,
                                           HttpServletRequest request){
        try {
            friendshipService.friendResponse(senderId, responseStatus, request.getUserPrincipal().getName());
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/pending")
    public Collection<User> pending(HttpServletRequest request){
        return friendshipService.getPendings(request.getUserPrincipal().getName());
    }

    @GetMapping("/friends")
    public Collection<User> get(HttpServletRequest request){
        return friendshipService.getFriends(request.getUserPrincipal().getName());
    }


}
