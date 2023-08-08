package com.example.demo.Service;

import com.example.demo.Model.Books;
import com.example.demo.Repository.BookRepository;
import com.example.demo.Repository.BookRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
@Component
public class BookServiceImpl implements BookServiceInterface  {

    private BookRepository bookRepository;

    private BookRepositoryCustom bookRepositoryCustom;

    public BookServiceImpl(@Qualifier("repositoryA")BookRepository bookRepository, @Qualifier("repositoryB")BookRepositoryCustom bookRepositoryCustom) {
        this.bookRepository = bookRepository;
        this.bookRepositoryCustom = bookRepositoryCustom;
    }
    public void incrementRating(Books book) {
        int currentRating = book.getRating();
        book.setRating(currentRating + 1);
        bookRepository.save(book); // Save the updated book to the database
    }

    //call method for finding author
    public List<Books> getBookByAuthor(String author) {
        // Sort by rating in descending order
        Sort sort = Sort.by(Sort.Direction.DESC,"rating");
        List<Books> booksList = bookRepository.findByAuthor(author, sort);

        for (Books book : booksList) {
            incrementRating(book); // call method to increment rating
        }

        return booksList;
    }

    public List<Books> getBookByExactTitle(String title) {
        Sort sort = Sort.by(Sort.Direction.DESC,"rating");
        List<Books> exactMatch = bookRepository.findByExactTitleMatch(title, sort);

        for (Books book : exactMatch) {
            incrementRating(book); // call method to increment rating
        }
        return exactMatch;
    }

    public List<Books> getBookByKeyword(String title) {

        List<String> keywords = Arrays.asList(title.split(" "));
        Sort sort = Sort.by(Sort.Direction.DESC,"rating");
        List<Books> booksList = bookRepositoryCustom.findByKeywords(keywords, sort);

        for (Books book : booksList) {
            incrementRating(book); // call method to increment rating
        }

        return booksList;
    }
}
