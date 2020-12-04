package hu.dreamteam.lux_rest.service;

import hu.dreamteam.lux_rest.entity.Post;
import hu.dreamteam.lux_rest.entity.User;
import hu.dreamteam.lux_rest.repository.PostRepo;
import hu.dreamteam.lux_rest.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class PostService {

    private PostRepo postRepo;
    private UserRepo userRepo;
    private FriendshipService friendshipService;

    @Autowired
    private void setRepos(PostRepo postRepo, UserRepo userRepo){
        this.postRepo = postRepo;
        this.userRepo = userRepo;
    }

    @Autowired
    private void setFriendshipService(FriendshipService friendshipService){
        this.friendshipService = friendshipService;
    }

    public List<Post> getPosts(String username){
        List<User> friends = (ArrayList<User>) friendshipService.getFriends(username);
        friends.add(userRepo.findByEmail(username));
        List<Post> posts = new ArrayList<>();
        for(User user : friends){
            for(Post post : user.getPosts()){
                posts.add(post);
            }
        }
        Collections.sort(posts, Collections.reverseOrder());
        return posts;
    }

    public void savePost(Post post, String user){
        post.setUser(userRepo.findByEmail(user));
        post.setDate(new Date());
        postRepo.save(post);
    }

}
