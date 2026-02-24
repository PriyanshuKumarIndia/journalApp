package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveUser(User user){
        try {
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Exception in user service saveUser method: ", e);
            throw new RuntimeException(e);
        }
    }

    public void saveNewUser(User user){
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Collections.singletonList("USER"));
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Exception in user service saveNewUser method: ", e);
            throw new RuntimeException(e);
        }
    }

    public void saveNewAdmin(User user){
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Collections.singletonList("ADMIN"));
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Exception in user service saveNewUser method: ", e);
            throw new RuntimeException(e);
        }
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(ObjectId Id){
        return userRepository.findById(Id);
    }

    public void deleteByUsername(String username) {
        userRepository.deleteByUsername(username);
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }
}
