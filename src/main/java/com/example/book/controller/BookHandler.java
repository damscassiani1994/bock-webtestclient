package com.example.book.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.book.model.Book;
import com.example.book.service.BookService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class BookHandler {
	private final BookService bookServicee;
	
	public BookHandler(BookService bookService) {
		this.bookServicee = bookService;
	}
	
	public Mono<ServerResponse> getBooks(ServerRequest serverRequest) {
		Flux<Book> books = this.bookServicee.getBooks();
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(books, Book.class);
	}
	
	public Mono<ServerResponse> getBook(ServerRequest serverRequest) {
		if(serverRequest.queryParam("id").isEmpty()) {
			return ServerResponse.status(500).contentType(MediaType.APPLICATION_JSON)
					.body(Mono.just("Parametro id reequerido"), String.class);
		}
			
		Mono<Book> books = this.bookServicee
				.getBook(Long.parseLong(serverRequest.queryParam("id").get()));
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(books, Book.class);
	}
	
	public Mono<ServerResponse> createBook(ServerRequest serverRequest) {
		Mono<Book> books = serverRequest.bodyToMono(Book.class)
				.flatMap(bookServicee::createBook);
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(books, Book.class);
	}
	
	public Mono<ServerResponse> updateBook(ServerRequest serverRequest) {
		Mono<Book> books = serverRequest.bodyToMono(Book.class)
				.flatMap(bookServicee::updateBook);
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(books, Book.class);
	}
	
	public Mono<ServerResponse> deleteBook(ServerRequest serverRequest) {
		Mono<Boolean> isDelete = bookServicee.deleteBook(serverRequest.pathVariable("id"));
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(isDelete, Boolean.class);
	}
}
