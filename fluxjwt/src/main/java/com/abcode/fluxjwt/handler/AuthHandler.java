package com.abcode.fluxjwt.handler;

import com.abcode.fluxjwt.domain.User;
import com.abcode.fluxjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class AuthHandler {

    @Autowired
    private UserRepository userRepository;

    public Mono<ServerResponse> signUp(ServerRequest request) {
        Mono<User> userMono = request.bodyToMono(User.class);

        return userMono.map(u -> new User(u.getUsername(), u.getPassword()))
                .flatMap(this.userRepository::save)
                .flatMap(user -> ServerResponse.ok().body(user, User.class));
    }
}
