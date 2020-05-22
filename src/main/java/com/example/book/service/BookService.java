package com.example.book.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.book.model.Book;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BookService {
	private WebClient webClient;
	
	public BookService() {
		webClient = WebClient.create("https://bookapi.getsandbox.com:443");
	}
	
	public Flux<Book> getBooks() {
		return webClient.get()
				.uri("/api/books")
				.header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
				.retrieve().bodyToFlux(Book.class);
	}
	
	public Mono<Book> getBook(long id) {
		return webClient.get()
				.uri(uriBuilder -> uriBuilder.path("/api/book/{id}")
						.build(id))
				.header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
				.retrieve().bodyToMono(Book.class);
	}
	
	public Mono<Book> createBook(Book book) {
		return webClient.post()
				.uri("/api/create-book")
				.accept(MediaType.APPLICATION_JSON)
				.header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
				.body(Mono.just(Mono.just(book)), Book.class)
				.retrieve().bodyToMono(Book.class);
	}
	
	
	public Mono<Book> updateBook(Book book) {
		return webClient.put()
				.uri("/api/update-book")
				.accept(MediaType.APPLICATION_JSON)
				.header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
				.body(Mono.just(Mono.just(book)), Book.class)
				.retrieve().bodyToMono(Book.class);
	}
	
	public Mono<Boolean> deleteBook(String id) {
		return webClient.delete()
				.uri(uriBuilder -> uriBuilder.path("/api/del-book/{id}")
						.build(id))
				.header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
				.retrieve().bodyToMono(Boolean.class);
	}
	
	
}
