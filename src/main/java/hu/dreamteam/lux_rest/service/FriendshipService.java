package hu.dreamteam.lux_rest.service;

import hu.dreamteam.lux_rest.entity.FriendRequest;
import hu.dreamteam.lux_rest.entity.User;
import hu.dreamteam.lux_rest.repository.FriendRequestRepo;
import hu.dreamteam.lux_rest.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class FriendshipService {

    private static final Character SENT = 'S';
    private static final Character REJECT = 'R';
    private static final Character ACCEPT = 'A';
    private FriendRequestRepo friendRequestRepo;
    private UserRepo userRepo;

    @Autowired
    private void  setRepos(FriendRequestRepo friendRequestRepo, UserRepo userRepo){
        this.userRepo = userRepo;
        this.friendRequestRepo = friendRequestRepo;
    }

    public void friendRequest(String sender, Long receiver) throws Exception{
        friendRequestRepo.save(new FriendRequest(userRepo.findByEmail(sender), userRepo.findById(receiver).get(), SENT));
    }

    public void friendResponse(Long senderId, Character responseStatus, String receiverName) throws Exception{
        User receiver = userRepo.findByEmail(receiverName);
        FriendRequest friendRequest = friendRequestRepo.findBySenderAndReceiver(userRepo.findById(senderId).get(), receiver);
        FriendRequest friendRequestRef = friendRequestRepo.getOne(friendRequest.getId());
        if(responseStatus == REJECT){
            friendRequestRef.setStatus(REJECT);
        }else if(responseStatus == ACCEPT){
            friendRequestRef.setStatus(ACCEPT);
        }else
            throw new Exception("Unknown response status");
        friendRequestRepo.save(friendRequestRef);
    }

    public Collection<User> getPendings(String username){
        User currentUser = userRepo.findByEmail(username);
        List<FriendRequest> friendRequests = friendRequestRepo.findAll();
        List<User> pending = new ArrayList<>();
        for(FriendRequest friendRequest : friendRequests){
            if(friendRequest.getReceiver().equals(currentUser) && friendRequest.getStatus() == SENT){
                pending.add(friendRequest.getSender());
            }else if(friendRequest.getSender().equals(currentUser) && friendRequest.getStatus() == SENT){
                pending.add(friendRequest.getReceiver());
            }
        }
        return pending;
    }

    public Collection<User> getFriends(String username){
        User currentUser = userRepo.findByEmail(username);
        List<FriendRequest> friendRequests = friendRequestRepo.findAll();
        List<User> friends = new ArrayList<>();
        for(FriendRequest friendRequest : friendRequests){
            if(friendRequest.getReceiver().equals(currentUser) && friendRequest.getStatus() == ACCEPT){
                friends.add(friendRequest.getSender());
            }else if(friendRequest.getSender().equals(currentUser) && friendRequest.getStatus() == ACCEPT){
                friends.add(friendRequest.getReceiver());
            }
        }
        return friends;
    }

}
