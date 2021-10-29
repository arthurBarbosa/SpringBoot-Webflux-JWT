package com.abcode.fluxjwt.handler;

import com.abcode.fluxjwt.JwtUtils;
import com.abcode.fluxjwt.domain.User;
import com.abcode.fluxjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class AuthHandler {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;

    public Mono<ServerResponse> signUp(ServerRequest request) {
        Mono<User> userMono = request.bodyToMono(User.class);

        return userMono.map(u -> {
                    User userPass = new User(u.getUsername(), u.getPassword());
                    userPass.setPassword(passwordEncoder.encode(u.getPassword()));
                    return userPass;
                })
                .flatMap(this.userRepository::save)
                .flatMap(user -> ServerResponse.ok().body(BodyInserters.fromValue(user)));
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        Mono<User> userMono = request.bodyToMono(User.class);
        return userMono
                .flatMap(u -> this.userRepository.findByUsername(u.getUsername())
                        .flatMap(user -> {
                            if (passwordEncoder.matches(u.getPassword(), user.getPassword())) {
                                return ServerResponse.ok()
                                        .body(BodyInserters.fromValue(jwtUtils.genToken(user)));
                            } else {
                                return ServerResponse.badRequest()
                                        .body(BodyInserters.fromValue("Invalid credentials"));
                            }

                        }).switchIfEmpty(ServerResponse.badRequest()
                                .body(BodyInserters.fromValue("User does not exists"))));
    }
}
