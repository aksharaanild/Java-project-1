package com.example.demo.Controller;

import com.example.demo.Model.Books;
import com.example.demo.Service.BookServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BooksControllerTest {

    @Mock
    private BookServiceInterface bookServiceInterface;

    @InjectMocks
    private BooksController booksController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);


    }

    @Test
    @DisplayName("Should return an empty map when there are no books by the specific author")
    void getSpecificAuthorWhenNoBooks() {
        String author = "John Doe";
        List<Books> emptyList = new ArrayList<>();
        when(bookServiceInterface.getBookByAuthor(author)).thenReturn(emptyList);

        Map<String, Books> result = booksController.getSpecificAuthor(author);

        assertEquals(0, result.size());
        verify(bookServiceInterface, times(1)).getBookByAuthor(author);
    }

    @Test
    @DisplayName("Should return a map of books by the specific author")
    void getSpecificAuthorTest() {
        String author = "John Doe";
        List<Books> booksList = new ArrayList<>();
        booksList.add(new Books(1L, "Book 1", author, "2021-01-01", "100", "50", "link1", 4));
        booksList.add(new Books(2L, "Book 2", author, "2021-02-01", "200", "100", "link2", 5));

        when(bookServiceInterface.getBookByAuthor(author)).thenReturn(booksList);

        Map<String, Books> expectedMap = new LinkedHashMap<>();
        expectedMap.put("Book 1", new Books(1L, "Book 1", author, "2021-01-01", "100", "50", "link1", 4));
        expectedMap.put("Book 2", new Books(2L, "Book 2", author, "2021-02-01", "200", "100", "link2", 5));

        Map<String, Books> actualMap = booksController.getSpecificAuthor(author);

        assertEquals(expectedMap, actualMap);
        verify(bookServiceInterface, times(1)).getBookByAuthor(author);
    }

    @Test
    @DisplayName("Should return an empty list when no books with the given title are found")
    void getSpecificTitleWhenNoBooksFound() {
        String title = "No Book";
        List<Books> emptyList = new ArrayList<>();
        when(bookServiceInterface.getBookByExactTitle(title)).thenReturn(emptyList);

        List<Books> result = booksController.getSpecificTitle(title);

        assertEquals(emptyList, result);
        verify(bookServiceInterface, times(1)).getBookByExactTitle(title);
    }

    @Test
    @DisplayName("Should return a list of books with the exact title")
    void getSpecificTitleWithExactMatch() {
        String title = "Java Programming";
        List<Books> expectedBooksList = new ArrayList<>();
        Books book1 = new Books(1L, "Java Programming", "John Doe", "2021-01-01", "100", "50", "www.example.com", 4);
        Books book2 = new Books(2L, "Java Programming", "Jane Smith", "2021-02-01", "200", "100", "www.example.com", 5);
        expectedBooksList.add(book1);
        expectedBooksList.add(book2);

        when(bookServiceInterface.getBookByExactTitle(title)).thenReturn(expectedBooksList);

        List<Books> actualBooksList = booksController.getSpecificTitle(title);

        assertEquals(expectedBooksList, actualBooksList);
        verify(bookServiceInterface, times(1)).getBookByExactTitle(title);
    }

    @Test
    @DisplayName("Should return an empty list when no books match the title")
    void getBooksByTitleWhenNoMatch() {
        String title = "Java Programming";
        List<Books> exactMatchBooks = new ArrayList<>();
        List<Books> keywordMatchBooks = new ArrayList<>();
        when(bookServiceInterface.getBookByExactTitle(title)).thenReturn(exactMatchBooks);
        when(bookServiceInterface.getBookByKeyword(title)).thenReturn(keywordMatchBooks);

        List<Books> result = booksController.getBooksByTitle(title);

        assertEquals(0, result.size());
        verify(bookServiceInterface, times(1)).getBookByExactTitle(title);
        verify(bookServiceInterface, times(1)).getBookByKeyword(title);
    }

    @Test
    @DisplayName("Should return a list of books that exactly match the title")
    void getBooksByTitleWhenExactMatch() {
        String title = "Java Programming";
        List<Books> exactMatchBooks = new ArrayList<>();
        exactMatchBooks.add(new Books(1L, "Java Programming", "John Doe", "2021-01-01", "100", "50", "www.example.com", 4));
        exactMatchBooks.add(new Books(2L, "Java Programming", "Jane Smith", "2021-02-01", "200", "100", "www.example.com", 5));
        when(bookServiceInterface.getBookByExactTitle(title)).thenReturn(exactMatchBooks);

        List<Books> keywordMatchBooks = new ArrayList<>();
        keywordMatchBooks.add(new Books(3L, "Java Programming Basics", "Mike Johnson", "2021-03-01", "150", "75", "www.example.com", 3));
        keywordMatchBooks.add(new Books(4L, "Advanced Java Programming", "Sarah Williams", "2021-04-01", "250", "120", "www.example.com", 4));
        when(bookServiceInterface.getBookByKeyword(title)).thenReturn(keywordMatchBooks);

        List<Books> result = booksController.getBooksByTitle(title);

        assertEquals(4, result.size());
        assertEquals("Java Programming", result.get(0).getTitle());
        assertEquals("John Doe", result.get(0).getAuthor());
        assertEquals("Java Programming", result.get(1).getTitle());
        assertEquals("Jane Smith", result.get(1).getAuthor());
        assertEquals("Java Programming Basics", result.get(2).getTitle());
        assertEquals("Mike Johnson", result.get(2).getAuthor());
        assertEquals("Advanced Java Programming", result.get(3).getTitle());
        assertEquals("Sarah Williams", result.get(3).getAuthor());

        verify(bookServiceInterface, times(1)).getBookByExactTitle(title);
        verify(bookServiceInterface, times(1)).getBookByKeyword(title);
    }

    @Test
    @DisplayName("Should return a combined list of books that match the title exactly and by keyword")
    void getBooksByTitleWhenCombinedMatch() {
        String title = "Java";
        List<Books> exactMatchBooks = Arrays.asList(
                new Books(1L, "Java Programming", "John Doe", "2021-01-01", "100", "50", "https://example.com/1", 4),
                new Books(2L, "Java Basics", "Jane Smith", "2021-02-01", "200", "100", "https://example.com/2", 5)
        );
        List<Books> keywordMatchBooks = Arrays.asList(
                new Books(3L, "Advanced Java", "John Doe", "2021-03-01", "150", "75", "https://example.com/3", 4),
                new Books(4L, "Java for Beginners", "Jane Smith", "2021-04-01", "250", "125", "https://example.com/4", 3)
        );
        List<Books> expectedBooksList = new ArrayList<>();
        expectedBooksList.addAll(exactMatchBooks);
        expectedBooksList.addAll(keywordMatchBooks);

        when(bookServiceInterface.getBookByExactTitle(title)).thenReturn(exactMatchBooks);
        when(bookServiceInterface.getBookByKeyword(title)).thenReturn(keywordMatchBooks);

        List<Books> actualBooksList = booksController.getBooksByTitle(title);

        assertEquals(expectedBooksList, actualBooksList);
        verify(bookServiceInterface, times(1)).getBookByExactTitle(title);
        verify(bookServiceInterface, times(1)).getBookByKeyword(title);
    }

    @Test
    @DisplayName("Should return a list of books that match the title by keyword")
    void getBooksByTitleWhenKeywordMatch() {
        String title = "Java";
        List<Books> exactMatchBooks = new ArrayList<>();
        List<Books> keywordMatchBooks = new ArrayList<>();
        Books book1 = new Books(1L, "Java Programming", "John Doe", "2021-01-01", "100", "50", "https://example.com/book1", 4);
        Books book2 = new Books(2L, "Java Basics", "Jane Smith", "2021-02-01", "200", "100", "https://example.com/book2", 5);
        keywordMatchBooks.add(book1);
        keywordMatchBooks.add(book2);
        when(bookServiceInterface.getBookByExactTitle(title)).thenReturn(Collections.emptyList());
        when(bookServiceInterface.getBookByKeyword(title)).thenReturn(keywordMatchBooks);

        List<Books> result = booksController.getBooksByTitle(title);

        assertEquals(keywordMatchBooks, result);
        verify(bookServiceInterface, times(1)).getBookByExactTitle(title);
        verify(bookServiceInterface, times(1)).getBookByKeyword(title);
    }
}