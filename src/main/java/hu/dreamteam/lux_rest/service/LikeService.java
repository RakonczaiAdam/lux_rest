package hu.dreamteam.lux_rest.service;

import hu.dreamteam.lux_rest.entity.Like;
import hu.dreamteam.lux_rest.repository.LikeRepo;
import hu.dreamteam.lux_rest.repository.PostRepo;
import hu.dreamteam.lux_rest.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {

    private LikeRepo likeRepo;
    private PostRepo postRepo;
    private UserRepo userRepo;

    @Autowired
    private void setRepos(LikeRepo likeRepo, PostRepo postRepo, UserRepo userRepo){
        this.likeRepo = likeRepo;
        this.postRepo = postRepo;
        this.userRepo = userRepo;
    }

    public void saveLike(String user, Long postId) throws Exception{
        likeRepo.save(new Like(userRepo.findByUsername(user), postRepo.findById(postId).get()));
    }

    public List<Like> getLikes(Long postId){
        return likeRepo.findByPost(postRepo.findById(postId).get());
    }

}
