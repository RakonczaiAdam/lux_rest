package hu.dreamteam.lux_rest.controller;

import hu.dreamteam.lux_rest.entity.User;
import hu.dreamteam.lux_rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {
    // comment for test commit
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    private void setUserService(UserService userService){
        this.userService = userService;
    }

    @Autowired
    private void setPasswordEncoder(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user){
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.saveUser(user);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
