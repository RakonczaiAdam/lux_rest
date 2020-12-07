package hu.dreamteam.lux_rest;

import hu.dreamteam.lux_rest.entity.Comment;
import hu.dreamteam.lux_rest.entity.Post;
import hu.dreamteam.lux_rest.entity.User;
import hu.dreamteam.lux_rest.repository.CommentRepo;
import hu.dreamteam.lux_rest.repository.PostRepo;
import hu.dreamteam.lux_rest.repository.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class LuxRestApplicationTests {

	@Autowired
	private UserRepo userRepo;
	@Autowired
	private CommentRepo commentRepo;
	@Autowired
	private PostRepo postRepo;

	@Test
	void commentInsert() {
		final String COMMENT_TEXT = "comment";
		User user = userRepo.findByUsername("adamuser");
		Post post = postRepo.findById(1l).get();
		Comment comment = new Comment();
		comment.addAttributes(COMMENT_TEXT, post, user, new Date());

		Comment insertedComment = commentRepo.save(comment);

		Assertions.assertEquals(insertedComment.getContent(), COMMENT_TEXT);
		Assertions.assertEquals(insertedComment.getPost(), post);
		Assertions.assertEquals(insertedComment.getUser(), user);
	}


}
