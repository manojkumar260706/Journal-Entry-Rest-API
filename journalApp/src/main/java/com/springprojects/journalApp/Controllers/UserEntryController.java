package com.springprojects.journalApp.Controllers;

import com.springprojects.journalApp.Entity.User;
import com.springprojects.journalApp.Repository.UserEntryRepository;
import com.springprojects.journalApp.Services.UserEntryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserEntryController {

    @Autowired
    private UserEntryServices userEntryServices;

    @Autowired
    private UserEntryRepository userEntryRepository;

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        User userInDB = userEntryServices.findUserByName(name);
        if (userInDB != null) {
            if (user.getName() != null && !user.getName().isEmpty()) {
                userInDB.setName(user.getName());
            }
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                userInDB.setPassword(user.getPassword());
            }
            userEntryServices.saveNewUser(userInDB);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        userEntryRepository.deleteByName(name);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
