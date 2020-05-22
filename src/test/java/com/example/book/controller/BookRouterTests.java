package com.example.book.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.book.model.Book;
import com.example.book.service.BookService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookRouterTests {
	@MockBean
	BookService bookService;
	Book book;
	Book book2;
	Book book3;
	
	@Autowired
	WebTestClient webTestClient;
	
	@Before
	public void setUp() {
		book = new Book().toBuilder().author("Fernando Trujillo")
				.id(1).name("La Biblia de los caidos").price("80000").build();
		book2 = new Book().toBuilder().author("Charles Brandt")
				.id(2).name("Jimmy Hoffa. Caso Cerrado").price("90000").build();
		book3 = new Book().toBuilder().author("Fernando Trujillo")
				.id(3).name("La Biblia de los caidos - Tomo 2").price("50000").build();
	}
	
	@Test
	public void getBooksTest() {
		when(bookService.getBooks()).thenReturn(Flux.just(book, book2, book3));
		
		webTestClient.get().uri("/books")
		.header("content-type", MediaType.APPLICATION_JSON_VALUE)
		.exchange().expectStatus().isOk().expectBodyList(Book.class)
		.value(value -> {
			assertEquals("La Biblia de los caidos", value.get(0).getName());
		});
	}
	
	
	@Test
	public void getBookTest() {
		when(bookService.getBook(1)).thenReturn(Mono.just(book));
		
		webTestClient.get().uri("/book?id=1")
		.exchange().expectStatus().isOk().expectBody(Book.class)
		.value(value -> {
			assertEquals("La Biblia de los caidos", value.getName());
		});
	}
	
	@Test
	public void getBookWithParamhNullTest() {
		webTestClient.get().uri("/book")
		.accept(MediaType.APPLICATION_JSON)
		.exchange().expectStatus().is5xxServerError().expectBody(String.class)
		.value(value -> {
			assertEquals("Parametro id reequerido", value);
		});
	}
	
	@Test
	public void createBookTest() {
		when(bookService.createBook(book)).thenReturn(Mono.just(book));
		
		webTestClient.post().uri("/book")
		.accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.APPLICATION_JSON)
		.body(Mono.just(book), Book.class)
		.exchange().expectStatus().isOk();
	}
	
	@Test
	public void updateBookTest() {
		when(bookService.updateBook(book)).thenReturn(Mono.just(book));
		
		webTestClient.put().uri("/book")
		.accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.APPLICATION_JSON)
		.body(Mono.just(book), Book.class)
		.exchange().expectStatus().isOk().expectBody(Book.class)
		.isEqualTo(book);
	}
	
	@Test
	public void deleteBookTest() {
		when(bookService.deleteBook("1")).thenReturn(Mono.just(true));
		
		webTestClient.delete().uri("/book/1")
		.accept(MediaType.APPLICATION_JSON)
		.exchange().expectStatus().isOk().expectBody(Boolean.class).isEqualTo(true);
	}
}
