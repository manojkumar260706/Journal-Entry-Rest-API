package com.springprojects.journalApp.Repository;

import com.springprojects.journalApp.Entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserEntryRepository extends MongoRepository<User, ObjectId> {
    User findByName(String name);

    void deleteByName(String name);
}
