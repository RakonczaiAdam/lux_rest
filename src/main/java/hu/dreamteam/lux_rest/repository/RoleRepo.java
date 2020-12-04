package hu.dreamteam.lux_rest.repository;

import hu.dreamteam.lux_rest.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {

    Role findByRole(String role);

}
