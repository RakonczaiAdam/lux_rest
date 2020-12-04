package hu.dreamteam.lux_rest.repository;

import hu.dreamteam.lux_rest.entity.FriendRequest;
import hu.dreamteam.lux_rest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FriendRequestRepo extends JpaRepository<FriendRequest, Long> {

    FriendRequest findBySenderAndReceiver(User sender, User receiver);

}
