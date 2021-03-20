package it.studiomascia.gestionale.service;

import it.studiomascia.gestionale.models.User;
import it.studiomascia.gestionale.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository user_Repository;
    @Autowired
    private RoleRepository role_Repository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

       
    public void insertDefaultNewUser(User user) {
        System.out.println("password prima della codifica = "+ user.getPassword());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        System.out.println("password dopo la codifica     = "+ user.getPassword());
        user.setAuthorities(new HashSet<>(role_Repository.findAll()));
        user_Repository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return user_Repository.findByUsername(username);
    }
    
    public void EncodeAndSave(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user_Repository.save(user);
    }
}