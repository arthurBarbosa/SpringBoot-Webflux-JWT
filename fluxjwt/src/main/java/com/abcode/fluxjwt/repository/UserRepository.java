package com.abcode.fluxjwt.repository;

import com.abcode.fluxjwt.domain.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
