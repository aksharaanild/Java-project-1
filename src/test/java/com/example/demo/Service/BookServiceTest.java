package com.example.demo.Service;

import com.example.demo.Model.Books;
import com.example.demo.Repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookServiceInterface bookServiceInterface;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return an empty list when an invalid author is provided")
    void getBookByAuthorWhenInvalidAuthorIsProvided() {
        String invalidAuthor = "Invalid Author";
        when(bookServiceInterface.getBookByAuthor(invalidAuthor)).thenReturn(new ArrayList<>());

        List<Books> result = bookService.getBookByAuthor(invalidAuthor);

        assertEquals(0, result.size());
        verify(bookServiceInterface, times(1)).getBookByAuthor(invalidAuthor);
    }

    @Test
    @DisplayName("Should return a list of books when author has multiple books")
    void getBookByAuthorWhenAuthorHasMultipleBooks() {
        String author = "John Doe";
        Books book1 = new Books(1L, "Book 1", author, "2021-01-01", "100", "50", "link1", 4);
        Books book2 = new Books(2L, "Book 2", author, "2021-02-01", "200", "100", "link2", 5);
        List<Books> expectedBooks = Arrays.asList(book1, book2);

        when(bookServiceInterface.getBookByAuthor(author)).thenReturn(expectedBooks);

        List<Books> actualBooks = bookService.getBookByAuthor(author);

        assertEquals(expectedBooks, actualBooks);
        verify(bookServiceInterface, times(1)).getBookByAuthor(author);
    }

    @Test
    @DisplayName("Should return a list of books when a valid author is provided")
    void getBookByAuthorWhenValidAuthorIsProvided() {
        String author = "John Doe";
        Books book1 = new Books(1L, "Book1", author, "2021-01-01", "100", "50", "link1", 4);
        Books book2 = new Books(2L, "Book2", author, "2021-02-01", "200", "100", "link2", 5);
        List<Books> expectedBooks = Arrays.asList(book1, book2);

        when(bookServiceInterface.getBookByAuthor(author)).thenReturn(expectedBooks);

        List<Books> actualBooks = bookService.getBookByAuthor(author);

        assertEquals(expectedBooks, actualBooks);
        verify(bookServiceInterface, times(1)).getBookByAuthor(author);
    }

    @Test
    @DisplayName("Should return an empty list when no book matches the provided title")
    void getBookByTitleWhenNoBookMatchesTitle() {
        String title = "Nonexistent Book";
        List<Books> emptyList = new ArrayList<>();

        when(bookServiceInterface.getBookByExactTitle(title)).thenReturn(emptyList);

        List<Books> result = bookService.getBookByTitle(title);

        assertEquals(emptyList, result);
        verify(bookServiceInterface, times(1)).getBookByExactTitle(title);
    }

    @Test
    @DisplayName("Should return a list of books when the exact title is provided")
    void getBookByTitleWhenExactTitleIsProvided() {
        String title = "The Great Gatsby";
        Books book1 = new Books(1L, "The Great Gatsby", "F. Scott Fitzgerald", "1925", "1000", "500", "www.example.com", 4);
        Books book2 = new Books(2L, "The Great Gatsby", "Baz Luhrmann", "2013", "2000", "1000", "www.example.com", 3);
        List<Books> expectedBooks = Arrays.asList(book1, book2);

        when(bookServiceInterface.getBookByExactTitle(title)).thenReturn(expectedBooks);

        List<Books> actualBooks = bookService.getBookByTitle(title);

        assertEquals(expectedBooks, actualBooks);
        verify(bookServiceInterface, times(1)).getBookByExactTitle(title);
    }

    @Test
    void getBookByKeywordTest() {
        String keyword = "Java";
        List<Books> expectedBooks = Arrays.asList(new Books(), new Books());

        when(bookServiceInterface.getBookByKeyword(keyword)).thenReturn(expectedBooks);

        List<Books> actualBooks = bookService.getBookByKeyword(keyword);

        assertEquals(expectedBooks, actualBooks);
        verify(bookServiceInterface, times(1)).getBookByKeyword(keyword);
    }

    @Test
    @DisplayName("Should return an empty list when no books contain the keyword in the title")
    void getBookByKeywordWhenKeywordDoesNotExistInTitle() {
        String keyword = "fiction";
        List<Books> booksList = new ArrayList<>();
        when(bookServiceInterface.getBookByKeyword(keyword)).thenReturn(booksList);

        List<Books> result = bookService.getBookByKeyword(keyword);

        assertEquals(0, result.size());
        verify(bookServiceInterface, times(1)).getBookByKeyword(keyword);
    }

    @Test
    @DisplayName("Should return a list of books that contain the keyword in the title")
    void getBookByKeywordWhenKeywordExistsInTitle() {
        String keyword = "Java";
        Books book1 = new Books(1L, "Java Programming", "John Doe", "2021-01-01", "100", "50", "www.example.com", 4);
        Books book2 = new Books(2L, "Python Programming", "Jane Smith", "2021-02-01", "200", "100", "www.example.com", 5);
        List<Books> expectedBooks = Arrays.asList(book1, book2);

        when(bookServiceInterface.getBookByKeyword(keyword)).thenReturn(expectedBooks);

        List<Books> actualBooks = bookService.getBookByKeyword(keyword);

        assertEquals(expectedBooks, actualBooks);
        verify(bookServiceInterface, times(1)).getBookByKeyword(keyword);
    }
}