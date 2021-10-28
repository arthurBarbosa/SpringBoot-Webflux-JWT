package com.abcode.fluxjwt;

import com.abcode.fluxjwt.handler.AuthHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class Configs {

    @Bean
    public RouterFunction<ServerResponse> auth(AuthHandler authHandler) {
        return RouterFunctions.route(POST("/sign-up").and(accept(APPLICATION_JSON)), authHandler::signUp);
    }
}
