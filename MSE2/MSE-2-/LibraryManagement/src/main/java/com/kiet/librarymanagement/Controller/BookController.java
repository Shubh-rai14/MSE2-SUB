package com.kiet.librarymanagement.Controller;

import com.kiet.librarymanagement.Model.Book;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

         List<Book> books = new ArrayList<>();




        @PostMapping
        public ResponseEntity<Book> addBook(@RequestBody Book book) {


            for (Book b : books) {
                if (b.getId().equals(book.getId())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }
            }

            books.add(book);

            return ResponseEntity.status(HttpStatus.CREATED).body(book);
        }




        @GetMapping
        public List<Book> getAllBooks() {

            return books;
        }



        @GetMapping("/{id}")
        public ResponseEntity<Book> getBookById(@PathVariable Integer id) {

            for (Book b : books) {

                if (b.getId().equals(id)) {

                    return ResponseEntity.ok(b);
                }
            }

            return ResponseEntity.notFound().build();
        }




        @PutMapping("/{id}")
        public ResponseEntity<Book> updateBook(
                @PathVariable Integer id,
                @RequestBody Book updatedBook) {

            for (Book b : books) {

                if (b.getId().equals(id)) {

                    b.setTitle(updatedBook.getTitle());
                    b.setAuthor(updatedBook.getAuthor());
                    b.setCategory(updatedBook.getCategory());
                    b.setAvailableCopies(updatedBook.getAvailableCopies());

                    return ResponseEntity.ok(b);
                }
            }

            return ResponseEntity.notFound().build();
        }




        @DeleteMapping("/{id}")
        public ResponseEntity<String> deleteBook(@PathVariable Integer id) {

            for (Book b : books) {

                if (b.getId().equals(id)) {

                    books.remove(b);

                    return ResponseEntity.ok("Book deleted successfully!");
                }
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Book not found!");
        }
    }

