package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Component
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(JournalEntry entry, String username){
        try {
            entry.setDate(LocalDateTime.now());
            User user = userService.findByUsername(username);
            if(user != null){
                JournalEntry newEntry = journalEntryRepository.save(entry);
                user.getJournalEntries().add(newEntry);
                userService.saveUser(user);
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    public Optional<JournalEntry> getEntryById(ObjectId Id, String username){
        User user = userService.findByUsername(username);
        return user.getJournalEntries().stream()
                .filter(e -> Objects.equals(e.getId(), Id))
                .findFirst();
    }

    @Transactional
    public Boolean deleteEntryById(ObjectId Id, String username) {
        User user = userService.findByUsername(username);
        try {
            user.getJournalEntries().removeIf(entry -> entry.getId().equals(Id));
            userService.saveUser(user);
            journalEntryRepository.deleteById(Id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<JournalEntry> updateEntryById(ObjectId Id, JournalEntry entry, String username) {
        JournalEntry old = getEntryById(Id, username).orElse(null);
        if(old != null) {
            old.setTitle(entry.getTitle() != null && !Objects.equals(entry.getTitle(), "") ? entry.getTitle() : old.getTitle());
            old.setContent(entry.getContent() != null && !Objects.equals(entry.getContent(), "") ? entry.getContent() : old.getContent());
            journalEntryRepository.save(old);
            return Optional.of(old);
        } else {
            return Optional.empty();
        }
    }
}
