package com.springprojects.journalApp.Services;

import com.springprojects.journalApp.Entity.JournalEntry;
import com.springprojects.journalApp.Entity.User;
import com.springprojects.journalApp.Repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryServices {
    @Autowired
    public JournalEntryRepository journalEntryRepository;

    @Autowired
    public UserEntryServices userEntryServices;

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName) {
        User user = userEntryServices.findUserByName(userName);
        journalEntry.setDate(LocalDateTime.now());
        JournalEntry saved = journalEntryRepository.save(journalEntry);
        user.getJournalEntries().add(saved);
        userEntryServices.saveUser(user);
    }

    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> getEntryById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String userName) {
        User user = userEntryServices.findUserByName(userName);
        if (!journalEntryRepository.existsById(id))
            return false;
        user.getJournalEntries().removeIf(x -> x.getId().equals(id));
        userEntryServices.saveUser(user);
        journalEntryRepository.deleteById(id);
        return true;
    }
}
