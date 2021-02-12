package hu.dreamteam.lux_rest;

import hu.dreamteam.lux_rest.entity.Comment;
import hu.dreamteam.lux_rest.entity.Post;
import hu.dreamteam.lux_rest.entity.User;
import hu.dreamteam.lux_rest.repository.CommentRepo;
import hu.dreamteam.lux_rest.repository.PostRepo;
import hu.dreamteam.lux_rest.repository.UserRepo;
import hu.dreamteam.lux_rest.service.UserService;
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
	@Autowired
	private UserService userService;

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

	@Test
	void newCommentTest(){
		String postContent = "Ez a content!";
		Date date =new Date();
		User user = userRepo.findByUsername("encyuser");
		Post post = new Post();
		Comment comment = new Comment();

		post.setUser(user);
		post.setContent(postContent);
		Post p = postRepo.save(post);

		comment.addAttributes("Hello",p,user,date);
		Comment insertedComment = commentRepo.save(comment);

		Assertions.assertEquals(insertedComment.getContent(),"Hello", "nem müködött a comment teszt");
	}

	@Test
	void postTest() {
		String postContent = "post content";
		Post post = new Post();
		User user = userRepo.findByUsername("encyuser");
		post.setUser(user);
		post.setContent(postContent);

		Post insertedPost = postRepo.save(post);
		Assertions.assertEquals(post.getContent(), insertedPost.getContent(), "nem egyeznek meg a contentek");
	}

	@Test
	void newUserTest() throws Exception {
		String pw ="root";
		String us = "Kati";
		User user = new User(us,pw);
		//User newUser = userRepo.save(user);
		//userService.saveUser(newUser);

		Assertions.assertEquals(user.getPassword(),pw );
		Assertions.assertEquals(user.getUsername(),us );

	}

}
