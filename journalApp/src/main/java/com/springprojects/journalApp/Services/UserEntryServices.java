package com.springprojects.journalApp.Services;

import com.springprojects.journalApp.Entity.User;
import com.springprojects.journalApp.Repository.UserEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class UserEntryServices {
    @Autowired
    private UserEntryRepository userEntryRepository;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void saveNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        userEntryRepository.save(user);
    }

    public void saveUser(User user){
        userEntryRepository.save(user);
    }

    public List<User> getAll() {
        return userEntryRepository.findAll();
    }

    public Optional<User> getEntryById(ObjectId id){
        return userEntryRepository.findById(id);
    }

    public boolean deleteById(ObjectId id){
        if(!userEntryRepository.existsById(id))
            return false;
        userEntryRepository.deleteById(id);
        return true;
    }

    public User findUserByName(String name){
        return userEntryRepository.findByName(name);
    }
}
