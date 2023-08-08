package com.example.demo.Controller;

import com.example.demo.Model.Books;
import com.example.demo.Service.BookServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
public class BooksController {

    private final BookServiceInterface bookServiceInterface;

    @Autowired
    public BooksController(BookServiceInterface bookServiceInterface) {

        this.bookServiceInterface = bookServiceInterface;
    }

    @GetMapping("/book/{author}")
    public Map<String, Books> getSpecificAuthor(@PathVariable String author){
        List<Books> booksList = bookServiceInterface.getBookByAuthor(author);

        LinkedHashMap<String, Books> booksMap = booksList.stream()
                .collect(Collectors.toMap(Books::getTitle, Function.identity(),
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        return booksMap;
    }

    @GetMapping("/book/title/{title}")
    public List<Books> getSpecificTitle(@PathVariable String title) {
        List<Books> booksList = bookServiceInterface.getBookByExactTitle(title);
        return booksList;
    }



    @GetMapping("/books/{title}")
    public List<Books> getBooksByTitle(@PathVariable String title) {
        List<Books> exactMatchBooks = bookServiceInterface.getBookByExactTitle(title);
        List<Books> keywordMatchBooks = bookServiceInterface.getBookByKeyword(title);

        // Combine the sorted lists
        List<Books> booksList = new ArrayList<>();
        booksList.addAll(exactMatchBooks);
        booksList.addAll(keywordMatchBooks);

        return booksList;
    }


}
