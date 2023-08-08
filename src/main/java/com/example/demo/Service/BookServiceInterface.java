package com.example.demo.Service;

import com.example.demo.Model.Books;

import java.util.List;

public interface BookServiceInterface {

    List<Books> getBookByAuthor(String author);
    List<Books> getBookByExactTitle(String title);

    List<Books> getBookByKeyword(String title);
}
