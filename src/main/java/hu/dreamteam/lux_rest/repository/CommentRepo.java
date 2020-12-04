package hu.dreamteam.lux_rest.repository;

import hu.dreamteam.lux_rest.entity.Comment;
import hu.dreamteam.lux_rest.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Post post);

}
