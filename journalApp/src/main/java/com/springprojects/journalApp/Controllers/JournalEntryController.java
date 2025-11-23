package com.springprojects.journalApp.Controllers;

import java.time.LocalDateTime;
import java.util.*;
import com.springprojects.journalApp.Entity.JournalEntry;
import com.springprojects.journalApp.Entity.User;
import com.springprojects.journalApp.Services.JournalEntryServices;
import com.springprojects.journalApp.Services.UserEntryServices;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    public JournalEntryServices journalEntryServices;

    @Autowired
    public UserEntryServices userEntryServices;

    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userEntryServices.findUserByName(userName);
        List<JournalEntry> entries = user.getJournalEntries();
        if (!entries.isEmpty())
            return new ResponseEntity<>(entries, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry myEntry) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            journalEntryServices.saveEntry(myEntry, userName);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/id/{myid}")
    public ResponseEntity<?> getEntryById(@PathVariable ObjectId myid) {
        JournalEntry journalEntry = journalEntryServices.getEntryById(myid).orElse(null);
        if (journalEntry != null)
            return new ResponseEntity<>(journalEntry, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{myid}")
    public ResponseEntity<?> deleteEntry(@PathVariable ObjectId myid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        if (journalEntryServices.deleteById(myid, userName))
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/id/{myid}")
    public ResponseEntity<?> putEntry(@PathVariable ObjectId myid, @RequestBody JournalEntry newEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        JournalEntry old = journalEntryServices.getEntryById(myid).orElse(null);
        if (old != null) {
            old.setTitle((newEntry.getTitle() != null && !newEntry.getTitle().equals("")) ? newEntry.getTitle()
                    : old.getTitle());
            old.setContent((newEntry.getContent() != null && !newEntry.getContent().equals("")) ? newEntry.getContent()
                    : old.getContent());
            journalEntryServices.saveEntry(old);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
