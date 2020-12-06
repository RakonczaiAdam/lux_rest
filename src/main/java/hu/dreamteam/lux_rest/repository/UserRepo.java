package hu.dreamteam.lux_rest.repository;

import hu.dreamteam.lux_rest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{

    User findByUsername(String email);

}
