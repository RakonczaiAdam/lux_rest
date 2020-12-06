package hu.dreamteam.lux_rest.service;

import hu.dreamteam.lux_rest.entity.Role;
import hu.dreamteam.lux_rest.entity.User;
import hu.dreamteam.lux_rest.repository.RoleRepo;
import hu.dreamteam.lux_rest.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private UserRepo userRepo;
    private RoleRepo roleRepo;
    private static final String USER_ROLE = "user";

    @Autowired
    private void setRepos(UserRepo userRepo, RoleRepo roleRepo){
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByEmail(username);
        if(user == null){
            throw new UsernameNotFoundException(username);
        }
        return new MyUserDetail(user);
    }

    public User findByEmail(String email){
        return userRepo.findByUsername(email);
    }

    public void saveUser(User user)throws Exception{
        Role userRole = roleRepo.findByRole(USER_ROLE);
        if(userRole == null) {
            userRole = roleRepo.save(new Role(USER_ROLE));
        }
        user.getRoles().add(userRole);
        User u = userRepo.save(user);
    }

}
