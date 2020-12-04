package hu.dreamteam.lux_rest.repository;

import hu.dreamteam.lux_rest.entity.Like;
import hu.dreamteam.lux_rest.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepo extends JpaRepository<Like, Long> {

    List<Like> findByPost(Post post);

}
