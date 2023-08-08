package com.example.demo.Service;

import com.example.demo.Model.Books;
import com.example.demo.Repository.BookRepository;
import com.example.demo.Repository.BookRepositoryCustom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookServiceImpl;

    @Mock
    private BookRepositoryCustom bookRepositoryCustom;

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        bookRepositoryCustom = mock(BookRepositoryCustom.class);
        bookServiceImpl = new BookServiceImpl(bookRepository, bookRepositoryCustom);
    }

    @Test
    @DisplayName("Should increment the book rating by one and save the updated book")
    void incrementRating() {
        Books book = new Books(1L, "Book Title", "Author Name", "2021-01-01", "100", "50", "www.example.com", 4);
        when(bookRepository.save(book)).thenReturn(book);

        bookServiceImpl.incrementRating(book);

        assertEquals(5, book.getRating());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    @DisplayName("Should return an empty list when no books are found by the given author")
    void getBookByAuthorWhenNoBooksFound() {
        String author = "John Doe";
        Sort sort = Sort.by(Sort.Direction.DESC, "rating");
        List<Books> emptyList = new ArrayList<>();
        when(bookRepository.findByAuthor(author, sort)).thenReturn(emptyList);

        List<Books> result = bookServiceImpl.getBookByAuthor(author);

        assertEquals(emptyList, result);
        verify(bookRepository, times(1)).findByAuthor(author, sort);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Should return a list of books by the given author and increment their ratings")
    void getBookByAuthorAndIncrementRating() {
        List<Books> mockBooksList = Arrays.asList(
                new Books(1L, "Book 1", "Author 1", "2023-07-26", "100", "50", "link1", 5),
                new Books(2L, "Book 2", "Author 1", "2023-07-26", "200", "100", "link2", 4)
        );

        when(bookRepository.findByAuthor(eq("Author 1"), any(Sort.class))).thenReturn(mockBooksList);

        List<Books> booksList = bookServiceImpl.getBookByAuthor("Author 1");

        assertEquals(2, booksList.size(), "The list should contain 2 books");
        assertEquals(6, booksList.get(0).getRating(), "Ratings should be incremented");
        assertEquals(5, booksList.get(1).getRating(), "Ratings should be incremented");
    }

    @Test
    @DisplayName("Should return an empty list when no books with the exact title are found")
    void getBookByExactTitleWhenNoMatchFound() {
        String title = "Java Programming";
        Sort sort = Sort.by(Sort.Direction.DESC, "rating");
        List<Books> emptyList = new ArrayList<>();
        when(bookRepository.findByExactTitleMatch(title, sort)).thenReturn(emptyList);

        List<Books> result = bookServiceImpl.getBookByExactTitle(title);

        assertEquals(emptyList, result);
        verify(bookRepository, times(1)).findByExactTitleMatch(title, sort);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Should return a list of books with exact title match and increment their ratings")
    void getBookByExactTitleAndIncrementRating() {
        String title = "Java Programming";
        List<Books> expectedBooks = new ArrayList<>();
        Books book1 = new Books(1L, "Java Programming", "John Doe", "2021-01-01", "100", "50", "https://example.com/book1", 4);
        Books book2 = new Books(2L, "Java Programming", "Jane Smith", "2021-02-01", "200", "100", "https://example.com/book2", 3);
        expectedBooks.add(book1);
        expectedBooks.add(book2);

        Sort sort = Sort.by(Sort.Direction.DESC, "rating");
        when(bookRepository.findByExactTitleMatch(title, sort)).thenReturn(expectedBooks);

        List<Books> actualBooks = bookServiceImpl.getBookByExactTitle(title);

        assertEquals(expectedBooks, actualBooks);
        assertEquals(5, book1.getRating());
        assertEquals(4, book2.getRating());

        verify(bookRepository, times(1)).findByExactTitleMatch(title, sort);
        verify(bookRepository, times(2)).save(any(Books.class));
    }

    @Test
    @DisplayName("Should return an empty list when no books contain the keyword in the title")
    void getBookByKeywordWhenNoBooksMatch() {
        String keyword = "java";
        List<String> keywords = Arrays.asList(keyword.split(" "));
        Sort sort = Sort.by(Sort.Direction.DESC, "rating");
        List<Books> emptyList = new ArrayList<>();

        when(bookRepositoryCustom.findByKeywords(keywords, sort)).thenReturn(emptyList);

        List<Books> result = bookServiceImpl.getBookByKeyword(keyword);

        assertEquals(emptyList, result);
        verify(bookRepositoryCustom, times(1)).findByKeywords(keywords, sort);
        verifyNoMoreInteractions(bookRepositoryCustom);
    }

    @Test
    @DisplayName("Should return a list of books that contain the keyword in the title and increment their ratings")
    void getBookByKeywordAndIncrementRating() {
        String keyword = "java";
        List<String> keywords = Arrays.asList(keyword.split(" "));
        Sort sort = Sort.by(Sort.Direction.DESC, "rating");
        List<Books> booksList = new ArrayList<>();
        booksList.add(new Books(1L, "Java Programming", "John Doe", "2021-01-01", "100", "50", "https://example.com", 4));
        booksList.add(new Books(2L, "Advanced Java", "Jane Smith", "2021-02-01", "200", "100", "https://example.com", 5));

        when(bookRepositoryCustom.findByKeywords(keywords, sort)).thenReturn(booksList);

        List<Books> result = bookServiceImpl.getBookByKeyword(keyword);

        assertEquals(2, result.size());
        assertEquals("Java Programming", result.get(0).getTitle());
        assertEquals("Advanced Java", result.get(1).getTitle());
        assertEquals(5, result.get(0).getRating());
        assertEquals(6, result.get(1).getRating());

        verify(bookRepository, times(2)).save(any(Books.class));
    }

}