package hu.dreamteam.lux_rest.controller;

import hu.dreamteam.lux_rest.entity.User;
import hu.dreamteam.lux_rest.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/friendship")
@CrossOrigin(origins = {"http://localhost:3000", "https://lux-client.herokuapp.com"})
public class FriendshipResourceController {

    private FriendshipService friendshipService;

    @Autowired
    private void setFriendshipService(FriendshipService friendshipService){
        this.friendshipService = friendshipService;
    }

    @PostMapping("/request")
    public ResponseEntity<String> request(@RequestParam(name = "receiver_username") String receiverUsername,
                                          HttpServletRequest request){
        try {
            friendshipService.friendRequest(request.getUserPrincipal().getName(), receiverUsername);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/response")
    public ResponseEntity<String> response(@RequestParam(name = "sender_username") String senderUsername,
                                           @RequestParam(name = "response_status") Character responseStatus,
                                           HttpServletRequest request){
        try {
            friendshipService.friendResponse(senderUsername, responseStatus, request.getUserPrincipal().getName());
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/pending")
    public List<User> pending(HttpServletRequest request){
        return friendshipService.getPendings(request.getUserPrincipal().getName());
    }

    @GetMapping("/friends")
    public List<User> get(HttpServletRequest request){
        return friendshipService.getFriends(request.getUserPrincipal().getName());
    }

    @GetMapping("/search")
    public ResponseEntity<User> search(@RequestParam(name = "name") String username){
        return new ResponseEntity<>(friendshipService.getUser(username), HttpStatus.OK);
    }


}
