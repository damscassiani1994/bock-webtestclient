package com.example.book.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BookRouter {

	@Bean
	RouterFunction<ServerResponse> getbookRouter(BookHandler bookHandler) {
		return route(GET("/books").and(accept(MediaType.APPLICATION_JSON)), bookHandler::getBooks)
				.andRoute(GET("/book").and(accept(MediaType.APPLICATION_JSON)), bookHandler::getBook)
				.andRoute(POST("/book").and(accept(MediaType.APPLICATION_JSON)), bookHandler::createBook)
				.andRoute(PUT("/book").and(accept(MediaType.APPLICATION_JSON)), bookHandler::updateBook)
				.andRoute(DELETE("/book/{id}").and(accept(MediaType.APPLICATION_JSON)), bookHandler::deleteBook);
						
	}
}
